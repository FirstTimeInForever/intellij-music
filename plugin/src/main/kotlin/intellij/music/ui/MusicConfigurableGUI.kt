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

    private lateinit var musicTypeRandomMinor: JRadioButton
    private lateinit var musicTypeRandomMajor: JRadioButton
    private lateinit var musicTypeRandomBoth: JRadioButton
    private lateinit var musicTypeMidi: JRadioButton
    private lateinit var midiDirChoose: TextFieldWithBrowseButton

    init {
        midiDirChoose.addActionListener {
            @NonNls val path = midiDirChoose.text.trim { it <= ' ' }
            selectDirectory(path, this::onSelectDirectory, rootPanel)
        }

        enabled.addActionListener { updateEnabled() }
        musicTypeMidi.addActionListener { updateEnabled() }
    }

    private fun onSelectDirectory(directory: String) {
        val file = File(directory)
        val midiFiles = file.listFiles { midiFile -> midiFile.extension == "mid" }
        if (midiFiles?.isEmpty() == true) {
            Messages.showMessageDialog(null, "Please select directory which contains midi files", "Fancy Music", Messages.getInformationIcon())
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
                || currentAlgorithmType() != config.algorithmType
                || midiDirChoose.text != config.midiDir
    }

    fun loadFromConfig(config: MusicConfig) {
        midiDirChoose.text = config.midiDir

        enabled.isSelected = config.enabled
        onlyInEditor.isSelected = config.onlyInEditor

        musicTypeRandomMinor.isSelected = config.algorithmType == MusicAlgorithmType.RANDOM_MINOR
        musicTypeRandomMajor.isSelected = config.algorithmType == MusicAlgorithmType.RANDOM_MAJOR
        musicTypeRandomBoth.isSelected = config.algorithmType == MusicAlgorithmType.RANDOM_BOTH
        musicTypeMidi.isSelected = config.algorithmType == MusicAlgorithmType.SEQUENTIAL

        updateEnabled()
    }

    private fun updateEnabled() {
        val isEnabled = enabled.isSelected
        onlyInEditor.isEnabled = isEnabled
        musicTypeRandomMinor.isEnabled = isEnabled
        musicTypeRandomMajor.isEnabled = isEnabled
        musicTypeRandomBoth.isEnabled = isEnabled
        musicTypeMidi.isEnabled = isEnabled
        midiDirChoose.isEnabled = isEnabled && musicTypeMidi.isSelected
    }

    fun saveToConfig(config: MusicConfig) {
        config.enabled = enabled.isSelected
        config.onlyInEditor = onlyInEditor.isSelected
        config.algorithmType = currentAlgorithmType()

        config.midiDir = midiDirChoose.text
    }

    private fun currentAlgorithmType(): MusicAlgorithmType {
        return when {
            musicTypeRandomMinor.isSelected -> MusicAlgorithmType.RANDOM_MINOR
            musicTypeRandomMajor.isSelected -> MusicAlgorithmType.RANDOM_MAJOR
            musicTypeRandomBoth.isSelected -> MusicAlgorithmType.RANDOM_BOTH
            musicTypeMidi.isSelected -> MusicAlgorithmType.SEQUENTIAL
            else -> throw RuntimeException("Unreachable")
        }
    }
}
