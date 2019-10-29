package intellij.music.core

import intellij.music.ui.MusicConfig
import intellij.music.ui.MusicKeyboardEvent
import java.io.File

/*
    Base controller class.
 */
class MusicController {
    private val config = MusicConfig.instance
    private val keyboardStorage: KeyboardStorage = KeyboardStorage(10.0)
    private val userFiles = UserDirectoryLoader()
    private var isSoundFontLoaded: Boolean = false

    private lateinit var midiBackend: MidiBackend
    private lateinit var midiFileController: MidiFileController
    private lateinit var randomNotesController: RandomNotesController

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

        when (config.algorithmType) {
            MusicAlgorithmType.RANDOM -> randomNotesController.keyboardPressed(event)
            MusicAlgorithmType.SEQUENTIAL -> midiFileController.keyboardPressed(event)
        }
    }

    fun nextTrack() {
        midiFileController.nextTrack()
    }

    fun reloadMidiFilesDirectory() {
        userFiles.reindexFiles()
        if (config.algorithmType == MusicAlgorithmType.SEQUENTIAL) {
            midiFileController.setRandomTrack()
        }
    }
}
