import java.util.List;

import uno.Card;
import uno.GameState;
import uno.UnoPlayer;

/**
 * This is the template Uno Player class.
 * 
 * @author Jonathan Ward <-- Change the author!
 * @version 2013-08-31
 */
public class MyUnoPlayer implements UnoPlayer
{
    int blueCounter;
    int greenCounter;
    int redCounter;
    int yellowCounter;


    /**
     * moveChoice - This method is called when it's 
     * your turn and you need to choose
     * what card to moveChoice.
     * 
     * @param hand
     *            The hand parameter tells you what's in your hand. You can call
     *            getColor(), getRank(), and getNumber() on each of the cards it
     *            contains to see what it is. The color will be the color of the
     *            card, or "Color.NONE" if the card is a wild card. The rank
     *            will be "Rank.NUMBER" for all numbered cards, and another
     *            value (e.g., "Rank.SKIP," "Rank.REVERSE," etc.) for special
     *            cards. The value of a card's "number" only has meaning if it
     *            is a number card. (Otherwise, it will be -1.)
     * 
     * @param upCard
     *            The upCard parameter works the same way, and tells you what
     *            the up card (in the middle of the table) is.
     * 
     * @param calledColor
     *            The calledColor parameter only has meaning if the up card is a
     *            wild, and tells you what color the moveChoiceer 
     *            who moveChoiceed that wild
     *            card called.
     * 
     * @param state
     *            The state parameter is a GameState object on which you can
     *            invoke methods if you choose to access certain detailed
     *            information about the game (like who is currently ahead, what
     *            colors each moveChoiceer has recently called, etc.)
     * 
     * @return You must return a value from this method indicating which card
     *         you wish to moveChoice. If you 
     *         return a number 0 or greater, that means
     *         you want to moveChoice the card 
     *         at that index. If you return -1, that
     *         means that you cannot moveChoice 
     *         any of your cards (none of them are
     *         legal moveChoices) in which case
     *          you will be forced to draw a card
     *         (this will happen automatically for you.)
     * 
     */
    public int play(List<Card> hand, Card upCard, Color calledColor,
        GameState state)
    {
        Color upCardColor;
        int[] array = state.getNumCardsInHandsOfPlayers();
        int moveChoiceerNext = array[0];
        int moveChoiceerBefore = array[2];

        if (upCard.getRank() == Rank.WILD || upCard.getRank() == Rank.WILD_D4)
        {
            upCardColor = calledColor;
        }
        else
        {
            upCardColor = upCard.getColor();
        }

        int numLegalCards = 0;
        for (int i = 0; i < hand.size(); i++) 
        {
            if (hand.get(i).getColor() == upCardColor) 
            {
                numLegalCards++;
            }
            else if (hand.get(i).getNumber() == upCard.getNumber() 
                && hand.get(i).getRank() == upCard.getRank() 
                && upCard.getRank() 
                != Rank.WILD && upCard.getRank() != Rank.WILD_D4) 
            {
                numLegalCards++;
            }
        }


        if (numLegalCards == 0) 
        {
            for (int i = 0; i < hand.size(); i++) 
            {
                if (hand.get(i).getRank() == Rank.WILD)
                {
                    return i;
                }
            }
            for (int i = 0; i < hand.size(); i++) 
            {
                if (hand.get(i).getRank() == Rank.WILD_D4) 
                {
                    return i;
                }
            }
        }
        if (numLegalCards == 0)
        {
            return -1;
        }
        int[] validCards = new int [numLegalCards];
        int j = 0;
        for (int i = 0; i < hand.size(); i++) 
        {
            if (hand.get(i).getColor() == upCardColor) 
            {
                validCards[j] = i;
                j++;
            }
            else if (hand.get(i).getNumber() == upCard.getNumber()
                && hand.get(i).getRank() == upCard.getRank() 
                && upCard.getRank() 
                != Rank.WILD && upCard.getRank() != Rank.WILD_D4) 
            {
                validCards[j] = i;
                j++;
            }
        }

        int[] numCardsInHandsOfNextPlayers
        = state.getNumCardsInHandsOfPlayers();
        for (int i = 0; i < numLegalCards;) 
        {

            if (numCardsInHandsOfNextPlayers[0] < 5)
            {
                if (hand.get(validCards[i]).getRank() == Rank.DRAW_TWO)
                {
                    return validCards[i];
                }
            }
            if (numCardsInHandsOfNextPlayers[0] < 5) 
            {
                if (hand.get(validCards[i]).getRank() == Rank.WILD_D4)
                {
                    return validCards[i];
                }
            } 

            if (numCardsInHandsOfNextPlayers[0] < 3 
                || numCardsInHandsOfNextPlayers[1] < 3 
                || numCardsInHandsOfNextPlayers[2] < 3) 
            {
                if (hand.get(validCards[i]).getRank() == Rank.WILD)
                {
                    return validCards[i];        
                }
            } 

            if (numCardsInHandsOfNextPlayers[0] < 5 
                && numCardsInHandsOfNextPlayers[1] > 4) 
            {
                if (hand.get(validCards[i]).getRank() == Rank.SKIP)
                {
                    return validCards[i];
                }
            }
            boolean doIHaveAGreen = false;
            boolean doIHaveABlue = false;
            boolean doIHaveAYellow = false;
            boolean doIHaveARed = false;

            for (int i1 = 0; i1 < validCards.length; i1++) 
            {
                if (hand.get(validCards[i1]).getColor() == Color.GREEN)
                {
                    doIHaveAGreen = true;
                }
                if (hand.get(validCards[i1]).getColor() == Color.BLUE)
                {
                    doIHaveABlue = true;
                }
                if (hand.get(validCards[i1]).getColor() == Color.YELLOW)
                {
                    doIHaveAYellow = true;
                }
                if (hand.get(validCards[i1]).getColor() == Color.RED)
                {
                    doIHaveARed = true;
                }
            }

            int yellow = 0;
            int green = 0;
            int red = 0;
            int blue = 0;

            for (int i1 = 0; i1 < hand.size(); i1++) 
            {
                if (doIHaveAGreen == true) 
                {
                    if (hand.get(i1).getColor() == Color.GREEN)
                    {
                        green++;
                    }
                }
                if (doIHaveABlue == true) 
                {
                    if (hand.get(i1).getColor() == Color.BLUE)
                    {
                        blue++;
                    }
                }
                if (doIHaveAYellow == true) 
                {
                    if (hand.get(i1).getColor() == Color.YELLOW)
                    {
                        yellow++;
                    }
                }
                if (doIHaveARed == true) 
                {
                    if (hand.get(i1).getColor() == Color.RED)
                    {
                        red++;
                    }
                }
            }

            int max = 0;
            Color maxColor = Color.GREEN;
            if (green > max) 
            {
                max = green;
                maxColor = Color.GREEN;
            }
            if (blue > max) 
            {
                max = blue;
                maxColor = Color.BLUE;
            }
            if (yellow > max) 
            {
                max = yellow;
                maxColor = Color.YELLOW;
            }
            if (red > max) 
            {
                max = red;
                maxColor = Color.RED;
            }


            int moveChoice1 = 0;
            for (int i1 = 0; i1 < numLegalCards; i1++) 
            {
                if (hand.get(validCards[i1]).getColor() == maxColor) 
                {
                    if (hand.get(validCards[i1]).getNumber() == 0)
                    {
                        moveChoice1 = validCards[i1];
                    }
                    if (hand.get(validCards[i1]).getNumber() == 1)
                    {
                        moveChoice1 = validCards[i1];
                    }
                    if (hand.get(validCards[i1]).getNumber() == 2)
                    {
                        moveChoice1 = validCards[i1];
                    }
                    if (hand.get(validCards[i1]).getNumber() == 3)
                    {
                        moveChoice1 = validCards[i1];
                    }
                    if (hand.get(validCards[i1]).getNumber() == 4)
                    {
                        moveChoice1 = validCards[i1];
                    }
                    if (hand.get(validCards[i1]).getNumber() == 5)
                    {
                        moveChoice1 = validCards[i1];
                    }
                    if (hand.get(validCards[i1]).getNumber() == 6)
                    {
                        moveChoice1 = validCards[i1];
                    }
                    if (hand.get(validCards[i1]).getNumber() == 7)
                    {
                        moveChoice1 = validCards[i1];
                    }
                    if (hand.get(validCards[i1]).getNumber() == 8)
                    {
                        moveChoice1 = validCards[i1];
                    }
                    if (hand.get(validCards[i1]).getNumber() == 9)
                    {
                        moveChoice1 = validCards[i1];
                    }
                    if (hand.get(validCards[i1]).getRank() == Rank.REVERSE)
                    {
                        moveChoice1 = validCards[i1];
                    }
                    if (hand.get(validCards[i1]).getRank() == Rank.SKIP)
                    {
                        moveChoice1 = validCards[i1];
                    }
                    if (hand.get(validCards[i1]).getRank() == Rank.DRAW_TWO)
                    {
                        moveChoice1 = validCards[i1]; 
                    }
                } 
            }
            return moveChoice1;
        }


        for (int i1 = 0; i1 < hand.size(); i1++)
        {

            if (hand.get(i1).canPlayOn(upCard, calledColor) == true)
            {


                if (hand.get(i1).getRank().equals(Rank.REVERSE) 
                    && moveChoiceerNext < moveChoiceerBefore)
                {
                    return i1;
                }

                if (hand.get(i1).getRank().equals(Rank.SKIP) 
                    || hand.get(i1).getRank().equals(Rank.DRAW_TWO) 
                    && moveChoiceerNext < hand.size())
                {
                    return i1;
                }
                if (hand.get(i1).getColor() == colorCount(hand))
                {
                    return i1;
                }

                if (hand.get(i1).getRank() == Rank.WILD_D4)
                {
                    return i1;
                }
                if (hand.get(i1).getRank() == Rank.WILD)
                {
                    return i1;
                }
                if (hand.get(i1).getRank() == Rank.DRAW_TWO)
                {
                    return i1;
                }
                if (hand.get(i1).getNumber() == 9)
                {
                    return i1;
                }
                if (hand.get(i1).getNumber() == 8)
                {
                    return i1;
                }
                if (hand.get(i1).getNumber() == 7)
                {
                    return i1;
                }
                if (hand.get(i1).getNumber() == 6)
                {
                    return i1;
                }
                if (hand.get(i1).getNumber() == 5)
                {
                    return i1;
                }
                if (hand.get(i1).getNumber() == 4)
                {
                    return i1;
                }
                if (hand.get(i1).getNumber() == 3)
                {
                    return i1;
                }
                if (hand.get(i1).getNumber() == 2)
                {
                    return i1;
                }

                else
                {
                    return i1;
                }
            }


        }
        return -1;
    }

    /**
     * callColor - This method will be 
     * called when you have just moveChoiceed a wild
     * card, and is your way of specifying which color you want to change it to.
     * 
     * @param hand
     *            Your current hand
     * 
     * @return You must return a valid Color value from this method. You must
     *         not return the value Color.NONE under any circumstances.
     */
    @SuppressWarnings("unused")
    public Color callColor(List<Card> hand)
    {

        return colorCount(hand);
    }




    /**
     * 
     * @param hand is arrayList of called hand.
     * @return Color
     */
    public Color colorCount(List<Card> hand)
    {
        blueCounter = 0;
        greenCounter = 0;
        yellowCounter = 0;
        redCounter = 0;


        for (int i = 0; i < hand.size(); i++)
        {

            if (hand.get(i).getColor() == Color.BLUE)
            {
                blueCounter++;
            }
            if (hand.get(i).getColor() == Color.RED)
            {
                redCounter++;
            }
            if (hand.get(i).getColor() == Color.YELLOW)
            {
                yellowCounter++;
            }
            if (hand.get(i).getColor() == Color.GREEN)
            {
                greenCounter++;
            }

        }
        if (blueCounter > redCounter 
            && blueCounter > greenCounter 
            && blueCounter > yellowCounter)
        {
            return Color.BLUE;
        }
        else if (redCounter > blueCounter 
            && redCounter > greenCounter 
            && redCounter > yellowCounter)
        {
            return Color.RED;
        }
        else if (greenCounter > blueCounter 
            && greenCounter > yellowCounter 
            && greenCounter > redCounter)
        {
            return Color.GREEN;
        }
        else if (yellowCounter > blueCounter 
            && yellowCounter > greenCounter 
            && yellowCounter > redCounter)
        {
            return Color.YELLOW;
        }
        else 
        {
            return Color.GREEN;            
        }
    }
}












