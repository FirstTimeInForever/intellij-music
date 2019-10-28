package intellij.music.core

import intellij.music.ui.MusicKeyboardEvent
import java.io.File

/*
    Base controller class.
 */
class MusicController {
    private val keyboardStorage: KeyboardStorage = KeyboardStorage(10.0)
    private val midiBackend: MidiBackend = MidiBackend(File(SOUNDFONT_PATH))
    private val midiFileController = MidiFileController(midiBackend, keyboardStorage)
    private val randomNotesController = RandomNotesController(midiBackend)

    init {
        midiFileController.setFile(File("/Users/hans/Downloads/Metallica_-_Seek_and_Destroy.mid"))
    }

    fun keyboardPressed(event: MusicKeyboardEvent) {
        midiFileController.keyboardPressed(event)
    }

    companion object {
        const val SOUNDFONT_PATH = "/Users/hans/Downloads/FluidR3 GM.sf2"
    }
}
