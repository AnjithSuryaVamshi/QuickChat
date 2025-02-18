package com.example.quickchat

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChatViewmodel : ViewModel() {
    private val userCollection  = Firebase.firestore.collection(USERS_COLLECTION)
    var userDataListner : ListenerRegistration? = null
    var chatDataListner : ListenerRegistration? = null
    private val _state = MutableStateFlow(AppState())
    val state = _state.asStateFlow()
    var chats by mutableStateOf<List<ChatData>>(emptyList())
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

    fun addChat(email: String) {
        Firebase.firestore.collection(CHAT_COLLECTION).where(
            Filter.or(
                Filter.and(
                    Filter.equalTo("user1.email", _state.value.userData?.email),
                    Filter.equalTo("user2.email", email)
                ),
                Filter.and(
                    Filter.equalTo("user1.email", email),
                    Filter.equalTo("user2.email", _state.value.userData?.email)
                )
            )
        ).get().addOnSuccessListener { querySnapshot ->
            if (querySnapshot.isEmpty) {
                userCollection.whereEqualTo("email", email).get().addOnSuccessListener { userQuerySnapshot ->
                    if (userQuerySnapshot.isEmpty) {
                        println("Failed to find user with email: $email")
                    } else {
                        val chatPartner = userQuerySnapshot.toObjects(UserData::class.java).firstOrNull()
                        if (chatPartner != null) {
                            println("Found chat partner: ${chatPartner.username}")
                            val id = Firebase.firestore.collection(CHAT_COLLECTION).document().id
                            val chat = ChatData(
                                chatId = id,
                                last = Message(
                                    senderId = "",
                                    content = "",
                                    time = null,
                                ),
                                user1 = ChatUserData(
                                    userId = _state.value.userData?.userId.toString(),
                                    username = _state.value.userData?.username.toString(),
                                    ppurl = _state.value.userData?.ppurl.toString(),
                                    email = _state.value.userData?.email.toString(),
                                    typing = false,
                                    unread = 0
                                ),
                                user2 = ChatUserData(
                                    username = chatPartner.username.toString(),
                                    typing = false,
                                    unread = 0,
                                    ppurl = chatPartner.ppurl.toString(),
                                    userId = chatPartner.userId.toString(),
                                    email = chatPartner.email.toString()
                                )
                            )
                            Firebase.firestore.collection(CHAT_COLLECTION).document(id).set(chat)
                                .addOnSuccessListener {
                                    println("Chat created successfully")
                                }
                                .addOnFailureListener { e ->
                                    println("Failed to create chat: ${e.message}")
                                }
                        } else {
                            println("Chat Partner is null!")
                        }
                    }
                }
            } else {
                println("Chat already exists")
            }
        }.addOnFailureListener { e ->
            println("Failed to query chats: ${e.message}")
        }
    }
    fun showChats(userId: String) {
        chatDataListner = Firebase.firestore.collection(CHAT_COLLECTION).where(
            Filter.or(
                Filter.equalTo("user1.userId", userId),
                Filter.equalTo("user2.userId", userId)
            )
        ).addSnapshotListener { value, error ->
            if (value != null) {
                chats = value.documents.mapNotNull {
                    it.toObject<ChatData>()
                }.sortedBy {
                    it.last?.time
                }.reversed()
            }
        }
    }


}