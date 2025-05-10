package com.tuapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.falconteam.laboratorio3.models.Task
import kotlinx.coroutines.flow.update

class GeneralViewModel : ViewModel() {
    private val _tasks = MutableStateFlow<MutableList<Task>>(mutableListOf())
    val tasks = _tasks.asStateFlow()

    fun addTask(task: Task) {
        _tasks.value = _tasks.value.toMutableList().apply { add(task) }
    }

    fun removeTask(id: Int) {
        _tasks.update { list -> list.filterNot { it.id == id }.toMutableList() }
    }

    fun toggleTaskCompletion(id: Int) {
        _tasks.update { list ->
            list.map {
                if (it.id == id) it.copy(isCompleted = !it.isCompleted) else it
            }.toMutableList()
        }
    }

    fun moveTask(from: Int, to: Int) {
        _tasks.update { currentList ->
            if (to < 0 || to >= currentList.size) return@update currentList
            val mutable = currentList.toMutableList()
            val task = mutable.removeAt(from)
            mutable.add(to, task)
            mutable.mapIndexed { index, t -> t.copy(id = index) }.toMutableList()
        }
    }

}
