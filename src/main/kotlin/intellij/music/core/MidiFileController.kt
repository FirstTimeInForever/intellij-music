package intellij.music.core

import com.intellij.openapi.diagnostic.logger
import intellij.music.ui.MusicKeyboardEvent
import java.io.File
import kotlin.random.Random

class MidiFileController(midiBackend: MidiBackend,
                         private val keyboardStorage: KeyboardStorage,
                         private val userFiles: MutableList<File>) {
    private val midiFilePlayer: MidiFilePlayer = MidiFilePlayer(midiBackend)
    private val apmController: ApmController = ApmController(::onApmUpdate)
    private var isActive: Boolean = false
    private var currentFileIndex: Int = -1

    private val logger = logger<MidiFileController>()

    init {
        nextTrack()
        midiFilePlayer.addPlayEndEventListener {
            nextTrack()
        }
    }

    fun nextTrack() {
        currentFileIndex += 1
        if(currentFileIndex >= userFiles.size) {
            currentFileIndex = 0
        }
        setCurrentFile()
    }

    fun setRandomTrack() {
        currentFileIndex = Random.nextInt(userFiles.size)
        setCurrentFile()
    }

    fun setCurrentFile() {
        val file = userFiles[currentFileIndex]
        logger.info("Start midi file: $file")

        midiFilePlayer.setAudioFile(file)
        midiFilePlayer.playFile()
        midiFilePlayer.pause()
    }

    fun reloadSequencer() {
        midiFilePlayer.reload()
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
        if (millisecondsSinceLastAction > 300) {
            midiFilePlayer.pause()
            isActive = false
            return
        }

        val bpmMultiplier = calculateBpm(actionsPerSecond).toFloat()
        midiFilePlayer.setBpmMultiplier(bpmMultiplier)
        logger.debug("aps: $actionsPerSecond  bpm: $bpmMultiplier")
    }

    private fun calculateBpm(actionsPerSecond: Float): Double {
        return when {
            actionsPerSecond < 0.5 -> 0.5 + 0.1 * actionsPerSecond / 0.5
            actionsPerSecond < 1.0 -> 0.6 + 0.1 * (actionsPerSecond - 0.5) / (1 - 0.5)
            actionsPerSecond < 3.0 -> 0.7 + 0.1 * (actionsPerSecond - 1) / (3 - 1)
            actionsPerSecond < 5.0 -> 0.8 + 0.2 * (actionsPerSecond - 3) / (5 - 3)
            actionsPerSecond < 10 -> 1.0 + 0.2 * (actionsPerSecond - 5) / (10 - 5)
            else -> 1.2 + 0.2 * (actionsPerSecond - 10) / (30 - 10)
//            else -> (0.7 + log10(1 + actionsPerSecond / 2))
        }
    }
}
