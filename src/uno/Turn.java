package uno;

/**
 * The Turn class describes a turn that has taken part in the game.
 * 
 * @author Alice McRae
 * @version 2013-08-25
 * 
 */
public class Turn
{
    private int id;
    private Card card;
    private UnoPlayer.Color colorCalled;
    private boolean drew;

    /**
     * Turn constructor sets all fields.
     * 
     * @param playerId
     *            The id of the current player
     * @param cardPlayed
     *            The card played
     * @param calledColor
     *            The called color
     * @param hadToDraw
     *            Whether or not the player had to draw
     */
    public Turn(int playerId, Card cardPlayed, UnoPlayer.Color calledColor,
            boolean hadToDraw)
    {
        id = playerId;
        card = cardPlayed;
        colorCalled = calledColor;
        drew = hadToDraw;
    }

    /**
     * @return the player id.
     */
    public int getPlayerId()
    {
        return id;
    }

    /**
     * 
     * @return the card played.
     */
    public Card getCardPlayed()
    {
        return card;
    }

    /**
     * 
     * @return the called color.
     */
    public UnoPlayer.Color getCalledColor()
    {
        return colorCalled;
    }

    /**
     * 
     * @return whether the player had to draw.
     */
    public boolean hadToDraw()
    {
        return drew;
    }
}
