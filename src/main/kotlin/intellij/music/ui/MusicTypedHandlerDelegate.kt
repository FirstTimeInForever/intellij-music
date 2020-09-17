package intellij.music.ui

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorKind
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import intellij.music.settings.MusicConfig

// https://intellij-support.jetbrains.com/hc/en-us/community/posts/206756295-TypedActionHandler-with-default-typing-behavior?flash_digest=fd9d93d7fb0d9a406689b0c9f6c765be9dca66c8
class MusicTypedHandlerDelegate : TypedHandlerDelegate() {
    private val config = MusicConfig.instance

    override fun charTyped(char: Char, project: Project, editor: Editor, file: PsiFile): Result {
        if (config.enabled && config.onlyInEditor && editor.editorKind == EditorKind.MAIN_EDITOR) {
            val application = MusicApplicationComponent.instance
            val event = MusicKeyboardEvent(char, 0, "en", 0)
            application.keyboardPressed(event)
        }
        return super.charTyped(char, project, editor, file)
    }
}
