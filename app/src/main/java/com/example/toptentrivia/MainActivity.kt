package com.example.toptentrivia


import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.toptentrivia.data.AppDatabase
import com.example.toptentrivia.data.model.User
import com.example.toptentrivia.ui.theme.TopTenTriviaTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val user = User(username = "demo", email = "demo@email.com", password = "1234")
//        CoroutineScope(Dispatchers.IO).launch {
//            val db = AppDatabase.getDatabase(applicationContext)
//            db.userDao().register(user)
//        }
        setContent {
            TopTenTriviaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = androidx.compose.material3.MaterialTheme.colorScheme.background
                ) {
                    TopTenTriviaApp()
                }
            }
        }
    }
}
