package intellij.music.ui

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import intellij.music.core.MusicAlgorithmType
import intellij.music.core.UserDirectoryLoader

@State(name = "MusicConfig", storages = [(Storage("MusicConfig.xml"))])
class MusicConfig : PersistentStateComponent<MusicConfig> {

    var enabled: Boolean = true
    var onlyInEditor: Boolean = false
    var algorithmType: MusicAlgorithmType = MusicAlgorithmType.RANDOM
    var midiDir: String = UserDirectoryLoader.defaultMidiDir.toString()

    override fun getState(): MusicConfig = this

    override fun loadState(state: MusicConfig) = XmlSerializerUtil.copyBean(state, this)

    companion object {
        val instance: MusicConfig
            get() = ServiceManager.getService(MusicConfig::class.java)
    }
}
