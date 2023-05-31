package com.momotmilosz.projektbam.data.database

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: Note)

    @Query("DELETE FROM note WHERE uid = :uid")
    fun delete(uid: Int)

    @Query("SELECT * FROM note WHERE user_id = :userId")
    fun getAllNotesObservableByUserId(userId: Int): Flow<MutableList<Note>>

    @Query("SELECT * FROM note WHERE user_id = :userId")
    fun getAllNotesByUserId(userId: Int): List<Note>

    @Query("SELECT * FROM note")
    fun getAllCursor(): Cursor
}