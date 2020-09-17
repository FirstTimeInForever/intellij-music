package intellij.music.settings

import com.intellij.openapi.components.service
import com.intellij.openapi.options.SearchableConfigurable
import intellij.music.ui.MusicApplicationService
import intellij.music.ui.MusicConfigurableGUI
import javax.swing.JComponent
import kotlin.properties.Delegates

class MusicConfigurable : SearchableConfigurable {
    private var gui by Delegates.notNull<MusicConfigurableGUI>()
    private val config = service<MusicSettings>()

    override fun isModified() = gui.isModified(config)

    override fun getId() = "preference.IntellijMusic"

    override fun getDisplayName() = "Fancy Music Plugin"

    override fun apply() {
        gui.saveToConfig(config)
        service<MusicApplicationService>().controller.onSettingsChanged()
    }

    override fun createComponent(): JComponent? {
        gui = MusicConfigurableGUI()
        gui.loadFromConfig(config)
        return gui.rootPanel
    }
}
