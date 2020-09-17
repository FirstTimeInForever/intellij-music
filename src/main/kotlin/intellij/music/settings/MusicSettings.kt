package intellij.music.settings

import com.intellij.openapi.components.*
import com.intellij.util.xmlb.XmlSerializerUtil
import intellij.music.core.MusicAlgorithmType
import intellij.music.core.UserDirectoryLoader

@State(name = "MusicSettings", storages = [(Storage("MusicSettings.xml"))])
class MusicSettings : PersistentStateComponent<MusicSettings> {
    var enabled: Boolean = true
    var onlyInEditor: Boolean = false
    var algorithmType = MusicAlgorithmType.RANDOM_MINOR
    var midiDir: String = UserDirectoryLoader.defaultMidiDir.toString()

    override fun getState(): MusicSettings = this

    override fun loadState(state: MusicSettings) = XmlSerializerUtil.copyBean(state, this)
}
