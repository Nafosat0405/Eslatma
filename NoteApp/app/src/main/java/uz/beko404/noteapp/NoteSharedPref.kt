package uz.beko404.noteapp

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class NoteSharedPref(context: Context) {

        private val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("notes_pref", Context.MODE_PRIVATE)
        private val editor: SharedPreferences.Editor = sharedPreferences.edit()

        private val gson = Gson()

        // Save a note: If note already exists, update it
        fun saveNote(note: Note) {
            val notesList = getAllNotes().toMutableList()

            // Check if the note with the same noteId already exists
            val existingNoteIndex = notesList.indexOfFirst { it.id == note.id }

            if (existingNoteIndex >= 0) {
                // If note exists, update it
                notesList[existingNoteIndex] = note
            } else {
                // Otherwise, add the new note
                notesList.add(note)
            }

            // Save the updated notes list
            val notesJson = gson.toJson(notesList)
            editor.putString("notes", notesJson).apply()
        }

        // Get all notes
        fun getAllNotes(): List<Note> {
            val notesJson = sharedPreferences.getString("notes", "[]") ?: "[]"
            return gson.fromJson(notesJson, Array<Note>::class.java).toList()
        }

        // Delete a note by its title or ID
        fun deleteNote(noteId: String) {
            val notesList = getAllNotes().toMutableList()
            notesList.removeIf { it.id == noteId }  // Remove by noteId
            val notesJson = gson.toJson(notesList)
            editor.putString("notes", notesJson).apply()
        }

}