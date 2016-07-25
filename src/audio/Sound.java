package audio;

import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import ld34.Configs;

public class Sound {
    
    public static Sound bonus = new Sound("/bonus.wav");
    public static Sound death = new Sound("/death.wav");
    public static Sound jump = new Sound("/jump.wav");
    public static Sound levelup = new Sound("/levelup.wav");
    public static Sound sf_jungle01 = new Sound("/jungle01.wav");
    public static Sound sf_jungle02 = new Sound("/jungle02.wav");
    
    public String path;
    public int volume;
    
    private Sound(String path){
        this.path = path;
        this.volume = Integer.parseInt(Configs.getInstance().getConfigValue("Sound"));
    }
    
    public void play(){
        try{
            URL url = this.getClass().getResource(this.path);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            
            AudioFormat audioFormat = audioInputStream.getFormat();
            
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            
            SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
            
            //clip.open(audioInputStream);
            
            //FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            
            if(sourceLine.isControlSupported(FloatControl.Type.MASTER_GAIN)){
                FloatControl gainControl = (FloatControl) sourceLine.getControl(FloatControl.Type.MASTER_GAIN);
                float attenuation = -80 + (80 * this.volume / 100);
                gainControl.setValue(attenuation);
            }
                
            sourceLine.start();
            
            int nBytesRead = 0;
            byte[] abData = new byte[128000];
            while (nBytesRead != -1) {
                    nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0) {
                    @SuppressWarnings("unused")
                    int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
                }
            }
            sourceLine.drain();
            sourceLine.close();
        }catch(IOException|UnsupportedAudioFileException|LineUnavailableException e)
        {
            e.printStackTrace();
        }
    }
}
