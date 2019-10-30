package intellij.music.core

import java.util.*
import javax.sound.midi.MidiChannel
import kotlin.concurrent.timerTask

class MidiNotesPlayer(backend: MidiBackend) {
    private val channel: MidiChannel = backend.synthesizer.channels[0]
    private var prevNote: Int = 0
    private val timer = Timer()

    fun ensureResetNotes() {
//        backend.reload()
        channel.allNotesOff()
    }

    fun playNote(note: Int, velocity: Int) {
        if (channel.program != 0) {
            channel.programChange(0, 0)
        }

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
