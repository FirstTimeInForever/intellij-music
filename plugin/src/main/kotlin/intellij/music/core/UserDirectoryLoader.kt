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
        println("Plugin directory path: $pluginDirectory")
        if (!userDirectory.exists() && !userDirectory.mkdirs()) {
            throw RuntimeException("Could not create user directory  ($userDirectory)")
        }
        if (!pluginDirectory.exists() && !pluginDirectory.mkdirs()) {
            throw RuntimeException("Could not create plugin directory  ($pluginDirectory)")
        }
    }

    fun initSoundFont(onSoundFontLoaded: (File) -> Unit) {
        if (soundFontFile.exists()) {
            reindexFiles()
            onSoundFontLoaded(soundFontFile)
        } else {
            loadFontFile(onSoundFontLoaded)
        }
    }

    private fun downloadFile(url: String, file: File) {
        val website = URL(url)
        val rbc = Channels.newChannel(website.openStream())
        val fos = FileOutputStream(file)
        fos.channel.transferFrom(rbc, 0, java.lang.Long.MAX_VALUE)
    }

    private fun loadFontFile(onSoundFontLoaded: (File) -> Unit) {
        ProgressManager.getInstance().run(object : Task.Backgroundable(
            null,
            "Fancy Music: Downloading sound font...",
            false,
            PerformInBackgroundOption.ALWAYS_BACKGROUND
        ) {
            override fun run(indicator: ProgressIndicator) {
                println("Fancy Music: Downloading midi files...")
                for (file in initialMidiFiles) {
                    downloadFile(initialMidiFilesUrl + file, File(userDirectory, file))
                }
                reindexFiles()

                println("Fancy Music: Downloading sound font...")
                downloadFile(soundFontUrl, soundFontFile)
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

    companion object {
        const val soundFontUrl = "https://raw.githubusercontent.com/urish/cinto/master/media/FluidR3%20GM.sf2"
        const val initialMidiFilesUrl = "https://raw.githubusercontent.com/FirstTimeInForever/intellij-music/master/plugin/assets/"
        val initialMidiFiles = listOf(
            "All_Star.mid",
            "Birds_Flying_High.mid",
            "Piano_Track_1.mid",
            "Piano_Track_2.mid",
            "Silent_Night.mid",
            "Yet_Dre.mid"
        )
    }
}
