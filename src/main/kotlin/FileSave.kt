import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

fun saveJsonFile(content: String)
{
    // 열려있는 Json에 덮어씌움
    currentOpenedFile?.writeText(content)
}

fun saveAsJsonFile(content: String)
{
    // 파일 선택 UI
    val chooser = JFileChooser()

    // 초기 경로 설정(바탕화면)
    val desktopPath = File(System.getProperty("user.home"), "Desktop")
    chooser.currentDirectory = desktopPath

    // Json만 선택 가능하도록 설정
    chooser.fileFilter = FileNameExtensionFilter("JSON Files", "json")

    val result = chooser.showSaveDialog(null)

    if(result == JFileChooser.APPROVE_OPTION)
    {
        var file = chooser.selectedFile

        if(!file.name.endsWith(".json"))
        {
            file = File(file.parentFile, "${file.name}.json")
        }

        file.writeText(content)
        currentOpenedFile = file
    }
}
