package uno;

import java.util.ArrayList;
import java.util.Random;

/**
 * <p>
 * A Deck of Uno cards, consisting of both a draw and discard pile. The methods
 * draw() and discard() act in concert to (a) remove and return the top card of
 * the Deck, and (b) recycle a previously drawn Card to the discard pile.
 * remix() puts all discarded cards back in play and reshuffles the deck.
 * </p>
 * <p>
 * The values of static member variables can be changed to adjust the content of
 * the deck.
 * </p>
 * 
 * @author Stephen Davies
 * @author Mitch Parry
 * @version 2013-08-15
 */
public class Deck
{

    /**
     * The number of non-zero number cards of each suit in the deck.
     */
    public static final int NUMBER_OF_DUP_REGULAR_CARDS = 2;

    /**
     * The number of zero number cards of each suit in the deck.
     */
    public static final int NUMBER_OF_DUP_ZERO_CARDS = 1;

    /**
     * The number of "special" cards (aka "action cards") of each suit in the
     * deck. These include, for instance, Skips and Reverses.
     */
    public static final int NUMBER_OF_DUP_SPECIAL_CARDS = 2;

    /**
     * The number of (standard, non-Draw-4) wild cards in the deck.
     */
    public static final int NUMBER_OF_WILD_CARDS = 4;

    /**
     * The number of Wild-Draw-4 in the deck.
     */
    public static final int NUMBER_OF_WILD_D4_CARDS = 4;

    private ArrayList<Card> cards = new ArrayList<Card>();
    private ArrayList<Card> discardedCards = new ArrayList<Card>();
    private Random rand;

    /**
     * Constructor for a new, full, shuffled Deck.
     */
    public Deck()
    {
        rand = new Random();
        fillDeck();
        shuffle();
    }

    /**
     * Fills the regular cards in the deck.
     */
    private void fillRegular()
    {
        for (int i = 1; i <= 9; i++)
        {
            for (int j = 0; j < NUMBER_OF_DUP_REGULAR_CARDS; j++)
            {
                cards.add(new Card(UnoPlayer.Color.RED, i));
                cards.add(new Card(UnoPlayer.Color.YELLOW, i));
                cards.add(new Card(UnoPlayer.Color.BLUE, i));
                cards.add(new Card(UnoPlayer.Color.GREEN, i));
            }
        }
    }

    /**
     * Fills the zero cards in the deck.
     */
    private void fillZeros()
    {
        // There are fewer "0" cards than other numbers.
        for (int j = 0; j < NUMBER_OF_DUP_ZERO_CARDS; j++)
        {
            cards.add(new Card(UnoPlayer.Color.RED, 0));
            cards.add(new Card(UnoPlayer.Color.YELLOW, 0));
            cards.add(new Card(UnoPlayer.Color.BLUE, 0));
            cards.add(new Card(UnoPlayer.Color.GREEN, 0));
        }
    }

    /**
     * Fills the special cards in the deck.
     */
    private void fillSpecial()
    {
        for (int j = 0; j < NUMBER_OF_DUP_SPECIAL_CARDS; j++)
        {
            cards.add(new Card(UnoPlayer.Color.RED, UnoPlayer.Rank.SKIP));
            cards.add(new Card(UnoPlayer.Color.YELLOW, UnoPlayer.Rank.SKIP));
            cards.add(new Card(UnoPlayer.Color.GREEN, UnoPlayer.Rank.SKIP));
            cards.add(new Card(UnoPlayer.Color.BLUE, UnoPlayer.Rank.SKIP));
            cards.add(new Card(UnoPlayer.Color.RED, UnoPlayer.Rank.REVERSE));
            cards.add(new Card(UnoPlayer.Color.YELLOW, UnoPlayer.Rank.REVERSE));
            cards.add(new Card(UnoPlayer.Color.GREEN, UnoPlayer.Rank.REVERSE));
            cards.add(new Card(UnoPlayer.Color.BLUE, UnoPlayer.Rank.REVERSE));
            cards.add(new Card(UnoPlayer.Color.RED, UnoPlayer.Rank.DRAW_TWO));
            cards.add(new Card(UnoPlayer.Color.YELLOW,
                    UnoPlayer.Rank.DRAW_TWO));
            cards.add(new Card(UnoPlayer.Color.GREEN, UnoPlayer.Rank.DRAW_TWO));
            cards.add(new Card(UnoPlayer.Color.BLUE, UnoPlayer.Rank.DRAW_TWO));
        }
    }

    /**
     * Fills the wild cards in the deck.
     */
    private void fillWild()
    {
        for (int i = 0; i < NUMBER_OF_WILD_CARDS; i++)
        {
            cards.add(new Card(UnoPlayer.Color.NONE, UnoPlayer.Rank.WILD));
        }
        for (int i = 0; i < NUMBER_OF_WILD_D4_CARDS; i++)
        {
            cards.add(new Card(UnoPlayer.Color.NONE, UnoPlayer.Rank.WILD_D4));
        }
    }

    /**
     * Fills the deck.
     */
    private void fillDeck()
    {
        fillRegular();
        fillZeros();
        fillSpecial();
        fillWild();
    }

    /**
     * When performed on a Deck that is <i>at least partially filled,</i>,
     * shuffles its cards randomly. Note that this does not fill the Deck, nor
     * does it combine any outstanding cards into the Deck.
     */
    public void shuffle()
    {
        for (int i = cards.size() - 1; i > 0; i--)
        {
            int x = rand.nextInt(i + 1);
            Card temp = cards.get(i);
            cards.set(i, cards.get(x));
            cards.set(x, temp);
        }
    }

    /**
     * Makes the first card not a wild.
     */
    public void makeFirstCardNonWild()
    {
        int lastIndex = cards.size() - 1;
        while (cards.get(lastIndex).followedByCall())
        {
            int x = rand.nextInt(lastIndex);
            Card temp = cards.get(lastIndex);
            cards.set(lastIndex, cards.get(x));
            cards.set(x, temp);
        }
    }

    /**
     * Determines if the deck is empty.
     * 
     * @return true only if the Deck currently has no cards.
     */
    public boolean isEmpty()
    {
        return cards.size() == 0;
    }

    /**
     * Draws the top card from the deck.
     * 
     * @return the top card of the Deck, and removes it.
     * 
     * @throws EmptyDeckException
     *             When called on an empty deck.
     * @see isEmpty
     */
    public Card draw() throws EmptyDeckException
    {
        if (cards.size() == 0)
        {
            throw new EmptyDeckException();
        }
        return cards.remove(cards.size() - 1);
    }

    /**
     * Discard the Card object passed so that it is out of play until a remix
     * operation.
     * 
     * @param c
     *            the card to discard
     */
    public void discard(Card c)
    {
        discardedCards.add(c);
    }

    /**
     * Remixes the deck by adding all previously discarded cards back into the
     * deck, and shuffling.
     */
    public void remix()
    {
        cards.addAll(discardedCards);
        discardedCards.clear();
        shuffle();
    }

    /**
     * Returns the list of discarded Cards since the last remix.
     * 
     * @return ArrayList<Card> A list of all Cards that have been discarded (via
     *         discard()) since the last Deck remix.
     */
    ArrayList<Card> getDiscardedCards()
    {
        return discardedCards;
    }

    /**
     * Main method to test the deck.
     * 
     * @param args
     *            not used
     */
    public static void main(String args[])
    {
        System.out.println("test Deck.");
        Deck d = new Deck();
        while (!d.isEmpty())
        {
            try
            {
                System.out.println(d.draw());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
