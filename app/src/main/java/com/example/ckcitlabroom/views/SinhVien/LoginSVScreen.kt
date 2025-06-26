import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.R
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch

@Composable
fun LoginSVScreen(
    navController: NavHostController,
    sinhVienViewModel: SinhVienViewModel
) {
    BackHandler {}

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

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarData = remember { mutableStateOf<CustomSnackbarData?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current.applicationContext
    val userPreferences = remember { SinhVienPreferences(context) }

    val loginState by userPreferences.loginStateFlow.collectAsState(initial = LoginSinhVienState())
    val loginResult by sinhVienViewModel.loginResult.collectAsState()
    val sinhvien = sinhVienViewModel.sinhvien

    val isAutoLoginChecked = remember { mutableStateOf(false) }
    val isNavigated = remember { mutableStateOf(false) }

    if (!loginState.isLoggedIn) {
        sinhVienViewModel.setSV(null)
    }

    // Tự động load SV khi đã lưu loginState
    LaunchedEffect(loginState) {
        if (loginState.isLoggedIn && loginState.maSinhVien != null && !isAutoLoginChecked.value) {
            isAutoLoginChecked.value = true
            sinhVienViewModel.getSinhVienByMaGOrEmail(loginState.maSinhVien.toString())
        }
    }

    // Hiển thị màn hình loading nếu đang tự động đăng nhập
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
                shape = RoundedCornerShape(16.dp)
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
                            coroutineScope.launch {
                                snackbarData.value = CustomSnackbarData(
                                    message = "Vui lòng nhập đầy đủ Email và Mật khẩu",
                                    type = SnackbarType.ERROR
                                )
                                snackbarHostState.showSnackbar("Thông báo")
                            }
                        } else if (email.contains("@") && !email.endsWith("@caothang.edu.vn")) {
                            coroutineScope.launch {
                                snackbarData.value = CustomSnackbarData(
                                    message = "Vui lòng sử dụng mail Cao Thắng để đăng nhập",
                                    type = SnackbarType.ERROR
                                )
                                snackbarHostState.showSnackbar("Thông báo")
                            }
                        } else {
                            sinhVienViewModel.checkLogin(email, password)
                        }
                    },
                    showSwitchAccount = true,
                    onSwitchAccountClick = {
                        navController.navigate(NavRoute.LOGINGIANGVIEN.route)
                    }
                )
            }

            // Xử lý kết quả đăng nhập
            LaunchedEffect(loginResult) {
                if (loginResult?.result == true) {
                    sinhVienViewModel.getSinhVienByMaGOrEmail(emailState.value)
                } else if (loginResult != null) {
                    Toast.makeText(context, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show()
                    sinhVienViewModel.resetLoginResult()
                }
            }

            // Xử lý thành công login và điều hướng
            LaunchedEffect(loginResult, sinhvien) {
                if (!isNavigated.value && loginResult?.result == true && sinhvien != null) {
                    if (sinhvien.TrangThai == 0) {
                        Toast.makeText(context, "Tài khoản bị khóa", Toast.LENGTH_SHORT).show()
                        sinhVienViewModel.resetLoginResult()
                    } else {
                        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                            val sinhvienWithToken = sinhvien.copy(Token = token)
                            sinhVienViewModel.setToken(token)
                            sinhVienViewModel.updateToken(sinhvien.MaSinhVien, token)

                            coroutineScope.launch {
                                userPreferences.saveLoginForSinhVien(sinhvienWithToken)

                                // ✅ Chỉ điều hướng 1 lần
                                if (!isNavigated.value) {
                                    isNavigated.value = true
                                    sinhVienViewModel.setSV(sinhvienWithToken)
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
    }
}


@Composable
fun LoginForm(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
    showSwitchAccount: Boolean = false,
    onSwitchAccountClick: (() -> Unit)? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = { Text("Email hoặc Mã số") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(7.dp, shape = RoundedCornerShape(12.dp)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                unfocusedBorderColor = Color.White,
                focusedBorderColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            placeholder = { Text("Mật khẩu") },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = "Toggle Password")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(7.dp, shape = RoundedCornerShape(12.dp)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                unfocusedBorderColor = Color.White,
                focusedBorderColor = Color.White,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onLoginClick,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1B8DDE),
                contentColor = Color.White
            )
        ) {
            Text("Đăng nhập", fontWeight = FontWeight.Bold)
        }

        if (showSwitchAccount && onSwitchAccountClick != null) {
            TextButton(
                modifier = Modifier.padding(8.dp),
                onClick = onSwitchAccountClick,
                colors = ButtonDefaults.textButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFF1B8DDE)
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Giảng viên đăng nhập")
            }
        }
    }
}







