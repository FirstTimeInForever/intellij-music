package intellij.music.core

import com.intellij.openapi.progress.PerformInBackgroundOption
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels

class UserDirectoryLoader {
    private val userDirectory = File(System.getProperty("user.home"), "my-midis")
    private val pluginDirectory = File(System.getProperty("user.home"), ".intellij-fancy-music-plugin")
    private val soundFontFile = File(pluginDirectory, "soundfont.sf2")
    val userFiles = mutableListOf<File>()

    init {
        println("User directory path: $userDirectory")
        if (!userDirectory.exists() && !userDirectory.mkdirs()) {
            throw RuntimeException("Could not create user directory  ($userDirectory)")
        }
        if (!pluginDirectory.exists() && !pluginDirectory.mkdirs()) {
            throw RuntimeException("Could not create plugin directory  ($pluginDirectory)")
        }

        reindexFiles()
    }

    fun initSoundFont(onSoundFontLoaded: (File) -> Unit) {
        if (soundFontFile.exists()) {
            onSoundFontLoaded(soundFontFile)
        } else {
            loadFontFile(onSoundFontLoaded)
        }
    }

    private fun loadFontFile(onSoundFontLoaded: (File) -> Unit) {
        ProgressManager.getInstance().run(object : Task.Backgroundable(
            null,
            "Fancy Music: Downloading sound font...",
            false,
            PerformInBackgroundOption.ALWAYS_BACKGROUND
        ) {
            override fun run(indicator: ProgressIndicator) {
                println("Fancy Music: Downloading sound font...")
                val website = URL("https://raw.githubusercontent.com/urish/cinto/master/media/FluidR3%20GM.sf2")
                val rbc = Channels.newChannel(website.openStream())
                val fos = FileOutputStream(soundFontFile)
                fos.channel.transferFrom(rbc, 0, java.lang.Long.MAX_VALUE)
                onSoundFontLoaded(soundFontFile)
            }
        })
    }

    fun reindexFiles() {
        userFiles.clear()
        for(file in userDirectory.listFiles()) {
//            System.out.println(file.extension)
            if(!file.isDirectory && file.extension == "mid") {
                userFiles.add(file)
            }
        }
    }
}
