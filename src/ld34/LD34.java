package ld34;

import core.Screen;
import core.TimerThread;

/**
 * LD34 class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class LD34 {
    
    /**
     * 
     * @param args 
     */
    public static void main(String[] args) {
        TimerThread timer = new TimerThread();
        timer.start();
        Screen screen = new Screen();
    }
}
