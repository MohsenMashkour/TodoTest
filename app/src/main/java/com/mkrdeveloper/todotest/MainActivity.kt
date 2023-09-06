package com.mkrdeveloper.todotest

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mkrdeveloper.todotest.ui.theme.TodoTestTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(modifier: Modifier = Modifier) {

    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    val sheetState = rememberModalBottomSheetState()
    val todoList = remember {
        mutableStateListOf("MkrDeveloper")
    }
    var todoText by remember {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()

    Scaffold(modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = { isSheetOpen = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }

        }) {
        Column {

            if (isSheetOpen) {
                ModalBottomSheet(
                    sheetState = sheetState, onDismissRequest = {
                        isSheetOpen = false

                        todoList.add(
                            todoText
                        )
                        todoText = ""
                    }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(text = "write your todo")
                        Spacer(modifier.height(20.dp))


                        OutlinedTextField(value = todoText, onValueChange = { todoText = it })

                        Button(onClick = {
                            isSheetOpen = false
                            scope.launch {
                                sheetState.hide()
                                todoList.add(
                                    todoText
                                )
                                todoText = ""
                            }

                        }, Modifier.padding(20.dp)) {
                            Text(text = "Submit")
                        }
                    }

                }
            }

            LazyColumn(
                state = rememberLazyListState(),
                modifier = Modifier.fillMaxSize()
            ) {

                items(todoList.size) { item ->

                    val state = rememberDismissState(
                        confirmValueChange = {
                            if (it == DismissValue.DismissedToStart) {
                                todoList.removeAt(item)
                            }
                            true
                        }
                    )

                    SwipeToDismiss(state = state, background = {
                                             val color = when(state.dismissDirection){
                                                 DismissDirection.StartToEnd-> Color.Green
                                                 DismissDirection.EndToStart-> Color.Red
                                                 null-> Color.Transparent
                                             }
                        Box(
                            modifier
                                .fillMaxSize()
                                .background(color)
                                .padding(horizontal = 20.dp, vertical = 6.dp)
                        ){

                            Icon(imageVector = Icons.Default.Delete, contentDescription ="delete", modifier.align(
                                Alignment.CenterEnd
                            ))
                            Icon(imageVector = Icons.Default.Edit, contentDescription ="delete", modifier.align(
                                Alignment.CenterStart
                            ))
                        }
                    },
                        dismissContent ={
                            Card(
                                modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                            ) {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    var isChecked by remember {
                                        mutableStateOf(false)
                                    }

                                    Checkbox(checked = isChecked, onCheckedChange = {
                                        isChecked = !isChecked
                                    })
                                    Spacer(modifier.width(16.dp))
                                    Text(text = todoList[item])

                                }
                            }
                        },
                        directions = setOf(
                            DismissDirection.EndToStart,
                            DismissDirection.StartToEnd
                        )
                    )

                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TodoTestTheme {
        MyApp()
    }
}