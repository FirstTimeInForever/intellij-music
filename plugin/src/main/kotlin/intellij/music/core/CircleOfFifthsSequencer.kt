package intellij.music.core


class CircleOfFifthsSequencer {
    private var previousNote = Pair("C", 3)
    private val modes: List<ScaleSequencer> = listOf(IonianScaleSequencer())
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

    fun changeMode() {
        currentModeIndex = modes.indexOf(modes.random())
    }
}
