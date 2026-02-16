package com.techzit.mynotesapp.addnote

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
import java.text.SimpleDateFormat

class AddNotesActivity : AppCompatActivity() {
    private lateinit var viewModel: NotesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_notes)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[NotesViewModel::class.java]

        val noteTitle = findViewById<EditText>(R.id.note_title)
        val noteDescription = findViewById<EditText>(R.id.note_description)
        val savebtn = findViewById<Button>(R.id.btn)

        savebtn.setOnClickListener {
            val title = noteTitle.text.toString()
            val description = noteDescription.text.toString()
            if (title.isNotEmpty() && description.isNotEmpty()){
                val currentDate = System.currentTimeMillis()
                val updatedDate = System.currentTimeMillis()
                viewModel.addNote(Note(noteTitle = title,description= description, createdDate = currentDate, updatedDate =  updatedDate))
                Toast.makeText(this, "$title Added", Toast.LENGTH_LONG).show()
                finish()
            }
            else{
                Toast.makeText(this, "Please enter title and description", Toast.LENGTH_LONG).show()
            }
            startActivity(Intent(applicationContext, NotesListActivity::class.java))
            this.finish()
        }

    }
}