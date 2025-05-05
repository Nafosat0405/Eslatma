package uz.beko404.noteapp

import java.util.UUID

data class Note(
    val id: String = UUID.randomUUID().toString(),
    var title: String,
    var content: String,
    var type: NoteType,
    val date: Long = System.currentTimeMillis()
)
