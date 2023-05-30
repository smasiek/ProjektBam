package com.momotmilosz.projektbam.data.datasource

import androidx.annotation.WorkerThread
import com.momotmilosz.projektbam.data.database.Note
import com.momotmilosz.projektbam.data.database.NoteDao
import kotlinx.coroutines.flow.Flow

class NoteDataSource(private val noteDao: NoteDao) {
    fun getUserNotes(userId: Int): Flow<MutableList<Note>> {
        return noteDao.getAllByUserId(userId)
    }

    @WorkerThread
    fun deleteNote(note: Note) {
        noteDao.delete(note)
    }

    @WorkerThread
    fun insert(note: Note) {
        noteDao.insert(note)
    }
}