package com.momotmilosz.projektbam.ui.notes

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.momotmilosz.projektbam.data.database.Note
import com.momotmilosz.projektbam.databinding.ActivityNotesBinding

class NotesActivity : AppCompatActivity() {
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var binding: ActivityNotesBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userId = intent.getIntExtra("userId", 0);

        noteViewModel = ViewModelProvider(this, NoteViewModelFactory())
            .get(NoteViewModel::class.java)

        binding.addNote.setOnClickListener {
            val noteMessage = binding.messageNote.text.toString()
            val note = Note(userId = userId, message = noteMessage)
            noteViewModel.insert(note)
        }

        noteViewModel.getUserNotes(userId).observe(this) { notes ->
            notes?.let {
                val notesView = binding.lvNotes
                val notesAdapter = NoteAdapter(this, notes)
                notesView.adapter = notesAdapter

                notesView.setOnItemClickListener { adapterView, view, i, l ->
                    Toast.makeText(this, "pos=" + i + " name=" + notes[i].message, Toast.LENGTH_LONG).show()
                }

//                var msg = ""
//
//                for (note in notes) {
//                    msg += note.message + "\n\n"
//                }
//                AlertDialog.Builder(this).setMessage(msg).show();
            }
        }
    }
}