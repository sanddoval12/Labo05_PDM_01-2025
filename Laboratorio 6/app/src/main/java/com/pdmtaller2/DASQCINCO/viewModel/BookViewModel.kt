package com.pdmtaller2.DASQCINCO.viewModel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pdmtaller2.DASQCINCO.repositorio.BookRepositorio
import com.pdmtaller2.DASQCINCO.data.Book
import kotlinx.coroutines.launch

class BookViewModel : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var books by mutableStateOf<List<Book>>(emptyList())
        private set

    var showSnackbar by mutableStateOf(false)

    private val repository = BookRepositorio()

    fun fetchBooks() {
        viewModelScope.launch {
            isLoading = true
            books = emptyList()
            showSnackbar = false
            books = repository.getBooks()
            isLoading = false
            showSnackbar = true
        }
    }
}