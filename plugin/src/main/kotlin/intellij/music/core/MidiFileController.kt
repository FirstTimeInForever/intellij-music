package intellij.music.core

import intellij.music.ui.MusicKeyboardEvent
import java.io.File
import java.lang.Math.log10

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

    fun checkActive() {
        if (!isActive) {
            isActive = true
            midiFilePlayer.resume()
        }
    }

    fun onApmUpdate(actionsPerSecond: Float, millisecondsSinceLastAction: Long) {
        if (millisecondsSinceLastAction > 300) {
            midiFilePlayer.pause()
            isActive = false
            return
        }

        val multiplier = 1f
        val bpmMultiplier = (0.7f + log10(1f + actionsPerSecond.toDouble() / 2f)).toFloat()
        midiFilePlayer.setBpmMultiplier(bpmMultiplier)
    }
}