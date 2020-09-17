package intellij.music.settings

import com.intellij.openapi.options.SearchableConfigurable
import intellij.music.ui.MusicApplicationComponent
import intellij.music.ui.MusicConfigurableGUI
import javax.swing.JComponent
import kotlin.properties.Delegates

class MusicConfigurable : SearchableConfigurable {
    private var gui by Delegates.notNull<MusicConfigurableGUI>()
    private val config = MusicConfig.instance

    override fun isModified(): Boolean {
        return gui.isModified(config)
    }

    override fun getId(): String {
        return "preference.IntellijMusic"
    }

    override fun getDisplayName(): String {
        return "Fancy Music Plugin"
    }

    override fun apply() {
        gui.saveToConfig(config)
        MusicApplicationComponent.instance.controller.onSettingsChanged()
    }

    override fun createComponent(): JComponent? {
        gui = MusicConfigurableGUI()
        gui.loadFromConfig(config)
        return gui.rootPanel
    }
}
