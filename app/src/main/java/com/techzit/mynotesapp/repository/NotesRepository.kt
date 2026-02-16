package com.newproject.notesapp.repository

import androidx.lifecycle.LiveData
import com.techzit.mynotesapp.db.Note
import com.techzit.mynotesapp.db.NotesDAO
import kotlinx.coroutines.flow.Flow

class NotesRepository(private val notesDAO: NotesDAO) {
    fun getAllNotes(): LiveData<List<Note>> = notesDAO.getAllNotes()
    fun oldestAbove(): LiveData<List<Note>> = notesDAO.oldestAbove()
    fun sortByTitle(): LiveData<List<Note>> = notesDAO.sortByTitle()

    suspend fun insert(note: Note) {
        notesDAO.insert(note)
    }

    suspend fun delete(note: Note) {
        notesDAO.delete(note)
    }

    suspend fun update(note: Note) {
        notesDAO.update(note)
    }
    fun searchNotes(searchQuery: String): Flow<List<Note>> {
        return notesDAO.searchNotes(searchQuery)
    }


}