package uno;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * <p>
 * A Game object represents a single game of Uno in an overall match (of
 * possibly many games). Games are instantiated by providing them with a roster
 * of players, including a Scoreboard object through which scores can be
 * accumulated. The play() method then kicks off the game, which will proceed
 * from start to finish and update the Scoreboard. Various aspects of the game's
 * state (<i>e.g.</i>, whether the direction of play is currently clockwise or
 * counterclockwise, whose player's turn is next) can be accessed and controlled
 * through methods on this class.
 * </p>
 * <p>
 * A GameState object can be obtained through the getGameState() method, which
 * allows UnoPlayers to selectively and legally examine certain aspects of the
 * game's state.
 * </p>
 * 
 * @author Stephen Davies
 * @author Mitch Parry
 * @version 2013-08-25
 */
public class Game
{

    /**
     * The number of cards each player will be dealt at start of game.
     */
    static final int INIT_HAND_SIZE = 7;

    /**
     * 
     * Direction of the game.
     * 
     * @author Alice McRae
     */
    public enum Direction
    {
        FORWARDS, BACKWARDS
    };

    /**
     * An object representing the state of the game at any point in time. Note
     * that much of the "state" is represented in the Game object itself, and
     * that this object provides a double-dispatch vehicle through which select
     * methods can access that state.
     */

    /*
     * package-visibility variables to simplify access between Game and
     * GameState classes
     */
    Deck deck;
    Hand h[];
    Card upCard;
    Direction direction;
    int currPlayer;
    UnoPlayer.Color calledColor;
    Scoreboard scoreboard;
    ArrayList<Turn> gameSummary;
    UnoPlayer.Color mostRecentColorCalled[];
    Card firstCard;
    boolean printVerbose;

    /**
     * Main constructor to instantiate a Game of Uno. Provided must be two
     * objects indicating the player roster: a Scoreboard, and a class list.
     * This constructor will deal hands to all players, select a non-action up
     * card, and reset all game settings so that play() can be safely called.
     * 
     * @param scoreboard
     *            A fully-populated Scoreboard object that contains the names of
     *            the contestants, in order.
     * @param playerClassList
     *            An array of Strings, each of which is a fully-qualified
     *            package/class name of a class that implements the UnoPlayer
     *            interface.
     * @param printVerbose
     *            Determines whether or not to print each play.
     */
    public Game(Scoreboard scoreboard, ArrayList<String> playerClassList,
            boolean printVerbose)
    {
        this.scoreboard = scoreboard;
        this.printVerbose = printVerbose;
        deck = new Deck();
        h = new Hand[scoreboard.getNumPlayers()];
        gameSummary = new ArrayList<Turn>();
        mostRecentColorCalled = new UnoPlayer.Color[scoreboard.getNumPlayers()];
        dealInitialHands(playerClassList);
        direction = Direction.FORWARDS;
        currPlayer = new java.util.Random().nextInt(scoreboard.getNumPlayers());
        calledColor = UnoPlayer.Color.NONE;
        firstCard = upCard;
    }

    /**
     * Deals the initial hands.
     * 
     * @param playerClassList
     *            An array of Strings, each of which is a fully-qualified
     *            package/class name of a class that implements the UnoPlayer
     *            interface
     */
    private void dealInitialHands(ArrayList<String> playerClassList)
    {
        try
        {
            for (int i = 0; i < scoreboard.getNumPlayers(); i++)
            {
                h[i] = new Hand(playerClassList.get(i),
                        scoreboard.getPlayerList()[i]);
                for (int j = 0; j < INIT_HAND_SIZE; j++)
                {
                    h[i].addCard(deck.draw());
                }
            }
            deck.makeFirstCardNonWild();
            upCard = deck.draw();
            while (upCard.followedByCall())
            {
                deck.discard(upCard);
                upCard = deck.draw();
            }
        }
        catch (Exception e)
        {
            System.out.println("Can't deal initial hands!");
            System.exit(1);
        }

    }

    /**
     * Return the number of the <i>next</i> player to play, provided the current
     * player doesn't jack that up by playing an action card.
     * 
     * @return An integer from 0 to scoreboard.getNumPlayers()-1.
     */
    public int getNextPlayer()
    {
        if (direction == Direction.FORWARDS)
        {
            return (currPlayer + 1) % scoreboard.getNumPlayers();
        }
        else
        {
            if (currPlayer == 0)
            {
                return scoreboard.getNumPlayers() - 1;
            }
            else
            {
                return currPlayer - 1;
            }
        }
    }

    /**
     * Go ahead and advance to the next player.
     */
    void advanceToNextPlayer()
    {
        currPlayer = getNextPlayer();
    }

    /**
     * Change the direction of the game from clockwise to counterclockwise. (or
     * vice versa.)
     */
    void reverseDirection()
    {
        if (direction == Direction.FORWARDS)
        {
            direction = Direction.BACKWARDS;
        }
        else
        {
            direction = Direction.FORWARDS;
        }
    }

    /**
     * Attempts to play a card using the current UnoPlayer.
     * 
     * @return the card played
     */
    private Card currPlayerPlay()
    {
        Card playedCard = null;
        try
        {
            playedCard = h[currPlayer].play(this);
        }
        catch (Exception e)
        {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            throw new IllegalArgumentException("Exception in code for player "
                    + h[currPlayer].getPlayerName() + "\n" + errors + "\n");
            // System.exit(1);
        }
        return playedCard;
    }

    /**
     * Attempts to draw and potentially play a card using the current UnoPlayer.
     * 
     * @return The card played after drawing (if any)
     * @throws EmptyDeckException
     *             when there are no cards to draw from deck
     */
    private Card currPlayerDraw() throws EmptyDeckException
    {
        Card drawnCard;
        Card playedCard;
        try
        {
            drawnCard = deck.draw();
        }
        catch (Exception e)
        {
            print("...deck exhausted, remixing...");
            deck.remix();
            drawnCard = deck.draw();
        }
        h[currPlayer].addCard(drawnCard);
        print(" has to draw (" + drawnCard + ").");
        playedCard = h[currPlayer].play(this);
        return playedCard;
    }

    /**
     * Updates the deck, upCard, and current called color.
     * 
     * @param playedCard
     *            The card just played (must not be null)
     * @param currentCall
     *            The last called color
     * @return The new called color (could be unchanged)
     */
    private UnoPlayer.Color updateGame(Card playedCard,
            UnoPlayer.Color currentCall)
    {
        print(" plays " + playedCard + " on " + upCard + ".");
        deck.discard(upCard);
        upCard = playedCard;
        if (upCard.followedByCall())
        {
            calledColor = h[currPlayer].callColor(this);
            currentCall = calledColor;
            mostRecentColorCalled[currPlayer] = calledColor;
            print(" (and calls " + calledColor + ").");
        }
        else
        {
            calledColor = UnoPlayer.Color.NONE;
        }
        return currentCall;
    }

    /**
     * Check if the current player has won.
     * 
     * @return true if the current player has won
     */
    private boolean isWin()
    {
        if (h[currPlayer].isEmpty())
        {
            int roundPoints = 0;
            for (int j = 0; j < scoreboard.getNumPlayers(); j++)
            {
                roundPoints += h[j].countCards();
            }
            println("\n" + h[currPlayer].getPlayerName()
                    + " wins! (and collects " + roundPoints + " points.)");
            scoreboard.addToScore(currPlayer, roundPoints);
            println("---------------\n" + scoreboard);
            return true;
        }
        return false;
    }

    /**
     * Advances to the next player after performing card effects.
     * 
     * @param playedCard
     *            The card just played
     * @throws EmptyDeckException
     *             if the deck is empty
     */
    private void advance(Card playedCard) throws EmptyDeckException
    {
        if (playedCard != null)
        {
            playedCard.performCardEffect(this);
        }
        else
        {
            advanceToNextPlayer();
        }
    }

    /**
     * The current player takes a turn.
     * 
     * @param currentCall
     *            The current called color
     * @return the potentially changed current color after this turn
     * @throws EmptyDeckException
     *             if the deck is empty
     */
    private UnoPlayer.Color takeTurn(UnoPlayer.Color currentCall)
        throws EmptyDeckException
    {
        boolean drawing = false;
        print(h[currPlayer].getPlayerName() + " (" + h[currPlayer] + ")");
        Card playedCard = currPlayerPlay();
        if (playedCard == null)
        {
            drawing = true;
            playedCard = currPlayerDraw();
        }
        if (playedCard != null)
        {
            currentCall = updateGame(playedCard, currentCall);
        }

        gameSummary.add(new Turn(currPlayer, playedCard, currentCall, drawing));

        // Check win
        if (isWin())
        {
            return currentCall;
        }
        // Check Uno
        if (h[currPlayer].size() == 1)
        {
            print(" UNO!");
        }
        println("");
        advance(playedCard);
        return currentCall;
    }

    /**
     * Play an entire Game of Uno from start to finish. Hands should have
     * already been dealt before this method is called, and a valid up card
     * turned up. When the method is completed, the Game's scoreboard object
     * will have been updated with new scoring favoring the winner.
     */
    public void play()
    {
        UnoPlayer.Color currentCall = UnoPlayer.Color.NONE;
        println("Initial upcard is " + upCard + ".");
        try
        {
            while (true)
            {
                currentCall = takeTurn(currentCall);
                if (isWin())
                {
                    return;
                }
            }
        }
        catch (EmptyDeckException e)
        {
            System.out.println("Deck exhausted! This game is a draw.");
        }
    }

    /**
     * Custom print command that only prints in verbose mode.
     * 
     * @param s
     *            The string to print
     */
    void print(String s)
    {
        if (printVerbose)
        {
            System.out.print(s);
        }
    }

    /**
     * Custom println command that only prints in verbose mode.
     * 
     * @param s
     *            The string to print
     */
    void println(String s)
    {
        if (printVerbose)
        {
            System.out.println(s);
        }
    }

    /**
     * Return the GameState object, through which the state of the game can be
     * accessed and safely manipulated.
     * 
     * @return The current GamesState
     */
    public GameState getGameState()
    {

        return new GameState(this);
    }

    /**
     * Return the Card that is currently the "up card" in the game.
     * 
     * @return The current up card
     */
    public Card getUpCard()
    {
        return upCard;
    }
}
