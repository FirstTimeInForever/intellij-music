package intellij.music.ui.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import intellij.music.core.MusicAlgorithmType
import intellij.music.ui.MusicConfig

class SwitchMusicTypeAction : AnAction() {
    private val config = MusicConfig.instance

    override fun actionPerformed(e: AnActionEvent) {
        config.algorithmType = when (config.algorithmType) {
            MusicAlgorithmType.SEQUENTIAL -> MusicAlgorithmType.RANDOM
            MusicAlgorithmType.RANDOM -> MusicAlgorithmType.SEQUENTIAL
        }
    }
}
