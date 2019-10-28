package intellij.music.ui

import com.intellij.openapi.options.SearchableConfigurable
import javax.swing.JComponent
import kotlin.properties.Delegates

class MusicConfigurable : SearchableConfigurable {
    private var gui by Delegates.notNull<MusicConfigurableGUI>()
    private var config: MusicConfig = MusicConfig.instance

    override fun isModified(): Boolean {
        return gui.isModified(config)
    }

    override fun getId(): String {
        return "preference.IntellijMusic"
    }

    override fun getDisplayName(): String {
        return "Intellij Music Plugin"
    }

    override fun apply() {
        gui.saveToConfig(config)
    }

    override fun createComponent(): JComponent? {
        gui = MusicConfigurableGUI()
        gui.loadFromConfig(config)
        return gui.rootPanel
    }
}
