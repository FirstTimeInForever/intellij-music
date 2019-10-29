package intellij.music.core

import java.io.File
import javax.sound.midi.MidiSystem
import javax.sound.midi.Synthesizer

class MidiBackend(private val soundfontFile: File) {
    val synthesizer: Synthesizer = MidiSystem.getSynthesizer()

    init {
        init()
    }

    fun init() {
        synthesizer.open()
        synthesizer.unloadAllInstruments(synthesizer.defaultSoundbank)
        val soundbank = MidiSystem.getSoundbank(soundfontFile)
        synthesizer.loadAllInstruments(soundbank)
    }

    fun unload() {
        synthesizer.close()
    }

    fun reload() {
        unload()
        init()
    }
}
