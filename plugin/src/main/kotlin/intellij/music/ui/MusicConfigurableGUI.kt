package intellij.music.ui

import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.ui.Messages
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
        midiDirChoose.addActionListener {
            @NonNls val path = midiDirChoose.text.trim { it <= ' ' }
            selectDirectory(path, this::onSelectDirectory, rootPanel)
        }

        enabled.addActionListener { updateEnabled() }
        musicType2.addActionListener { updateEnabled() }
    }

    private fun onSelectDirectory(directory: String) {
        val file = File(directory)
        val midiFiles = file.listFiles { midiFile -> midiFile.extension == "mid" }
        if (midiFiles?.isEmpty() == true) {
            Messages.showMessageDialog(null, "Please select directory which contains midi files", "Fancy Music", Messages.getInformationIcon());
            return
        }

        midiDirChoose.text = directory
    }

    private fun selectDirectory(path0: String, dirConsumer: (String) -> Unit, component: Component?) {
        var path = path0
        val descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor()
            .withTitle("Select directory with midi files")
            .withShowFileSystemRoots(true)

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

        updateEnabled()
    }

    private fun updateEnabled() {
        val isEnabled = enabled.isSelected
        onlyInEditor.isEnabled = isEnabled
        musicType1.isEnabled = isEnabled
        musicType2.isEnabled = isEnabled
        midiDirChoose.isEnabled = isEnabled && musicType2.isSelected
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
