package intellij.music.core.sequencer

interface ScaleSequencer {
    fun nextNote(previousNote: Pair<String, Int>): Pair<String, Int>
    fun getChord(): List<Pair<String, Int>>
}
