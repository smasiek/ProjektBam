package com.momotmilosz.projektbam.ui.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.momotmilosz.projektbam.data.database.Note
import com.momotmilosz.projektbam.data.repository.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(private val noteRepository: NoteRepository) : ViewModel() {
    private val uiScope = CoroutineScope(
        Dispatchers.IO
    )

    fun getUserNotes(userId: Int): LiveData<MutableList<Note>> {
        return noteRepository.getUserNotes(userId).asLiveData()
    }

    fun insert(username: String, note: Note) = uiScope.launch {
        noteRepository.insertNote(username,note)
    }

    fun delete(uid: Int) = uiScope.launch {
        noteRepository.deleteNote(uid)
    }
}