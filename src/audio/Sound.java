package audio;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.DataLine.Info;
import ld34.profile.Settings;
import static javax.sound.sampled.AudioSystem.getAudioInputStream;
import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

/**
 * Sound class
 *
 * @version %I%, %G%
 * @author Afendar
 */
public class Sound
{
    private String m_path;
    private int m_volume;

    /**
     *
     * @param path
     */
    public Sound(String path)
    {
        m_path = path;
        m_volume = Integer.parseInt(Settings.getInstance().getConfigValue("sound"));
    }

    /**
     *
     * @return
     */
    public String getPath()
    {
        return m_path;
    }

    /**
     *
     * @return
     */
    public int getVolume()
    {
        return m_volume;
    }

    /**
     *
     * @param path
     */
    public void setPath(String path)
    {
        this.m_path = path;
    }

    /**
     *
     * @param volume
     */
    public void setVolume(int volume)
    {
        this.m_volume = volume;
    }

    /**
     *
     */
    public void play2()
    {
        try
        {
            URI url = this.getClass().getResource(m_path).toURI();
            final File file = new File(url);
            try (final AudioInputStream in = getAudioInputStream(file))
            {

                final AudioFormat outFormat = getOutFormat(in.getFormat());
                final Info info = new Info(SourceDataLine.class, outFormat);

                try (final SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info))
                {

                    if (line != null)
                    {
                        line.open(outFormat);
                        line.start();
                        AudioInputStream inputStream = AudioSystem.getAudioInputStream(outFormat, in);
                        stream(inputStream, line);
                        line.drain();
                        line.stop();
                    }
                }
            }
            catch (UnsupportedAudioFileException | LineUnavailableException | IOException e)
            {
                e.getMessage();
            }
        }
        catch (URISyntaxException e)
        {
            e.getMessage();
        }
    }

    /**
     *
     * @param inFormat
     * @return
     */
    private AudioFormat getOutFormat(AudioFormat inFormat)
    {
        final int ch = inFormat.getChannels();
        final float rate = inFormat.getSampleRate();
        return new AudioFormat(PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }

    /**
     *
     * @param in
     * @param line
     */
    private void stream(AudioInputStream in, SourceDataLine line)
    {
        try
        {
            final byte[] buffer = new byte[65536];
            for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length))
            {
                line.write(buffer, 0, n);
            }
        }
        catch (IOException e)
        {
            e.getMessage();
        }
    }

    /**
     *
     */
    public void play()
    {
        m_volume = Integer.parseInt(Settings.getInstance().getConfigValue("sound"));
        try
        {
            URL url = this.getClass().getResource(m_path);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);

            AudioFormat audioFormat = audioInputStream.getFormat();

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

            SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);

            if (sourceLine.isControlSupported(FloatControl.Type.MASTER_GAIN))
            {
                FloatControl gainControl = (FloatControl) sourceLine.getControl(FloatControl.Type.MASTER_GAIN);
                float range = gainControl.getMaximum() - (gainControl.getMinimum() / 2);
                float volume = m_volume / 100.0f;
                float gain = (range * volume) + (gainControl.getMinimum() / 2);
                gainControl.setValue(gain);
            }

            sourceLine.start();

            int nBytesRead = 0;
            byte[] abData = new byte[128000];
            while (nBytesRead != -1)
            {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0)
                {
                    int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
                }
            }
            sourceLine.drain();
            sourceLine.close();
        }
        catch (IOException | UnsupportedAudioFileException | LineUnavailableException e)
        {
            e.getMessage();
        }
    }
}
