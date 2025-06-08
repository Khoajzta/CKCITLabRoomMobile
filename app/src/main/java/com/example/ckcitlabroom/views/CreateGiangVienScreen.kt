import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGiangVienScreen(
    navController: NavHostController,
    giangVienViewModel: GiangVienViewModel,
) {
    val danhSachGiangVien = giangVienViewModel.danhSachAllGiangVien
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarData = remember { mutableStateOf<CustomSnackbarData?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val maGVState = remember { mutableStateOf("") }
    val tenGVState = remember { mutableStateOf("") }
    val ngaySinhState = remember { mutableStateOf("") }
    val gioiTinhState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val matKhauState = remember { mutableStateOf("") }

    // Trong Composable:
    val ngaySinhHienThi = remember { mutableStateOf("") } // hiển thị "dd-MM-yyyy"
    val ngaySinhDb = remember { mutableStateOf("") }       // gửi về DB "yyyy-MM-dd"


    var gioiTinhExpanded by remember { mutableStateOf(false) }
    val gioiTinhOptions = listOf("Nam", "Nữ", "Khác")

    LaunchedEffect(Unit) {
        giangVienViewModel.getAllGiangVien()
    }
    DisposableEffect(Unit) {
        onDispose {
            giangVienViewModel.stopPollingGiangVien()
        }
    }

    var showDatePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    if (showDatePicker) {
        val datePickerDialog = android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)

                // Format hiển thị: dd-MM-yyyy
                val sdfHienThi = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                ngaySinhHienThi.value = sdfHienThi.format(calendar.time)

                // Format lưu DB: yyyy-MM-dd
                val sdfDb = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                ngaySinhDb.value = sdfDb.format(calendar.time)

                showDatePicker = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.setOnDismissListener { showDatePicker = false }
        datePickerDialog.show()
    }


    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(630.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Thêm Giảng Viên", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                item {
                    Text("Mã GV", fontWeight = FontWeight.Bold, color = Color.Black)
                    OutlinedTextField(
                        value = maGVState.value,
                        onValueChange = { maGVState.value = it },
                        placeholder = { Text("Nhập mã giảng viên") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
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

                    Text("Tên Giảng Viên", fontWeight = FontWeight.Bold, color = Color.Black)
                    OutlinedTextField(
                        value = tenGVState.value,
                        onValueChange = { tenGVState.value = it },
                        placeholder = { Text("Nhập tên giảng viên") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
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

                    Text("Ngày Sinh", fontWeight = FontWeight.Bold, color = Color.Black)
                    OutlinedTextField(
                        value = ngaySinhHienThi.value,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Chọn ngày sinh") },
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(Icons.Default.DateRange, contentDescription = "Chọn ngày sinh")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
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


                    Text("Giới Tính", fontWeight = FontWeight.Bold, color = Color.Black)
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
                                .menuAnchor(),
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

                    Text("Email", fontWeight = FontWeight.Bold, color = Color.Black)
                    OutlinedTextField(
                        value = emailState.value,
                        onValueChange = { emailState.value = it },
                        placeholder = { Text("Nhập email") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
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

                    Text("Mật Khẩu", fontWeight = FontWeight.Bold, color = Color.Black)
                    OutlinedTextField(
                        value = matKhauState.value,
                        onValueChange = { matKhauState.value = it },
                        placeholder = { Text("Nhập mật khẩu") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
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
                    if (maGVState.value.isBlank() || tenGVState.value.isBlank() ||
                        ngaySinhDb.value.isBlank() || gioiTinhState.value.isBlank() ||
                        emailState.value.isBlank() || matKhauState.value.isBlank()
                    ) {
                        coroutineScope.launch {
                            snackbarData.value = CustomSnackbarData(
                                message = "Vui lòng nhập đầy đủ thông tin!", type = SnackbarType.ERROR
                            )
                            snackbarHostState.showSnackbar("Thông báo")
                        }
                    } else {
                        val daTonTai = danhSachGiangVien.any { it.MaGV == maGVState.value }
                        if (daTonTai) {
                            coroutineScope.launch {
                                snackbarData.value = CustomSnackbarData(
                                    message = "Mã giảng viên đã tồn tại!", type = SnackbarType.ERROR
                                )
                                snackbarHostState.showSnackbar("Thông báo")
                            }
                        } else {
                            val giangVienMoi = GiangVien(
                                MaGV = maGVState.value,
                                TenGiangVien = tenGVState.value,
                                NgaySinh = ngaySinhDb.value,
                                GioiTinh = gioiTinhState.value,
                                Email = emailState.value,
                                MatKhau = matKhauState.value,
                                MaLoaiTaiKhoan = 2,
                                TrangThai = 1
                            )
                            giangVienViewModel.createGiangVien(giangVienMoi)

                            coroutineScope.launch {
                                snackbarData.value = CustomSnackbarData(
                                    message = "Thêm giảng viên thành công",
                                    type = SnackbarType.SUCCESS
                                )
                                snackbarHostState.showSnackbar("Thông báo")
                                delay(1000)
                                navController.popBackStack()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0XFF1B8DDE))
            ) {
                Text("Thêm giảng viên")
            }
        }
    }
}

