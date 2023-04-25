package com.example.mavhealth.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mavhealth.R
import com.example.mavhealth.data.Contact
import com.example.mavhealth.databinding.FragmentAddContactDialogBinding
import androidx.lifecycle.DefaultLifecycleObserver

class AddContactDialogFragment : DialogFragment(), DefaultLifecycleObserver {
    private var _binding: FragmentAddContactDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ContactViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentAddContactDialogBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ContactViewModel::class.java]
        return binding.root
    }
/*
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super<DialogFragment>.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth)
        activity?.lifecycle?.removeObserver(this)
        //viewLifecycleOwner = getViewLifeOwner()
        viewModel = ViewModelProvider(this)[ContactViewModel::class.java]
        viewModel.result.observe(this, Observer{
            val message = if (it == null) {
                getString(R.string.added_contact)
            }else{
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
            dismiss()
        })

        binding.buttonAdd.setOnClickListener {
            val fullName = binding.editTextFullName.text.toString().trim()
            val contactNumber = binding.editTextContact.toString().trim()
            if(fullName.isEmpty())
            {
                binding.editTextFullName.error = "This is a required field"
                return@setOnClickListener
            }
            if(contactNumber.isEmpty())
            {
                binding.editTextContact.error = "This is a required field"
                return@setOnClickListener
            }
            val contact = Contact()
            contact.name = fullName
            contact.number = contactNumber
            viewModel.addContact(contact)
        }
    }
*/


    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ContactViewModel::class.java]
        viewModel.result.observe(this, Observer{
            val message = if (it == null) {
                getString(R.string.added_contact)
            }else{
                getString(R.string.error, it.message)
            }
            Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
            dismiss()
        })
        binding.buttonAdd.setOnClickListener {
            val fullName = binding.editTextFullName.text.toString().trim()
            val contactNumber = binding.editTextContact.toString().trim()
            if(fullName.isEmpty())
            {
                binding.editTextFullName.error = "This is a required field"
                return@setOnClickListener
            }
            if(contactNumber.isEmpty())
            {
                binding.editTextContact.error = "This is a required field"
                return@setOnClickListener
            }
            val contact = Contact()
            contact.name = fullName
            contact.number = contactNumber
            viewModel.addContact(contact)
        }
    }

/*
    override fun onAttach(context: Context)
    {
        super.onAttach(context)
        activity?.lifecycle?.addObserver(this)
    }
*/
}