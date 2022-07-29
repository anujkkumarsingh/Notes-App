package com.example.mynotes.Model


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "Notes")
@Parcelize
class Notes(

    @PrimaryKey(autoGenerate = true)
    var id: Int?=null,

    var title: String,

    var notes: String,

    var imgPath:String,

    var date: String,
): Parcelable