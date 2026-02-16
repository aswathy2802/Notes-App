package com.techzit.mynotesapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.newproject.notesapp.repository.NotesRepository
import com.techzit.mynotesapp.db.Note
import com.techzit.mynotesapp.db.NoteDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.collections.List


class NotesViewModel(app: Application): AndroidViewModel(app) {

    private val repository: NotesRepository
    private val sortType = MutableLiveData("New")
    var allNotes: LiveData<List<Note>>
    // init block runs when Viewmodel is Created
    init {
        val dao = NoteDatabase.getDatabase(app).getNotesDao() // get dao instance from Room db
        repository = NotesRepository(dao)
       allNotes = sortType.switchMap { type->
           when(type){
               "New" -> repository.getAllNotes()
               "Old" -> repository.oldestAbove()
               "Title" -> repository.sortByTitle()
               else -> repository.getAllNotes()
           }
       }

    }
    // each fn will be used in Activities
    fun deleteNote(note: Note) = viewModelScope.launch(Dispatchers.IO){
        repository.delete(note)
    }
    fun updateNote(note: Note) = viewModelScope.launch(Dispatchers.IO){
        repository.update(note)
    }
    fun addNote(note: Note) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(note)
    }
    fun searchNotes(searchQuery: String): LiveData<List<Note>>{
        // all others functions need viewmodelScope coz they are using coroutines to do background work and also they use suspend fun
        // here only we return livedata from repository
        return repository.searchNotes(searchQuery).asLiveData() //dao return flow so we need to convert it to livedata

    }
    fun sortOldAbove(){
        sortType.value ="Old"
//        allNotes=repository.oldestAbove()
    }
    fun sortNewAbove(){
        sortType.value = "New"
   //    allNotes= repository.getAllNotes()
    }
    fun sortTitle(){
        sortType.value = "Title"
 //       allNotes=repository.sortByTitle()
    }

}


