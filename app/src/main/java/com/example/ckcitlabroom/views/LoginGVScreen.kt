import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginGVScreen(navController: NavHostController, giangVienViewModel: GiangVienViewModel) {
    val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)
    val cardOffset by animateDpAsState(
        targetValue = if (imeBottom > 0) 20.dp else 120.dp,
        label = "CardOffset"
    )
    val animatedElevation by animateDpAsState(
        targetValue = if (imeBottom > 0) 20.dp else 7.dp,
        label = "CardElevation"
    )

    val giangVien = giangVienViewModel.giangvien

    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val loginResult by giangVienViewModel.loginResult.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarData = remember { mutableStateOf<CustomSnackbarData?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B8DDE)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(cardOffset))

        Card(
            modifier = Modifier.width(340.dp).height(460.dp),
            colors = CardDefaults.cardColors(Color(0XFF1B8DDE)),
            elevation = CardDefaults.cardElevation(animatedElevation)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(100.dp).clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = emailState.value,
                    onValueChange = { emailState.value = it },
                    placeholder = { Text("Email hoặc Mã GV") },
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = passwordState.value,
                    onValueChange = { passwordState.value = it },
                    placeholder = { Text("Mật khẩu") },
                    singleLine = true,
                    shape = RoundedCornerShape(50),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = icon, contentDescription = "Toggle Password")
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedBorderColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier.padding(16.dp)
                ) { data ->
                    snackbarData.value?.let { customData ->
                        Snackbar(
                            containerColor = Color(0xFF1B8DDE),
                            contentColor = Color.White,
                            shape = RoundedCornerShape(12.dp),
                            action = {
                                TextButton(onClick = {
                                    snackbarData.value = null
                                }) {
                                    Text("Đóng", color = Color.White)
                                }
                            }
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (customData.type == SnackbarType.SUCCESS) Icons.Default.Info else Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = if (customData.type == SnackbarType.SUCCESS) Color.Cyan else Color.Yellow,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = customData.message)
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        giangVienViewModel.checkLogin(
                            emailState.value.trim(),
                            passwordState.value.trim()
                        )
                    },
                    shape = RoundedCornerShape(30),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Đăng nhập", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Kết quả login
        LaunchedEffect(loginResult) {
            loginResult?.let {
                if (it.result) {
                    coroutineScope.launch {
                        snackbarData.value = CustomSnackbarData(
                            message = "Đăng nhập thành công", type = SnackbarType.SUCCESS
                        )
                        snackbarHostState.showSnackbar("Thông báo")
                    }

                    giangVienViewModel.getGiangVienByMaGOrEmail(emailState.value)

                } else {
                    coroutineScope.launch {
                        snackbarData.value = CustomSnackbarData(
                            message = "Email hoặc mật khẩu không chính xác", type = SnackbarType.ERROR
                        )
                        snackbarHostState.showSnackbar("Thông báo")
                    }
                    giangVienViewModel.resetLoginResult()
                }
            }
        }

        LaunchedEffect(giangVien) {
            giangVien?.let {
               giangVienViewModel.setGV(giangVien)
                navController.navigate(NavRoute.HOME.route + "?magiangvien=${giangVien.MaGV}")
            }
        }
    }
}







