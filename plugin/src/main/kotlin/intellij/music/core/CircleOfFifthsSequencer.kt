package intellij.music.core

import intellij.music.core.MidiNotes.notes
import kotlin.math.abs
import kotlin.random.Random


object MidiNotes {
    fun noteToMidi(note: Pair<String, Int>): Int {
        val index = notes.indexOf(note.first)
        return index + note.second * notes.size
    }

    fun octaveShift(notesPlayed: Int, currentOctave: Int): Int {
        fun sign(x: Double): Int {
            return if (x < 0) {
                -1
            } else {
                +1
            }
        }
        var pr = notesPlayed * 0.1
        var shift = 0
        if (Random.nextFloat() < pr) {
            pr = abs(3.0 - currentOctave) / 7.0
            pr *= 2.0
            shift = selectRandom(listOf(-1, +1), listOf(pr, 1.0 - pr)) * sign(pr)
        }
        if(shift + currentOctave < 1) {
            shift = 0
        }
        else if(shift + currentOctave > 7) {
            shift = 0
        }
        return shift
    }

    val BASE_OCTAVE: Int = 3

    val notes = listOf("C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B")
        get() = field

    val scaleNotes: Map<String, List<String>> = mapOf(
        "C" to listOf("C", "D", "E", "F", "G", "A", "B", "C"),
        "D" to listOf("D", "E", "Gb", "G", "A", "B", "Db", "D"),
        "E" to listOf("E", "Gb", "Ab", "A", "B", "Db", "Eb", "E"),
        "F" to listOf("F", "G", "A", "Bb", "C", "D", "E", "F"),
        "G" to listOf("G", "A", "B", "C", "D", "E", "Gb", "G"),
        "A" to listOf("A", "B", "Db", "D", "E", "Gb", "Ab", "A"),
        "B" to listOf("B", "Db", "Eb", "E", "Gb", "Ab", "Bb", "B")
    )
        get() = field
}





interface ScaleSequencer {
    fun nextNote(previousNote: Pair<String, Int>): Pair<String, Int>
    fun getChord(): List<Pair<String, Int>>
}

class IonianScaleSequencer: ScaleSequencer {
    private var currentScale = "C"
    private var notesPlayedInScale = 0
    private var notesPlayedInOctave = 0
    private var currentOctave = listOf(MidiNotes.BASE_OCTAVE, MidiNotes.BASE_OCTAVE + 1).random()
    private var previousNoteIndex: Int = 0

    override fun nextNote(previousNote: Pair<String, Int>): Pair<String, Int> {
        notesPlayedInOctave += 1
        notesPlayedInScale += 1
        tryToChangeScale()
        return shift()
    }

    override fun getChord(): List<Pair<String, Int>> {
        val base = Pair(MidiNotes.scaleNotes[currentScale]!![0], currentOctave)
        val third = Pair(MidiNotes.scaleNotes[currentScale]!![2], currentOctave)
        val fifth = Pair(MidiNotes.scaleNotes[currentScale]!![5], currentOctave)
        return listOf(base, third, fifth)
    }

    private fun shift(): Pair<String, Int> {
        previousNoteIndex = if (modeShifts[previousNoteIndex].isNotEmpty()) {
            modeShifts[previousNoteIndex].random()
        }
        else {
            listOf(0, 4, 5, 7).random()
        }
        val scale = MidiNotes.scaleNotes[currentScale] ?: error("Failed to get scale!")
        if (Random.nextBoolean()) {
            if (previousNoteIndex == 0 || previousNoteIndex == 7) {
                val octaveShift = MidiNotes.octaveShift(notesPlayedInOctave, currentOctave)
                if(octaveShift != 0) {
                    notesPlayedInOctave = 0
                    currentOctave += octaveShift
                }
            }
        }
        return Pair(scale[previousNoteIndex], currentOctave)
    }

    private fun tryToChangeScale() {
        val otherScale = if (currentScale == "F") {
            "F"
        }
        else {
            "C"
        }
        val pr = notesPlayedInScale * 0.005
        if (Random.nextFloat() < pr) {
            currentScale = otherScale
            notesPlayedInScale = 0
        }
    }


    companion object {
        val modeShifts: List<List<Int>> = listOf(
            listOf(1, 2, 3, 4, 5, 6, 7),
            listOf(),
            listOf(),
            listOf(1),
            listOf(4, 7),
            listOf(),
            listOf(5, 1),
            listOf(1, 2, 3, 4, 5, 6, 7)
        )
    }
}


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
