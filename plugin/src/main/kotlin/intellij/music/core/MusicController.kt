package intellij.music.core

import intellij.music.ui.MusicConfig
import intellij.music.ui.MusicKeyboardEvent
import java.io.File

/*
    Base controller class.
 */
class MusicController {
    private var config = MusicConfig.instance
    private val keyboardStorage: KeyboardStorage = KeyboardStorage(10.0)
    private val userFiles = UserDirectoryLoader()
    private var isSoundFontLoaded: Boolean = false

    private lateinit var midiBackend: MidiBackend
    private lateinit var midiFileController: MidiFileController
    private lateinit var randomNotesController: RandomNotesController

    var isMuted = false
        get() = field

    init {
        userFiles.initSoundFont(::onSoundFontLoaded)
    }

    private fun onSoundFontLoaded(soundfontFile: File) {
        midiBackend = MidiBackend(soundfontFile)
        midiFileController = MidiFileController(midiBackend, keyboardStorage, userFiles.userFiles)
        randomNotesController = RandomNotesController(midiBackend)
        isSoundFontLoaded = true
    }

    fun keyboardPressed(event: MusicKeyboardEvent) {
        if (!isSoundFontLoaded) {
            return
        }
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
