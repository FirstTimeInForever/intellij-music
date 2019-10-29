package intellij.music.core

import intellij.music.ui.MusicConfig
import intellij.music.ui.MusicKeyboardEvent
import java.io.File
import java.nio.file.Paths

/*
    Base controller class.
 */
class MusicController {
    private var config = MusicConfig.instance
    private val keyboardStorage: KeyboardStorage = KeyboardStorage(10.0)
    private val userDirectoryPath = Paths.get(System.getProperty("user.home"), "my-midis")
    private val userFiles = UserDirectoryLoader(userDirectoryPath.toFile())
    private val midiBackend: MidiBackend = MidiBackend(userFiles.soundFontFile!!)
    private val midiFileController = MidiFileController(midiBackend, keyboardStorage, userFiles.userFiles)
    private val randomNotesController = RandomNotesController(midiBackend)
    var isMuted = false
        get() = field


    fun keyboardPressed(event: MusicKeyboardEvent) {
        if(isMuted) {
            return
        }
        when (config.algorithmType) {
            MusicAlgorithmType.RANDOM -> randomNotesController.keyboardPressed(event)
            MusicAlgorithmType.SEQUENTIAL -> midiFileController.keyboardPressed(event)
        }
    }

    fun nextTrack() {
        midiFileController.nextTrack()
    }

    fun setMuteState(state: Boolean) {
        isMuted = state
    }
}
