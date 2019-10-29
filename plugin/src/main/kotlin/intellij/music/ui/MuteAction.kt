package intellij.music.ui

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction

class MuteAction : ToggleAction() {
    private var config: MusicConfig = MusicConfig.instance

    override fun isSelected(e: AnActionEvent): Boolean {
        return !config.enabled
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        config.enabled = !state
    }
}
