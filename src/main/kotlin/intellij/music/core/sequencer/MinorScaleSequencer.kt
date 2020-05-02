package intellij.music.core.sequencer

import com.intellij.openapi.diagnostic.logger
import intellij.music.core.MidiNotes
import kotlin.random.Random

class MinorScaleSequencer: ScaleSequencer {
    private var currentScale = "A"
    private var notesPlayedInScale = 0
    private var notesPlayedInOctave = 0
    private var currentOctave = listOf(MidiNotes.BASE_OCTAVE, MidiNotes.BASE_OCTAVE + 1).random()
    private var previousNoteIndex: Int = 0

    private val logger = logger<MinorScaleSequencer>()

    override fun nextNote(previousNote: Pair<String, Int>): Pair<String, Int> {
        notesPlayedInOctave += 1
        notesPlayedInScale += 1
        tryToChangeScale()
        return shift()
    }

    override fun getChord(): List<Pair<String, Int>> {
        val base = Pair(MidiNotes.minorScaleNotes[currentScale]!![0], currentOctave)
        val third = Pair(MidiNotes.minorScaleNotes[currentScale]!![2], currentOctave)
        val fifth = Pair(MidiNotes.minorScaleNotes[currentScale]!![5], currentOctave)
        return listOf(base, third, fifth)
    }

    private fun shift(): Pair<String, Int> {
        previousNoteIndex = if (modeShifts[previousNoteIndex].isNotEmpty()) {
            modeShifts[previousNoteIndex].random()
        }
        else {
            listOf(0, 4, 5, 7).random()
        }
        val scale = MidiNotes.minorScaleNotes[currentScale] ?: error("Failed to get scale!")
        if (Random.nextBoolean()) {
            if (previousNoteIndex == 0 || previousNoteIndex == 7) {
                val octaveShift =
                    MidiNotes.octaveShift(notesPlayedInOctave, currentOctave)
                if(octaveShift != 0) {
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
            currentScale = MidiNotes.minorScaleNotes.keys.random()
            logger.info("Changed scale to: $currentScale")
            notesPlayedInScale = 0
        }
    }


    companion object {
        val modeShifts: List<List<Int>> = listOf(
            listOf(1, 2, 3, 4, 5, 6, 7),
            listOf(3, 4),
            listOf(4),
            listOf(2, 7),
            listOf(7),
            listOf(2, 4, 1),
            listOf(1, 3),
            listOf(1, 2, 3, 4, 5, 6, 7)
        )
    }
}
