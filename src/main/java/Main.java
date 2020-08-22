import classes.MainWindow;
import classes.MusicPlayer;

import javax.sound.sampled.*;

public class Main {
    public static void main(String[] args) {
        MainWindow window = new MainWindow();
        window.setVisible(true);

        MusicPlayer player = new MusicPlayer(new String[] {"sound.mp3", "sound1.mp3"});
        try {
            player.changeVolume(0.1f);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        player.start();
        player.interrupt();
    }
}
