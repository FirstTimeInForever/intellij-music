package intellij.music.core

import intellij.music.ui.MusicKeyboardEvent
import java.io.File

/*
    Base controller class.
 */
class MusicController {
    private val keyboardStorage: KeyboardStorage = KeyboardStorage(10.0)
    private val midiPlayer: MidiPlayer = MidiPlayer(File("/home/dima/music/FluidR3 GM.sf2"))
    private val apmController: ApmController = ApmController { apm -> onApmUpdate(apm) }
    private var isActive: Boolean = false

    init {
        val file = File("/home/dima/music-temp/midis/test.mid")
        midiPlayer.setAudioFile(file)
        midiPlayer.playFile()
        midiPlayer.pause()
    }

    fun keyboardPressed(event: MusicKeyboardEvent) {
        checkActive()
        keyboardStorage.addEvent(event)
        apmController.onAction()
    }

    fun checkActive() {
        if (!isActive) {
            isActive = true
            midiPlayer.resume()
        }
    }

    fun onApmUpdate(apm: Float) {
        System.err.println("apm: $apm")
        val minimalApm = 0.1
        if (apm < minimalApm) {
            midiPlayer.pause()
            isActive = false
            return
        }

        val multiplier = 0.1f
        midiPlayer.setBpmMultiplier(apm * multiplier)
    }
}
