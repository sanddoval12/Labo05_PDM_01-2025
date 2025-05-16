package com.pdmtaller2.DASQCINCO.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pdmtaller2.DASQCINCO.ui.theme.Labo06Theme
import com.pdmtaller2.DASQCINCO.ui.screens.BookScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Labo06Theme {
                BookScreen()
            }
        }
    }
}
