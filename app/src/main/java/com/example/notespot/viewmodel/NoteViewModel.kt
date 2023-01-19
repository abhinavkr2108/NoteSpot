package com.example.notespot.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notespot.database.NoteDatabase
import com.example.notespot.model.Note
import com.example.notespot.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(val repository: NoteRepository):ViewModel() {

    fun GetAllNotes(): LiveData<List<Note>>{
        return repository.GetAllNotes()
    }
    fun SearchNotes(query: String): LiveData<List<Note>> = repository.SearchNotes(query)

    fun InsertNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.InsertNote(note)
    }
    fun UpdateNote(note: Note) = viewModelScope.launch(Dispatchers.IO) {
        repository.UpdateNote(note)
    }
    fun DeleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO){
        repository.DeleteNote(note)
    }
}