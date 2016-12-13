import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import uno.Game;
import uno.Scoreboard;

/**
 * <p>
 * An entire terminal-based simulation of a multi-game Uno match. Command-line
 * switches can control certain aspects of the game. Output is provided to the
 * screen about game flow and final scores.
 * </p>
 * 
 * @author Stephen Davies
 * @version 2013-08-25
 */
public class UnoSimulation
{
    /**
     * <p>
     * The name of a file (relative to working directory) containing
     * comma-separated lines, each of which contains a player name (unrestricted
     * text) and the <i>prefix</i> of the (package-less) class name (implementer
     * of UnoPlayer) that player will use as a playing strategy.
     * </p>
     * 
     * For example, if the file contained these lines:
     * 
     * <pre>
     * Fred,fsmith
     * Jane,jdoe
     * Billy,bbob
     * Thelma,tlou
     * </pre>
     * 
     * then the code would pit Fred (whose classname was "uno.fsmith_UnoPlayer")
     * against Jane (whose classname was "uno.jdoe_Unoplayer") against,
     * Billy,... etc.
     */
    public static String playerFileName = "players.txt";

    /**
     * Controls how many messages fly by the screen while narrating an Uno match
     * in text.
     */
    static boolean printVerbose = true;

    /*
     * The names ("Joe") and classes ("uno.jsmith_UnoPlayer") of competing
     * players.
     */
    private static ArrayList<String> playerNames = new ArrayList<String>();
    private static ArrayList<String> playerClasses = new ArrayList<String>();

    /**
     * Process the command line arguments.
     * 
     * @param args
     *            the command line arguments
     * @return the number of games
     */
    private static int processCommandLine(String[] args)
    {
        int numGames = 0;
        if (args.length < 2 || args.length > 3)
        {
            System.out
                    .println(
                    "Usage: UnoSimulation playerFileName numberOfGames "
                            + "[verbose|quiet].");
            System.exit(1);
        }
        playerFileName = args[0];
        numGames = Integer.valueOf(args[1]);
        if (args.length == 3 && args[2].equals("quiet"))
        {
            printVerbose = false;
        }
        if (args.length == 3 && args[2].equals("verbose"))
        {
            printVerbose = true;
        }
        return numGames;
    }

    /**
     * Load the player data from the player file.
     * 
     * @throws Exception
     *             if the file does not exist.
     */
    private static void loadPlayerData() throws Exception
    {
        BufferedReader br = new BufferedReader(new FileReader(
                playerFileName));
        String playerLine = br.readLine();
        while (playerLine != null)
        {
            Scanner line = new Scanner(playerLine);
            line.useDelimiter(",");
            playerNames.add(line.next());
            playerClasses.add(line.next());
            playerLine = br.readLine();
            line.close();
        }
        br.close();
    }

    /**
     * Run an Uno simulation of some number of games pitting some set of
     * opponents against each other.
     * 
     * @param args
     *            The mandatory command-line argument (numberOfGames = args[0])
     *            should contain an integer specifying how many games to play in
     *            the match. The optional second command-line argument (args[1])
     *            should be either the word "verbose" or "quiet" and controls
     *            the magnitude of output.
     * @throws Exception
     *             when it fails to load the player file.
     */
    public static void main(String args[]) throws Exception
    {
        int numGames = processCommandLine(args);
        loadPlayerData();
        Scoreboard s = new Scoreboard(playerNames.toArray(new String[0]));
        for (int i = 0; i < numGames; i++)
        {
            Game g = new Game(s, playerClasses, printVerbose);
            g.play();
        }
        System.out.println(s);
    }

}
