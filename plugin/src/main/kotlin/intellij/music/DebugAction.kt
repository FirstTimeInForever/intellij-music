package intellij.music

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import java.awt.im.InputContext

//data class MusicKeyboardEvent(
//    val char: Char,
//    val code: Int,
//    val layout: String
//)

class DebugAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
//        ApplicationManager.getApplication().invokeLater { run() }

        val instance = InputContext.getInstance()
        val locale = instance.locale
        println(locale)
    }

    private fun run() {
//        val event = MusicKeyboardEvent('a', 65, "en")
//
//        val baseUrl = "http://localhost:7777"
//        val (request, response, result) = "${baseUrl}/keyboard-event"
//            .httpPost()
//            .jsonBody(event)
//            .responseString()
//        response.isSuccessful


//        when (result) {
//            is Result.Failure -> {
//                val ex = result.getException()
//                println(ex)
//            }
//            is Result.Success -> {
//                val data = result.get()
//                println(data)
//            }
//        }
    }
}
