package intellij.music.ui.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import intellij.music.ui.MusicApplicationComponent

class SwitchToRandomScaleAction: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        MusicApplicationComponent.instance.controller.toggleScaleSwitch(true)
    }
}
