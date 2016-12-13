package uno;

/**
 * <p>
 * A GameState object provides programmatic access to certain (legal) aspects of
 * an Uno game, so that interested players can take advantage of that
 * information. Note that not all aspects of a game's state (<i>e.g.</i>, the
 * direction of play, whose turn it is next, the actual cards in each player's
 * hand (!), etc.) are reflected in the GameState object -- only those for which
 * it makes sense for a player to have access.
 * </p>
 * 
 * @since 2.0
 * @author Stephen Davies
 * @author Alice McRae
 * @version 2013-08-25
 */
public class GameState
{

    private Game theGame;
    private int[] numCardsInHandsOfPlayers;
    private int[] totalScoreOfPlayers;
    private Card firstCard;
    private int numPlayers;

    /**
     * Blank constructor, used only during testing.
     */
    public GameState()
    {
        firstCard = null;
        testingInitialization();
    }

    /**
     * constructor with singleton topcard, used only during testing.
     * 
     * @param topCard
     *            the top card of the deck
     */
    public GameState(Card topCard)
    {
        firstCard = topCard;
        testingInitialization();
    }

    /**
     * Instantiate a new GameState object whose job it is to provide safe access
     * to the Game object passed.
     * 
     * @param game
     *            the current game
     */
    GameState(Game game)
    {

        firstCard = game.firstCard;
        numPlayers = game.scoreboard.getNumPlayers();
        numCardsInHandsOfPlayers = new int[game.scoreboard.getNumPlayers()];
        totalScoreOfPlayers = new int[game.scoreboard.getNumPlayers()];

        if (game.direction == Game.Direction.FORWARDS)
        {
            for (int i = 0; i < game.h.length; i++)
            {
                numCardsInHandsOfPlayers[i] = game.h[i].size();
                totalScoreOfPlayers[i] = game.scoreboard.getScore(i);
            }
        }
        else
        {
            for (int i = 0; i < game.h.length; i++)
            {
                numCardsInHandsOfPlayers[i] = game.h[i].size();
                totalScoreOfPlayers[i] = game.scoreboard.getScore(i);
            }
        }
        theGame = game;
    }

    /**
     * Initialization test.
     */
    private void testingInitialization()
    {
        numPlayers = 4;
        numCardsInHandsOfPlayers = new int[4];
        totalScoreOfPlayers = new int[4];
    }

    /**
     * Gets the first card turned up.
     * 
     * @return the card turned up at the very start of the game. this does not
     *         change after a reshuffle.
     */
    public Card getFirstUpCard()
    {
        return firstCard;
    }

    /**
     * Get the number of cards for each player.
     * 
     * @return an array of ints indicating the number of cards each player has
     *         remaining. The array is ordered by playerId.
     */
    public int[] getNumCardsInHandsOfPlayers()
    {
        return numCardsInHandsOfPlayers;
    }

    /**
     * Gets the total score for each player.
     * 
     * @return an array of ints indicating the total overall score each player
     *         has. The array is ordered by playerId.
     */
    public int[] getTotalScoreOfPlayers()
    {
        return totalScoreOfPlayers;
    }

    /**
     * Gets the number of cards in the discard pile.
     * 
     * @return the number of cards, including the starting card, and the upcard,
     *         in the pile of played cards, since the deck was reshuffled
     */
    public int getNumberOfCardsInDiscardPile()
    {
        return theGame.deck.getDiscardedCards().size() + 1;
    }

    /**
     * Gets the current player id.
     * 
     * @return the player id for the current player. This is used for a player
     *         to get their own player id when it is their turn.
     */
    public int getCurrentPlayerId()
    {
        if (theGame == null)
        {
            return 0;
        }
        return theGame.currPlayer;
    }

    /**
     * Gets the number of players.
     * 
     * @return the number of players in the game.
     */
    public int getNumberOfPlayers()
    {
        return numPlayers;
    }

    /**
     * Gets the number of turns taken in the game.
     * 
     * @return the number of previous turns taken in the game, not including the
     *         current turn.
     */
    public int getNumberOfTurnsTaken()
    {
        if (theGame == null)
        {
            return 0;
        }
        return theGame.gameSummary.size();
    }

    /**
     * Get the turn from index 'value'.
     * 
     * @param value
     *            Index of the turn
     * @return information about the preceding turns in the game. The value zero
     *         refers to the first turn taken in the game, value 1 is the second
     *         turn, etc. This information allows a player to "remember" what
     *         each player has played, and when a player had to draw. returns
     *         null if value is less than zero, or value is greater than or
     *         equal to getNumberOfTurnsTaken()
     */
    public Turn getTurn(int value)
    {
        if (theGame != null)
        {
            if (value >= 0 && value < theGame.gameSummary.size())
            {
                return theGame.gameSummary.get(value);
            }
            else
            {
                return null;
            }
        }
        return null;
    }

    /**
     * Checks if the current game direction is clockise.
     * 
     * @return information about the direction of the game. Returns true, if the
     *         play is by increasing playerId, and false otherwise. When in a
     *         clockwise direction, play continues with playerIds increasing by
     *         1, cycling back to zero. In reverse direction, the play continues
     *         with playerIds decreasing by 1; after playerId 0, the largest
     *         playerId plays next (assuming no special card is played to skip a
     *         turn).
     */
    public boolean directionClockwise()
    {
        if (theGame == null)
        {
            return true;
        }
        return theGame.direction == Game.Direction.FORWARDS;
    }
}
