package com.example.ckcitlabroom.views.SinhVien

import CustomSnackbarData
import SinhVien
import SinhVienViewModel
import SnackbarType
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import android.app.DatePickerDialog
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.ckcitlabroom.viewmodels.LopHocViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSinhVienScreen(
    sinhVienViewModel: SinhVienViewModel,
    lopHocViewModel: LopHocViewModel,
    masv: String
) {
    val sinhVien = sinhVienViewModel.sinhvien

    val maSVState = remember { mutableStateOf("") }
    val tenSVState = remember { mutableStateOf("") }
    val ngaySinhState = remember { mutableStateOf("") }
    val gioiTinhState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val maLopState = remember { mutableStateOf("") }  // Thêm mã lớp

    var gioiTinhExpanded by remember { mutableStateOf(false) }
    val gioiTinhOptions = listOf("Nam", "Nữ", "Khác")

    var lopExpanded by remember { mutableStateOf(false) }
    val lopOptions = lopHocViewModel.danhSachAllLopHoc.map { it.MaLopHoc } // Danh sách mã lớp

    var showDatePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarData = remember { mutableStateOf<CustomSnackbarData?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(masv) {
        sinhVienViewModel.getSinhVienByMaSV(masv)
    }

    LaunchedEffect(sinhVien) {
        sinhVien?.let {
            maSVState.value = it.MaSinhVien
            tenSVState.value = it.TenSinhVien
            val parts = it.NgaySinh.split("-")
            if (parts.size == 3) {
                ngaySinhState.value = "${parts[2]}-${parts[1]}-${parts[0]}"
            }
            gioiTinhState.value = it.GioiTinh
            emailState.value = it.Email
            maLopState.value = it.MaLop ?: ""  // Khởi tạo mã lớp
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            sinhVienViewModel.stopPollingSinhVien()
        }
    }

    if (showDatePicker) {
        val parts = ngaySinhState.value.split("-")
        val day = parts.getOrNull(0)?.toIntOrNull() ?: calendar.get(Calendar.DAY_OF_MONTH)
        val month = parts.getOrNull(1)?.toIntOrNull()?.minus(1) ?: calendar.get(Calendar.MONTH)
        val year = parts.getOrNull(2)?.toIntOrNull() ?: calendar.get(Calendar.YEAR)

        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                ngaySinhState.value = "${selectedDay.toString().padStart(2, '0')}-${(selectedMonth + 1).toString().padStart(2, '0')}-$selectedYear"
                showDatePicker = false
            },
            year,
            month,
            day
        ).show()
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(650.dp),
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
                Text("Chỉnh Sửa Thông Tin Sinh Viên", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (sinhVien != null) {
                    item {
                        Text("Mã Sinh Viên", color = Color.Black, fontWeight = FontWeight.Bold)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(Color.White)
                                .border(1.dp, Color.Black, shape = RoundedCornerShape(12.dp)),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(maSVState.value, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                            }
                        }
                    }

                    item {
                        Text("Tên Sinh Viên", color = Color.Black, fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            value = tenSVState.value,
                            onValueChange = { tenSVState.value = it },
                            shape = RoundedCornerShape(12.dp),
                            placeholder = { Text("Nhập tên sinh viên") },
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

                    item {
                        Text("Ngày Sinh", color = Color.Black, fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = ngaySinhState.value,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            trailingIcon = {
                                IconButton(onClick = { showDatePicker = true }) {
                                    Icon(Icons.Default.DateRange, contentDescription = "Chọn ngày")
                                }
                            },
                            placeholder = { Text("Chọn ngày sinh") },
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

                    item {
                        Text("Giới Tính", color = Color.Black, fontWeight = FontWeight.Bold)
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
                                gioiTinhOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option) },
                                        onClick = {
                                            gioiTinhState.value = option
                                            gioiTinhExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Thêm phần chọn Mã Lớp
                    item {
                        Text("Mã Lớp", color = Color.Black, fontWeight = FontWeight.Bold)
                        ExposedDropdownMenuBox(
                            expanded = lopExpanded,
                            onExpandedChange = { lopExpanded = !lopExpanded }
                        ) {
                            OutlinedTextField(
                                value = maLopState.value,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = lopExpanded)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .padding(bottom = 12.dp),
                                placeholder = { Text("Chọn mã lớp") },
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
                                expanded = lopExpanded,
                                onDismissRequest = { lopExpanded = false }
                            ) {
                                lopOptions.forEach { maLop ->
                                    DropdownMenuItem(
                                        text = { Text(maLop) },
                                        onClick = {
                                            maLopState.value = maLop
                                            lopExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Text("Email", color = Color.Black, fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            value = emailState.value,
                            onValueChange = { emailState.value = it },
                            shape = RoundedCornerShape(12.dp),
                            placeholder = { Text("Nhập email") },
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
                } else {
                    item {
                        Text("Không tìm thấy sinh viên!", color = Color.Red)
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
                    if (tenSVState.value.isBlank() || ngaySinhState.value.isBlank() ||
                        gioiTinhState.value.isBlank() || emailState.value.isBlank() || maLopState.value.isBlank()
                    ) {
                        coroutineScope.launch {
                            snackbarData.value = CustomSnackbarData(
                                message = "Vui lòng nhập đầy đủ thông tin!",
                                type = SnackbarType.ERROR
                            )
                            snackbarHostState.showSnackbar("Thông báo")
                        }
                    } else {
                        val parts = ngaySinhState.value.split("-")
                        val ngaySinhDB = if (parts.size == 3) "${parts[2]}-${parts[1]}-${parts[0]}" else ""

                        val svUpdate = SinhVien(
                            MaSinhVien = maSVState.value,
                            TenSinhVien = tenSVState.value,
                            NgaySinh = ngaySinhDB,
                            GioiTinh = gioiTinhState.value,
                            Email = emailState.value,
                            MaLop = maLopState.value, // Cập nhật mã lớp
                            MatKhau = sinhVien?.MatKhau ?: "",
                            MaLoaiTaiKhoan = sinhVien?.MaLoaiTaiKhoan ?: 3,
                            TrangThai = sinhVien?.TrangThai ?: 1
                        )

                        sinhVienViewModel.updateSinhVien(svUpdate)
                        coroutineScope.launch {
                            snackbarData.value = CustomSnackbarData(
                                message = "Cập nhật sinh viên thành công!",
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
                Text("Cập Nhật", color = Color.White)
            }
        }
    }
}



