package intellij.music

import javax.swing.JCheckBox
import javax.swing.JPanel

class MusicConfigurableGUI {
    private var enabled: JCheckBox? = null
    private var onlyInEditor: JCheckBox? = null
    var rootPanel: JPanel? = null

    fun isModified(config: MusicConfig): Boolean {
        return enabled!!.isSelected != config.enabled
                || onlyInEditor!!.isSelected != config.onlyInEditor
    }

    fun loadFromConfig(config: MusicConfig) {
        enabled!!.isSelected = config.enabled
        onlyInEditor!!.isSelected = config.onlyInEditor
        onlyInEditor!!.isEnabled = config.enabled

        enabled!!.addActionListener { e ->
            onlyInEditor!!.isEnabled = enabled!!.isSelected
        }
    }

    fun saveToConfig(config: MusicConfig) {
        config.enabled = enabled!!.isSelected
        config.onlyInEditor = onlyInEditor!!.isSelected
    }
}
