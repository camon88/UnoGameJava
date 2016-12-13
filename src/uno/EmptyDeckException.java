package uno;

/**
 * A specific subclass of Exception indicating that an attempt was made to draw
 * a card from a Deck and no such card could be drawn despite attempts to remix.
 * 
 * @author Stephen Davies
 * @author Alice McRae
 * @version 2013-08-13
 */
public class EmptyDeckException extends Exception
{
    private static final long serialVersionUID = 1L;
}
