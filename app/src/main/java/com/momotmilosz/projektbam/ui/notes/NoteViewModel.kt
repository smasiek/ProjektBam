package com.momotmilosz.projektbam.ui.notes

import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.momotmilosz.projektbam.SecretApplication
import com.momotmilosz.projektbam.data.database.Note
import com.momotmilosz.projektbam.data.repository.LoginRepository
import com.momotmilosz.projektbam.data.repository.NoteRepository
import com.momotmilosz.projektbam.data.security.SecretManager
import com.momotmilosz.projektbam.utils.BackupUtils
import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import kotlinx.coroutines.*
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.util.stream.Collectors

class NoteViewModel(private val noteRepository: NoteRepository) : ViewModel() {
    private val DATABASE_FILE_NAME = "data.csv"
    private val ZIP_FILE_NAME = "data.zip"

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun getUserNotes(userId: Int): LiveData<MutableList<Note>> {
        return noteRepository.getUserNotesObservable(userId).asLiveData()
    }

    fun insert(username: String, note: Note) = uiScope.launch {
        withContext(Dispatchers.IO){
            noteRepository.insertNote(username, note)
        }
    }

    fun delete(uid: Int) = uiScope.launch {
        withContext(Dispatchers.IO){
            noteRepository.deleteNote(uid)
        }
    }

    fun exportDatabase(password: String) = uiScope.launch {
        withContext(Dispatchers.IO){
            exportDatabaseToFile(password)
        }
    }

    fun importDatabase(password: String) = uiScope.launch {
        withContext(Dispatchers.IO){
            importDataFromFile(password)
        }
    }

    @Throws(IOException::class)
    private fun exportDatabaseToFile(password: String) {
        val downloadsPath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileToSave =
            File(downloadsPath, DATABASE_FILE_NAME)
        val zipToSave =
            File(downloadsPath, ZIP_FILE_NAME)
        try {
            if (LoginRepository.user != null) {
                val user = LoginRepository.user!!
                val notes: List<Note> = noteRepository.getUserNotes(user.userId)
                if (notes.isNotEmpty()) {
                    val fileWriter = FileWriter(fileToSave, false)
                    val writer = CSVWriter(fileWriter)
                    val shortNotes = notes.stream()
                        .map { note: Note ->
                            arrayOf(
                                note.uid.toString(),
                                note.userId.toString(),
                                note.message
                            )
                        }
                        .collect(Collectors.toList())
                    writer.writeAll(shortNotes)
                    try {
                        writer.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            BackupUtils.backupFile(zipToSave, fileToSave, password)
        } finally {
            cleanUp(fileToSave)
        }
    }

    @Throws(IOException::class)
    private fun importDataFromFile(password: String) {
        val context = SecretApplication.appContext
        val username = LoginRepository.user!!.displayName
        val downloadsPath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileToRead =
            File(downloadsPath, DATABASE_FILE_NAME)
        val zipToRead =
            File(downloadsPath, ZIP_FILE_NAME)
        restoreBackup(password, downloadsPath, fileToRead, zipToRead)
        try {
            val fileReader = FileReader(fileToRead)
            val reader = CSVReader(fileReader)
            val data: List<Array<String>> = reader.readAll()
            getNotesFromCsv(data, username, context)
                .forEach { note: Note ->
                    try {
                        noteRepository.insertNote(
                            username,
                            Note(note.uid, note.userId, note.message)
                        )
                    } catch (e: Exception) {
                        makeToast("Something went wrong", context)
                    }
                }
        } catch (e: Exception) {
            makeToast("No such file", context)
        } finally {
            cleanUp(fileToRead)
        }
    }

    private fun restoreBackup(
        password: String,
        downloadsPath: File,
        fileToRead: File,
        zipToRead: File
    ) {
        try {
            BackupUtils.restoreBackup(zipToRead, fileToRead, downloadsPath.path, password)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            makeToast("Invalid file or password.", SecretApplication.appContext)

        }
    }

    private fun getNotesFromCsv(
        data: List<Array<String>>,
        username: String,
        context: Context
    ): List<Note> {
        val notes: MutableList<Note> = ArrayList()
        for (datum in data) {
            val newCard: Note? = parseDataFromCsv(datum, username, context)
            if (newCard != null) {
                notes.add(newCard)
            }
        }
        return notes
    }

    private fun parseDataFromCsv(record: Array<String>, username: String, context: Context): Note? {
        val secretManager = SecretManager()
        if (record.size != 3) {
            return null
        } else {
            try {
                val uid = record[0]
                val userId = record[1]
                val noteDecrypted: String =
                    secretManager.decryptString(username, record[2], context)
                return Note(Integer.valueOf(uid), Integer.valueOf(userId), noteDecrypted)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                makeToast("Failed to import file. Invalid format", context)
            }
        }
        return null
    }

    private fun cleanUp(fileToRead: File) {
        if (fileToRead.exists()) {
            fileToRead.delete()
        }
    }

    private fun makeToast(toastText: String, context: Context): Job = viewModelScope.launch {
        withContext(Dispatchers.Main) {
            Toast.makeText(
                context,
                toastText,
                Toast.LENGTH_LONG
            ).show()
        }
    }
}