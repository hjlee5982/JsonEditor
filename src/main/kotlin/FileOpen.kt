import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

fun openJsonFile(): String
{
    // 파일 선택 UI
    val chooser = JFileChooser()

    // 초기 경로 설정(바탕화면)
    val desktopPath = File(System.getProperty("user.home"), "Desktop")
    chooser.currentDirectory = desktopPath

    // Json만 선택 가능하도록 설정
    chooser.fileFilter = FileNameExtensionFilter("JSON Files", "json")

    val result = chooser.showOpenDialog(null)

    if(result == JFileChooser.APPROVE_OPTION)
    {
        // 가져온 파일 저장
        currentOpenedFile = chooser.selectedFile
    }

    return currentOpenedFile?.readText() ?: ""
}