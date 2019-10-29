package intellij.music.ui.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import intellij.music.ui.MusicApplicationComponent

class SwitchToMinorScaleAction: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        MusicApplicationComponent.instance.controller.toggleScaleSwitch(false)
        MusicApplicationComponent.instance.controller.setScale(false)
    }
}
