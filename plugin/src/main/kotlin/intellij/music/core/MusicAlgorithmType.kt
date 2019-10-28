package intellij.music.core

enum class MusicAlgorithmType {
    RANDOM,
    SEQUENTIAL;


    fun isRandom(): Boolean {
        return this == RANDOM
    }
}