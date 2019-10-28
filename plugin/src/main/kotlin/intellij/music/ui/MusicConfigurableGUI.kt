package intellij.music.ui

import intellij.music.core.MusicAlgorithmType
import intellij.music.ui.MusicConfig
import javax.swing.JCheckBox
import javax.swing.JPanel
import javax.swing.JRadioButton

class MusicConfigurableGUI {
    lateinit var rootPanel: JPanel
    private lateinit var enabled: JCheckBox
    private lateinit var onlyInEditor: JCheckBox

    private lateinit var musicType1: JRadioButton
    private lateinit var musicType2: JRadioButton

    fun isModified(config: MusicConfig): Boolean {
        return enabled.isSelected != config.enabled
                || onlyInEditor.isSelected != config.onlyInEditor
                || musicType1.isSelected != config.algorithmType.isRandom()
    }

    fun loadFromConfig(config: MusicConfig) {
        enabled.isSelected = config.enabled
        onlyInEditor.isSelected = config.onlyInEditor
        onlyInEditor.isEnabled = config.enabled

        musicType1.isSelected = config.algorithmType == MusicAlgorithmType.RANDOM
        musicType2.isSelected = !musicType1.isSelected

        enabled.addActionListener { e ->
            onlyInEditor.isEnabled = enabled.isSelected
        }
    }

    fun saveToConfig(config: MusicConfig) {
        config.enabled = enabled.isSelected
        config.onlyInEditor = onlyInEditor.isSelected
        config.algorithmType = if (musicType1.isSelected) {
            MusicAlgorithmType.RANDOM
        } else {
            MusicAlgorithmType.SEQUENTIAL
        }
    }
}
