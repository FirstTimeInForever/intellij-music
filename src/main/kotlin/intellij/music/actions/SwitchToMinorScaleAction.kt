package intellij.music.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import intellij.music.core.MusicAlgorithmType

class SwitchToMinorScaleAction: MusicAction() {
    override fun actionPerformed(event: AnActionEvent) {
        settings.algorithmType = MusicAlgorithmType.RANDOM_MINOR
        application.controller.onSwitchMusicTypeAction()
    }
}
