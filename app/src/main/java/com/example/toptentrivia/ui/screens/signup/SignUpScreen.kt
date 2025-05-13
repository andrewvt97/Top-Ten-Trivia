package com.example.toptentrivia.ui.screens.signup


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.toptentrivia.R
import com.example.toptentrivia.ui.AppViewModelProvider
import com.example.toptentrivia.ui.navigation.NavigationDestination
import com.example.toptentrivia.ui.screens.UserViewModel
import com.example.toptentrivia.ui.theme.TopTenTriviaTheme

object SignUpDestination : NavigationDestination {
    override val route = "signup"
    override val titleRes = R.string.signup_title
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SignUpScreen(
    navigateToLogin: () -> Unit,
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel,
    viewModel: SignUpViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.signUpUiState.collectAsState()


    LaunchedEffect(uiState.value.signUpSuccessful) {
        if (uiState.value.signUpSuccessful) {
            userViewModel.loadUser(uiState.value.username)
            navigateToHome()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Top Ten Trivia",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.value.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Enter Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.value.username,
            onValueChange = { viewModel.onUsernameChange(it) },
            label = { Text("Enter Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.value.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Enter Password") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.signUp() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.value.isLoading
        ) {
            Text("Sign Up")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Already have an account?")
            TextButton(onClick = navigateToLogin) {
                Text("Sign in!")
            }
        }

        if (uiState.value.errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = uiState.value.errorMessage ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun SignUpScreenPreview() {
//    TopTenTriviaTheme {
//        SignUpScreen(navigateToLogin = {})
//    }
//}
