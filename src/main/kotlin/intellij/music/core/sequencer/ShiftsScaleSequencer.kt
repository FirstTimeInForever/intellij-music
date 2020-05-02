package intellij.music.core.sequencer

import com.intellij.openapi.diagnostic.logger
import kotlin.random.Random

class ShiftsScaleSequencer(private val scaleNotes: Map<String, List<String>>, private val modeShifts: List<List<Int>>) :
    ScaleSequencer {
    private var currentScale = "A"
    private var notesPlayedInScale = 0
    private var notesPlayedInOctave = 0
    private var currentOctave = listOf(Notes.BASE_OCTAVE, Notes.BASE_OCTAVE + 1).random()
    private var previousNoteIndex: Int = 0

    private val logger = logger<ShiftsScaleSequencer>()

    override fun nextNote(previousNote: Pair<String, Int>): Pair<String, Int> {
        notesPlayedInOctave += 1
        notesPlayedInScale += 1
        tryToChangeScale()
        return shift()
    }

    override fun getChord(): List<Pair<String, Int>> {
        val base = Pair(scaleNotes[currentScale]!![0], currentOctave)
        val third = Pair(scaleNotes[currentScale]!![2], currentOctave)
        val fifth = Pair(scaleNotes[currentScale]!![5], currentOctave)
        return listOf(base, third, fifth)
    }

    private fun shift(): Pair<String, Int> {
        previousNoteIndex = if (modeShifts[previousNoteIndex].isNotEmpty()) {
            modeShifts[previousNoteIndex].random()
        } else {
            listOf(0, 4, 5, 7).random()
        }
        val scale = scaleNotes[currentScale] ?: error("Failed to get scale!")
        if (Random.nextBoolean()) {
            if (previousNoteIndex == 0 || previousNoteIndex == 7) {
                val octaveShift =
                    Notes.octaveShift(notesPlayedInOctave, currentOctave)
                if (octaveShift != 0) {
                    notesPlayedInOctave = 0
                    currentOctave += octaveShift
                }
            }
        }
        return Pair(scale[previousNoteIndex], currentOctave)
    }

    private fun tryToChangeScale() {
        val pr = notesPlayedInScale * 0.001
        if (Random.nextFloat() < pr) {
            currentScale = scaleNotes.keys.random()
            logger.info("Changed scale to: $currentScale")
            notesPlayedInScale = 0
        }
    }

    companion object {
        fun createMinorScaleSequencer(): ShiftsScaleSequencer =
            ShiftsScaleSequencer(
                Notes.minorScaleNotes,
                listOf(
                    listOf(1, 2, 3, 4, 5, 6, 7),
                    listOf(3, 4),
                    listOf(4),
                    listOf(2, 7),
                    listOf(7),
                    listOf(2, 4, 1),
                    listOf(1, 3),
                    listOf(1, 2, 3, 4, 5, 6, 7)
                )
            )

        fun createMajorScaleSequencer(): ShiftsScaleSequencer =
            ShiftsScaleSequencer(
                Notes.majorScaleNotes,
                listOf(
                    listOf(1, 2, 3, 4, 5, 6, 7),
                    listOf(),
                    listOf(),
                    listOf(1),
                    listOf(4, 7),
                    listOf(),
                    listOf(5, 1),
                    listOf(1, 2, 3, 4, 5, 6, 7)
                )
            )
    }
}
