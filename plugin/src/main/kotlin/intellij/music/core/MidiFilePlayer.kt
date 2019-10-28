package intellij.music.core

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import javax.sound.midi.MetaEventListener
import javax.sound.midi.MidiSystem

class MidiFilePlayer(backend: MidiBackend) {
    private val sequencer = MidiSystem.getSequencer(false)
    private var inputStream: InputStream? = null
    private var baseBpm: Float = 1f

    init {
        sequencer.transmitter.receiver = backend.synthesizer.receiver
        sequencer.open()
    }

    fun setAudioFile(file: File) {
        inputStream?.close()
        inputStream = BufferedInputStream(FileInputStream(file))
    }

    fun setBpmMultiplier(multiplier: Float) {
        sequencer.tempoInBPM = baseBpm * multiplier
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

    fun resume() {
        sequencer.start()
    }

    fun playFile(initialBpmMultiplier: Float = 1f) {
        sequencer.setSequence(inputStream)
        baseBpm = sequencer.tempoInBPM
        setBpmMultiplier(initialBpmMultiplier)
        sequencer.start()
    }
}
