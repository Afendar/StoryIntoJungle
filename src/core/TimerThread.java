package core;

/**
 * TimerThread class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class TimerThread extends Thread {
    
    public static int MILLI = 0;
    
    @Override
    public void run(){
        while(true){
            try{Thread.sleep(1);}
            catch(InterruptedException e){e.getMessage();}
            MILLI++;
        }
    }
    
}
