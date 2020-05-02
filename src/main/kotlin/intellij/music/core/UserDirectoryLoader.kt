package intellij.music.core

import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.progress.PerformInBackgroundOption
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import intellij.music.ui.MusicConfig
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels

class UserDirectoryLoader {
    private val config = MusicConfig.instance
    private val pluginDirectory = defaultMidiDir
    private val soundFontFile = File(pluginDirectory, "soundfont.sf2")
    val userFiles = mutableListOf<File>()

    private val midiDirectoryWatcher = MidiDirectoryWatcher(this::reindexFiles)

    private val logger = logger<UserDirectoryLoader>()

    private val userDirectory
        get() = File(config.midiDir)

    init {
        logger.info("User directory path: $userDirectory")
        logger.info("Plugin directory path: $pluginDirectory")
        if (!userDirectory.exists() && !userDirectory.mkdirs()) {
            throw RuntimeException("Could not create user directory  ($userDirectory)")
        }
        if (!pluginDirectory.exists() && !pluginDirectory.mkdirs()) {
            throw RuntimeException("Could not create plugin directory  ($pluginDirectory)")
        }
    }

    fun initSoundFont(onSoundFontLoaded: (File) -> Unit) {
        reindexFiles()
        if (soundFontFile.exists() && userFiles.isNotEmpty()) {
            onSoundFontLoaded(soundFontFile)
        } else {
            config.midiDir = defaultMidiDir.toString()
            downloadFontAndMidiFiles(onSoundFontLoaded)
        }
        midiDirectoryWatcher.setDirectory(userDirectory)
    }

    private fun downloadFile(url: String, file: File) {
        val website = URL(url)
        val rbc = Channels.newChannel(website.openStream())
        val fos = FileOutputStream(file)
        fos.channel.transferFrom(rbc, 0, java.lang.Long.MAX_VALUE)
    }

    private fun downloadFontAndMidiFiles(onSoundFontLoaded: (File) -> Unit) {
        ProgressManager.getInstance().run(object : Task.Backgroundable(
            null,
            "Fancy Music: Downloading sound font...",
            false,
            PerformInBackgroundOption.ALWAYS_BACKGROUND
        ) {
            override fun run(indicator: ProgressIndicator) {
                logger.info("Downloading midi files...")
                for (file in initialMidiFiles) {
                    downloadFile(initialMidiFilesUrl + file, File(userDirectory, file))
                }
                reindexFiles()

                logger.info("Downloading sound font...")
                downloadFile(soundFontUrl, soundFontFile)
                onSoundFontLoaded(soundFontFile)
            }
        })
    }

    fun reindexFiles() {
        val files: Array<File> = userDirectory.listFiles { file -> !file.isDirectory && file.extension == "mid" } ?: emptyArray()
        logger.info("Reindex, found files:  ${files.map { it.name }}")

        userFiles.clear()
        userFiles.addAll(files)
        userFiles.shuffle()
    }

    fun reloadMidiFilesDirectory() {
        midiDirectoryWatcher.setDirectory(userDirectory)
        reindexFiles()
    }

    companion object {
        val defaultMidiDir = File(System.getProperty("user.home"), ".intellij-fancy-music-plugin")

        const val soundFontUrl = "https://raw.githubusercontent.com/urish/cinto/master/media/FluidR3%20GM.sf2"
        const val initialMidiFilesUrl = "https://raw.githubusercontent.com/FirstTimeInForever/intellij-music/master/assets/"
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
