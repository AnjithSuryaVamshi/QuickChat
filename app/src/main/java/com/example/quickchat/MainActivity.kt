package com.example.quickchat

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quickchat.googlesignin.GoogleAuthUi
import com.example.quickchat.screens.ChatUI
import com.example.quickchat.screens.ChatsScreenUi
import com.example.quickchat.screens.ProfileScreenUi
import com.example.quickchat.screens.QRgenerator
import com.example.quickchat.screens.QrScannerUi
import com.example.quickchat.screens.SigninScreen
import com.example.quickchat.ui.theme.QuickChatTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val viewModel: ChatViewmodel by viewModels()
    private val googleAuthUi by lazy {
        GoogleAuthUi(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext),
            viewModel = viewModel
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuickChatTheme {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        val state by viewModel.state.collectAsState()
                        val navController = rememberNavController()
                        NavHost(navController = navController, startDestination = StartScreen) {

                            composable<StartScreen> {
                                LaunchedEffect(key1 = Unit) {
                                    val userData = googleAuthUi.getSignedInUser()
                                    if (userData != null) {
                                        viewModel.getUserData(userData.userId)
                                        viewModel.showChats(userData.userId)
                                        navController.navigate(ChatsScreen)
                                    } else {
                                        navController.navigate(SignInSc) {
                                            popUpTo(0) { inclusive = true }
                                        }

                                    }
                                }


                            }


                            composable<SignInSc> {
                                val launcher = rememberLauncherForActivityResult(
                                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                                    onResult = {
                                        if (it.resultCode == RESULT_OK) {
                                            lifecycleScope.launch {
                                                val result =
                                                    googleAuthUi.signInWithIntent(
                                                        it.data ?: return@launch
                                                    )
                                                viewModel.onSignInResult(result)

                                            }

                                        }
                                    }
                                )
                                LaunchedEffect(state.isSignedIn) {
                                    if (state.isSignedIn) {
                                        val userData = googleAuthUi.getSignedInUser()
                                        userData?.run {
                                            viewModel.addUserDataToFirestore(userData)
                                            viewModel.getUserData(userData.userId)
                                            viewModel.showChats(userData.userId)
                                            navController.navigate(ChatsScreen) {
                                                popUpTo(0) { inclusive = true } // Clears entire backstack
                                            }
                                        }
                                    }
                                }


                                SigninScreen(onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUi.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                    }

                                })
                            }
                            composable<ChatsScreen> {
                                ChatsScreenUi(viewmodel = viewModel,
                                    state = state,
                                    navController = navController,
                                    showSingleChat = { usr, id ->
                                        viewModel.getTp(id)
                                        viewModel.setChatUser(usr, id)
                                        navController.navigate(ChatScreen)

                                    },
                                    showQr = {
                                        navController.navigate(QrScreen)
                                    },
                                    showScanner = {
                                        navController.navigate(QrScannerScreen)
                                    }
                                )
                            }

                            composable<ChatScreen>(
                                enterTransition = {
                                    slideInHorizontally(initialOffsetX = {
                                        fullWidth -> fullWidth
                                    },
                                        animationSpec = tween(200)
                                        )
                                },
                                exitTransition = {
                                    slideOutHorizontally(
                                        targetOffsetX = {
                                            fullWidth -> -fullWidth
                                        },
                                        animationSpec = tween(200)
                                    )
                                }
                            ) {

                                //val user = state.user2 ?: return@composable
                                ChatUI(
                                    viewmodel = viewModel,
                                    navController = navController,
                                    messages = viewModel.messages,
                                    userData = state.user2!!,
                                    chatId = state.chatId,
                                    state = state,
                                    //messages = viewModel.messages,
                                    onBack = {
                                        navController.popBackStack()
                                    }
                                )
                            }
                            composable<QrScreen> {
                                QRgenerator(navController = navController,viewmodel = viewModel)
                            }

                            composable<QrScannerScreen> {
                                QrScannerUi(navController = navController, viewmodel = viewModel)
                            }
                            composable<ProfileScreen> {
                                ProfileScreenUi(
                                    viewModel,
                                    state,
                                    navController
                                ) {
                                    lifecycleScope.launch {
                                        googleAuthUi.signOut()
                                        Toast.makeText(
                                            applicationContext,
                                            "SignOut Successful",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.navigate(SignInSc) {
                                            popUpTo(0) { inclusive = true }
                                            launchSingleTop = true
                                        }

                                    }
                                }
                            }

                        }

                    }

                }
            }
        }
    }
}
