package ld34;

import core.Game;
import core.TimerThread;

/**
 * LD34 class for entry point of program
 *
 * @version %I%, %G%
 * @author Afendar
 */
public class LD34
{
    /**
     * main function of program.
     *
     * @param args
     */
    public static void main(String[] args)
    {   
        if(args.length == 0)
        {
            throw new RuntimeException("Unknow profile name");
        }

        TimerThread timer = new TimerThread();
        timer.start();
        Game game = new Game(args[0]);
    }
}
