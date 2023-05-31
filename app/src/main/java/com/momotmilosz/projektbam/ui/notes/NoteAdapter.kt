package com.momotmilosz.projektbam.ui.notes

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.momotmilosz.projektbam.data.database.Note


class NoteAdapter(private val activity: NotesActivity, private val notes:  MutableList<Note>) : BaseAdapter(){


    override fun getCount(): Int {
        return notes.size
    }

    override fun getItem(p0: Int): Any {
        return notes[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View, p2: ViewGroup?): View {
        val noteMessageView: TextView = p1.findViewById(com.momotmilosz.projektbam.R.id.noteId)
        noteMessageView.text = notes[p0].message
        return noteMessageView
    }
}
