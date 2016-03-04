package audio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Sound {
    
    public static Sound bonus = new Sound("/bonus.wav");
    public static Sound death = new Sound("/death.wav");
    public static Sound jump = new Sound("/jump.wav");
    public static Sound levelup = new Sound("/levelup.wav");
    
    public String path;
    
    private Sound(String path){
        this.path = path;
    }
    
    public void play(){
        try{
            URL url = this.getClass().getResource(this.path);
            InputStream in = url.openStream();
            AudioStream as = new AudioStream(in);
            AudioPlayer.player.start(as);
        }catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
