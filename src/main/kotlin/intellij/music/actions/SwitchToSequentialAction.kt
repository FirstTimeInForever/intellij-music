package intellij.music.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import intellij.music.core.MusicAlgorithmType

class SwitchToSequentialAction: MusicAction() {
    override fun actionPerformed(event: AnActionEvent) {
        settings.algorithmType = MusicAlgorithmType.SEQUENTIAL
        application.controller.onSwitchMusicTypeAction()
    }
}
