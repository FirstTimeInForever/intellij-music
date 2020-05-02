package intellij.music.core

import intellij.music.ui.MusicConfig
import intellij.music.ui.MusicKeyboardEvent
import java.util.*
import java.util.concurrent.TimeUnit

class RandomNotesController(midiBackend: MidiBackend) {
    val notesPlayer = MidiNotesPlayer(midiBackend)
    private val circleSequencer = CircleOfFifthsSequencer()
    private var lastNoteTime = Date()
    private val config = MusicConfig.instance

    fun keyboardPressed(event: MusicKeyboardEvent) {
        circleSequencer.setCurrentScale(config.algorithmType == MusicAlgorithmType.RANDOM_MAJOR)
        val diff = TimeUnit.MILLISECONDS.toMillis(Date().time - lastNoteTime.time)
        if(diff < 130) {
            return
        }
        else if(MusicConfig.instance.algorithmType == MusicAlgorithmType.RANDOM_BOTH && diff > 900) {
            circleSequencer.changeMode()
        }
        if (diff > 900) {
            notesPlayer.ensureResetNotes()
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
