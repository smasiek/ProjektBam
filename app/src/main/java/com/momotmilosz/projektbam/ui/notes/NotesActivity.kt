package com.momotmilosz.projektbam.ui.notes

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.momotmilosz.projektbam.SecretApplication
import com.momotmilosz.projektbam.R
import com.momotmilosz.projektbam.data.database.Note
import com.momotmilosz.projektbam.data.repository.LoginRepository.Companion.user
import com.momotmilosz.projektbam.data.security.SecretManager
import com.momotmilosz.projektbam.databinding.ActivityNotesBinding

class NotesActivity : AppCompatActivity() {
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var binding: ActivityNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userId = intent.getIntExtra("userId", 0)

        noteViewModel = ViewModelProvider(this, NoteViewModelFactory())
            .get(NoteViewModel::class.java)

        binding.addNote.setOnClickListener {
            val noteMessage = binding.messageNote.text.toString()
            val note = Note(userId = userId, message = noteMessage)
            noteViewModel.insert(
                user?.displayName ?: throw IllegalAccessException("No logged user"), note
            )
        }

        noteViewModel.getUserNotes(userId).observe(this) { notes ->
            val decryptedNotes: MutableList<Note> = decryptNotes(notes)
            decryptedNotes.let {
                val notesView = binding.notesLv
                notesView.adapter = ArrayAdapter(this, R.layout.note_row, notes)


                notesView.setOnItemClickListener { adapterView, view, i, l ->
                    Toast.makeText(this, "pos=" + i + " name=" + notes[i].message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun decryptNotes(notes: MutableList<Note>): MutableList<Note> {
        val secretManager = SecretManager()
        val context = SecretApplication.appContext as SecretApplication

        val decryptedNotes: MutableList<Note> = mutableListOf()
        for (note in notes) {
            decryptedNotes.add(
                Note(
                    note.uid,
                    note.userId,
                    secretManager.decryptString(user?.displayName, note.message, context)
                )
            )
        }
        return decryptedNotes
    }
}