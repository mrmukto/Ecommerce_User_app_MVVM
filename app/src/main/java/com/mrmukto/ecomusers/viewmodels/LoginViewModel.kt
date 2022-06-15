package com.mrmukto.ecomusers.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mrmukto.ecomusers.models.EcomUser
import com.mrmukto.ecomusers.repos.UserRepository
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class LoginViewModel : ViewModel() {
    enum class Authentication {
        AUTHENTICATED, UNAUTHENTICATED
    }
    val authLD: MutableLiveData<Authentication> = MutableLiveData()
    val errMsgLD: MutableLiveData<String> = MutableLiveData()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val userRepository = UserRepository()
    init {
        if (auth.currentUser != null) {
            authLD.value = Authentication.AUTHENTICATED
        } else {
            authLD.value = Authentication.UNAUTHENTICATED
        }
    }

    fun loginUser(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                userRepository.updateLastSignInTimeAndAvailableStatus(
                    auth.currentUser!!.uid,
                    time = Timestamp(Date(auth.currentUser!!.metadata!!.lastSignInTimestamp)),
                    status = true
                )
                authLD.value = Authentication.AUTHENTICATED
            }.addOnFailureListener {
                errMsgLD.value = it.localizedMessage
            }
    }
    fun registerUser(email: String, pass: String) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnSuccessListener {
                val ecomUser = EcomUser(
                    userId = it.user?.uid,
                    userEmail = it.user?.email,
                    userCreationDate = Timestamp(Date(it.user?.metadata?.creationTimestamp!!)),
                    userLastSignInTimestamp = Timestamp(Date(it.user?.metadata?.lastSignInTimestamp!!)),
                )
                userRepository.addNewUser(ecomUser)
                authLD.value = Authentication.AUTHENTICATED
            }.addOnFailureListener {
                errMsgLD.value = it.localizedMessage
            }
    }

    fun updateAppExitTimeAndAvailableStatus(status: Boolean, time: Timestamp) {
        userRepository.updateAppExitTimeAndAvailableStatus(
            userId = auth.currentUser!!.uid,
            status = status,
            time = time
        )
    }

    fun updateAvailableStatus(status: Boolean) {
        userRepository.updateAvailableStatus(
            userId = auth.currentUser!!.uid,
            status = status
        )
    }

    fun logout() {
        userRepository.updateAppExitTimeAndAvailableStatus(
            userId = auth.currentUser!!.uid,
            time = Timestamp(Date(System.currentTimeMillis())),
            status = false
        ) {
            auth.signOut()
            authLD.value = Authentication.UNAUTHENTICATED
        }
    }
}