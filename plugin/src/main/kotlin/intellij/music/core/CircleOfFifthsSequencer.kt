package intellij.music.core

import kotlin.math.abs
import kotlin.random.Random

class CircleOfFifthsSequencer {
    var previousNote = 0
    var currentScale = "C"
    var currentOctave = listOf(3, 4).random()
    var notesPlayedInCurrentScale = 0
    var notesPlayedInCurrentOctave = 0

    public fun nextNote(): Pair<String, Int> {
//        tryToChangeScale()
        ++notesPlayedInCurrentScale
        ++notesPlayedInCurrentOctave
        return shiftIonianNote()
    }

    fun shiftIonianNote(): Pair<String, Int> {
        if (ionianModeShifts[previousNote].isNotEmpty()) {
            previousNote = ionianModeShifts[previousNote].random()
        } else {
            previousNote = listOf(0, 4, 5, 7).random()
        }
        val scale = notes[currentScale]!!
        if (Random.nextBoolean()) {
            if (previousNote == 0 || previousNote == 7) {
                shiftOctave()
            }
        }
        return Pair(scale[previousNote], currentOctave)
    }

    fun tryToChangeScale() {
        val otherScale = if (currentScale == "F") {
            "F"
        } else {
            "C"
        }
        val pr = notesPlayedInCurrentScale * 0.005
        // res = random.choices([True, False], [pr, 1 - pr])
        // print(f'pr: {pr} pressed: {self.notes_played_in_current_scale} res: {res}')
        if (Random.nextFloat() < pr) {
            println("Changing scale")
            currentScale = otherScale
            notesPlayedInCurrentScale = 0
        }
    }

    private fun shiftOctave() {
        fun sign(x: Double): Int {
            return if (x < 0) {
                -1
            } else {
                +1
            }
        }

        var pr = notesPlayedInCurrentOctave * 0.1
        // pr *= max(self.current_octave, 1 - self.current_octave)
        if (Random.nextFloat() < pr) {
            pr = abs(3.0 - currentOctave) / 7.0
            pr *= 2.0
            currentOctave += selectRandom(listOf(-1, +1), listOf(pr, 1.0 - pr)) * sign(pr)
            normalizeOctave()
            notesPlayedInCurrentOctave = 0
        }
    }

    private fun normalizeOctave() {
        if (currentOctave < 1) {
            currentOctave = 1
        } else if (currentOctave > 7) {
            currentOctave = 7
        }
    }

    companion object {
        val ionianModeShifts: List<List<Int>> = listOf(
            listOf(1, 2, 3, 4, 5, 6, 7),
            listOf(),
            listOf(),
            listOf(1),
            listOf(4, 7),
            listOf(),
            listOf(5, 1),
            listOf(1, 2, 3, 4, 5, 6, 7)
        )

        val notes: Map<String, List<String>> = mapOf(
            "C" to listOf("C", "D", "E", "F", "G", "A", "B", "C"),
            "D" to listOf("D", "E", "Gb", "G", "A", "B", "Db", "D"),
            "E" to listOf("E", "Gb", "Ab", "A", "B", "Db", "Eb", "E"),
            "F" to listOf("F", "G", "A", "Bb", "C", "D", "E", "F"),
            "G" to listOf("G", "A", "B", "C", "D", "E", "Gb", "G"),
            "A" to listOf("A", "B", "Db", "D", "E", "Gb", "Ab", "A"),
            "B" to listOf("B", "Db", "Eb", "E", "Gb", "Ab", "Bb", "B")
        )
    }
}
