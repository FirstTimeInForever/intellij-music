package intellij.music.ui

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


class MusicApplicationComponent : BaseComponent {
    val controller: MusicController = MusicController()

    override fun initComponent() {
        LOG.setLevel(Level.INFO)
        LOG.info("Initializing plugin data structures")

        initFocusListener()
        MusicKeyEventListener().initKeyListener()
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

    fun keyboardPressed(event: MusicKeyboardEvent) {
        ApplicationManager.getApplication().executeOnPooledThread {
            controller.keyboardPressed(event)
        }
    }

    override fun disposeComponent() {
        LOG.info("Disposing plugin data structures")
    }

    override fun getComponentName(): String {
        return "musicApplicationComponent"
    }

    companion object {
        private val LOG = Logger.getInstance(MusicApplicationComponent::class.java)

        val instance: MusicApplicationComponent
            get() = ApplicationManager.getApplication().getComponent(MusicApplicationComponent::class.java)
    }
}
