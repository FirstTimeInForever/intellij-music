package intellij.music.core.sequencer

import intellij.music.core.*


class CircleOfFifthsSequencer {
    private var previousNote = Pair("C", 3)
    private val modes: List<ScaleSequencer> = listOf(
        ShiftsScaleSequencer.createMajorScaleSequencer(),
        ShiftsScaleSequencer.createMinorScaleSequencer()
    )
    private var currentModeIndex = 0

    fun nextNote(): Int {
        previousNote = modes[currentModeIndex].nextNote(previousNote)
        return Notes.noteToMidi(previousNote)
    }

    fun getChord(): List<Int> {
        val chord = modes[currentModeIndex].getChord()
        val result = mutableListOf<Int>()
        for(note in chord) {
            result.add(Notes.noteToMidi(note))
        }
        return result
    }

    fun setCurrentScale(major: Boolean) {
        currentModeIndex = if (major) {
            0
        } else {
            1
        }
    }

    fun changeMode() {
        currentModeIndex = modes.size - 1 - selectRandom(
            listOf(0, 1),
            listOf(0.5, 0.5)
        )
    }
}
