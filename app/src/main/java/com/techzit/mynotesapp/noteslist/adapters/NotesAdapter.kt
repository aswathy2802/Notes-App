package com.techzit.mynotesapp.noteslist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.techzit.mynotesapp.R
import com.techzit.mynotesapp.db.Note
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotesAdapter(
 //interfaces are like tv remote to chnge channel, inc vol . Tv(Adapter) doesn't decide
 private val noteClickDeleteInterface: NoteClickDeleteInterface,
 private val noteClickInterface: NoteClickInterface
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
 //created a var for all our notes list
 private val allNotes = ArrayList<Note>()

 //created a view holder class
 inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

  //var added in item_note
  val titleText: TextView = itemView.findViewById(R.id.titleText)
  val descriptionText: TextView = itemView.findViewById(R.id.descriptionText)
  val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
  val updatedDate: TextView = itemView.findViewById(R.id.updatedDate)
  val createdDate: TextView = itemView.findViewById(R.id.createdDate)
 }

 override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
  // inflating our layout for each item in recyclerview
  return NoteViewHolder(
      LayoutInflater.from(parent.context).inflate(
       R.layout.item_note,
       parent,
       false)
  )
 }

/*
 private var notes: List<String> = emptyList()

 fun setNotes(notes: List<String>) {
  this.notes = notes
  notifyDataSetChanged()
 }

*/

 override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
 // on here we are setting data to each item in recyclerview
  holder.titleText.text = allNotes[position].noteTitle
  holder.descriptionText.text = allNotes[position].description
  holder.updatedDate.text = formatDate(allNotes[position].updatedDate)
  holder.createdDate.text = formatDate(allNotes[position].createdDate)

  holder.deleteButton.setOnClickListener{
   noteClickDeleteInterface.onDeleteIconClick(allNotes[position])
  }

  holder.itemView.setOnClickListener {
   noteClickInterface.onNoteClick(allNotes[position])
  /* val context = holder.itemView.context
    val intent = Intent(context , EditNotesActivity::class.java)
   intent.putExtra("noteId", position)
   context.startActivity(intent)*/

  }
 }

 fun formatDate(time: Long): String{
  val format = SimpleDateFormat("dd MM yyyy, hh:mm a", Locale.getDefault())
  return format.format(Date(time))
 }

 override fun getItemCount(): Int = allNotes.size

 interface NoteClickDeleteInterface{
  fun onDeleteIconClick(note: Note)   // will implement this method in NotesListActivity
 }

 interface NoteClickInterface{
  fun onNoteClick(note: Note)
 }
//update our list of notes
 fun updateList(newNote: List<Note>){
  allNotes.clear()
 // on below line we are adding a new list to our all notes list.
 allNotes.addAll(newNote)
 // on below line we are calling notify data
 // this function to notify our adapter.
  notifyDataSetChanged()
 }
}