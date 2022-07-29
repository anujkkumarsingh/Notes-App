package com.example.mynotes.ui.adapter

import android.R
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.Model.Notes
import com.example.mynotes.ViewModel.NotesViewModel
import com.example.mynotes.databinding.ItemNotesBinding
import com.example.mynotes.ui.fragment.HomeFragmentDirections

class NotesAdapter(val requireContext: Context,val notesList: List<Notes>) : RecyclerView.Adapter<NotesAdapter.notesViewHolder> (){
    class notesViewHolder(val binding:ItemNotesBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): notesViewHolder {
        return notesViewHolder(ItemNotesBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: notesViewHolder, position: Int) {
        val data = notesList[position]
        holder.binding.itemTitle.text=data.title
        holder.binding.itemNote.text=data.notes
        holder.binding.itemDate.text=data.date


        holder.binding.editNt.setOnClickListener{
            val action = HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(data)
            Navigation.findNavController(it).navigate(action)
        }


        holder.binding.shareNt.setOnClickListener {
            var note=data.notes.toString()
            var share = Intent()
            share.action = ACTION_SEND
            share.type="text/plain"
            share.putExtra(EXTRA_TEXT, note)
            Log.e("222", "onBindViewHolder: $note")
            it.getContext().startActivity(createChooser(share,"Share via"))


        }



   }


    override fun getItemCount()= notesList.size

}


