package com.example.notespot.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notespot.model.Note

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase:RoomDatabase() {

    abstract fun getNoteDao(): DAO

    companion object{
        @Volatile
        private var INSTANCE:NoteDatabase? =null

        fun getDatabase(context: Context):NoteDatabase{
            if (INSTANCE==null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, NoteDatabase::class.java, "noteDB").build()
                }
            }
            return INSTANCE!!
        }

    }
}