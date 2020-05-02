package intellij.music.ui.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import intellij.music.core.MusicAlgorithmType
import intellij.music.ui.MusicApplicationComponent
import intellij.music.ui.MusicConfig

class SwitchToRandomScaleAction: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        MusicConfig.instance.algorithmType = MusicAlgorithmType.RANDOM_BOTH
        MusicApplicationComponent.instance.controller.onSwitchMusicTypeAction()
    }
}
