package intellij.music.core

import intellij.music.ui.MusicKeyboardEvent
import java.io.File

/*
    Base controller class.
 */
class MusicController {
    private val keyboardStorage: KeyboardStorage = KeyboardStorage(10.0)
    private val midiPlayer: MidiPlayer = MidiPlayer(File("/home/dima/music/FluidR3 GM.sf2"))
    private val apmController: ApmController = ApmController(::onApmUpdate)
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

    fun onApmUpdate(actionsPerSecond: Float, millisecondsSinceLastAction: Long) {
        if (millisecondsSinceLastAction > 500) {
            midiPlayer.pause()
            isActive = false
            return
        }

        val multiplier = 1f
        val bpmMultiplier = (actionsPerSecond * multiplier).coerceIn(0.5f, 2f)
        midiPlayer.setBpmMultiplier(bpmMultiplier)
    }
}
