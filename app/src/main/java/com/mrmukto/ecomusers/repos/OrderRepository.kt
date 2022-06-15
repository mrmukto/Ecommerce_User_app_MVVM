package com.mrmukto.ecomusers.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mrmukto.ecomusers.models.OrderSettings
import com.mrmukto.ecomusers.utils.collectionOrderSettings
import com.mrmukto.ecomusers.utils.documentOrderSettings
import com.google.firebase.firestore.FirebaseFirestore

class OrderRepository {
    private val db = FirebaseFirestore.getInstance()

    fun getOrderSettings() : LiveData<OrderSettings> {
        val settingsLD = MutableLiveData<OrderSettings>()
        db.collection(collectionOrderSettings)
            .document(documentOrderSettings)
            .addSnapshotListener { value, error ->
                if (error != null ){
                    return@addSnapshotListener
                }

                settingsLD.value = value!!.toObject(OrderSettings::class.java)
            }
        return settingsLD
    }
}