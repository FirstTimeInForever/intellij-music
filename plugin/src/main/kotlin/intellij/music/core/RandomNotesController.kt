package intellij.music.core

import intellij.music.ui.MusicKeyboardEvent

class RandomNotesController(midiBackend: MidiBackend) {
    private val notesPlayer = MidiNotesPlayer(midiBackend)
    private val circleSequencer = CircleOfFifthsSequencer()

    fun keyboardPressed(event: MusicKeyboardEvent) {
        notesPlayer.playNote(circleSequencer.nextNote(), BASE_NOTE_VELOCITY)
    }

    companion object {
        const val BASE_NOTE_VELOCITY: Int = 200
    }
}