package intellij.music.core.sequencer

import intellij.music.core.selectRandom
import kotlin.math.abs
import kotlin.random.Random

object Notes {
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

    const val BASE_OCTAVE: Int = 3

    val notes = listOf("C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B")

    val majorScaleNotes: Map<String, List<String>> = mapOf(
        "C" to listOf("C", "D", "E", "F", "G", "A", "B", "C"),
        "D" to listOf("D", "E", "Gb", "G", "A", "B", "Db", "D"),
        "E" to listOf("E", "Gb", "Ab", "A", "B", "Db", "Eb", "E"),
        "F" to listOf("F", "G", "A", "Bb", "C", "D", "E", "F"),
        "G" to listOf("G", "A", "B", "C", "D", "E", "Gb", "G"),
        "A" to listOf("A", "B", "Db", "D", "E", "Gb", "Ab", "A"),
        "B" to listOf("B", "Db", "Eb", "E", "Gb", "Ab", "Bb", "B")
    )

    val minorScaleNotes: Map<String, List<String>> = mapOf(
        "A" to listOf("Bb", "Cb", "Db", "Eb", "Fb", "Gb", "Ab", "Bb"),
        "B" to listOf("B", "Db", "D", "E", "Gb", "G", "A", "B"),
        "C" to listOf("C", "D", "Eb", "F", "G", "Ab", "Bb", "C"),
        "D" to listOf("D", "E", "F", "G", "A", "Bb", "C", "D"),
        "E" to listOf("E", "Gb", "G", "A", "B", "C", "D", "E"),
        "F" to listOf("F", "G", "Ab", "Bb", "C", "Db", "Eb", "F"),
        "G" to listOf("G", "A", "Bb", "C", "D", "Eb", "F", "G")
    )
}
