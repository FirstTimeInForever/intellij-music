package intellij.music.core

import java.io.File
import javax.sound.midi.MidiSystem

class MidiBackend(private val soundfontFile: File) {
    val synthesizer = MidiSystem.getSynthesizer()

    init {
        init()
    }

    private fun init() {
        synthesizer.open()
        synthesizer.unloadAllInstruments(synthesizer.defaultSoundbank)
        val soundbank = MidiSystem.getSoundbank(soundfontFile)
        synthesizer.loadAllInstruments(soundbank)
    }

//    fun unload() {
//        synthesizer.close()
//    }
//
//    fun reload() {
//        unload()
//        init()
//    }
}
