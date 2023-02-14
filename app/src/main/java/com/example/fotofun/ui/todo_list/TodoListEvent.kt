package com.example.fotofun.ui.todo_list

import com.example.fotofun.data.entities.Student

sealed class TodoListEvent {
    data class OnDeleteTodoClick(val student: Student): TodoListEvent()
    data class OnDoneChange(val student: Student, val isDone: Boolean): TodoListEvent()
    object OnUndoDeleteClick: TodoListEvent()
    data class OnTodoClick(val student: Student): TodoListEvent()
    object OnAddTodoClick: TodoListEvent()
}
