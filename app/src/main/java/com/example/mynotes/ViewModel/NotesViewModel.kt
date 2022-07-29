package com.example.mynotes.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.mynotes.Database.NotesDatabase
import com.example.mynotes.Model.Notes
import com.example.mynotes.Repository.NotesRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import java.lang.Appendable

class NotesViewModel(application: Application): AndroidViewModel(application) {

    val repository:NotesRepository

    init {
        val dao=NotesDatabase.getDatabaseInstance(application).myNotesDao()
        repository= NotesRepository(dao)
    }

    fun addNotes(notes: Notes){
        repository.insertNotes(notes)
    }

    fun getNotes(): LiveData<List<Notes>> = repository.getAllNotes()

    fun deleteNotes(id: Int){
        repository.deleteNotes(id)
    }

    fun updateNotes(notes:Notes){
        repository.updateNotes(notes)
    }



}