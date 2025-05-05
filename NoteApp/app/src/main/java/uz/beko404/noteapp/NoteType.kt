package uz.beko404.noteapp

enum class NoteType(val displayName: String, val colorHex: String) {
    PERSONAL("Personal", "#A7D8DE"),
    WORK("Work", "#FDCB6E"),
    STUDY("Study", "#E17055"),
    OTHER("Other", "#D3D3D3")
}