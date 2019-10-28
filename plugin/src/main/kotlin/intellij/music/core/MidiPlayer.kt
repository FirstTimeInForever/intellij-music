package intellij.music.core

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import javax.sound.midi.*


class MidiPlayer {
    private val sequencer = MidiSystem.getSequencer(false)
    private var inputStream: InputStream? = null

    init {
        val soundfontFile = File("/Users/hans/Downloads/FluidR3 GM.sf2")
        createSequencer(soundfontFile)
    }

    private fun createSequencer(soundfont: File) {
        val synthesizer = MidiSystem.getSynthesizer()
        synthesizer.open()
        synthesizer.unloadAllInstruments(synthesizer.defaultSoundbank)
        val soundbank = MidiSystem.getSoundbank(soundfont)
        synthesizer.loadAllInstruments(soundbank)
        sequencer.transmitter.receiver = synthesizer.receiver
        sequencer.open()
    }

    fun setAudioFile(file: File) {
        inputStream?.close()
        inputStream = BufferedInputStream(FileInputStream(file))
    }

    fun setBpmMultiplier(multiplier: Float) {
        sequencer.tempoInBPM = sequencer.tempoInBPM * multiplier
    }

    fun addPlayEndEventListener(callback: () -> Unit) {
        sequencer.addMetaEventListener(MetaEventListener {
            if(it.type == 0x2f) {
                callback()
            }
        })
    }

    fun unloadBackend() {
        inputStream?.close()
        sequencer.close()
    }

    fun pause() {
        sequencer.stop()
    }

    fun playFile(initialBpmMultiplier: Float = 1f) {
        sequencer.setSequence(inputStream)
        setBpmMultiplier(initialBpmMultiplier)
        sequencer.start()
    }
}

//fun main() {
//    val player = MidiPlayer()
//    player.setAudioFile(File("/Users/hans/Downloads/silent_night_easy.mid"))
//    player.playFile()
//    player.pause()
//    player.setBpmMultiplier(2f)
//    player.addPlayEndEventListener {
//        System.out.println("Done")
//        player.unloadBackend()
//    }
//}
