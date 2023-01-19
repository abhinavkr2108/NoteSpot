package com.example.notespot.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notespot.database.NoteDatabase
import com.example.notespot.repository.NoteRepository
import com.example.notespot.viewmodel.NoteViewModel
import com.example.notespot.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {
    //val noteViewModel: NoteViewModel by viewModels()
    lateinit var repository: NoteRepository
    lateinit var noteViewModel: NoteViewModel
    lateinit var noteDatabase: NoteDatabase
    lateinit var noteViewModelFactory: NoteViewModelFactory
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        noteDatabase = NoteDatabase.getDatabase(this)
        repository = NoteRepository(noteDatabase)
        noteViewModelFactory = NoteViewModelFactory(repository)
        noteViewModel = ViewModelProvider(this,noteViewModelFactory).get(NoteViewModel::class.java)

    }
}