import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun DoiMatKhauSvScreen(
    maSinhVien: String,
    sinhVienViewModel: SinhVienViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current

    var emailState by remember { mutableStateOf("") }
    var matkhaucuState by remember { mutableStateOf("") }
    var matkhaumoiState by remember { mutableStateOf("") }
    var matkhaumoi2State by remember { mutableStateOf("") }

    var showPasswordOld by remember { mutableStateOf(false) }
    var showPasswordNew by remember { mutableStateOf(false) }
    var showPasswordConfirm by remember { mutableStateOf(false) }

    val sinhvien = sinhVienViewModel.sinhvien

    LaunchedEffect(maSinhVien) {
        sinhVienViewModel.getSinhVienByMaGOrEmail(maSinhVien)
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
            Text("Đổi mật khẩu", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)

            OutlinedTextField(
                value = emailState,
                onValueChange = { emailState = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
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
                value = matkhaucuState,
                onValueChange = { matkhaucuState = it },
                label = { Text("Mật khẩu cũ") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (showPasswordOld) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPasswordOld = !showPasswordOld }) {
                        Icon(
                            imageVector = if (showPasswordOld) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                },
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
                value = matkhaumoiState,
                onValueChange = { matkhaumoiState = it },
                label = { Text("Mật khẩu mới") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (showPasswordNew) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPasswordNew = !showPasswordNew }) {
                        Icon(
                            imageVector = if (showPasswordNew) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                },
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
                value = matkhaumoi2State,
                onValueChange = { matkhaumoi2State = it },
                label = { Text("Nhập lại mật khẩu mới") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (showPasswordConfirm) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPasswordConfirm = !showPasswordConfirm }) {
                        Icon(
                            imageVector = if (showPasswordConfirm) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null
                        )
                    }
                },
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
                onClick = {
                    val email = emailState.trim()
                    val oldPass = matkhaucuState
                    val newPass = matkhaumoiState
                    val confirmPass = matkhaumoi2State

                    when {
                        email.isEmpty() || oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty() -> {
                            Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                        }

                        sinhvien == null -> {
                            Toast.makeText(context, "Không tìm thấy sinh viên", Toast.LENGTH_SHORT).show()
                        }

                        sinhvien.Email != email -> {
                            Toast.makeText(context, "Email không đúng", Toast.LENGTH_SHORT).show()
                        }

                        sinhvien.MatKhau != oldPass -> {
                            Toast.makeText(context, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show()
                        }

                        newPass != confirmPass -> {
                            Toast.makeText(context, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show()
                        }

                        !newPass.matches(Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^&*()_+=-]).{8,}$")) -> {
                            Toast.makeText(context, "Mật khẩu mới phải có ít nhất 8 ký tự, gồm chữ hoa, chữ thường và ký tự đặc biệt", Toast.LENGTH_LONG).show()
                        }

                        else -> {
                            val updatedSV = sinhvien.copy(MatKhau = newPass)
                            sinhVienViewModel.updateSinhVien(updatedSV)
                            Toast.makeText(context, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(45.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF1B8DDE)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Đổi mật khẩu", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
