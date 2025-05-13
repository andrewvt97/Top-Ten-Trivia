package com.example.toptentrivia

import android.os.Build
import android.text.Html
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.toptentrivia.ui.navigation.TriviaNavHost

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TopTenTriviaApp() {
    val navController = rememberNavController()
    TriviaNavHost(navController = navController)
}

