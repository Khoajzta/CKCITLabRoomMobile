import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


@Composable
fun DoiMatKhauGVScreen(
    maGiangVien: String,
    giangVienViewModel: GiangVienViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current

    var giangvien = giangVienViewModel.giangvien

    LaunchedEffect(giangvien) {
        giangVienViewModel.getGiangVienByMaGOrEmail(maGiangVien)
    }

    var emailState = remember { mutableStateOf("") }
    var matkhaucuState = remember { mutableStateOf("") }
    var matkhaumoiState = remember { mutableStateOf("") }
    var matkhaumoi2State = remember { mutableStateOf("") }

    var showOldPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    fun isStrongPassword(password: String): Boolean {
        val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^&*()_+\\-=[\\]{};':\"\\\\|,.<>/?]).{8,}$")
        return regex.matches(password)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(7.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Đổi mật khẩu",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                label = { Text("Email") },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    unfocusedLabelColor = Color.Gray,
                    focusedLabelColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.Black
                )
            )

            OutlinedTextField(
                value = matkhaucuState.value,
                onValueChange = { matkhaucuState.value = it },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                label = { Text("Mật khẩu cũ") },
                visualTransformation = if (showOldPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showOldPassword = !showOldPassword }) {
                        Icon(
                            imageVector = if (showOldPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    unfocusedLabelColor = Color.Gray,
                    focusedLabelColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.Black
                )
            )

            OutlinedTextField(
                value = matkhaumoiState.value,
                onValueChange = { matkhaumoiState.value = it },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                label = { Text("Mật khẩu mới") },
                visualTransformation = if (showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showNewPassword = !showNewPassword }) {
                        Icon(
                            imageVector = if (showNewPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    unfocusedLabelColor = Color.Gray,
                    focusedLabelColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.Black
                )
            )

            OutlinedTextField(
                value = matkhaumoi2State.value,
                onValueChange = { matkhaumoi2State.value = it },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                label = { Text("Nhập lại mật khẩu mới") },
                visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                        Icon(
                            imageVector = if (showConfirmPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle password visibility"
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    unfocusedLabelColor = Color.Gray,
                    focusedLabelColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    cursorColor = Color.Black
                )
            )

            Button(
                modifier = Modifier.fillMaxWidth().height(45.dp),
                onClick = {
                    val email = emailState.value.trim()
                    val oldPassword = matkhaucuState.value
                    val newPassword = matkhaumoiState.value
                    val confirmPassword = matkhaumoi2State.value
                    val giangVien = giangVienViewModel.giangvien

                    when {
                        email.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty() -> {
                            Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                        }

                        giangVien == null -> {
                            Toast.makeText(context, "Không tìm thấy thông tin giảng viên", Toast.LENGTH_SHORT).show()
                        }

                        giangVien.Email != email -> {
                            Toast.makeText(context, "Email không khớp với tài khoản", Toast.LENGTH_SHORT).show()
                        }

                        giangVien.MatKhau != oldPassword -> {
                            Toast.makeText(context, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show()
                        }

                        newPassword != confirmPassword -> {
                            Toast.makeText(context, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show()
                        }

                        !isStrongPassword(newPassword) -> {
                            Toast.makeText(context, "Mật khẩu mới phải từ 8 ký tự, bao gồm chữ hoa, chữ thường và ký tự đặc biệt", Toast.LENGTH_LONG).show()
                        }

                        else -> {
                            val updatedGV = giangVien.copy(MatKhau = newPassword)
                            giangVienViewModel.updateGiangVien(updatedGV)
                            Toast.makeText(context, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF1B8DDE)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Đổi mật khẩu", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
