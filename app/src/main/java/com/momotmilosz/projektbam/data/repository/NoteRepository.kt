package com.momotmilosz.projektbam.data.repository

import com.momotmilosz.projektbam.data.database.Note
import com.momotmilosz.projektbam.data.datasource.NoteDataSource
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val dataSource: NoteDataSource) {
    fun getUserNotes(userId: Int): Flow<MutableList<Note>> {
        return dataSource.getUserNotes(userId)
    }

    fun deleteNote(note: Note) {
        dataSource.deleteNote(note)
    }

    fun insertNote(note: Note) {
        dataSource.insert(note)
    }
}