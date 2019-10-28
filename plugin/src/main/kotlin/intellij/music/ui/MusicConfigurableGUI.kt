package intellij.music

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

    private fun isRandomMusic(): Boolean {
        return musicType1.isSelected
    }

    fun isModified(config: MusicConfig): Boolean {
        return enabled.isSelected != config.enabled
                || onlyInEditor.isSelected != config.onlyInEditor
                || isRandomMusic() != config.isRandomMusic
    }

    fun loadFromConfig(config: MusicConfig) {
        enabled.isSelected = config.enabled
        onlyInEditor.isSelected = config.onlyInEditor
        onlyInEditor.isEnabled = config.enabled

        musicType1.isSelected = config.isRandomMusic
        musicType2.isSelected = !config.isRandomMusic

        enabled.addActionListener { e ->
            onlyInEditor.isEnabled = enabled.isSelected
        }
    }

    fun saveToConfig(config: MusicConfig) {
        config.enabled = enabled.isSelected
        config.onlyInEditor = onlyInEditor.isSelected
        config.isRandomMusic = isRandomMusic()
    }
}
