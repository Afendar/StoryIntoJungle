package audio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Sound {
    
    public static Sound bonus = new Sound("sfx/bonus.wav");
    public static Sound death = new Sound("sfx/death.wav");
    public static Sound jump = new Sound("sfx/jump.wav");
    public static Sound levelup = new Sound("sfx/levelup.wav");
    
    public String path;
    
    private Sound(String path){
        this.path = path;
    }
    
    public void play(){
        try{
            InputStream in = new FileInputStream(path);
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
