package intellij.music.ui

import com.intellij.ide.IdeEventQueue
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.BaseComponent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.ex.EditorEventMulticasterEx
import com.intellij.openapi.editor.ex.FocusChangeListener
import intellij.music.core.MusicController
import org.apache.log4j.Level
import java.awt.event.KeyEvent


class MusicApplicationComponent : BaseComponent {
    private var config = MusicConfig.instance
    val controller: MusicController = MusicController()

    override fun initComponent() {
        LOG.setLevel(Level.INFO)
        LOG.info("Initializing plugin data structures")

        initFocusListener()
        initKeyListener()
    }

    private fun initFocusListener() {
        val editorEventMulticaster = EditorFactory.getInstance().eventMulticaster as EditorEventMulticasterEx
        editorEventMulticaster.addFocusChangeListener(object : FocusChangeListener {
            override fun focusGained(editor: Editor) {}

            override fun focusLost(editor: Editor) {
//                stopMusic()
            }
        }, Disposable {})
    }

    private fun initKeyListener() {
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
            ApplicationManager.getApplication().executeOnPooledThread {
                val numberModifiers = getNumberModifiers(e)
                val event = MusicKeyboardEvent(keyChar, keyCode, layout, numberModifiers)
                controller.keyboardPressed(event)
            }
        }
    }

    private fun getNumberModifiers(e: KeyEvent): Int {
        var numberModifiers = 0
        if (e.isControlDown) ++numberModifiers
        if (e.isAltDown) ++numberModifiers
        if (e.isMetaDown) ++numberModifiers
        return numberModifiers
    }

    override fun disposeComponent() {
        LOG.info("Disposing plugin data structures")
    }

    override fun getComponentName(): String {
        return "musicApplicationComponent"
    }

    companion object {
        private val LOG = Logger.getInstance(MusicApplicationComponent::class.java)
    }
}
