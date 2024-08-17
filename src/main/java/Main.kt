import classes.MainWindow
import classes.MusicPlayer
import javax.sound.sampled.LineUnavailableException

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val window = MainWindow()
        window.isVisible = true
        val player = MusicPlayer(listOf("sound.mp3", "sound1.mp3"))
        try {
            player.changeVolume(0.1f)
        } catch (e: LineUnavailableException) {
            e.printStackTrace()
        }
        player.start()
        player.interrupt()
    }
}