package intellij.music.core

import java.util.*

class ApmController(val onApmUpdate: (actionsPerSecond: Float, timeSinceLastAction: Long) -> Unit) {
    init {
        Thread {
            Thread.sleep(5000)
            while (true) {
                recalcApm()
                Thread.sleep(RECALC_INTERVAL)
            }
        }.start()
    }

    private val actionTimes: Deque<Long> = ArrayDeque()

    fun onAction() {
        val currentTime = System.currentTimeMillis()
        synchronized(this) {
            actionTimes.addLast(currentTime)
        }
    }

    private fun recalcApm() {
        val currentTime = System.currentTimeMillis()
        val (numberActions, millisecondsSinceLastAction) = synchronized(this) {
            while (actionTimes.peekFirst()?.let { currentTime - it > WINDOW_LENGTH } == true) {
                actionTimes.removeFirst()
            }
            val millisecondsSinceLastAction = actionTimes.peekLast()?.let { currentTime - it } ?: 1000
            Pair(actionTimes.size, millisecondsSinceLastAction)
        }

        val actionsPerWindow = numberActions.toFloat()
        val actionsPerSecond = actionsPerWindow / (WINDOW_LENGTH / 1000)
        onApmUpdate(actionsPerSecond, millisecondsSinceLastAction)
        System.err.println("aps: $actionsPerSecond,  $millisecondsSinceLastAction  ${currentTime % 1000000}")
    }

    companion object {
        const val RECALC_INTERVAL: Long = 300  // milliseconds
        const val WINDOW_LENGTH: Long = 10 * 1000  // milliseconds
    }
}
