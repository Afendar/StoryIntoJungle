package ld34;

import core.Screen;
import core.TimerThread;

public class LD34 {
    
    public static void main(String[] args) {
        TimerThread timer = new TimerThread();
        timer.start();
        Screen screen = new Screen();
    }
}
