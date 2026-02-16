package com.techzit.mynotesapp.noteslist

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.techzit.mynotesapp.NotesViewModel
import com.techzit.mynotesapp.R
import com.techzit.mynotesapp.addnote.AddNotesActivity
import com.techzit.mynotesapp.db.Note
import com.techzit.mynotesapp.editnote.EditNotesActivity
import com.techzit.mynotesapp.noteslist.adapters.NotesAdapter
import com.techzit.mynotesapp.noteslist.adapters.NotesAdapter.NoteClickDeleteInterface
import com.techzit.mynotesapp.noteslist.adapters.NotesAdapter.NoteClickInterface

class NotesListActivity : AppCompatActivity(), NoteClickInterface , NoteClickDeleteInterface , AdapterView.OnItemSelectedListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: NotesViewModel
    private lateinit var addFab: FloatingActionButton
    private lateinit var adapter: NotesAdapter
    private lateinit var spinner: Spinner
    var sortList = arrayOf("Oldest Above", "Newest Above", "Title A-Z")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_notes_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recyclerView_Notes)
        addFab = findViewById(R.id.addFab)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = NotesAdapter(this, this)
        recyclerView.adapter = adapter


        //set up viewmodel
        //application is the global app instance that lives as long as app runsAccess resources
        //✔ Access database
        //✔ Access shared preferences
        //✔ Access file directory
        //✔ Store global variables
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[NotesViewModel::class.java]

        //we are calling all notes method to observe the changes on the list
       observeNotes()
        addFab.setOnClickListener() {
            val intent = Intent(this, AddNotesActivity::class.java)
            startActivity(intent)
            this.finish()
        }
        spinner = findViewById<Spinner>(R.id.spinner)
        spinner.onItemSelectedListener = this

        val spinnerAdapter: ArrayAdapter<CharSequence> = ArrayAdapter(this, android.R.layout.simple_spinner_item, sortList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        val selection="Newest Above"
        val spinnerPosition = spinnerAdapter.getPosition(selection)
        spinner.setSelection(spinnerPosition)

        /*findViewById<FloatingActionButton>(R.id.add).setOnClickListener {
            // This looks up the action ID you defined in nav_graph.xml
            it.findNavController().navigate(R.id.action_main_to_addNote)   }*/

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val search = menu?.findItem(R.id.menu_search)
        val searchView = search?.actionView as? SearchView


        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(query: String?): Boolean {
                searchDatabase(query)
                return true
            }

        })        // need to add this queryListener
        return true
    }



    private fun searchDatabase(query: String?){
        if(query.isNullOrEmpty()){
            viewModel.allNotes.observe(this@NotesListActivity){list ->
                list?.let {
                    adapter.updateList(it)}
            }
        }else {
            viewModel.searchNotes(query).observe(this@NotesListActivity) {
                adapter.updateList(it)
            }
        }
    }


    override fun onDeleteIconClick(note: Note) {
        confirmDeleteDialog(note)
    }
    override fun onNoteClick(note: Note) {
        val intent = Intent(this@NotesListActivity, EditNotesActivity::class.java)
        intent.putExtra("noteType", "Edit")
        intent.putExtra("noteTitle", note.noteTitle)
        intent.putExtra("noteDescription", note.description)
        intent.putExtra("noteId", note.id)
        intent.putExtra("noteCreatedDate", note.createdDate)
        startActivity(intent)
    }
    private fun confirmDeleteDialog(note: Note) {

        AlertDialog.Builder(this)
            .setTitle("Delete Note")
            .setMessage("Do you really want to delete this note?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.deleteNote(note)
                Toast.makeText(this, "${note.noteTitle} Deleted", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (position){
            0 -> viewModel.sortOldAbove()
            1 -> viewModel.sortNewAbove()
            2 -> viewModel.sortTitle()
        }
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
private fun observeNotes(){
    viewModel.allNotes.observe(this) { list ->
        list?.let {
            adapter.updateList(it)
            if(it.isEmpty()){
                spinner.visibility = View.GONE
            }else {
                spinner.visibility = View.VISIBLE
            }
}}}

}

