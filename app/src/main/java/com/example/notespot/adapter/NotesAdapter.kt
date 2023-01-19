package com.example.notespot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.databinding.NoteItemLayoutBinding
import com.example.notespot.model.Note
import com.example.notespot.ui.fragments.AddNoteDirections
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.LinkResolverDef
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonConfiguration
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class NotesAdapter:ListAdapter<Note, NotesAdapter.NotesViewHolder>(DiffUtilCallback()) {

   inner class NotesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val rvBinding= NoteItemLayoutBinding.bind(itemView)
        val title: MaterialTextView = rvBinding.noteTitle
        val content: TextView = rvBinding.noteItem
        val date: MaterialTextView = rvBinding.noteDate
        val parent: MaterialCardView = rvBinding.noteItemCardView
        val markWon = Markwon.builder(itemView.context)
            .usePlugin(object : AbstractMarkwonPlugin() {
                override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
                    builder.linkResolver(LinkResolverDef())
                }
            })
            .build()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_item_layout,parent,false)
        )
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        getItem(position).let { note->
            holder.apply {
                title.text = note.title
                markWon.setMarkdown(content,note.content)
                parent.setCardBackgroundColor(note.color)
                //date.text = SimpleDateFormat.getDateInstance().format(Date())
                date.text = note.date
                itemView.setOnClickListener {
                    val direction = AddNoteDirections.actionAddNoteToSaveOrUpdateNote()
                    direction.note = getItem(position)
                    it.findNavController().navigate(direction)
                }
                content.setOnClickListener {
                    val direction = AddNoteDirections.actionAddNoteToSaveOrUpdateNote()
                    direction.note = getItem(position)
                    it.findNavController().navigate(direction)
                }
                parent.setOnClickListener {
                    val direction = AddNoteDirections.actionAddNoteToSaveOrUpdateNote()
                    direction.note = getItem(position)
                    it.findNavController().navigate(direction)
                }
            }
        }
    }
}