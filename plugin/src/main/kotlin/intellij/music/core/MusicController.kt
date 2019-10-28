package intellij.music.core

import intellij.music.ui.MusicKeyboardEvent
import java.io.File

/*
    Base controller class.
 */
class MusicController {
    private val keyboardStorage: KeyboardStorage = KeyboardStorage(10.0)
    private val midiBackend: MidiBackend = MidiBackend(File("/Users/hans/Downloads/FluidR3 GM.sf2"))
    private val midiFileController = MidiFileController(midiBackend, keyboardStorage)
    private val randomNotesController = RandomNotesController(midiBackend)

    fun keyboardPressed(event: MusicKeyboardEvent) {
        randomNotesController.keyboardPressed(event)
    }
}
