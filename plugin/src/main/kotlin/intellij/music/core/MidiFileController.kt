package intellij.music.core

import intellij.music.ui.MusicKeyboardEvent
import java.io.File

class MidiFileController(midiBackend: MidiBackend, private val keyboardStorage: KeyboardStorage) {
    private val midiFilePlayer: MidiFilePlayer = MidiFilePlayer(midiBackend)
    private val apmController: ApmController = ApmController(::onApmUpdate)
    private var isActive: Boolean = false

    fun setFile(file: File) {
        midiFilePlayer.setAudioFile(file)
        midiFilePlayer.playFile()
        midiFilePlayer.pause()
    }

    fun keyboardPressed(event: MusicKeyboardEvent) {
        checkActive()
        keyboardStorage.addEvent(event)
        apmController.onAction()
    }

    private fun checkActive() {
        if (!isActive) {
            isActive = true
            midiFilePlayer.resume()
        }
    }

    private fun onApmUpdate(actionsPerSecond: Float, millisecondsSinceLastAction: Long) {
        if (millisecondsSinceLastAction > 500) {
            midiFilePlayer.pause()
            isActive = false
            return
        }

        val multiplier = 1f
        val bpmMultiplier = (actionsPerSecond * multiplier).coerceIn(0.5f, 2f)
        midiFilePlayer.setBpmMultiplier(bpmMultiplier)
    }
}