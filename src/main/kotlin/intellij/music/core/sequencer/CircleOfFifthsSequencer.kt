package intellij.music.core.sequencer

import intellij.music.core.*


class CircleOfFifthsSequencer {
    private var previousNote = Pair("C", 3)
    private val modes: List<ScaleSequencer> = listOf(
        MajorScaleSequencer(),
        MinorScaleSequencer()
    )
    private var currentModeIndex = 0

    fun nextNote(): Int {
        previousNote = modes[currentModeIndex].nextNote(previousNote)
        return MidiNotes.noteToMidi(previousNote)
    }

    fun getChord(): List<Int> {
        val chord = modes[currentModeIndex].getChord()
        val result = mutableListOf<Int>()
        for(note in chord) {
            result.add(MidiNotes.noteToMidi(note))
        }
        return result
    }

    fun setCurrentScale(major: Boolean) {
        if (major) {
            currentModeIndex = 0
        }
        else {
            currentModeIndex = 1
        }
    }

    fun changeMode() {
        currentModeIndex = modes.size - 1 - selectRandom(
            listOf(0, 1),
            listOf(0.5, 0.5)
        )
    }
}
