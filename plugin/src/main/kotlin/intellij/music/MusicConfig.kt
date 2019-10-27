package intellij.music

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "MusicConfig", storages = [(Storage("MusicConfig.xml"))])
class MusicConfig : PersistentStateComponent<MusicConfig> {

    var enabled: Boolean = true
    var onlyInEditor: Boolean = false

    override fun getState(): MusicConfig = this

    override fun loadState(state: MusicConfig) = XmlSerializerUtil.copyBean(state, this)
}
