package com.pdmtaller2.DASQCINCO.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pdmtaller2.DASQCINCO.viewModel.BookViewModel
import com.pdmtaller2.DASQCINCO.ui.theme.components.BookCard

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookScreen(bookViewModel: BookViewModel = viewModel()) {
    val books = bookViewModel.books
    val isLoading = bookViewModel.isLoading
    val showSnackbar = bookViewModel.showSnackbar


    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            snackbarHostState.showSnackbar("Datos cargados correctamente")
            bookViewModel.showSnackbar = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { bookViewModel.fetchBooks() },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Get Data")
            }

            Spacer(modifier = Modifier.height(20.dp))

            when {
                isLoading -> {
                    CircularProgressIndicator()
                }

                books.isEmpty() -> {
                    Text("Sin datos que mostrar")
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(books) { book ->
                            BookCard(book)
                        }
                    }

                }
            }
        }
    }
}
