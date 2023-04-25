package com.example.mavhealth.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mavhealth.data.Contact
import com.example.mavhealth.databinding.RecyclerViewContactBinding

class ContactAdapter : RecyclerView.Adapter<ContactAdapter.ViewHolder>()
{
    inner class ViewHolder(private val binding: RecyclerViewContactBinding) : RecyclerView.ViewHolder(binding.root)
    {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapter.ViewHolder
    {
        return ViewHolder(RecyclerViewContactBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ContactAdapter.ViewHolder, position: Int)
    {

    }

    override fun getItemCount(): Int
    {
        return contacts.size
    }

    var contacts = mutableListOf<Contact>()
}