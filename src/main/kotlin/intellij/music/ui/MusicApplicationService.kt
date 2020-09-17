package intellij.music.ui

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import intellij.music.core.MusicController

@Service
class MusicApplicationService {
    val controller: MusicController = MusicController()
    private val keyEventListener = MusicKeyEventListener()

    fun keyPressed(event: MusicKeyboardEvent) {
        ApplicationManager.getApplication().executeOnPooledThread {
            controller.keyboardPressed(event)
        }
    }
}
