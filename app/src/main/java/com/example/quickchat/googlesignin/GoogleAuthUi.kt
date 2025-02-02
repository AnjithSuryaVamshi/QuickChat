package com.example.quickchat.googlesignin

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import com.example.quickchat.ChatViewmodel
import com.example.quickchat.SignInResult
import com.example.quickchat.UserData
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class GoogleAuthUi(
    context: Context,
    private val oneTapClient: SignInClient,
    val viewModel: ChatViewmodel
) {
    private val auth = Firebase.auth
    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null

        }
        return result?.pendingIntent?.intentSender
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(
            GoogleIdTokenRequestOptions.builder().setSupported(true)
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("502124261251-f8967lbvl0g3tsmv7rbe20v8bvkjjcrn.apps.googleusercontent.com")
                .build()
        ).setAutoSelectEnabled(true).build()

    }

    suspend fun signInWithIntent(intent: Intent): SignInResult {
        viewModel.resetState()
        val cred = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleTokenId = cred.googleIdToken
        val googleCred = GoogleAuthProvider.getCredential(googleTokenId, null)
        return try {
            val user = auth.signInWithCredential(googleCred).await().user
            SignInResult(
                errorMessage = null,
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        ppurl = photoUrl.toString(),
                        email = email.toString()
                    )


                }
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                errorMessage = e.message,
                data = null
            )
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            ppurl = photoUrl.toString(),
            email = email.toString()
        )
    }



}