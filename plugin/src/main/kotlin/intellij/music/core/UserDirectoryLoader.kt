package intellij.music.core

import java.io.File
import java.lang.RuntimeException
import java.nio.file.Paths

class UserDirectoryLoader(private val userDirectory: File) {
    val userFiles = mutableListOf<File>()
    var soundFontFile: File? = null

    init {
        System.out.println("User directory path: " + userDirectory.toString())
        if(!userDirectory.exists()) {
//            System.out.println("User directory does not exists. Trying to create it.")
//            if(!userDirectory.mkdirs()) {
//                throw RuntimeException("Could not create user directory")
//            }
            throw RuntimeException("User directory does not exists!")
        }
        soundFontFile = Paths.get(userDirectory.toString(), "soundfont.sf2").toFile()
        if(!soundFontFile!!.exists()) {
            throw RuntimeException("Could not load soundfont!")
        }
        reindexFiles()
    }

    fun reindexFiles() {
        userFiles.clear()
        for(file in userDirectory.listFiles()) {
//            System.out.println(file.extension)
            if(!file.isDirectory() && file.extension.equals("mid")) {
                userFiles.add(file)
            }
        }
    }
}
