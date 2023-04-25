package com.example.mavhealth.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mavhealth.data.Contact
import com.example.mavhealth.data.NODE_CONTACTS
import com.google.firebase.database.FirebaseDatabase

class ContactViewModel: ViewModel()
{
    private val dbContacts = FirebaseDatabase.getInstance().getReference(NODE_CONTACTS)
    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?> get() = _result
    fun addContact(contact: Contact)
    {
        contact.id = dbContacts.push().key
        dbContacts.child(contact.id!!).setValue(contact).addOnCompleteListener{
            if(it.isSuccessful)
            {
                _result.value = null
            }
            else
            {
                _result.value = it.exception
            }
        }

    }
}