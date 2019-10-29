package intellij.music.core

import intellij.music.ui.MusicKeyboardEvent
import java.util.*
import java.util.concurrent.TimeUnit

class RandomNotesController(midiBackend: MidiBackend) {
    val notesPlayer = MidiNotesPlayer(midiBackend)
    private val circleSequencer = CircleOfFifthsSequencer()

    private var lastNoteTime = Date()

    var switchScales = false
    private var currentScaleIsMajor = true

    fun setCurrentScale(major: Boolean) {
        currentScaleIsMajor = major
        circleSequencer.setCurrentScale(currentScaleIsMajor)
    }

    fun keyboardPressed(event: MusicKeyboardEvent) {
        val diff = TimeUnit.MILLISECONDS.toMillis(Date().time - lastNoteTime.time)
        if(diff < 130) {
            return
        }
        else if(switchScales && diff > 900) {
            circleSequencer.changeMode()
        }
        if(event.numberModifiers != 0) {
            notesPlayer.playChord(circleSequencer.getChord(), BASE_NOTE_VELOCITY * 2)
            circleSequencer.nextNote()
            return
        }
        notesPlayer.playNote(circleSequencer.nextNote(), BASE_NOTE_VELOCITY)
        lastNoteTime = Date()
    }

    companion object {
        const val BASE_NOTE_VELOCITY: Int = 200
    }
}
