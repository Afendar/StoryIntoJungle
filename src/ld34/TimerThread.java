package ld34;

public class TimerThread extends Thread {
    
    public static int MILLI = 0;
    
    public void run(){
        while(true){
            try{Thread.sleep(1);}
            catch(InterruptedException e){e.printStackTrace();}
            MILLI++;
        }
    }
    
}
