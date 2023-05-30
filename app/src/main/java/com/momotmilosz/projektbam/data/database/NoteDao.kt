package com.momotmilosz.projektbam.data.database

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Delete
    fun delete(note: Note)

    @Query("SELECT * FROM note WHERE user_id = :userId")
    fun getAllByUserId(userId: Int): Flow<MutableList<Note>>

    @Query("SELECT * FROM note WHERE uid = :noteId")
    fun getNoteById(noteId: Int): Note

    @Query("SELECT * FROM note")
    fun getAllCursor(): Cursor
}