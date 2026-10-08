package com.example.listycity

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

/**
 * Keeps the city list in sync with the Firestore "cities" collection.
 *
 * Writes (add, update, delete) go straight to Firestore. The snapshot
 * listener then receives the change and rebuilds the local list, so the
 * UI always shows what is actually in the database, including after the
 * app is restarted.
 */
class CityRepository {
    private val db = Firebase.firestore
    private val citiesRef = db.collection("cities")

    private val _cities = mutableStateListOf<City>()

    val cities: List<City>
        get() = _cities

    init {
        citiesRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e(TAG, "Listening to cities failed", error)
                return@addSnapshotListener
            }
            _cities.clear()
            snapshot?.documents?.forEach { doc ->
                doc.toObject(City::class.java)
                    ?.copy(id = doc.id)
                    ?.let { _cities.add(it) }
            }
        }
    }

    fun addCity(city: City) {
        citiesRef.add(city)
            .addOnFailureListener { Log.e(TAG, "Adding city failed", it) }
    }

    fun updateCity(city: City) {
        if (city.id.isNotEmpty()) {
            citiesRef.document(city.id).set(city)
                .addOnFailureListener { Log.e(TAG, "Updating city failed", it) }
        }
    }

    fun deleteCity(city: City) {
        if (city.id.isNotEmpty()) {
            citiesRef.document(city.id).delete()
                .addOnFailureListener { Log.e(TAG, "Deleting city failed", it) }
        }
    }

    private companion object {
        const val TAG = "CityRepository"
    }
}
