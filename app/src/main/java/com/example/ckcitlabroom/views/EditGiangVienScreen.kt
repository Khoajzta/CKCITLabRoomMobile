import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lapstore.viewmodels.MayTinhViewModel
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGiangVienScreen(
    giangVienViewModel: GiangVienViewModel,
    magv: String
) {
    var giangVien = giangVienViewModel.giangvien

    val maGVState = remember { mutableStateOf("") }
    val tenGVState = remember { mutableStateOf("") }
    val ngaySinhState = remember { mutableStateOf("") }
    val gioiTinhState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }

    var gioiTinhExpanded by remember { mutableStateOf(false) }
    val gioiTinhOptions = listOf("Nam", "Nữ", "Khác")

    var showDatePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarData = remember { mutableStateOf<CustomSnackbarData?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(magv) {
        giangVienViewModel.getGiangVienByMaGV(magv)
    }

    LaunchedEffect(giangVien) {
        giangVien?.let {
            maGVState.value = it.MaGV
            tenGVState.value = it.TenGiangVien
            ngaySinhState.value = it.NgaySinh
            gioiTinhState.value = it.GioiTinh
            emailState.value = it.Email
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            giangVienViewModel.stopPollingGiangVien()
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                ngaySinhState.value = "$year-${month + 1}-${dayOfMonth.toString().padStart(2, '0')}"
                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(700.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Chỉnh Sửa Thông Tin Giảng Viên", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (giangVien != null) {
                    item {
                        Text(
                            text = "Mã Giảng Viên",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(Color.White)
                                .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(12.dp)),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 10.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    giangVien.MaGV,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Tên Giảng Viên",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            value = tenGVState.value,
                            onValueChange = { tenGVState.value = it },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                focusedBorderColor = Color.Black,
                                unfocusedBorderColor = Color.Black,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            placeholder = { Text("Nhập tên giảng viên") },
                            shape = RoundedCornerShape(12.dp),
                        )
                    }

                    item {
                        Text(
                            text = "Ngày Sinh (YYYY-MM-DD)",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            value = ngaySinhState.value,
                            onValueChange = { ngaySinhState.value = it },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { showDatePicker = true }) {
                                    Icon(Icons.Default.DateRange, contentDescription = "Chọn ngày")
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                focusedBorderColor = Color.Black,
                                unfocusedBorderColor = Color.Black,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            placeholder = { Text("Nhập ngày sinh") },
                            shape = RoundedCornerShape(12.dp),
                        )
                    }

                    item {
                        Text(
                            text = "Giới Tính",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        ExposedDropdownMenuBox(
                            expanded = gioiTinhExpanded,
                            onExpandedChange = { gioiTinhExpanded = !gioiTinhExpanded }
                        ) {
                            OutlinedTextField(
                                value = gioiTinhState.value,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = gioiTinhExpanded)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .padding(bottom = 12.dp),
                                placeholder = { Text("Chọn giới tính") },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.White,
                                    focusedContainerColor = Color.White,
                                    focusedBorderColor = Color.Black,
                                    unfocusedBorderColor = Color.Black,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = gioiTinhExpanded,
                                onDismissRequest = { gioiTinhExpanded = false }
                            ) {
                                gioiTinhOptions.forEach { selectionOption ->
                                    DropdownMenuItem(
                                        text = { Text(selectionOption) },
                                        onClick = {
                                            gioiTinhState.value = selectionOption
                                            gioiTinhExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Email",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            value = emailState.value,
                            onValueChange = { emailState.value = it },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                focusedBorderColor = Color.Black,
                                unfocusedBorderColor = Color.Black,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            placeholder = { Text("Nhập email") },
                            shape = RoundedCornerShape(12.dp),
                        )
                    }

                } else {
                    item {
                        Text("Lỗi khi lấy API")
                    }
                }
            }

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
                            TextButton(onClick = { snackbarData.value = null }) {
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
                    if (tenGVState.value.isBlank() || ngaySinhState.value.isBlank() ||
                        gioiTinhState.value.isBlank() || emailState.value.isBlank()
                    ) {
                        coroutineScope.launch {
                            snackbarData.value = CustomSnackbarData(
                                message = "Vui lòng nhập đầy đủ thông tin!",
                                type = SnackbarType.ERROR
                            )
                            snackbarHostState.showSnackbar("Thông báo")
                        }
                    } else {
                        val giangVienMoi = GiangVien(
                            MaGV = maGVState.value,
                            TenGiangVien = tenGVState.value,
                            NgaySinh = ngaySinhState.value,
                            GioiTinh = gioiTinhState.value,
                            Email = emailState.value,
                            MatKhau = giangVien?.MatKhau ?: "", // lấy mật khẩu cũ
                            MaLoaiTaiKhoan = giangVien?.MaLoaiTaiKhoan ?: 2, // lấy mã loại TK cũ
                            TrangThai = giangVien?.TrangThai ?: 1
                        )
                        giangVienViewModel.updateGiangVien(giangVienMoi)
                        coroutineScope.launch {
                            snackbarData.value = CustomSnackbarData(
                                message = "Cập nhật giảng viên thành công!",
                                type = SnackbarType.SUCCESS
                            )
                            snackbarHostState.showSnackbar("Thông báo")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF1B8DDE))
            ) {
                Text("Lưu Thông Tin")
            }
        }
    }
}