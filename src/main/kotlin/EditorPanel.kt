import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.io.File

@Composable
@Preview
fun editorPanel()
{
    var jsonContent by remember { mutableStateOf("") }

    MaterialTheme()
    {
        Column(modifier = Modifier.padding(16.dp).fillMaxSize())
        {
            Row(modifier =  Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween)
            {
                Button(onClick = { jsonContent = openJsonFile()})
                {
                    Text("Open")
                }
                Button(onClick = {saveJsonFile(jsonContent)})
                {
                    Text("Save")
                }
                Button(onClick = {saveAsJsonFile(jsonContent)})
                {
                    Text("Save As")
                }
            }

            Spacer(Modifier.height(8.dp))

            Text("JSON CONTENT")
            TextField(
                value         = jsonContent,
                onValueChange = {jsonContent = it},
                modifier      = Modifier.fillMaxSize()
            )
        }
    }
}