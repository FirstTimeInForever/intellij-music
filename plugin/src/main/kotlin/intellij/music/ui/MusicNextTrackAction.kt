package intellij.music.ui

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager

class MusicNextTrackAction: AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val application = ApplicationManager.getApplication().getComponent(MusicApplicationComponent::class.java)
        application.controller.nextTrack()
    }
}