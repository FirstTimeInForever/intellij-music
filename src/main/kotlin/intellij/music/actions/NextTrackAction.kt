package intellij.music.actions

import com.intellij.openapi.actionSystem.AnActionEvent
import intellij.music.core.MusicAlgorithmType

class NextTrackAction : MusicAction() {
    override fun update(event: AnActionEvent) {
        event.presentation.isEnabled = settings.algorithmType == MusicAlgorithmType.SEQUENTIAL
    }

    override fun actionPerformed(event: AnActionEvent) {
        application.controller.nextTrack()
    }
}
