package intellij.music.core

interface ScaleSequencer {
    fun nextNote(previousNote: Pair<String, Int>): Pair<String, Int>
    fun getChord(): List<Pair<String, Int>>
}
