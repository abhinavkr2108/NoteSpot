package com.example.notespot.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.notespot.model.Note

@Dao
interface DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun InsertNote(note: Note)

    @Update
    suspend fun UpdateNote(note: Note)

    @Query("SELECT * FROM Note ORDER BY id DESC")
    fun GetAllNotes(): LiveData<List<Note>>

    @Query("SELECT * FROM Note WHERE title LIKE :query OR content LIKE :query OR date LIKE :query")
    fun SearchNote(query: String): LiveData<List<Note>>

    @Delete
    suspend fun DeleteNote(note: Note)
}