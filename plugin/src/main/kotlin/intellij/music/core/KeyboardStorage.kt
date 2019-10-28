package intellij.music.core

import intellij.music.ui.MusicKeyboardEvent
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.TimeUnit


class KeyboardStorage(timeInterval: Double) {
    data class MusicEvent(
        val time: Date,
        val event: MusicKeyboardEvent
    )

    private var eventsQueue: Queue<MusicEvent> = ConcurrentLinkedDeque<MusicEvent>()
    
    var interval: Double = timeInterval
    val frequency: Double
        get() = eventsQueue.size / interval

    fun addEvent(event: MusicKeyboardEvent) {
        val now = Date()

        eventsQueue.removeIf { element ->
            TimeUnit.SECONDS.toSeconds(now.time - element.time.time) <= this.interval
        }

        eventsQueue.add(MusicEvent(now, event))
    }
}