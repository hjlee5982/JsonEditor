import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

fun main()
{
    application()
    {
        Window(onCloseRequest = ::exitApplication, title = "Json Editor", state = WindowState(size = DpSize(1280.dp, 720.dp)))
        {
            editorPanel()
        }
    }
}