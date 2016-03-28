package audio;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import ld34.Configs;

public class Sound {
    
    public static Sound bonus = new Sound("/bonus.wav");
    public static Sound death = new Sound("/death.wav");
    public static Sound jump = new Sound("/jump.wav");
    public static Sound levelup = new Sound("/levelup.wav");
    
    public String path;
    public int volume;
    
    private Sound(String path){
        this.path = path;
        this.volume = (int)Configs.getInstance().getConfigValue("sound");
    }
    
    public void play(){
        try{
            URL url = this.getClass().getResource(this.path);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float attenuation = -80 + (80 * this.volume / 100);
            gainControl.setValue(attenuation);
            clip.start();
        }catch(IOException|UnsupportedAudioFileException|LineUnavailableException e)
        {
            e.printStackTrace();
        }
    }
}
