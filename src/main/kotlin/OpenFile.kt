import kotlinx.serialization.json.Json
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter


fun loadJson(): ItemWrapper?
{
    val chooser = JFileChooser()

    val desktopPath = File(System.getProperty("user.home"), "Desktop")
    chooser.currentDirectory = desktopPath

    chooser.fileFilter = FileNameExtensionFilter("JSON Files", "json")

    val result = chooser.showOpenDialog(null)

    if(result == JFileChooser.APPROVE_OPTION)
    {
        val file = chooser.selectedFile
        val text = file.readText()

        return Json.decodeFromString<ItemWrapper>(text)
    }

    return null
}

