package intellij.music.ui

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import com.intellij.openapi.application.ApplicationManager

class MuteAction: ToggleAction() {
    private val application = ApplicationManager.getApplication().getComponent(MusicApplicationComponent::class.java)
    override fun isSelected(e: AnActionEvent): Boolean {
        return application.controller.isMuted
    }

    override fun setSelected(e: AnActionEvent, state: Boolean) {
        application.controller.setMuteState(state)
    }
}
