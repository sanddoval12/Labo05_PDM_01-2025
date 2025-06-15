package com.falconteam.laboratorio_5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
//import com.falconteam.laboratorio_5.data.database.PostDatabase
import com.falconteam.laboratorio_5.navigation.RootNavigation
import com.falconteam.laboratorio_5.screens.auth.AuthViewModel
import com.falconteam.laboratorio_5.ui.theme.Laboratorio5Theme
import com.falconteam.laboratorio_5.utils.BlogsitoViewModel
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    private val blogViewModel = BlogsitoViewModel()
    private val authViewModel = AuthViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            Laboratorio5Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    blogViewModel.connectWSS(Random.nextInt(1000, 9999).toString())
                    RootNavigation(
                        blogViewModel = blogViewModel,
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}