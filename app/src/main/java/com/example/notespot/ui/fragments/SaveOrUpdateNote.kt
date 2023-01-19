package com.example.notespot.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.notesapp.R
import com.example.notesapp.databinding.BottomSheetLayoutBinding
import com.example.notesapp.databinding.FragmentSaveOrUpdateNoteBinding
import com.example.notespot.model.Note
import com.example.notespot.ui.activities.MainActivity
import com.example.notespot.viewmodel.NoteViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.transition.MaterialContainerTransform
import com.yahiaangelo.markdownedittext.MarkdownEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.text.DateFormat
import java.util.*
import java.text.SimpleDateFormat


class SaveOrUpdateNote : Fragment(R.layout.fragment_save_or_update_note) {

    private lateinit var contentBinding: FragmentSaveOrUpdateNoteBinding
    private lateinit var navController: NavController
    private lateinit var noteViewModel: NoteViewModel
    private var note: Note? = null
    private var color = -1
    private val currentDate = SimpleDateFormat.getDateInstance().format(Date())
    //private val date = DateFormat.getDateInstance().format(calendar)


    private val args : SaveOrUpdateNoteArgs by navArgs()

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        val animation = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragment
            scrimColor = Color.TRANSPARENT
            duration = 300
        }
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        noteViewModel = (activity as MainActivity).noteViewModel

        contentBinding = FragmentSaveOrUpdateNoteBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            note = SaveOrUpdateNoteArgs.fromBundle(it).note

        }

        SetUpUI()
        navController = Navigation.findNavController(view)

        contentBinding.etNoteContent.setStylesBar(contentBinding.stylesBar)
        contentBinding.stylesBar.stylesList = arrayOf(MarkdownEditText.TextStyle.BOLD,
                                                MarkdownEditText.TextStyle.ITALIC,
                                                MarkdownEditText.TextStyle.STRIKE,
                                                MarkdownEditText.TextStyle.LINK,
                                                MarkdownEditText.TextStyle.UNORDERED_LIST,
                                                MarkdownEditText.TextStyle.ORDERED_LIST,
                                                MarkdownEditText.TextStyle.TASKS_LIST)

        contentBinding.etNoteTitle.setOnClickListener {
            contentBinding.stylesBar.isVisible = false
        }

        contentBinding.etNoteContent.setOnClickListener(View.OnClickListener {
            contentBinding.stylesBar.isVisible = true

        })


        contentBinding.saveButton.setOnClickListener {
            SaveNote()
        }

        contentBinding.fabColorPicker.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(requireContext(),R.style.BottomSheetDialogTheme)
            val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
            with(bottomSheetDialog){
                setContentView(bottomSheetView)
                show()
            }
            val bottomSheetBinding= BottomSheetLayoutBinding.bind(bottomSheetView)

            bottomSheetBinding.apply {
                colorPicker.apply {
                    setSelectedColor(color)
                    setOnColorSelectedListener {
                        color = it
                        contentBinding.apply {
                            saveUpdateTopBar.setBackgroundColor(color)
                            SaveOrUpdateFragment.setBackgroundColor(color)
                            bottomBar.setBackgroundColor(color)
                            saveButton.setBackgroundColor(color)
                            backButton.setBackgroundColor(color)
                        }
                        bottomSheetLayout.setCardBackgroundColor(color)
                    }
                }
                bottomSheetLayout.setBackgroundColor(color)
            }

            bottomSheetView.post {
                bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        contentBinding.backButton.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun SetUpUI() {
        if (note==null){
            contentBinding.viewDate.text = "Edited On: ${SimpleDateFormat.getDateInstance().format(Date())}"
        }
        if (note!=null){
            contentBinding.apply {
                contentBinding.viewDate.text = "Edited On: ${SimpleDateFormat.getDateInstance().format(Date())}"
                etNoteTitle.setText(note?.title)
                etNoteContent.setText(note?.content)
                viewDate.setText(note?.date)
                color = note!!.color
                SaveOrUpdateFragment.setBackgroundColor(color)
                bottomBar.setBackgroundColor(color)
            }
        }
    }

    private fun SaveNote(){
        if (contentBinding.etNoteTitle.text.toString().isEmpty() or contentBinding.etNoteContent.text.toString().isEmpty()){
            Toast.makeText(activity, "Please Enter All the Fields", Toast.LENGTH_SHORT).show()
        }
        else{
            note = args.note
            when(note){
                null -> {
                    noteViewModel.InsertNote(
                        Note(
                            0,
                            contentBinding.etNoteTitle.text.toString(),
                            contentBinding.etNoteContent.getMD(),
                            currentDate,
                            color
                        )
                    )
                    navController.navigate(SaveOrUpdateNoteDirections.actionSaveOrUpdateNoteToAddNote())
                }
                else->{
                    UpdateNote()
                    navController.navigate(SaveOrUpdateNoteDirections.actionSaveOrUpdateNoteToAddNote())

                }
            }
        }
    }

    private fun UpdateNote() {
            noteViewModel.UpdateNote(Note(
                note!!.id,
                contentBinding.etNoteTitle.text.toString(),
                contentBinding.etNoteContent.getMD(),
                currentDate,
                color
            ))

    }

}