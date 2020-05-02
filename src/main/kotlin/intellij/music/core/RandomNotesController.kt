package intellij.music.core

import intellij.music.core.sequencer.CircleOfFifthsSequencer
import intellij.music.ui.MusicConfig
import intellij.music.ui.MusicKeyboardEvent
import java.util.*
import java.util.concurrent.TimeUnit

class RandomNotesController(midiBackend: MidiBackend) {
    private val notesPlayer = MidiNotesPlayer(midiBackend)
    private val circleSequencer = CircleOfFifthsSequencer()
    private var lastNoteTime = Date()
    private val config = MusicConfig.instance

    companion object {
        private const val INACTIVE_TIMEOUT = 130
        private const val CHANGE_MODE_TIMEOUT = 900
        const val BASE_NOTE_VELOCITY: Int = 200
    }

    fun keyboardPressed(event: MusicKeyboardEvent) {
        circleSequencer.setCurrentScale(config.algorithmType == MusicAlgorithmType.RANDOM_MAJOR)
        val diff = TimeUnit.MILLISECONDS.toMillis(Date().time - lastNoteTime.time)
        if(diff < INACTIVE_TIMEOUT) {
            return
        }
        else if(MusicConfig.instance.algorithmType == MusicAlgorithmType.RANDOM_BOTH && diff > CHANGE_MODE_TIMEOUT) {
            circleSequencer.changeMode()
        }
        if (diff > CHANGE_MODE_TIMEOUT) {
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
}
