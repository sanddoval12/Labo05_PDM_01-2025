package com.pdmtaller2.DASQCINCO.repositorio

import com.pdmtaller2.DASQCINCO.data.Book
import kotlinx.coroutines.delay

class BookRepositorio {

    suspend fun getBooks(): List<Book> {
        delay(3000)
        return List(10) {
            Book("Luna de pluton", "Dross")
        }
    }
}