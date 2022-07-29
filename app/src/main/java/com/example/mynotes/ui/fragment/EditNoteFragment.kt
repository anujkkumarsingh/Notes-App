package com.example.mynotes.ui.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.mynotes.Model.Notes
import com.example.mynotes.R
import com.example.mynotes.ViewModel.NotesViewModel
import com.example.mynotes.databinding.FragmentEditNoteBinding
import java.text.SimpleDateFormat

class  EditNoteFragment : Fragment() {

    lateinit var binding: FragmentEditNoteBinding
    lateinit var dateString:String
    val argNotes by navArgs<EditNoteFragmentArgs>()
    val viewModel: NotesViewModel by viewModels()
    var REQUEST_CODE_STORAGE_PERMISSION: Int =1
    var REQUEST_CODE_SELECT_IMAGE: Int =2
    var path:String=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding=FragmentEditNoteBinding.inflate(layoutInflater,container,false)

        setHasOptionsMenu(true)


        val date = System.currentTimeMillis()

        val sdf = SimpleDateFormat("MMM dd, yyyy   h:mm a")
        dateString= sdf.format(date)
        binding.txtTime1.setText(dateString)

        binding.editTextEdit.setText(argNotes.data.title)
        binding.editText.setText(argNotes.data.notes)
        if(argNotes.data.imgPath!="")
        {
            binding.imgNote1.visibility=View.VISIBLE
            binding.imgNote1.setImageURI(Uri.parse(argNotes.data.imgPath))
        }

        binding.btnEdit.setOnClickListener{
            saveEdittedNote(it)
        }
        binding.shareBtn.setOnClickListener {
            shareNote(it);
        }

        binding.txtAddimage1.setOnClickListener {
            if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                selectImage()
            }
            else
            {
                val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                activity?.let { it1 ->
                    ActivityCompat.requestPermissions(
                        it1,
                        permissions,
                        REQUEST_CODE_STORAGE_PERMISSION
                    )
                }
            }
        }
        return binding.root
    }

    private fun shareNote(it: View?)
    {

        val note=binding.editText.text.toString()
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type="text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, note)
        startActivity(Intent.createChooser(shareIntent,"Share via"))
    }
    private fun saveEdittedNote(it: View?) {
        val title = binding.editTextEdit.text.toString()
        val note=binding.editText.text.toString()
        val datetime =dateString.toString()
        val imgpath= path

        val data= Notes(argNotes.data.id, title = title, notes=note,imgpath, date = datetime)
        viewModel.updateNotes(data)
        //Toast.makeText(requireActivity(), "Saved", Toast.LENGTH_LONG).show()

        Navigation.findNavController(it!!).navigate(com.example.mynotes.R.id.action_editNoteFragment_to_homeFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_delete,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.menu_delete)
        {
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Are you sure you want to Delete?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    // Delete selected note from database
                    viewModel.deleteNotes(argNotes.data.id!!)
                    Navigation.findNavController(requireView()).navigate(com.example.mynotes.R.id.action_editNoteFragment_to_homeFragment)
                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        return super.onOptionsItemSelected(item)
    }
    private fun selectImage(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type="image/*"
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==REQUEST_CODE_STORAGE_PERMISSION && grantResults.size>0)
        {
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                selectImage()
            }
            else
            {
                Toast.makeText(context,"Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUEST_CODE_SELECT_IMAGE && resultCode== Activity.RESULT_OK)
        {
            binding.imgNote1.visibility= View.VISIBLE
            if (data != null) {
                binding.imgNote1.setImageURI(data.data)
            }
            path=data?.data.toString()
        }
    }





}