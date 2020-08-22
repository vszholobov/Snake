package classes;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Port;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MusicPlayer extends Thread{
    ArrayList<String> songList;

    public MusicPlayer(String[] songs) {
        this.songList = new ArrayList<>(Arrays.asList(songs));
    }

    public void changeVolume(float volume) throws LineUnavailableException {
        Port.Info source = Port.Info.SPEAKER;
        if (AudioSystem.isLineSupported(source)) {
            Port outline = (Port) AudioSystem.getLine(source);
            outline.open();
            FloatControl volumeControl = (FloatControl) outline.getControl(FloatControl.Type.VOLUME);
            volumeControl.setValue(volume);
        }
    }

    public void addSong(String songName) {
        this.songList.add(songName);
    }

    @Override
    public void run() {
        for(String songName : this.songList) {
            try {
                this.playSong(songName);
            } catch (JavaLayerException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void playSong(String songName) throws JavaLayerException, IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(songName);
        Player player = new Player(is);
        player.play();
        is.close();
    }
}
