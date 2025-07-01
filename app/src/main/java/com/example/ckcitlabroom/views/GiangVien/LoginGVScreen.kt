import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch

@Composable
fun LoginGVScreen(
    navController: NavHostController,
    giangVienViewModel: GiangVienViewModel
) {
    val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)
    val cardOffset by animateDpAsState(
        targetValue = if (imeBottom > 0) 20.dp else 120.dp,
        label = "CardOffset"
    )
    val animatedElevation by animateDpAsState(
        targetValue = if (imeBottom > 0) 20.dp else 7.dp,
        label = "CardElevation"
    )

    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current.applicationContext
    val userPreferences = remember { GiangVienPreferences(context) }
    val loginState by userPreferences.loginStateFlow.collectAsState(initial = LoginGiangVienState())
    val loginResult by giangVienViewModel.loginResult.collectAsState()
    val giangVien = giangVienViewModel.giangvien

    val isAutoLoginChecked = remember { mutableStateOf(false) }
    val isNavigated = remember { mutableStateOf(false) }

    if (!loginState.isLoggedIn) {
        giangVienViewModel.setGV(null)
    }

    LaunchedEffect(loginState) {
        if (loginState.isLoggedIn && loginState.maGiangVien != null && !isAutoLoginChecked.value) {
            isAutoLoginChecked.value = true
            giangVienViewModel.getGiangVienByMaGOrEmail(loginState.maGiangVien.toString())
        }
    }

    if (loginState.isLoggedIn && !isAutoLoginChecked.value) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            DotLoading()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(cardOffset))

            Card(
                modifier = Modifier
                    .width(340.dp)
                    .height(460.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(animatedElevation),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    LoginForm(
                        email = emailState.value,
                        onEmailChange = { emailState.value = it },
                        password = passwordState.value,
                        onPasswordChange = { passwordState.value = it },
                        onLoginClick = {
                            val email = emailState.value.trim()
                            val password = passwordState.value.trim()

                            if (email.isEmpty() || password.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "Vui lòng nhập đầy đủ thông tin đăng nhập",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else if (email.contains("@") && !email.endsWith("@caothang.edu.vn")) {
                                Toast.makeText(
                                    context,
                                    "Vui lòng sử dụng email caothang.edu.vn",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                giangVienViewModel.checkLogin(email, password)
                            }
                        },
                    )
                    TextButton(
                        modifier = Modifier.padding(bottom = 20.dp),
                        onClick = { navController.navigate(NavRoute.LOGINSINHVIEN.route) },
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color(0xFF1B8DDE)
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Sinh viên đăng nhập")
                    }
                }
            }
        }
    }

    LaunchedEffect(loginResult) {
        loginResult?.let {
            if (it.result) {
                giangVienViewModel.getGiangVienByMaGOrEmail(emailState.value)
            } else {
                Toast.makeText(context, "Email hoặc mật khẩu không chính xác", Toast.LENGTH_SHORT)
                    .show()
                giangVienViewModel.resetLoginResult()
            }
        }
    }

    LaunchedEffect(loginResult, giangVien) {
        if (!isNavigated.value && loginResult?.result == true && giangVien != null) {
            if (giangVien.TrangThai == 0) {
                Toast.makeText(context, "Tài khoản của bạn đã bị khóa", Toast.LENGTH_SHORT).show()
                giangVienViewModel.resetLoginResult()
            } else {
                FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                    val giangVienWithToken = giangVien.copy(Token = token)
                    giangVienViewModel.setToken(token)
                    giangVienViewModel.updateToken(giangVien.MaGV, token)

                    coroutineScope.launch {
                        userPreferences.saveLoginForGiangVien(giangVienWithToken)

                        if (!isNavigated.value) {
                            isNavigated.value = true
                            giangVienViewModel.setGV(giangVienWithToken)
                            navController.navigate(NavRoute.HOME.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                }
            }
        }
    }
}







