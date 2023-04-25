package com.example.mavhealth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mavhealth.ARG_PARAM1
import com.example.mavhealth.ARG_PARAM2
import com.example.mavhealth.R
import com.example.mavhealth.databinding.FragmentContactsBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ContactsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactsFragment : Fragment()
{
    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private val adapter = ContactAdapter()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewContacts.adapter = adapter

        binding.addButton.setOnClickListener {
            AddContactDialogFragment().show(childFragmentManager, "")
        }



    }

    override fun onDestroy()
    {
        super.onDestroy()
        _binding = null
    }

}