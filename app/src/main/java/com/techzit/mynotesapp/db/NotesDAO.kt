package com.techzit.mynotesapp.db
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDAO
{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("Select * from notesTable order by updatedOn DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Update
    suspend fun update(note: Note)

    @Query("Select * from notesTable where title like '%' || :searchQuery || '%' OR description like '%' || :searchQuery || '%'")
    fun searchNotes(searchQuery: String): Flow<List<Note>>

    @Query("Select * from notesTable order by updatedOn ASC")
    fun oldestAbove(): LiveData<List<Note>>

    @Query("Select * from notesTable order by Title ASC")
    fun sortByTitle(): LiveData<List<Note>>
}