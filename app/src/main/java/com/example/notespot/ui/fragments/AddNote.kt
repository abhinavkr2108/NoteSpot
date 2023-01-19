package com.example.notespot.ui.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentAddNoteBinding
import com.example.notespot.adapter.NotesAdapter
import com.example.notespot.database.NoteDatabase
import com.example.notespot.repository.NoteRepository
import com.example.notespot.ui.activities.MainActivity
import com.example.notespot.viewmodel.NoteViewModel
import com.example.notespot.viewmodel.NoteViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialElevationScale


class AddNote : Fragment(R.layout.fragment_add_note) {


   private lateinit var noteBinding: FragmentAddNoteBinding
   private lateinit var noteViewModel: NoteViewModel

   private val  notesAdapter: NotesAdapter = NotesAdapter()


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        exitTransition = MaterialElevationScale(false).apply {
            duration = 350
        }
        enterTransition = MaterialElevationScale(true).apply {
            duration = 350
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        noteViewModel = (activity as MainActivity).noteViewModel
        super.onViewCreated(view, savedInstanceState)
        noteBinding = FragmentAddNoteBinding.bind(view)

        SetUpRecyclerView()

        noteBinding.searchNotes.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                noteBinding.imgNoData.isVisible = false
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()){
                    val text = s.toString()
                    val query = "%$text%"
                    if (query.isNotEmpty()){
                        SearchQuery(query)
                    }
                    else{
                        ObserveChanges()
                    }
                }
                else{
                    ObserveChanges()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //Empty Function
            }
        })

        val navController = Navigation.findNavController(view)

        noteBinding.fabAddNote.setOnClickListener {
            navController.navigate(AddNoteDirections.actionAddNoteToSaveOrUpdateNote())
        }
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val note = notesAdapter.currentList[position]
                noteViewModel.DeleteNote(note)
                Snackbar.make(view, "Note Deleted Successfully", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        noteViewModel.InsertNote(note)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(noteBinding.rvNotes)
        }
    }

    private fun SetUpRecyclerView() {
        noteBinding.rvNotes.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = notesAdapter
        }
        ObserveChanges()
    }

    private fun SearchQuery(query: String?){
        val searchQuery = "%$query%"
        noteViewModel.SearchNotes(searchQuery).observe(viewLifecycleOwner, Observer {
            notesAdapter.submitList(it)
        })
    }
    private fun ObserveChanges() {
        noteViewModel.GetAllNotes().observe(viewLifecycleOwner, Observer {
            noteBinding.imgNoData.isVisible = it.isEmpty()
            notesAdapter.submitList(it)
        })
    }

}