package com.momotmilosz.projektbam.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.momotmilosz.projektbam.SecretApplication
import com.momotmilosz.projektbam.data.datasource.NoteDataSource
import com.momotmilosz.projektbam.data.repository.NoteRepository

class NoteViewModelFactory(): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(
                noteRepository = NoteRepository(
                    dataSource = NoteDataSource((SecretApplication.appContext as SecretApplication).database.noteDao())
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}