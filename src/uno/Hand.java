package uno;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * <p>
 * A Hand of Uno cards, held by a particular player. A Hand object is
 * responsible for playing a Card (<i>i.e.</i>, actually choosing a card to
 * play) when the player's turn comes up. To do this, it implements the strategy
 * pattern by which this choice can be delegated to an arbitrary implementer of
 * the UnoPlayer class.
 * </p>
 * 
 * @author Stephen Davies
 * @author Alice McRae
 * @version 2013-08-25
 */
public class Hand
{

    private ArrayList<Card> cards;
    private UnoPlayer player;
    private String playerName;

    /**
     * Instantiate a Hand object to be played by the UnoPlayer class, and the
     * player name, passed as arguments. This implements a strategy pattern
     * whereby the constructor accepts various strategies that implement the
     * UnoPlayer interface.
     * 
     * @param unoPlayerClassName
     *            a class that implements UnoPlayer interface
     * @param playerName
     *            the name of the player
     */
    public Hand(String unoPlayerClassName, String playerName)
    {
        try
        {
            player = (UnoPlayer) Class.forName(unoPlayerClassName)
                .newInstance();
        }
        catch (Exception e)
        {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            throw new RuntimeException("Problem with " + unoPlayerClassName
                + ".\n" + errors);
            // System.exit(1);
        }
        this.playerName = playerName;
        cards = new ArrayList<Card>();
    }

    /**
     * Add (draw) a card to the hand.
     * 
     * @param c
     *            the card to add
     */
    void addCard(Card c)
    {
        cards.add(c);
    }

    /**
     * Get the size of the hand.
     * 
     * @return the number of cards in the hand.
     */
    public int size()
    {
        return cards.size();
    }

    /**
     * It's your turn: play a card. When this method is called, the Hand object
     * choose a Card from the Hand based on the strategy that is controlling it
     * (<i>i.e.</i>, whose code was passed to the Hand constructor.) If the
     * player cannot legally play any of his/her cards, null is returned.
     * 
     * @param game
     *            The current game
     * @return The Card object to be played (which has been removed from this
     *         Hand as a side effect), or null if no such Card can be played.
     */
    Card play(Game game)
    {
        int playedCard;
        ArrayList<Card> copiedCards = copyCards();
        playedCard = player.play(copiedCards, game.getUpCard(),
            game.calledColor, game.getGameState());
        verify(cards, playedCard, game.getUpCard(), game.calledColor);
        if (playedCard == -1)
        {
            return null;
        }
        else
        {
            Card toPlay = cards.remove(playedCard);
            return toPlay;
        }
    }

    /**
     * Designed to be called in response to a wild card having been played on
     * the previous call to this object's play() method. This method will choose
     * one of the four colors based on the strategy controlling it (<i>i.e.</i>,
     * the class whose code was passed to the Hand constructor.)
     * 
     * @param game
     *            The current game
     * @return A Color value, <i>not</i> Color.NONE.
     */
    UnoPlayer.Color callColor(Game game)
    {
        UnoPlayer.Color c = player.callColor(cards);
        if (c == UnoPlayer.Color.NONE)
        {
            String message = playerName + " has called an illegal color\n";
            message += "Your hand: " + cards + "\n";
            message += "Played card: " + game.getUpCard() + "\n";
            message += "called color: " + c + "\n";
            throw new IllegalArgumentException(message);
        }
        return c;
    }

    /**
     * @return true only if this Hand has no cards, which should trigger a
     *         winning condition.
     */
    public boolean isEmpty()
    {
        return cards.size() == 0;
    }

    /**
     * @return a string rendering of this Hand. See Card::toString() for notes
     *         about how individual cards are rendered.
     */
    public String toString()
    {
        String retval = "";
        for (int i = 0; i < cards.size(); i++)
        {
            retval += cards.get(i);
            if (i < cards.size() - 1)
            {
                retval += ",";
            }
        }
        return retval;
    }

    /**
     * @return the forfeit value of this Hand, as it now stands (in other words,
     *         the sum of all the forfeit values of cards still possessed.).
     */
    public int countCards()
    {
        int total = 0;
        for (int i = 0; i < cards.size(); i++)
        {
            total += cards.get(i).forfeitCost();
        }
        return total;
    }

    /**
     * @return the name of the contestant.
     */
    public String getPlayerName()
    {
        return playerName;
    }

    /**
     * @return a copy of the cards ArrayList.
     */
    public ArrayList<Card> copyCards()
    {
        ArrayList<Card> copyOfCards = new ArrayList<Card>();
        for (Card card : cards)
        {
            copyOfCards.add(card);
        }
        return copyOfCards;
    }

    /**
     * Check for the legality of a play.
     * 
     * @param cards
     *            The cards in the player's hand.
     * @param playedCard
     *            The card played.
     * @param topCard
     *            The top card of the discard pile.
     * @param colorToPlay
     *            The color that must be played.
     */
    public void verify(ArrayList<Card> cards, int playedCard, Card topCard,
        UnoPlayer.Color colorToPlay)
    {
        Card possibleCard;
        boolean ok = true;
        if (playedCard >= cards.size())
        {
            ok = false;
        }
        else if (playedCard != -1)
        {
            possibleCard = cards.get(playedCard);
            ok = possibleCard.canPlayOn(topCard, colorToPlay);
        }
        else
        {
            for (Card c : cards)
            {
                if (c.canPlayOn(topCard, colorToPlay))
                {
                    ok = false;
                }
            }
        }
        if (!ok)
        {
            String message = playerName + " has made an illegal play\n";
            message += "Your hand: " + cards + "\n";
            message += "Top card: " + topCard + "\n";
            message += "called color: " + colorToPlay + "\n";
            message += "played index: " + playedCard + "\n";
            throw new IllegalArgumentException(message);
        }
    }
}
