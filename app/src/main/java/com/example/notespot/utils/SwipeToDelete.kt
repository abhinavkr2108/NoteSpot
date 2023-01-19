/*package com.example.notespot.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.notespot.adapter.NotesAdapter

class SwipeToDelete:ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val note = NotesAdapter.differ.currentList[position]
        viewModel.deleteArticle(article)
        Snackbar.make(view, "Article Deleted Successfully", Snackbar.LENGTH_LONG).apply {
            setAction("Undo"){
                viewModel.insertArticle(article)
            }
            show()
    }
}*/