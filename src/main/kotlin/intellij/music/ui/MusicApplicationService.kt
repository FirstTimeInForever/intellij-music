package intellij.music.ui

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service
import intellij.music.core.MusicController

@Service
class MusicApplicationService {
    val controller: MusicController = MusicController()

    init {
        MusicKeyEventListener().initKeyListener()
    }

    fun keyboardPressed(event: MusicKeyboardEvent) {
        ApplicationManager.getApplication().executeOnPooledThread {
            controller.keyboardPressed(event)
        }
    }
}
