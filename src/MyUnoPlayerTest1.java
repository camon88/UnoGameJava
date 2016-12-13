import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

import uno.Card;
import uno.GameState;
import uno.UnoPlayer;

/**
 * JUnit test class for the MyUnoPlayer.
 * 
 * @author Stephen Davies
 * @author Mitch Parry
 * @version 2013-08-24
 * 
 */
public class MyUnoPlayerTest1
{
    private UnoPlayer thePlayer = new MyUnoPlayer();

    /**
     * One test for the play method.
     */
    @Test
    public void testPlay1()
    {
        List<Card> hand = new ArrayList<Card>();
        hand.add(new Card(UnoPlayer.Color.RED, UnoPlayer.Rank.NUMBER, 4));
        hand.add(new Card(UnoPlayer.Color.GREEN, UnoPlayer.Rank.NUMBER, 7));
        hand.add(new Card(UnoPlayer.Color.GREEN, UnoPlayer.Rank.REVERSE, -1));
        hand.add(new Card(UnoPlayer.Color.BLUE, UnoPlayer.Rank.NUMBER, 2));
        hand.add(new Card(UnoPlayer.Color.BLUE, UnoPlayer.Rank.SKIP, -1));
        hand.add(new Card(UnoPlayer.Color.NONE, UnoPlayer.Rank.WILD, -1));

        Card upCard = new Card(UnoPlayer.Color.RED, UnoPlayer.Rank.NUMBER, 7);
        int cardPlayed = thePlayer.play(hand, upCard, UnoPlayer.Color.RED, new
                GameState(upCard));

        // Let's see whether the card played was legit.
        assertTrue("Player didn't think a card could be played.\nThis is an "
                + "error, since cards 0, 1, and 5 are legal plays.",
                cardPlayed >= 0);
        assertTrue("Player tried to play " + hand.get(cardPlayed)
                + ", which is an error.", cardPlayed == 0 || cardPlayed == 1
                || cardPlayed == 5);
        System.out.println("Player played " + hand.get(cardPlayed));
    }

    /**
     * Test cases 1 - 4000.
     */
    @Test
    public void testCaseProcessor1()
    {
        final int START = 1;
        final int STOP = 1;
        try
        {
            testCases(START, STOP);
        }
        catch (FileNotFoundException e)
        {
            fail("testCases.txt not found.");
        }
        catch (IOException e)
        {
            String error = "Failed to read testCases.txt: \n"
                    + e.getStackTrace();
            fail(error);
        }
    }

    /**
     * Test cases 4001 - 7000.
     */
    @Test
    public void testCaseProcessor2()
    {
        final int START = 2;
        final int STOP = 2;
        try
        {
            testCases(START, STOP);
        }
        catch (FileNotFoundException e)
        {
            fail("testCases.txt not found.");
        }
        catch (IOException e)
        {
            String error = "Failed to read testCases.txt: \n"
                    + e.getStackTrace();
            fail(error);
        }
    }

    /**
     * Test cases 7001 - 10000.
     */
    @Test
    public void testCaseProcessor3()
    {
        final int START = 3;
        final int STOP = 3;
        try
        {
            testCases(START, STOP);
        }
        catch (FileNotFoundException e)
        {
            fail("testCases.txt not found.");
        }
        catch (IOException e)
        {
            String error = "Failed to read testCases.txt: \n"
                    + e.getStackTrace();
            fail(error);
        }
    }

    /**
     * Tests a subset of all cases from start to finish.
     * 
     * @param start
     *            The starting test case
     * @param stop
     *            The last test case
     * @throws FileNotFoundException
     *             If the testCases.txt file does not exist.
     * @throws IOException
     *             if the file doesn't have enough lines.
     */
    private void testCases(int start, int stop) throws FileNotFoundException,
        IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(
                "testCases.txt"));
        int n = 1;
        String handLine = null;
        while ((handLine = br.readLine()) != null)
        {
            String upCardLine = br.readLine();
            String calledColorLine = br.readLine();
            String validPlaysLine = br.readLine();
            // consume --------- delimiter
            br.readLine();

            if (n >= start && n <= stop)
            {
                // run n-th test
                testStrings(handLine, upCardLine, calledColorLine,
                        validPlaysLine, n);
            }
            n++;
        }
        br.close();
    }

    /**
     * Prints feedback to help debug a failed test based on played card.
     * 
     * @param hand
     *            The hand.
     * @param upCard
     *            The up card.
     * @param calledColor
     *            The called color.
     * @param validPlays
     *            All valid plays.
     * @param cardPlayed
     *            The actual card played.
     * @return the error string
     */
    private String printCardError(List<Card> hand, Card upCard,
            UnoPlayer.Color calledColor, ArrayList<Integer> validPlays,
            int cardPlayed)
    {
        String s = "";
        s += "Whoops -- your play() method has an error!\n";
        s += "You were given this hand:\n";
        for (int i = 0; i < hand.size(); i++)
        {
            s += "  " + i + ". " + hand.get(i) + "\n";
        }

        s += "and the up card was: " + upCard + "\n";
        if (upCard.getRank() == UnoPlayer.Rank.WILD
                || upCard.getRank() == UnoPlayer.Rank.WILD_D4)
        {
            s += "and the called color was: " + calledColor + "\n";
        }
        s += "and you (wrongly) returned " + cardPlayed + "." + "\n";
        s += "Valid plays would have included: ";
        for (int i = 0; i < validPlays.size(); i++)
        {
            s += validPlays.get(i);
            if (i < validPlays.size() - 1)
            {
                s += ",";
            }
        }
        s += "\n";
        return s;
    }

    /**
     * Prints feedback to help debug a failed test based on a called color.
     * 
     * @param hand
     *            The hand.
     * @param upCard
     *            The up card.
     * @param calledColor
     *            The previous called color.
     * @param validPlays
     *            All valid plays.
     * @param newColor
     *            The new color called.
     * @return the error string
     */
    private String printColorError(List<Card> hand, Card upCard,
            UnoPlayer.Color calledColor, ArrayList<Integer> validPlays,
            UnoPlayer.Color newColor)
    {
        String s = "";
        s += "Whoops -- your callColor() method has an error!\n";
        s += "You were given this hand:\n";
        for (int i = 0; i < hand.size(); i++)
        {
            s += "  " + i + ". " + hand.get(i) + "\n";
        }

        s += "and the up card was: " + upCard + "\n";
        if (upCard.getRank() == UnoPlayer.Rank.WILD
                || upCard.getRank() == UnoPlayer.Rank.WILD_D4)
        {
            s += "and the called color was: " + calledColor + "\n";
        }
        s += "and you (wrongly) returned " + newColor + ".\n";
        return s;
    }

    /**
     * Tests the player using the hand, upCard, calledColor, and validPlays.
     * 
     * @param hand
     *            The players hand
     * @param upCard
     *            The current up card
     * @param calledColor
     *            The previously called color
     * @param validPlays
     *            The valid plays
     * @param n
     *            the test number.
     */
    private void testHand(List<Card> hand, Card upCard,
            UnoPlayer.Color calledColor, ArrayList<Integer> validPlays, int n)
    {
        int cardPlayed = thePlayer.play(hand, upCard, calledColor,
                new GameState(upCard));

        if (!validPlays.contains(new Integer(cardPlayed)))
        {
            String error = printCardError(hand, upCard, calledColor,
                    validPlays, cardPlayed);
            fail("Failed test " + n + ".\n" + error);
        }

        UnoPlayer.Color color = thePlayer.callColor(hand);

        if (color != UnoPlayer.Color.RED && color != UnoPlayer.Color.BLUE
                && color != UnoPlayer.Color.GREEN
                && color != UnoPlayer.Color.YELLOW)
        {
            String error = printColorError(hand, upCard, calledColor,
                    validPlays, color);
            fail("Failed test " + n + ".\n" + error);
        }

    }

    /**
     * Read a hand from its string representation.
     * 
     * @param handLine
     *            The string representation for the hand
     * @return the hand as an arraylist of cards
     */
    private ArrayList<Card> readHandString(String handLine)
    {
        ArrayList<Card> hand = new ArrayList<Card>();
        Scanner handLineScanner = new Scanner(handLine);
        handLineScanner.useDelimiter(",");
        while (handLineScanner.hasNext())
        {
            String cardString = handLineScanner.next();
            Card card = readCardString(cardString);
            hand.add(card);
        }
        handLineScanner.close();
        return hand;
    }

    /**
     * Read a card from its string representation.
     * 
     * @param cardString
     *            The String representation for the card.
     * @return The card
     */
    private Card readCardString(String cardString)
    {
        Scanner upCardLineScanner = new Scanner(cardString);
        Card card = new Card(UnoPlayer.Color.valueOf(upCardLineScanner.next()),
                UnoPlayer.Rank.valueOf(upCardLineScanner.next()),
                upCardLineScanner.nextInt());
        upCardLineScanner.close();
        return card;
    }

    /**
     * Read a comma separated list of integers from a string.
     * 
     * @param intString
     *            The string of comma separated integers
     * @return The list of integers
     */
    private ArrayList<Integer> readIntString(String intString)
    {
        ArrayList<Integer> intList = new ArrayList<Integer>();
        Scanner intStringScanner = new Scanner(intString);
        intStringScanner.useDelimiter(",");
        while (intStringScanner.hasNextInt())
        {
            intList.add(new Integer(intStringScanner.nextInt()));
        }
        intStringScanner.close();
        return intList;
    }

    /**
     * Test case from strings to represent games state.
     * 
     * @param handLine
     *            A string representing the hand.
     * @param upCardLine
     *            A string representing the up card.
     * @param calledColorLine
     *            A string representing the called color.
     * @param validPlaysLine
     *            A string representing the valid plays.
     * @param n
     *            the test number.
     */
    private void testStrings(String handLine, String upCardLine,
            String calledColorLine, String validPlaysLine, int n)
    {
        ArrayList<Card> hand = readHandString(handLine);
        Card upCard = readCardString(upCardLine);
        UnoPlayer.Color calledColor = UnoPlayer.Color.valueOf(calledColorLine);
        ArrayList<Integer> validPlays = readIntString(validPlaysLine);

        testHand(hand, upCard, calledColor, validPlays, n);
    }
}
