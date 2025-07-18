import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Composable
fun LoginGVScreen(
    navController: NavHostController,
    giangVienViewModel: GiangVienViewModel
) {
    val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    val offsetY by animateDpAsState(
        targetValue = if (imeVisible) (-150).dp else 0.dp,
        label = "CardOffset"
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .width(340.dp)
                    .offset(y = offsetY),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(12.dp),
                shape = RoundedCornerShape(16.dp)
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
                            val password = hashPasswordMD5(passwordState.value.trim())

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

                    GoogleLoginButtonGiangVien(giangVienViewModel, navController)

                    TextButton(
                        modifier = Modifier.padding(bottom = 16.dp),
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

    // Xử lý kết quả đăng nhập
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

    // Điều hướng sang Home nếu đăng nhập thành công
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


@Composable
fun GoogleLoginButtonGiangVien(
    giangVienViewModel: GiangVienViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    val activity = context as Activity
    val auth = FirebaseAuth.getInstance()
    val lifecycleScope = (context as ComponentActivity).lifecycleScope
    val userPreferences = remember { GiangVienPreferences(context) }
    val isLoading = remember { mutableStateOf(false) }

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken("833001661760-qrmhtiovh0s953a12n6u8hqmni8j7k52.apps.googleusercontent.com")
        .requestEmail()
        .build()

    val googleSignInClient = GoogleSignIn.getClient(context, gso)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val accountEmail = account.email
            Log.d("GoogleSignIn", "Tài khoản Google: $accountEmail")

            isLoading.value = true

            lifecycleScope.launch {
                try {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    auth.signInWithCredential(credential).await()

                    val user = auth.currentUser
                    val email = user?.email

                    if (email != null) {
                        Log.d("GiangVienLogin", "Email đăng nhập: $email")

                        if (email != null && email.endsWith("@caothang.edu.vn")) {
                            val giangvien = withContext(Dispatchers.IO) {
                                giangVienViewModel.getGiangVienByMaGOrEmailNow(email)
                            }

                            if (giangvien != null && !giangvien.MaGV.isNullOrEmpty()) {
                                Log.d("GiangVienLogin", "Giảng viên: $giangvien")

                                val token = FirebaseMessaging.getInstance().token.await()
                                val giangvienWithToken = giangvien.copy(Token = token)

                                giangVienViewModel.setToken(token)
                                giangVienViewModel.updateToken(giangvien.MaGV, token)

                                userPreferences.saveLoginForGiangVien(giangvienWithToken)
                                giangVienViewModel.setGV(giangvienWithToken)

                                isLoading.value = false
                                delay(300)

                                navController.navigate(NavRoute.HOME.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            } else {
                                // Không phải email giảng viên
                                isLoading.value = false
                                Log.e(
                                    "GiangVienLogin",
                                    "Không tìm thấy giảng viên hợp lệ với email $email hoặc MaGV null"
                                )
                                Toast.makeText(
                                    context,
                                    "Tài khoản này không phải giảng viên",
                                    Toast.LENGTH_SHORT
                                ).show()
                                auth.signOut()
                                googleSignInClient.signOut()
                            }
                        } else {
                            // Email không hợp lệ domain
                            isLoading.value = false
                            Log.e("GiangVienLogin", "Email không hợp lệ: $email")
                            Toast.makeText(
                                context,
                                "Vui lòng đăng nhập bằng email @caothang.edu.vn",
                                Toast.LENGTH_SHORT
                            ).show()
                            auth.signOut()
                            googleSignInClient.signOut()
                        }

                    } else {
                        isLoading.value = false
                        Log.e("GiangVienLogin", "Email null sau khi đăng nhập Google")
                        Toast.makeText(
                            context,
                            "Không lấy được email từ tài khoản Google",
                            Toast.LENGTH_SHORT
                        ).show()
                        auth.signOut()
                        googleSignInClient.signOut()
                    }

                } catch (e: Exception) {
                    isLoading.value = false
                    Log.e("GoogleSignIn", "Lỗi đăng nhập: $e")
                    Toast.makeText(context, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
                    auth.signOut()
                    googleSignInClient.signOut()
                }
            }

        } catch (e: Exception) {
            isLoading.value = false
            Log.e("GoogleSignIn", "Lỗi lấy tài khoản Google: $e")
            Toast.makeText(context, "Đăng nhập Google thất bại", Toast.LENGTH_SHORT).show()
        }
    }

    Button(
        onClick = {
            isLoading.value = true
            googleSignInClient.signOut().addOnCompleteListener {
                launcher.launch(googleSignInClient.signInIntent)
            }
        },
        modifier = Modifier
            .padding(start = 24.dp, end = 24.dp, top = 12.dp)
            .height(50.dp)
            .shadow(6.dp, RoundedCornerShape(12.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google Logo",
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp)
            )
            Text(
                text = "Đăng nhập với Google",
                color = Color(0xFF555555),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }

    if (isLoading.value) {
        Dialog(onDismissRequest = { }) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.White, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFF1B8DDE),
                    strokeWidth = 4.dp
                )
            }
        }
    }
}








