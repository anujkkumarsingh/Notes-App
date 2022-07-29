package com.example.mynotes.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mynotes.Model.Notes


@Dao
interface NotesDao {

    @Query(value = "SELECT * FROM Notes")
    fun getNotes():LiveData<List<Notes>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotes(notes:Notes)

    @Query(value = "DELETE FROM Notes WHERE id = :id")
    fun deleteNotes(id: Int)

    @Update
    fun updateNotes(notes:Notes)
}