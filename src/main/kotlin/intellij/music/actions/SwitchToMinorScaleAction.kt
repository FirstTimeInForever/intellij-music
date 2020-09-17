package intellij.music.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import intellij.music.core.MusicAlgorithmType
import intellij.music.ui.MusicApplicationComponent
import intellij.music.settings.MusicConfig

class SwitchToMinorScaleAction: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        MusicConfig.instance.algorithmType = MusicAlgorithmType.RANDOM_MINOR
        MusicApplicationComponent.instance.controller.onSwitchMusicTypeAction()
    }
}
