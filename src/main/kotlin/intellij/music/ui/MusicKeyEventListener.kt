package intellij.music.ui

import com.intellij.ide.IdeEventQueue
import intellij.music.settings.MusicConfig
import java.awt.event.KeyEvent

class MusicKeyEventListener {
    private val config = MusicConfig.instance

    fun initKeyListener() {
        IdeEventQueue.getInstance().addPostprocessor({ e ->
            if (e is KeyEvent && config.enabled && !config.onlyInEditor) {
                onKeyEvent(e)
            }
            false
        }, null)
    }

    private fun onKeyEvent(e: KeyEvent) {
        val keyChar = e.keyChar
        val keyCode = e.keyCode
        if (e.id == KeyEvent.KEY_PRESSED && keyChar != KeyEvent.CHAR_UNDEFINED) {
            val layout = if (e.keyCode == e.extendedKeyCode) {
                "en"
            } else {
                "ru"
            }

            val numberModifiers = getNumberModifiers(e)
            val event = MusicKeyboardEvent(keyChar, keyCode, layout, numberModifiers)
            MusicApplicationComponent.instance.keyboardPressed(event)
        }
    }

    private fun getNumberModifiers(e: KeyEvent): Int {
        var numberModifiers = 0
        if (e.isControlDown) ++numberModifiers
        if (e.isAltDown) ++numberModifiers
        if (e.isMetaDown) ++numberModifiers
        return numberModifiers
    }
}
