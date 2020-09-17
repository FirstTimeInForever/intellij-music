package intellij.music.ui

import com.intellij.ide.IdeEventQueue
import com.intellij.openapi.components.service
import intellij.music.settings.MusicSettings
import java.awt.event.KeyEvent

class MusicKeyEventListener {
    private val settings = service<MusicSettings>()

    init {
        IdeEventQueue.getInstance().addPostprocessor({ event ->
            if (event is KeyEvent && settings.enabled && !settings.onlyInEditor) {
                onKeyEvent(event)
            }
            false
        }, null)
    }

    private fun onKeyEvent(event: KeyEvent) {
        if (event.id == KeyEvent.KEY_PRESSED && event.keyChar != KeyEvent.CHAR_UNDEFINED) {
            val layout = if (event.keyCode == event.extendedKeyCode) {
                "en"
            } else "ru"
            val numberModifiers = getNumberModifiers(event)
            service<MusicApplicationService>().keyPressed(
                MusicKeyboardEvent(event.keyChar, event.keyCode, layout, numberModifiers)
            )
        }
    }

    private fun getNumberModifiers(event: KeyEvent): Int {
        var numberModifiers = 0
        if (event.isControlDown) ++numberModifiers
        if (event.isAltDown) ++numberModifiers
        if (event.isMetaDown) ++numberModifiers
        return numberModifiers
    }
}
