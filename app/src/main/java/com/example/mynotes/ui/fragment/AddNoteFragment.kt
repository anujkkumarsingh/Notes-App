package com.example.mynotes.ui.fragment


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.mynotes.Model.Notes
import com.example.mynotes.ViewModel.NotesViewModel
import com.example.mynotes.databinding.FragmentAddNoteBinding
import java.text.SimpleDateFormat


class AddNoteFragment : Fragment() {

    lateinit var binding:FragmentAddNoteBinding
    lateinit var dateString: String
    val viewModel: NotesViewModel by viewModels()
    var REQUEST_CODE_STORAGE_PERMISSION: Int =1
    var REQUEST_CODE_SELECT_IMAGE: Int =2
    var path: String=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding=FragmentAddNoteBinding.inflate(layoutInflater,container,false)

        val date = System.currentTimeMillis()

        val sdf = SimpleDateFormat("MMM dd, yyyy   h:mm a")
        dateString= sdf.format(date)
        binding.txtTime.setText(dateString)

        binding.btncreate.setOnClickListener {
            createNotes(it)
        }

       binding.txtAddimage.setOnClickListener {
            if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                selectImage()
            }
           else
            {
                val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                activity?.let { it1 -> requestPermissions(it1,permissions,REQUEST_CODE_STORAGE_PERMISSION) }
            }
       }

        return binding.root
    }

    private fun createNotes(it: View?) {

        val title = binding.editTextCreate.text.toString()
        val note=binding.editTextNote.text.toString()
        val datetime =dateString.toString()
        val imgpath=path

        val data= Notes(null, title = title, notes=note,imgpath,date = datetime)
        viewModel.addNotes(data)
       // Toast.makeText(requireActivity(), "Done", Toast.LENGTH_LONG).show()

        Navigation.findNavController(it!!).navigate(com.example.mynotes.R.id.action_addNoteFragment_to_homeFragment)


    }


    private fun selectImage(){
        val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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
                Toast.makeText(context,"Permission Denied",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQUEST_CODE_SELECT_IMAGE && resultCode==Activity.RESULT_OK)
        {
            binding.imgNote.visibility= View.VISIBLE
            if (data != null) {
                binding.imgNote.setImageURI(data.data)
            }
            path=data?.data.toString()
        }
    }





}