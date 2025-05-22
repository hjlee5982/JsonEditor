import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun editorPanel()
{
    val columnNames = remember { mutableStateListOf<String>() }
    val dataRows    = remember { mutableStateListOf<MutableList<MutableState<String>>>() }

    var showAddItemDialog by remember { mutableStateOf(false) }
    var newItemName       by remember { mutableStateOf("") }

    var currentFilePath by remember { mutableStateOf<String?>(null)}

    val focusRequester    = remember { FocusRequester() }
    val localFocusManager = LocalFocusManager.current

    Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()))
    {
        // 상단 버튼
        Row(modifier = Modifier.padding(bottom = 16.dp))
        {
            Button(onClick = { showAddItemDialog = true })
            {
                Text("AddItem")
            }
            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { val newRow = MutableList(columnNames.size) { mutableStateOf("") }; dataRows.add(newRow)}, enabled = columnNames.isNotEmpty())
            {
                Text("AddField")
            }
            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { openButtonClickEvent(columnNames, dataRows) })
            {
                Text("Open")
            }
            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { currentFilePath = saveButtonClickEvent(columnNames, dataRows, currentFilePath) })
            {
                Text("Save")
            }
            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { currentFilePath = saveAsButtonClickEvent(columnNames, dataRows, currentFilePath) })
            {
                Text("SaveAs")
            }
        }

        // 테이블 출력
        if (columnNames.isNotEmpty())
        {
            // 컬럼 헤더 (첫 줄)
            Row()
            {
                Spacer(modifier = Modifier.width(72.dp))

                for ((index, col) in columnNames.withIndex())
                {
                    Column(modifier = Modifier.weight(1f).padding(4.dp))
                    {
                        Row(verticalAlignment = Alignment.CenterVertically)
                        {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Column", modifier = Modifier.size(16.dp).clickable{ columnNames.removeAt(index); dataRows.forEach { it.removeAt(index) }})

                            if (index > 0)
                            {
                                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Move Left", modifier = Modifier.size(16.dp).clickable{ columnNames.swap(index, index - 1); dataRows.forEach { it.swap(index, index - 1) }})
                            }
                            if (index < columnNames.lastIndex)
                            {
                                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Move Right", modifier = Modifier.size(16.dp).clickable{ columnNames.swap(index, index + 1); dataRows.forEach { it.swap(index, index + 1) }})
                            }
                        }

                        Text(col, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        }

        // 데이터 행
        for ((rowIndex, row) in dataRows.withIndex())
        {
            Row()
            {
                Row(modifier = Modifier.width(72.dp).padding(12.dp))
                {
                    Icon(Icons.Default.Delete, contentDescription = "Delete row", modifier = Modifier.size(16.dp).clickable{dataRows.removeAt(rowIndex)})

                    if(rowIndex > 0)
                    {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Move up", modifier = Modifier.size(16.dp).clickable{ dataRows.swap(rowIndex, rowIndex - 1) })
                    }
                    if(rowIndex < dataRows.lastIndex)
                    {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "DMove down", modifier = Modifier.size(16.dp).clickable{ dataRows.swap(rowIndex, rowIndex + 1) })
                    }
                }

                Row()
                {
                    for ((colIndex, cell) in row.withIndex())
                    {
                        val cellState = dataRows[rowIndex][colIndex]

                        TextField(
                            value = cellState.value,
                            onValueChange = { newValue -> cellState.value = newValue },
                            modifier = Modifier.weight(1f).padding(4.dp),
                            singleLine = true
                        )
                    }
                }
            }
        }
    }

    // AddItem Dialog
    if (showAddItemDialog)
    {
        Dialog(onCloseRequest = { showAddItemDialog = false })
        {
            Column(modifier = Modifier.padding(16.dp))
            {
                Text("새 컬럼 이름 입력")
                Spacer(modifier = Modifier.height(8.dp))

                LaunchedEffect(showAddItemDialog)
                {
                    if (showAddItemDialog)
                    {
                        focusRequester.requestFocus()
                    }
                }

                TextField(
                    value = newItemName,
                    onValueChange = { newItemName = it },
                    label = { Text("Item name") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (newItemName.isNotBlank()) {
                                columnNames.add(newItemName)

                                for (row in dataRows)
                                {
                                    row.add(mutableStateOf(""))
                                }

                                newItemName = ""
                                showAddItemDialog = false
                                localFocusManager.clearFocus() // 포커스 해제(optional)
                            }
                        }
                    ),
                    modifier = Modifier.focusRequester(focusRequester)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    if (newItemName.isNotBlank())
                    {
                        columnNames.add(newItemName)

                        for (row in dataRows)
                        {
                            row.add(mutableStateOf(""))
                        }

                        newItemName = ""
                        showAddItemDialog = false
                    }
                })
                {
                    Text("추가")
                }
            }
        }
    }
}

fun <T> MutableList<T>.swap(i: Int, j: Int)
{
    val temp = this[i]
    this[i] = this[j]
    this[j] = temp
}

fun openButtonClickEvent(columnNames : SnapshotStateList<String>, dataRows: SnapshotStateList<MutableList<MutableState<String>>>)
{
    val result = loadJson()

    result?.let()
    {
        columnNames.clear()
        dataRows.clear()

        val firstItem = it.Items.firstOrNull()

        if(firstItem != null)
        {
            columnNames.addAll(firstItem.keys)
        }

        for(item in it.Items)
        {
            val row = columnNames.map { key -> mutableStateOf(item[key] ?: "")}.toMutableList()
            dataRows.add(row)
        }
    }
}

fun saveButtonClickEvent(columnNames : SnapshotStateList<String>, dataRows: SnapshotStateList<MutableList<MutableState<String>>>, currentFilePath: String?) : String?
{
    if(currentFilePath == null)
    {
        val selectedPath = showSaveFileDialog()

        if(selectedPath != null)
        {
            saveToJsonFile(columnNames, dataRows, selectedPath)
            return selectedPath
        }
    }
    else
    {
        saveToJsonFile(columnNames, dataRows, currentFilePath)
        return currentFilePath
    }

    return null
}

fun saveAsButtonClickEvent(columnNames : SnapshotStateList<String>, dataRows: SnapshotStateList<MutableList<MutableState<String>>>, currentFilePath: String?) : String?
{
    val selectedPath = showSaveFileDialog()

    if(selectedPath != null)
    {
        saveToJsonFile(columnNames, dataRows, selectedPath)
        return selectedPath
    }

    return null
}