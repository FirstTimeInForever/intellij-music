package intellij.music.ui

import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.openapi.vfs.VirtualFileManager
import intellij.music.core.MusicAlgorithmType
import org.jetbrains.annotations.NonNls
import java.awt.Component
import java.io.File
import javax.swing.JCheckBox
import javax.swing.JPanel
import javax.swing.JRadioButton

class MusicConfigurableGUI {
    lateinit var rootPanel: JPanel
    private lateinit var enabled: JCheckBox
    private lateinit var onlyInEditor: JCheckBox

    private lateinit var musicType1: JRadioButton
    private lateinit var musicType2: JRadioButton
    private lateinit var midiDirChoose: TextFieldWithBrowseButton

    init {
        midiDirChoose.addActionListener { e ->
            @NonNls val path = midiDirChoose.text.trim { it <= ' ' }
            selectDirectory(
                path,
                { s -> midiDirChoose.text = s },
                rootPanel
            )
        }
    }

    private fun selectDirectory(
        path: String,
        dirConsumer: (String) -> Unit,
        component: Component?
    ) {
        var path = path
        val descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
            .withTitle("dialog.title.select.configuration.directory")
            .withDescription("dialog.description.select.configuration.directory")
            .withShowFileSystemRoots(true)
            .withHideIgnored(false)
            .withShowHiddenFiles(true)

        path = "file://" + path.replace(File.separatorChar, '/')
        val root = VirtualFileManager.getInstance().findFileByUrl(path)

        val file = FileChooser.chooseFile(descriptor, component, null, root) ?: return
        val resultPath = file.path.replace('/', File.separatorChar)
        dirConsumer(resultPath)
    }

    fun isModified(config: MusicConfig): Boolean {
        return enabled.isSelected != config.enabled
                || onlyInEditor.isSelected != config.onlyInEditor
                || musicType1.isSelected != config.algorithmType.isRandom()
                || midiDirChoose.text != config.midiDir
    }

    fun loadFromConfig(config: MusicConfig) {
        midiDirChoose.text = config.midiDir

        enabled.isSelected = config.enabled
        onlyInEditor.isSelected = config.onlyInEditor

        musicType1.isSelected = config.algorithmType == MusicAlgorithmType.RANDOM
        musicType2.isSelected = !musicType1.isSelected

        enabled.addActionListener { e -> updateEnabled() }
        updateEnabled()
    }

    private fun updateEnabled() {
        val isEnabled = enabled.isSelected
        onlyInEditor.isEnabled = isEnabled
        musicType1.isEnabled = isEnabled
        musicType2.isEnabled = isEnabled
        midiDirChoose.isEnabled = isEnabled
    }

    fun saveToConfig(config: MusicConfig) {
        config.enabled = enabled.isSelected
        config.onlyInEditor = onlyInEditor.isSelected
        config.algorithmType = if (musicType1.isSelected) {
            MusicAlgorithmType.RANDOM
        } else {
            MusicAlgorithmType.SEQUENTIAL
        }

        config.midiDir = midiDirChoose.text
    }
}
