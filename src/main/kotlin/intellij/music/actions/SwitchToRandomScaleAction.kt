package intellij.music.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import intellij.music.core.MusicAlgorithmType

class SwitchToRandomScaleAction: MusicAction() {
    override fun actionPerformed(event: AnActionEvent) {
        settings.algorithmType = MusicAlgorithmType.RANDOM_BOTH
        application.controller.onSwitchMusicTypeAction()
    }
}
