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
            MusicAlgorithmType.SEQUENTIAL -> midiFileController.keyboardPressed(event)
            else -> randomNotesController.keyboardPressed(event)
        }
    }

    fun nextTrack() {
        midiFileController.nextTrack()
    }

    private fun reloadMidiFilesDirectory() {
        userFiles.reloadMidiFilesDirectory()
        if (config.algorithmType == MusicAlgorithmType.SEQUENTIAL) {
            midiFileController.setRandomTrack()
        }
    }

    fun onSettingsChanged() {
        reloadMidiFilesDirectory()
        randomNotesController.notesPlayer.ensureResetNotes()
        midiBackend.reload()
    }
}
