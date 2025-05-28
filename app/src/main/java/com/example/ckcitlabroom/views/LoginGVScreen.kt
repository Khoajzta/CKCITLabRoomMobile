import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginGVScreen(navController: NavHostController, giangVienViewModel: GiangVienViewModel) {
    val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)

    var giangvien by remember { mutableStateOf<GiangVien?>(null) }

    val cardElevationState = remember { mutableStateOf(7.dp) }
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    val cardOffset by animateDpAsState(
        targetValue = if (imeBottom > 0) 20.dp else (LocalConfiguration.current.screenHeightDp.dp / 5),
        label = "CardOffset"
    )

    val animatedCardElevation by animateDpAsState(
        targetValue = cardElevationState.value,
        label = "CardElevation"
    )


    LaunchedEffect(imeBottom) {
        cardElevationState.value = if (imeBottom > 0) 20.dp else 7.dp
    }

    // Lắng nghe kết quả đăng nhập thành công
    LaunchedEffect(giangVienViewModel.giangvien) {
        giangVienViewModel.giangvien?.let { gv ->
            Log.d("LoginSuccess", "Đăng nhập thành công: $gv")
            // Hoặc điều hướng sang màn khác, lưu local, hoặc hiển thị snackbar
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0XFF1B8DDE)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(cardOffset))

        Card(
            modifier = Modifier
                .height(450.dp)
                .width(350.dp),
            colors = CardDefaults.cardColors(Color(0XFF1B8DDE)),
            elevation = CardDefaults.cardElevation(animatedCardElevation)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(25.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(40.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .shadow(7.dp, RoundedCornerShape(50.dp))
                        .fillMaxWidth(),
                    value = emailState.value,
                    onValueChange = { emailState.value = it },
                    shape = RoundedCornerShape(50.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    placeholder = { Text("Email/MaGV") },
                )

                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .shadow(7.dp, RoundedCornerShape(50.dp))
                        .fillMaxWidth(),
                    value = passwordState.value,
                    onValueChange = { passwordState.value = it },
                    shape = RoundedCornerShape(50.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    placeholder = { Text("Mật Khẩu") },
                )

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = {
                        giangVienViewModel.checkLogin(
                            emailState.value,
                            passwordState.value
                        )
                        giangvien = giangVienViewModel.giangvien


                        Log.d("LoginResult", "Đăng nhập thành công: $giangvien")

                        if(giangvien!=null){

                        }

                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            "giangvien",
                            giangvien
                        )
                        navController.navigate(NavRoute.HOME.route)

                    }
                ) {
                    Text("Đăng Nhập", fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}




