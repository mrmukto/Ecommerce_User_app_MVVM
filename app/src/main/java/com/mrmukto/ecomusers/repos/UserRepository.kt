package com.mrmukto.ecomusers.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mrmukto.ecomusers.models.CartItem
import com.mrmukto.ecomusers.models.EcomUser
import com.mrmukto.ecomusers.utils.collectionCart
import com.mrmukto.ecomusers.utils.collectionUser
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    fun addNewUser(ecomUser: EcomUser) {
        db.collection(collectionUser).document(ecomUser.userId!!)
            .set(ecomUser)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }

    fun updateUserAddress(userId: String, address: String) {
        db.collection(collectionUser).document(userId)
            .update("userAddress", address)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }

    fun getUser(userId: String) : LiveData<EcomUser> {
        val userLD = MutableLiveData<EcomUser>()
        db.collection(collectionUser)
            .document(userId)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                userLD.value = value!!.toObject(EcomUser::class.java)
            }
        return userLD
    }

    fun addToCart(userId: String, cartItem: CartItem) {
        db.collection(collectionUser)
            .document(userId)
            .collection(collectionCart)
            .document(cartItem.productId!!)
            .set(cartItem)
            .addOnFailureListener {

            }.addOnSuccessListener {

            }
    }

    fun updateCartQuantity(userId: String, cartItem: CartItem) {
        db.collection(collectionUser)
            .document(userId)
            .collection(collectionCart)
            .document(cartItem.productId!!)
            .update("quantity", cartItem.quantity)
            .addOnFailureListener {

            }.addOnSuccessListener {

            }
    }

    fun removeFromCart(userId: String, cartItem: CartItem) {
        db.collection(collectionUser)
            .document(userId)
            .collection(collectionCart)
            .document(cartItem.productId!!)
            .delete()
            .addOnFailureListener {

            }.addOnSuccessListener {

            }
    }

    fun getAllCartItems(userId: String) : LiveData<List<CartItem>> {
        val cartLD = MutableLiveData<List<CartItem>>()
        db.collection(collectionUser)
            .document(userId)
            .collection(collectionCart)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                val tempList = mutableListOf<CartItem>()
                for (doc in value!!.documents) {
                    doc.toObject(CartItem::class.java)?.let { tempList.add(it) }
                }
                cartLD.value = tempList
            }
        return cartLD
    }

    fun updateAppExitTimeAndAvailableStatus(userId: String, time: Timestamp, status: Boolean, callback: (() -> Unit)? = null) {
        db.collection(collectionUser)
            .document(userId)
            .update(mapOf("available" to status, "lastUserAppExitTime" to time))
            .addOnSuccessListener {
                callback?.invoke()
            }.addOnFailureListener {

            }
    }

    fun updateLastSignInTimeAndAvailableStatus(userId: String, time: Timestamp, status: Boolean) {
        db.collection(collectionUser)
            .document(userId)
            .update(mapOf("available" to status, "userLastSignInTimestamp" to time))
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }

    fun updateAvailableStatus(userId: String, status: Boolean) {
        db.collection(collectionUser)
            .document(userId)
            .update("available", status)
            .addOnSuccessListener {

            }.addOnFailureListener {

            }
    }
}