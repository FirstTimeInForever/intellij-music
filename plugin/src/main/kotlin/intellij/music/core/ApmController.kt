package intellij.music.core

import java.util.*

class ApmController(val onApmUpdate: (apm: Float) -> Unit) {
    init {
        Thread {
            Thread.sleep(5000)
            while (true) {
                recalcApm()
                onApmUpdate(apm)
                Thread.sleep(RECALC_INTERVAL)
            }
        }.start()
    }

    private var apm: Float = 1f
    private val actionTimes: Deque<Long> = ArrayDeque()

    fun onAction() {
        val currentTime = System.currentTimeMillis()
        synchronized(this) {
            actionTimes.addLast(currentTime)
        }
    }

    private fun recalcApm() {
        val currentTime = System.currentTimeMillis()
        val numberActions = synchronized(this) {
            while (actionTimes.peekFirst()?.let { currentTime - it > WINDOW_LENGTH } == true) {
                actionTimes.removeFirst()
            }
            actionTimes.size
        }

        val actionsPerWindow = numberActions.toFloat()
        apm = actionsPerWindow
    }

    companion object {
        const val RECALC_INTERVAL: Long = 300  // milliseconds
        const val WINDOW_LENGTH: Long = 3 * 1000  // milliseconds
    }
}
