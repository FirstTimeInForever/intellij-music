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

        val bpmMultiplier = calculateBpm(actionsPerSecond).toFloat()
        midiFilePlayer.setBpmMultiplier(bpmMultiplier)
        System.err.println("aps: $actionsPerSecond  bpm: $bpmMultiplier")
    }

    fun calculateBpm(actionsPerSecond: Float): Double {
        return when {
            actionsPerSecond < 0.2 -> 0.5 + 0.1 * actionsPerSecond / 0.2
            actionsPerSecond < 0.5 -> 0.6 + 0.1 * (actionsPerSecond - 0.2) / (0.5 - 0.2)
            actionsPerSecond < 1.0 -> 0.7 + 0.1 * (actionsPerSecond - 0.5) / (1 - 0.5)
            actionsPerSecond < 3.0 -> 0.8 + 0.1 * (actionsPerSecond - 1) / (3 - 1)
            actionsPerSecond < 5.0 -> 0.9 + 0.2 * (actionsPerSecond - 3) / (5 - 3)
            actionsPerSecond < 10 -> 1.1 + 0.2 * (actionsPerSecond - 5) / (10 - 5)
            else -> 1.3 + 0.1 * (actionsPerSecond - 10) / (30 - 10)
//            else -> (0.7 + log10(1 + actionsPerSecond / 2))
        }
    }
}
