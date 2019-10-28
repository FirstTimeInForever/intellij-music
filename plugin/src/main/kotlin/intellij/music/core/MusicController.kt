package intellij.music.core

import intellij.music.ui.MusicKeyboardEvent
import java.io.File
import kotlin.math.log10

/*
    Base controller class.
 */
class MusicController {
    private val keyboardStorage: KeyboardStorage = KeyboardStorage(10.0)
    private val midiBackend: MidiBackend = MidiBackend(File("/Users/hans/Downloads/FluidR3 GM.sf2"))
    private val midiFilePlayer: MidiFilePlayer = MidiFilePlayer(midiBackend)
    private val apmController: ApmController = ApmController(::onApmUpdate)
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

    fun onApmUpdate(actionsPerSecond: Float, millisecondsSinceLastAction: Long) {
        if (millisecondsSinceLastAction > 500) {
            midiFilePlayer.pause()
            isActive = false
            return
        }

//        val multiplier = 1f
//        val bpmMultiplier = (actionsPerSecond * multiplier).coerceIn(0.5f, 2f)
        val bpmMultiplier = 0.7f + log10(1f + actionsPerSecond) / 2f
        midiFilePlayer.setBpmMultiplier(bpmMultiplier)
    }
}
