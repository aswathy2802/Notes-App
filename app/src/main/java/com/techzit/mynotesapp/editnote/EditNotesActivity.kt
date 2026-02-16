package com.techzit.mynotesapp.editnote

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.techzit.mynotesapp.NotesViewModel
import com.techzit.mynotesapp.R
import com.techzit.mynotesapp.db.Note
import com.techzit.mynotesapp.noteslist.NotesListActivity

class EditNotesActivity : AppCompatActivity() {
    private lateinit var viewModel: NotesViewModel
    private var noteId = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_notes)
       viewModel = ViewModelProvider(
           this,
           ViewModelProvider.AndroidViewModelFactory(application)
       )[NotesViewModel::class.java]

        val title = findViewById<EditText>(R.id.notetitle)
        val description = findViewById<EditText>(R.id.notedescription)
        val updatebtn = findViewById<Button>(R.id.btnsave)


        // adding data to edit-text
        val notetitle = intent.getStringExtra("noteTitle")
        val notedescription = intent.getStringExtra("noteDescription")
        noteId = intent.getIntExtra("noteId", -1)
        val createdDate = intent.getLongExtra("noteCreatedDate", -1)
        title.setText(notetitle)
        description.setText(notedescription)

        updatebtn.setOnClickListener {
            val updatedTitle = title.text.toString()
            val updatedDescription = description.text.toString()
            val updateDate = System.currentTimeMillis()
            val updatedNote = Note(noteId, updatedTitle, updatedDescription, createdDate, updateDate)
            viewModel.updateNote(updatedNote)
            Toast.makeText(this,"Note Updated..", Toast.LENGTH_LONG).show()
            startActivity(Intent(applicationContext, NotesListActivity::class.java))
            this.finish()
        }

    }
}