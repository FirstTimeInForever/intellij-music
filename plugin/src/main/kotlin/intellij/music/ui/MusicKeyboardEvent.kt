package intellij.music.ui

data class MusicKeyboardEvent(
    val char: Char,
    val code: Int,
    val layout: String,
    val numberModifiers: Int
)