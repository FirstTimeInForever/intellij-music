package intellij.music.core

import intellij.music.ui.MusicKeyboardEvent
import java.io.File

/*
    Base controller class.
 */
class MusicController {
    private val keyboardStorage: KeyboardStorage = KeyboardStorage(10.0)
    private val midiBackend: MidiBackend = MidiBackend(File("/Users/hans/Downloads/FluidR3 GM.sf2"))
    private val midiFilePlayer: MidiFilePlayer = MidiFilePlayer(midiBackend)
    private val apmController: ApmController = ApmController { apm -> onApmUpdate(apm) }
    private var isActive: Boolean = false

    init {
        val file = File("/Users/hans/Downloads/Metallica_-_Seek_and_Destroy.mid")
        midiFilePlayer.setAudioFile(file)
        midiFilePlayer.playFile()
        midiFilePlayer.pause()
    }

    fun keyboardPressed(event: MusicKeyboardEvent) {
        checkActive()
        keyboardStorage.addEvent(event)
        apmController.onAction()
    }

    fun checkActive() {
        if (!isActive) {
            isActive = true
            midiFilePlayer.resume()
        }
    }

    fun onApmUpdate(apm: Float) {
        System.err.println("apm: $apm")
        val minimalApm = 0.1
        if (apm < minimalApm) {
            midiFilePlayer.pause()
            isActive = false
            return
        }

        val multiplier = 0.1f
        midiFilePlayer.setBpmMultiplier(apm * multiplier)
    }
}
