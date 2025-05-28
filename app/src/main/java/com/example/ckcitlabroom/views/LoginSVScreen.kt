import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.R

@Composable
fun LoginSVScreen(navController: NavHostController) {
    val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)

    // Trạng thái elevation của card
    val cardElevationState = remember { mutableStateOf(7.dp) }

    // Trạng thái text của từng TextField
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }

    // Animate vị trí card
    val cardOffset by animateDpAsState(
        targetValue = if (imeBottom > 0) 20.dp else (LocalConfiguration.current.screenHeightDp.dp / 5),
        label = "CardOffset"
    )

    // Animate elevation theo trạng thái
    val animatedCardElevation by animateDpAsState(
        targetValue = cardElevationState.value,
        label = "CardElevation"
    )

    // Cập nhật trạng thái elevation khi bàn phím mở hoặc tắt
    LaunchedEffect(imeBottom) {
        if (imeBottom > 0) {
            cardElevationState.value = 20.dp
        } else {
            cardElevationState.value = 7.dp
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0XFF1B8DDE)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(cardOffset))

        Card(
            modifier = Modifier
                .padding(bottom = 150.dp)
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
                        .shadow(elevation = 7.dp, shape = RoundedCornerShape(50.dp))
                        .fillMaxWidth(),
                    value = emailState.value,
                    onValueChange = { emailState.value = it },
                    shape = RoundedCornerShape(50.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedTextColor = Color.Black,
                        focusedTextColor = Color.Black
                    ),
                    placeholder = { Text("Email/MSSV") },
                )

                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .shadow(elevation = 7.dp, shape = RoundedCornerShape(50.dp))
                        .fillMaxWidth(),
                    value = passwordState.value,
                    onValueChange = { passwordState.value = it },
                    shape = RoundedCornerShape(50.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedTextColor = Color.Black,
                        focusedTextColor = Color.Black
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    placeholder = { Text("Mật Khẩu") },
                )

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 7.dp, shape = RoundedCornerShape(50.dp))
                        .height(53.dp),
                    onClick = {
                        // Xử lý login tại đây với emailState.value và passwordState.value
                        Log.d("Login", "Email: ${emailState.value}, Password: ${passwordState.value}")
                    }
                ) {
                    Text("Đăng Nhập", fontWeight = FontWeight.ExtraBold)
                }

                TextButton(
                    modifier = Modifier.padding(8.dp),
                    onClick = {
                        navController.navigate(NavRoute.LOGINGIANGVIEN.route)
                    },
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color.Transparent, // không nền
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        "Giảng Viên Đăng nhập"
                    )
                }
            }
        }
    }
}



