package com.momotmilosz.projektbam.ui.notes

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.momotmilosz.projektbam.R
import com.momotmilosz.projektbam.data.database.Note
import com.momotmilosz.projektbam.data.repository.LoginRepository.Companion.user
import com.momotmilosz.projektbam.databinding.ActivityEditNoteFormBinding

class EditNoteForm : AppCompatActivity() {
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var binding: ActivityEditNoteFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteViewModel = ViewModelProvider(this, NoteViewModelFactory())
            .get(NoteViewModel::class.java)

        val incommingIntent = intent.extras

        val noteUid = incommingIntent?.getInt("note_uid")
        val noteMessage = incommingIntent?.getString("note_message")

        val editedMessage = findViewById<EditText>(R.id.editNoteMessage)
        editedMessage.setText(noteMessage)

        val saveBtn = findViewById<Button>(R.id.saveEditNoteBtn)
        saveBtn.setOnClickListener {
            val editedNote = Note(noteUid, user!!.userId!!, editedMessage.text.toString())
            noteViewModel.insert(user!!.displayName, editedNote)
            goToNotesActivity(user!!.userId!!)
        }

        val cancelBtn = findViewById<Button>(R.id.cancelEditNoteBtn)
        cancelBtn.setOnClickListener {
            goToNotesActivity(user!!.userId!!)
        }

        val deleteBtn = findViewById<Button>(R.id.deleteNoteBtn)
        deleteBtn.setOnClickListener {
            noteViewModel.delete(noteUid!!)
            goToNotesActivity(user!!.userId!!)
        }
    }

    private fun goToNotesActivity(userId: Int) {
        val notesIntent = Intent(this, NotesActivity::class.java)
        notesIntent.putExtra("userId", userId)
        startActivity(notesIntent)
    }
}