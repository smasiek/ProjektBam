package com.momotmilosz.projektbam.data.datasource

import androidx.annotation.WorkerThread
import com.momotmilosz.projektbam.data.database.Note
import com.momotmilosz.projektbam.data.database.NoteDao
import kotlinx.coroutines.flow.Flow

class NoteDataSource(private val noteDao: NoteDao) {
    fun getUserNotesObservable(userId: Int): Flow<MutableList<Note>> {
        return noteDao.getAllNotesObservableByUserId(userId)
    }

    fun getUserNotes(userId: Int): List<Note> {
        return noteDao.getAllNotesByUserId(userId)
    }
    @WorkerThread
    fun deleteNote(uid: Int) {
        noteDao.delete(uid)
    }

    @WorkerThread
    fun insert(note: Note) {
        noteDao.insert(note)
    }
}