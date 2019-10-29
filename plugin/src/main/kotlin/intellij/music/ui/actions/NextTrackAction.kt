package intellij.music.ui.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import intellij.music.core.MusicAlgorithmType
import intellij.music.ui.MusicApplicationComponent
import intellij.music.ui.MusicConfig

class NextTrackAction : AnAction() {
    private val config = MusicConfig.instance

    override fun update(event: AnActionEvent) {
        event.presentation.isEnabled = config.algorithmType == MusicAlgorithmType.SEQUENTIAL
    }

    override fun actionPerformed(e: AnActionEvent) {
        MusicApplicationComponent.instance.controller.nextTrack()
    }
}
