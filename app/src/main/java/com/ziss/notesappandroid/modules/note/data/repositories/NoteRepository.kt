package com.ziss.notesappandroid.modules.note.data.repositories

import arrow.core.Either.Left
import arrow.core.Either.Right
import com.ziss.notesappandroid.core.common.ApiException
import com.ziss.notesappandroid.core.common.ServerFailure
import com.ziss.notesappandroid.modules.note.data.data_sources.local.NoteLocalDataSource
import com.ziss.notesappandroid.modules.note.data.data_sources.remote.NoteRemoteDataSource
import com.ziss.notesappandroid.modules.note.data.models.NoteDto
import kotlinx.coroutines.flow.flow

class NoteRepository(
    private val remoteDataSource: NoteRemoteDataSource,
    private val localDataSource: NoteLocalDataSource
) {
    fun createNote(note: NoteDto) = flow {
        try {
            val result = remoteDataSource.create(note)
            localDataSource.clearAll()
            emit(Right(result))
        } catch (e: ApiException) {
            emit(Left(ServerFailure(e.error)))
        }
    }

    fun deleteNote(id: Int) = flow {
        try {
            val result = remoteDataSource.delete(id)
            localDataSource.clearAll()
            emit(Right(result))
        } catch (e: ApiException) {
            emit(Left(ServerFailure(e.error)))
        }
    }

    fun fetchNotes(
        refresh: Boolean = false,
        loadMore: Boolean = false,
        page: Int? = 1,
        limit: Int = 10,
    ) = flow {
        val localResult = localDataSource.findAll()
        if (localResult.isNotEmpty() && !refresh && !loadMore) {
            emit(Right(localResult))
        } else {
            try {
                val remoteResult = remoteDataSource.fetch(page!!, limit)
                if (refresh) {
                    localDataSource.clearAll()
                    localDataSource.insertAll(remoteResult.data!!)
                }
                emit(Right(remoteResult.data ?: emptyList()))
            } catch (e: ApiException) {
                emit(Left(ServerFailure(e.error)))
            }
        }
    }

    fun getNoteById(id: Int) = flow {
        try {
            val result = remoteDataSource.getById(id)
            emit(Right(result))
        } catch (e: ApiException) {
            emit(Left(ServerFailure(e.error)))
        }
    }

    fun updateNote(id: Int, note: NoteDto) = flow {
        try {
            val result = remoteDataSource.update(id, note)
            localDataSource.clearAll()
            emit(Right(result))
        } catch (e: ApiException) {
            emit(Left(ServerFailure(e.error)))
        }
    }
}