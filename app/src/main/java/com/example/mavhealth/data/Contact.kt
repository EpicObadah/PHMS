package com.example.mavhealth.data

import com.google.firebase.database.Exclude

data class Contact (@get:Exclude var id: String? = null, var name: String? = null, var number : String? = null)
{

}