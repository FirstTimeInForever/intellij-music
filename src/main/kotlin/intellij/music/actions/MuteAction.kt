package intellij.music.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import com.intellij.openapi.components.service
import intellij.music.settings.MusicSettings

class MuteAction : ToggleAction() {
    private val settings = service<MusicSettings>()

    override fun isSelected(event: AnActionEvent): Boolean {
        return !settings.enabled
    }

    override fun setSelected(event: AnActionEvent, state: Boolean) {
        settings.enabled = !state
    }
}
