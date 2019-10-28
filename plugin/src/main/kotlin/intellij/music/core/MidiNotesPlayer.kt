package intellij.music.core

import javax.sound.midi.MidiChannel

class MidiNotesPlayer(backend: MidiBackend) {
    private val channel: MidiChannel = backend.synthesizer.channels[0]
    private var prevNote: Int = 0

    init {
        channel.noteOn(60, 100)
    }

    fun playNote(note: Int, velocity: Int) {
        channel.noteOff(prevNote)
        channel.noteOn(note, velocity)
        prevNote = note
    }
}
