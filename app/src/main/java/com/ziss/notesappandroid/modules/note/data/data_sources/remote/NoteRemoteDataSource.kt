package com.ziss.notesappandroid.modules.note.data.data_sources.remote

import com.ziss.notesappandroid.modules.note.data.data_sources.remote.services.NoteService
import com.ziss.notesappandroid.modules.note.data.models.NoteDto

class NoteRemoteDataSource(private val noteService: NoteService) {
    suspend fun create(note: NoteDto) = noteService.create(note)

    suspend fun fetch(page: Int, limit: Int) = noteService.fetch(page, limit)

    suspend fun update(id: Int, note: NoteDto) = noteService.update(id, note)

    suspend fun getById(id: Int) = noteService.getById(id)

    suspend fun delete(id: Int): String {
        noteService.delete(id)
        return "Note Deleted"
    }
}