package com.momotmilosz.projektbam.data.repository

import com.momotmilosz.projektbam.SecretApplication
import com.momotmilosz.projektbam.data.database.Note
import com.momotmilosz.projektbam.data.datasource.NoteDataSource
import com.momotmilosz.projektbam.data.security.SecretManager
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val dataSource: NoteDataSource) {
    fun getUserNotesObservable(userId: Int): Flow<MutableList<Note>> {
        return dataSource.getUserNotesObservable(userId)
    }

    fun getUserNotes(userId: Int): List<Note>  {
        return dataSource.getUserNotes(userId)
    }

    fun deleteNote(uid: Int) {
        dataSource.deleteNote(uid)
    }

    fun insertNote(username: String, note: Note) {
        val secretManager = SecretManager()
        val context = SecretApplication.appContext as SecretApplication
        val encryptedNoteString = secretManager.encryptString(username, note.message, context)
        val encryptedNote = Note(note.uid, note.userId, encryptedNoteString)
        dataSource.insert(encryptedNote)
    }
}