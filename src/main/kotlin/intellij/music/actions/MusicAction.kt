package intellij.music.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.components.service
import intellij.music.settings.MusicSettings
import intellij.music.ui.MusicApplicationService

abstract class MusicAction : AnAction() {
    protected val settings = service<MusicSettings>()
    protected val application = service<MusicApplicationService>()
}
