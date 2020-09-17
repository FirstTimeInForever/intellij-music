package intellij.music.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import intellij.music.settings.MusicConfig

class MuteAction : ToggleAction() {
    private val config = MusicConfig.instance

    override fun isSelected(e: AnActionEvent): Boolean {
        return !config.enabled
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        config.enabled = !state
    }
}
