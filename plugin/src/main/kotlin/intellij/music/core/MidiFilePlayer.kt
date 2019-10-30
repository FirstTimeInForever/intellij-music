package intellij.music.core

import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import javax.sound.midi.MetaEventListener
import javax.sound.midi.MidiSystem

class MidiFilePlayer(val backend: MidiBackend) {
    private val sequencer = MidiSystem.getSequencer(false)
    private var inputStream: InputStream? = null
    private var baseBpm: Float = 1f

    init {
        init()
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

    fun init() {
        sequencer.transmitter.receiver = backend.synthesizer.receiver
        sequencer.open()
    }

    fun unload() {
        inputStream?.close()
        sequencer.close()
    }

    fun reload() {
        unload()
        init()
    }

    fun pause() {
        if (sequencer.isOpen) {
            sequencer.stop()
        }
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
