package com.falconteam.laboratorio3.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.falconteam.laboratorio3.models.Task
import com.falconteam.laboratorio3.ui.components.Cards
import com.tuapp.viewmodel.GeneralViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(viewModel: GeneralViewModel, navController: NavController) {
    val openDialog = remember { mutableStateOf(false) }
    val newCard = remember { mutableStateOf(Task(0, "", "", Date(), false)) }
    val openDateDialog = remember { mutableStateOf(false) }
    val tasks = viewModel.tasks.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Lista de Tareas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { openDialog.value = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.Black)
            }
        }
    ) { paddingValues ->
        if (openDialog.value) {
            Dialog(
                onDismissRequest = { openDialog.value = false },
                properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
            ) {
                Card {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Agregar tarea", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(16.dp))
                        TextField(
                            value = newCard.value.title,
                            onValueChange = { newCard.value = newCard.value.copy(title = it) },
                            label = { Text("Título") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = newCard.value.description,
                            onValueChange = { newCard.value = newCard.value.copy(description = it) },
                            label = { Text("Descripción") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = newCard.value.endDate.toString(),
                            readOnly = true,
                            onValueChange = {},
                            label = { Text("Fecha") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { openDateDialog.value = true }) {
                            Text("Seleccionar Fecha")
                        }

                        if (openDateDialog.value) {
                            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = Date().time)
                            DatePickerDialog(
                                onDismissRequest = { openDateDialog.value = false },
                                confirmButton = {
                                    Button(onClick = {
                                        openDateDialog.value = false
                                        newCard.value = newCard.value.copy(
                                            endDate = datePickerState.selectedDateMillis?.let { Date(it) } ?: Date()
                                        )
                                    }) {
                                        Text("OK")
                                    }
                                }
                            ) {
                                DatePicker(state = datePickerState)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            val task = com.falconteam.laboratorio3.models.Task(
                                id = viewModel.tasks.value.size,
                                title = newCard.value.title,
                                description = newCard.value.description,
                                endDate = newCard.value.endDate,
                                isCompleted = newCard.value.isCompleted
                            )
                            viewModel.addTask(task)
                            newCard.value = Task(0, "", "", Date(), false)
                            openDialog.value = false
                        }) {
                            Text("Agregar")
                        }
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = paddingValues.calculateTopPadding() + 16.dp)
        ) {
            items(tasks.value) { task ->
                Log.d("Task", task.toString())
                Cards(
                    pos = task.id,
                    max = tasks.value.size - 1,
                    title = task.title,
                    description = task.description,
                    endDate = task.endDate,
                    checked = task.isCompleted,

                    delete = { id -> viewModel.removeTask(id) },

                    check = { isChecked, id ->
                        viewModel.toggleTaskCompletion(id)
                    },

                    changePosition = { from, to ->
                        viewModel.moveTask(from, to)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
