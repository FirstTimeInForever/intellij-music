package intellij.music.core

import java.io.File
import java.nio.file.FileSystems
import java.nio.file.StandardWatchEventKinds.*
import java.nio.file.WatchKey

class MidiDirectoryWatcher(private val onDirectoryContentChange: () -> Unit) {
    private val watcher = FileSystems.getDefault().newWatchService()
    @Volatile
    private var currentKey: WatchKey? = null

    init {
        Thread {
            while (true) {
                val key = watcher.take()
                key.pollEvents()
                onDirectoryContentChange()
                key.reset()
            }
        }.start()
    }

    fun setDirectory(directory: File) {
        currentKey?.cancel()
        currentKey = directory.toPath().register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY)
    }
}
