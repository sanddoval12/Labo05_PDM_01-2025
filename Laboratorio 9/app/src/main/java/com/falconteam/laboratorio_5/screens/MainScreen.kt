package com.falconteam.laboratorio_5.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.falconteam.laboratorio_5.ui.components.PostComponent
import com.falconteam.laboratorio_5.ui.theme.Laboratorio5Theme
import com.falconteam.laboratorio_5.ui.theme.Typography
import com.falconteam.laboratorio_5.utils.BlogsitoViewModel
import com.falconteam.laboratorio_5.utils.ValidationEvent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: BlogsitoViewModel,
    onCloseSessionClick: () -> Unit
) {
    val listPosts = viewModel.listPosts.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is ValidationEvent.Success -> {

                }
                is ValidationEvent.Error -> {

                }

                ValidationEvent.LogOut -> {
                    onCloseSessionClick()
                }
            }
        }
    }

    if (viewModel.showModal.value) {
        AlertDialog(
            onDismissRequest = { viewModel.showModal() },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.addNewPost()
                        viewModel.showModal()
                    }
                ) {
                    Text(text = "Guardar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.showModal() }
                ) {
                    Text(text = "Cancelar")
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "NewPost"
                )
            },
            title = {
                Text(text = "Redacta una publicacion!")
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = viewModel.newPostTitle.value,
                        onValueChange = {
                            viewModel.newPostTitle.value = it
                        },
                        label = {
                            Text(text = "titulo")
                        }
                    )

                    OutlinedTextField(
                        value = viewModel.newPostDescription.value,
                        onValueChange = {
                            viewModel.newPostDescription.value = it
                        },
                        label = {
                            Text(text = "Descripcion")
                        }
                    )
                }
            }
        )
    }

    if (viewModel.showModalAddComment.value) {
        AlertDialog(
            onDismissRequest = { viewModel.showModalAddComment() },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.addNewComment()
                        viewModel.showModalAddComment()
                    }
                ) {
                    Text(text = "Guardar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.showModalAddComment() }
                ) {
                    Text(text = "Cancelar")
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "NewPost"
                )
            },
            title = {
                Text(text = "Redacta un comentario para la publicacion!")
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = viewModel.newPostComment.value,
                        onValueChange = {
                            viewModel.newPostComment.value = it
                        },
                        label = {
                            Text(text = "Comentario")
                        }
                    )
                }
            }
        )
    }

    Laboratorio5Theme {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.showModal() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
            },
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Blogsito")
                    },
                    actions = {
                        TextButton(
                            onClick = {
                                viewModel.logOut()
                            }
                        ) {
                            Text(
                                text = "Cerrar sesion",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                when {
                    listPosts.value.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Se el primero en escribir una publicación!",
                                style = Typography.displayMedium
                            )
                        }
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            items(listPosts.value) {
                                PostComponent(
                                    id = it.id,
                                    title = it.title,
                                    description = it.description,
                                    author = it.author,
                                    comments = it.comments,
                                    onAddComment = { postId ->
                                        viewModel.newPostId.value = postId
                                        viewModel.showModalAddComment()
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}