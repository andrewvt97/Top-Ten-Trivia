package com.example.toptentrivia.ui.screens.logout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.toptentrivia.R
import com.example.toptentrivia.data.model.User
import com.example.toptentrivia.ui.navigation.NavigationDestination
import com.example.toptentrivia.ui.screens.UserViewModel
import com.example.toptentrivia.ui.screens.navbar.BottomNavBar

object LogoutDestination : NavigationDestination {
    override val route = "logout"
    override val titleRes = R.string.login_title
}


@Composable
fun LogoutScreen(
    userViewModel: UserViewModel,
    navigateToLogin: () -> Unit,
) {
    val user = userViewModel.user.collectAsState().value ?: return


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar()
        Spacer(modifier = Modifier.height(24.dp))
        Icon(
            painter = painterResource(id = R.drawable.profile_picture),
            contentDescription = "Profile Icon",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFF0A2742).copy(alpha = 0.2f))
                .padding(12.dp),
            tint = Color(0xFF0A2742)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = user.username,
            fontSize = 18.sp,
            color = Color(0xFF0A2742)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                userViewModel.clearUser()
                navigateToLogin()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A2742)),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(48.dp)
        ) {
            Text("Logout", color = Color.White, fontSize = 16.sp)
        }
    }


}

@Composable
private fun TopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0A2742))
            .padding(16.dp)
    ) {
        Text(
            text = "Top Ten Trivia",
            color = Color.White,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
