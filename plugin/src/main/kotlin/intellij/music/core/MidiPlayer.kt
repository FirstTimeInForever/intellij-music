package intellij.music.core

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.*
import javax.sound.midi.*


class MidiPlayer {
    private val sequencer = MidiSystem.getSequencer()
    private var inputStream: InputStream? = null

    fun setAudioFile(file: File) {
        inputStream?.close()
        inputStream = BufferedInputStream(FileInputStream(file))
    }

    fun setBpmMultiplier(multiplier: Float) {
        sequencer.tempoInBPM = sequencer.tempoInBPM * multiplier
    }

    fun playFile(initialBpmMultiplier: Float = 1f) {
        sequencer.open()
        sequencer.setSequence(inputStream)
        setBpmMultiplier(initialBpmMultiplier)
        sequencer.start()
    }
}

fun main() {
    val player = MidiPlayer()
    player.setAudioFile(File("/Users/hans/Downloads/Metallica_-_Seek_and_Destroy.mid"))
    player.playFile()
    Timer().schedule(object : TimerTask() {
        override fun run() {
            player.setBpmMultiplier(4f)
        }
    }, 5000)
}
