package intellij.music

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import java.awt.im.InputContext

class MusicDebugAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val instance = InputContext.getInstance()
        val locale = instance.locale
        println(locale)
    }
}
