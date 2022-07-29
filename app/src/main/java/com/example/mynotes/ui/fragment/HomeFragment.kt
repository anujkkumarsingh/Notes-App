package com.example.mynotes.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.Model.Notes
import com.example.mynotes.R
import com.example.mynotes.SwipeToDelete
import com.example.mynotes.ViewModel.NotesViewModel
import com.example.mynotes.databinding.FragmentHomeBinding
import com.example.mynotes.ui.adapter.NotesAdapter
import com.google.android.material.snackbar.Snackbar


class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    val viewModel: NotesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentHomeBinding.inflate(layoutInflater,container,false)

        viewModel.getNotes().observe(viewLifecycleOwner) { notesList ->
            binding.rv.layoutManager=LinearLayoutManager(requireContext())
            binding.rv.adapter=NotesAdapter(requireContext(),notesList)
            val swipeDelete = object: SwipeToDelete(){
                @SuppressLint("NotifyDataSetChanged")
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    val position=viewHolder.adapterPosition

                    val deletedNote:Notes=notesList.get(position)
                    notesList[position].id?.let { viewModel.deleteNotes(it) }

                    Snackbar.make(requireView(),"Note Deleted",Snackbar.LENGTH_LONG).setAction("Undo",View.OnClickListener {
                        viewModel.addNotes(deletedNote)
                        binding.rv.adapter?.notifyDataSetChanged()
                    }).show()

                }
            }

            val touchhelper = ItemTouchHelper(swipeDelete)
            touchhelper.attachToRecyclerView(binding.rv)
        }

        binding.btnAddNotes.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_addNoteFragment)
        }

        


        return binding.root
    }
}
