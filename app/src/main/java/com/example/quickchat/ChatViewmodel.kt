package com.example.quickchat

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChatViewmodel : ViewModel() {
    private val userCollection  = Firebase.firestore.collection(USERS_COLLECTION)
    var userDataListner : ListenerRegistration? = null
    private val _state = MutableStateFlow(AppState())
    val state = _state.asStateFlow()
    fun resetState(){

    }

    fun onSignInResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignedIn = result.data != null,
                errorMessage = result.errorMessage,
                //userData = result.data
            )
        }


    }

    fun addUserDataToFirestore(userData: UserData) {
        val userDataMap = mapOf(
            "userId" to userData?.userId,
            "username" to userData?.username,
            "ppurl" to userData?.ppurl,
            "email" to userData?.email

        )
        val userDocument = userCollection.document(userData.userId)
        userDocument.get().addOnSuccessListener {
            if(it.exists()){
                userDocument.update(userDataMap)
            }else{
                userDocument.set(userDataMap).addOnSuccessListener {
                    Log.d(ContentValues.TAG, "User data added successfully")
                }.addOnFailureListener {
                    Log.e(ContentValues.TAG, "Error adding user data")
                }
            }
        }


    }

    fun getUserData(userId: String) {
        userDataListner = userCollection.document(userId).addSnapshotListener { value, error ->
            if (value != null) {
                _state.update {
                    it.copy(userData = value.toObject(UserData::class.java))
                }
            }
        }
    }
    fun hideDialog(){
        _state.update {
            it.copy(showDialog = false)
        }
    }
    fun showDialog(){
        _state.update {
            it.copy(showDialog = true)
        }
    }

    fun setSrEmail(email: String) {
        _state.update {
            it.copy(srEmail = email)
        }


    }

    fun addChat(srEmail: String) {


    }


}