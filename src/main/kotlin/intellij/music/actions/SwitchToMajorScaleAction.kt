package intellij.music.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import intellij.music.core.MusicAlgorithmType

class SwitchToMajorScaleAction: MusicAction() {
    override fun actionPerformed(event: AnActionEvent) {
        settings.algorithmType = MusicAlgorithmType.RANDOM_MAJOR
        application.controller.onSwitchMusicTypeAction()
    }
}
