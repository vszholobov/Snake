package snake

import javazoom.jl.decoder.JavaLayerException
import javazoom.jl.player.Player
import java.io.IOException
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.FloatControl
import javax.sound.sampled.LineUnavailableException
import javax.sound.sampled.Port

class MusicPlayer(songs: List<String>) : Thread() {
    var songList: List<String>

    init {
        songList = songs
    }

    @Throws(LineUnavailableException::class)
    fun changeVolume(volume: Float) {
        val source = Port.Info.SPEAKER
        if (AudioSystem.isLineSupported(source)) {
            val outline = AudioSystem.getLine(source) as Port
            outline.open()
            val volumeControl = outline.getControl(FloatControl.Type.VOLUME) as FloatControl
            volumeControl.value = volume
        }
    }

    override fun run() {
        for (songName in songList) {
            try {
                playSong(songName)
            } catch (e: JavaLayerException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @Throws(JavaLayerException::class, IOException::class)
    fun playSong(songName: String?) {
        val `is` = this.javaClass.classLoader.getResourceAsStream(songName)
        val player = Player(`is`)
        player.play()
        `is`.close()
    }
}