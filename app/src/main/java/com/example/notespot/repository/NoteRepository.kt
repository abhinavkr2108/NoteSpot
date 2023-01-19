package com.example.notespot.repository

import com.example.notespot.database.NoteDatabase
import com.example.notespot.model.Note
import com.example.notespot.viewmodel.NoteViewModel

class NoteRepository(val db: NoteDatabase) {

    fun GetAllNotes() = db.getNoteDao().GetAllNotes()
    fun SearchNotes(query: String) = db.getNoteDao().SearchNote(query)

    suspend fun InsertNote(note: Note) = db.getNoteDao().InsertNote(note)
    suspend fun UpdateNote(note: Note) = db.getNoteDao().UpdateNote(note)
    suspend fun DeleteNote(note: Note) = db.getNoteDao().DeleteNote(note)
}