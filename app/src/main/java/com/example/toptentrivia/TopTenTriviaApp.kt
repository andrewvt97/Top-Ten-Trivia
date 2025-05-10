package com.example.toptentrivia

import android.text.Html
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.toptentrivia.ui.navigation.TriviaNavHost

@Composable
fun TopTenTriviaApp() {
    val navController = rememberNavController()
    TriviaNavHost(navController = navController)
}

// move this or get rid of this
fun decodeHtml(encoded: String): String {
    return Html.fromHtml(encoded, Html.FROM_HTML_MODE_LEGACY).toString()
}