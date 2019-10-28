package intellij.music.core

import intellij.music.ui.MusicKeyboardEvent

/*
    Base controller class.
 */
class MusicController {
    private val keyboardStorage: KeyboardStorage = KeyboardStorage(10.0)
    private val apmController: ApmController = ApmController()

    fun keyboardPressed(event: MusicKeyboardEvent) {
        keyboardStorage.addEvent(event)
        apmController.onAction()
    }
}
