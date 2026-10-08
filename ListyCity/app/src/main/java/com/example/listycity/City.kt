package com.example.listycity

import com.google.firebase.firestore.Exclude

/**
 * A city stored in the Firestore "cities" collection.
 *
 * Every property needs a default value so Firestore can build the object
 * with toObject(). The id is the Firestore document ID: it is filled in
 * after reading a document and is not written back as a field.
 */
data class City(
    @get:Exclude val id: String = "",
    val name: String = "",
    val province: String = ""
)
