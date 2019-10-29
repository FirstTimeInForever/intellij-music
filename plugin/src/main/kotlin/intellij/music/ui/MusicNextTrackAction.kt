package intellij.music.ui

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class MusicNextTrackAction: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        MusicApplicationComponent.instance.controller.nextTrack()
    }
}
