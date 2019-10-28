package intellij.music.core

import java.util.*

class ApmController {
    init {
        Thread {
            recalcApm()
            Thread.sleep(RECALC_INTERVAL)
        }.start()
    }

    private var apm: Long = 1
        get() = field
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
            while (actionTimes.first?.let { currentTime - it > WINDOW_LENGTH } == true) {
                actionTimes.removeFirst()
            }
            actionTimes.size
        }

        val actionsPerWindow = numberActions / WINDOW_LENGTH
        apm = actionsPerWindow
    }

    companion object {
        const val RECALC_INTERVAL: Long = 500  // milliseconds
        const val WINDOW_LENGTH: Long = 3 * 1000  // milliseconds
    }
}
