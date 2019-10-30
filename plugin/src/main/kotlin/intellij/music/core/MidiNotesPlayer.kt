package intellij.music.core

import java.util.*
import javax.sound.midi.MidiChannel
import kotlin.concurrent.timerTask

class MidiNotesPlayer(val backend: MidiBackend) {
    private val channel: MidiChannel = backend.synthesizer.channels[0]
    private var prevNote: Int = 0
    private val timer = Timer()

//    init {
//        channel.noteOn(60, 100)
//    }

    fun ensureResetNotes() {
        backend.reload()
        channel.allNotesOff()
    }

    fun playNote(note: Int, velocity: Int) {
        channel.noteOn(note, velocity)
        prevNote = note
        timer.schedule(timerTask {
            channel.noteOff(note)
        }, velocity.toLong())
    }

    fun playChord(notes: List<Int>, velocity: Int) {
        for(note in notes) {
            channel.noteOn(note, velocity)
        }
    }
}
