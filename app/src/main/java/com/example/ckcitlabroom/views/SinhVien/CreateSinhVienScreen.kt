package com.example.ckcitlabroom.views.SinhVien

import CustomSnackbarData
import NavRoute
import SinhVien
import SinhVienViewModel
import android.icu.util.Calendar
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.viewmodels.LopHocViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSinhVienScreen(
    navController: NavHostController,
    sinhVienViewModel: SinhVienViewModel,
    lopHocViewModel: LopHocViewModel
) {
    val danhSachSinhVien = sinhVienViewModel.danhSachAllSinhVien
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarData = remember { mutableStateOf<CustomSnackbarData?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val maSVState = remember { mutableStateOf("") }
    val tenSVState = remember { mutableStateOf("") }
    val ngaySinhHienThi = remember { mutableStateOf("") } // hiển thị "dd-MM-yyyy"
    val ngaySinhDb = remember { mutableStateOf("") }
    val gioiTinhState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val matKhauState = remember { mutableStateOf("") }
    val maLopState = remember { mutableStateOf("") }

    var lopExpanded by remember { mutableStateOf(false) }
    val lopOptions = lopHocViewModel.danhSachAllLopHoc.map { it.MaLopHoc }

    var gioiTinhExpanded by remember { mutableStateOf(false) }
    val gioiTinhOptions = listOf("Nam", "Nữ", "Khác")

    LaunchedEffect(Unit) {
        sinhVienViewModel.getAllSinhVien()
        lopHocViewModel.getAllLopHoc()
    }
    DisposableEffect(Unit) {
        onDispose {
            sinhVienViewModel.stopPollingSinhVien()
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
                val sdfHienThi = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
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
                Text("Thêm Sinh Viên", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                item {
                    Text("Mã SV", fontWeight = FontWeight.Bold, color = Color.Black)
                    OutlinedTextField(
                        value = maSVState.value,
                        onValueChange = { maSVState.value = it },
                        placeholder = { Text("Nhập mã sinh viên") },
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

                    Text("Tên Sinh Viên", fontWeight = FontWeight.Bold, color = Color.Black)
                    OutlinedTextField(
                        value = tenSVState.value,
                        onValueChange = { tenSVState.value = it },
                        placeholder = { Text("Nhập tên sinh viên") },
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
                            onDismissRequest = { gioiTinhExpanded = false },
                            containerColor = Color.White
                        ) {
                            gioiTinhOptions.forEach { selectionOption ->
                                DropdownMenuItem(
                                    text = { Text(selectionOption, color = Color.Black) },
                                    onClick = {
                                        gioiTinhState.value = selectionOption
                                        gioiTinhExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Text("Mã Lớp", fontWeight = FontWeight.Bold, color = Color.Black)
                    ExposedDropdownMenuBox(
                        expanded = lopExpanded,
                        onExpandedChange = { lopExpanded = !lopExpanded }
                    ) {
                        OutlinedTextField(
                            value = maLopState.value,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = lopExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
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
                            onDismissRequest = { lopExpanded = false },
                            containerColor = Color.White
                        ) {
                            lopOptions.forEach { maLop ->
                                DropdownMenuItem(
                                    text = { Text(maLop, color = Color.Black) },
                                    onClick = {
                                        maLopState.value = maLop
                                        lopExpanded = false
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
                }
            }

            Button(
                onClick = {
                    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@caothang\\.edu\\.vn$")
                    val passwordRegex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,}$")

                    val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val today = LocalDate.now()
                    val birthDate = try {
                        LocalDate.parse(ngaySinhDb.value, dateFormat)
                    } catch (e: Exception) {
                        null
                    }

                    if (maSVState.value.isBlank() || tenSVState.value.isBlank() ||
                        ngaySinhDb.value.isBlank() || gioiTinhState.value.isBlank() ||
                        emailState.value.isBlank()
                    ) {
                        Toast.makeText(
                            context,
                            "Vui lòng điền đầy đủ thông tin",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (birthDate == null) {
                        Toast.makeText(context, "Ngày sinh không hợp lệ", Toast.LENGTH_SHORT).show()
                    } else if (today.year - birthDate.year < 18) {
                        Toast.makeText(context, "Sinh viên phải đủ 18 tuổi", Toast.LENGTH_SHORT)
                            .show()
                    } else if (!emailRegex.matches(emailState.value)) {
                        Toast.makeText(
                            context,
                            "Email phải có định dạng @caothang.edu.vn",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val daTonTai = danhSachSinhVien.any { it.MaSinhVien == maSVState.value }
                        if (daTonTai) {
                            Toast.makeText(context, "Mã sinh viên đã tồn tại", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            val sinhVienMoi = SinhVien(
                                MaSinhVien = maSVState.value,
                                TenSinhVien = tenSVState.value,
                                NgaySinh = ngaySinhDb.value,
                                GioiTinh = gioiTinhState.value,
                                Email = emailState.value,
                                MatKhau = maSVState.value,
                                MaLop = maLopState.value,
                                MaLoaiTaiKhoan = 3,
                                TrangThai = 1
                            )
                            sinhVienViewModel.createSinhVien(sinhVienMoi)
                            Toast.makeText(context, "Thêm sinh viên thành công", Toast.LENGTH_SHORT)
                                .show()
                            navController.navigate(NavRoute.QUANLYSINHVIEN.route + "?startIndex={0}")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0XFF1B8DDE))
            ) {
                Text("Thêm sinh viên", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

