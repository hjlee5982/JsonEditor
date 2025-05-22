import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter


val PrettyJson = Json{ prettyPrint = true; prettyPrintIndent = "\t"}

fun saveToJsonFile(columnNames : SnapshotStateList<String>, dataRows: SnapshotStateList<MutableList<MutableState<String>>>, filePath: String?)
{
    val items = dataRows.map()
    {
        row -> val stringRow = row.map {it.value};
        columnNames.zip(stringRow).toMap()
    }

    val json = PrettyJson.encodeToString(ItemWrapper(items))

    if (filePath != null)
    {
        File(filePath).writeText(json)
    }
}

fun showSaveFileDialog(): String?
{
    val fileChooser = JFileChooser()
    fileChooser.dialogTitle = "Save Json File"
    fileChooser.fileSelectionMode = JFileChooser.FILES_ONLY

    val filter = FileNameExtensionFilter("JSON files", "json")
    fileChooser.fileFilter = filter

    val desktopPath = File(System.getProperty("user.home"), "Desktop")
    fileChooser.currentDirectory = desktopPath

    val userSelection = fileChooser.showSaveDialog(null)

    if(userSelection == JFileChooser.APPROVE_OPTION)
    {
        var selectedFile = fileChooser.selectedFile;

        if(!selectedFile.name.endsWith(".json", ignoreCase = true))
        {
            selectedFile = File(selectedFile.parentFile, selectedFile.name + ".json")
        }

        return selectedFile.absolutePath
    }

    return null
}