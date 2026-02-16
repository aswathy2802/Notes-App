package com.techzit.mynotesapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notesTable")
data class Note(
    @PrimaryKey(autoGenerate = true) var id: Int=0,
    @ColumnInfo(name = "Title") val noteTitle: String,
    @ColumnInfo(name = "Description") val description: String,
    @ColumnInfo(name = "createdOn") val createdDate: Long,
    @ColumnInfo(name = "updatedOn") val updatedDate: Long
) {

}