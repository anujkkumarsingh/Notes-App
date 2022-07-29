package com.example.mynotes.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mynotes.Dao.NotesDao
import com.example.mynotes.Model.Notes
import kotlinx.coroutines.InternalCoroutinesApi

@Database(entities = [Notes::class], version = 1, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun myNotesDao(): NotesDao

    companion object{
        @Volatile
        var INSTANCE:NotesDatabase?=null

        fun getDatabaseInstance(context:Context):NotesDatabase{
            val tmpInstance= INSTANCE

            if(tmpInstance!=null)
            {
                return tmpInstance
            }
            synchronized(this)
            {
                val roomDatabaseInstance = Room.databaseBuilder(context,NotesDatabase::class.java,"Notes").allowMainThreadQueries().build()
                INSTANCE=roomDatabaseInstance
                return roomDatabaseInstance
            }

        }
    }

}