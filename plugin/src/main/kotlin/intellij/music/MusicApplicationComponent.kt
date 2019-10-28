package intellij.music

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.core.isSuccessful
import com.github.kittinunf.fuel.gson.jsonBody
import com.intellij.ide.IdeEventQueue
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.BaseComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.diagnostic.Logger
import org.apache.log4j.Level
import java.awt.event.KeyEvent

data class MusicKeyboardEvent(
    val char: Char,
    val code: Int,
    val layout: String
)

// or try TypedActionHandler?
class MyApplicationComponent : BaseComponent {

    private var config: MusicConfig = ServiceManager.getService(MusicConfig::class.java)
    private var isActivated = false

    override fun initComponent() {
        LOG.setLevel(Level.INFO);
        LOG.info("Initializing plugin data structures")

        IdeEventQueue.getInstance().addPostprocessor({ e ->
            if (e is KeyEvent) {
                onKeyEvent(e)
            }
            false
        }, null)
    }

    private fun onKeyEvent(e: KeyEvent) {
        if (!config.enabled) return

        val keyChar = e.keyChar
        val keyCode = e.keyCode
        if (e.id == KeyEvent.KEY_PRESSED && keyChar != KeyEvent.CHAR_UNDEFINED) {
            val layout = if (e.keyCode == e.extendedKeyCode) {
                "en"
            } else {
                "ru"
            }
            ApplicationManager.getApplication().executeOnPooledThread {
                submitKeyboardEvent(keyChar, keyCode, layout)
            }
        }
    }

    private fun submitKeyboardEvent(keyChar: Char, keyCode: Int, layout: String) {
        checkActivated()

        val event = MusicKeyboardEvent(keyChar, keyCode, layout)
//        val json = "{\"char\": \"${keyChar}\", \"code\": ${keyCode}, \"layout\": \"${layout}\"}"
        val (request, response, result) = Fuel.post("${baseUrl}/keyboard-event")
            .jsonBody(event)
            .responseString()
        assert(response.isSuccessful)
    }

    private fun checkActivated() {
        if (isActivated) return

        isActivated = true
        val (request, response, result) = Fuel.post("${baseUrl}/start")
            .jsonBody("", Charsets.UTF_8)
            .responseString()
        assert(response.isSuccessful)
    }

    override fun disposeComponent() {
        LOG.info("Disposing plugin data structures")
    }

    override fun getComponentName(): String {
        return "musicApplicationComponent"
    }

    companion object {
        private val LOG = Logger.getInstance(MyApplicationComponent::class.java)
        private const val baseUrl = "http://localhost:5000"
    }
}
