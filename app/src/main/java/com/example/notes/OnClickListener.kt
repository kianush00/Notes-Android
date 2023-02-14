package com.example.notes

interface OnClickListener {
    fun onChecked(note: Note)
    fun onLongClick(note: Note, currentAdapter: NoteAdapter)
}