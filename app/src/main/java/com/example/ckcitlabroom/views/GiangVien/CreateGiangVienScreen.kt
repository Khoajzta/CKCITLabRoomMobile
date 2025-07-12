import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGiangVienScreen(
    navController: NavHostController,
    giangVienViewModel: GiangVienViewModel,
) {
    val danhSachGiangVien = giangVienViewModel.danhSachAllGiangVien
    val suffix = "@caothang.edu.vn"

    val suffixTransformation = remember {
        object : VisualTransformation {
            override fun filter(text: AnnotatedString): TransformedText {
                val out = AnnotatedString(text.text + suffix)
                val offsetMap = object : OffsetMapping {
                    override fun originalToTransformed(offset: Int): Int = offset
                    override fun transformedToOriginal(offset: Int): Int =
                        offset.coerceAtMost(text.text.length)
                }
                return TransformedText(out, offsetMap)
            }
        }
    }

    val maGVState = remember { mutableStateOf("") }
    val tenGVState = remember { mutableStateOf("") }
    val gioiTinhState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val matKhauState = remember { mutableStateOf("") }

    val ngaySinhHienThi = remember { mutableStateOf("") }
    val ngaySinhDb = remember { mutableStateOf("") }


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

    val coroutineScope = rememberCoroutineScope()

    // Launcher for picking Excel file
    val excelLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val data: Intent? = result.data
        val fileUri: Uri? = data?.data
        if (fileUri != null) {
            coroutineScope.launch {
                parseExcelFileAndImport(context, fileUri, giangVienViewModel)
            }
        }

    }

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
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Thêm Giảng Viên",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }

            LazyColumn(
                modifier = Modifier
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
                                .padding(bottom = 12.dp)
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

                    Text("Email", fontWeight = FontWeight.Bold, color = Color.Black)
                    OutlinedTextField(
                        value = emailState.value,
                        onValueChange = { input ->
                            emailState.value = input.substringBefore('@')
                        },
                        placeholder = { Text("Nhập email") },
                        visualTransformation = suffixTransformation,
                        singleLine = true,
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
                    val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val today = LocalDate.now()
                    val birthDate = try {
                        LocalDate.parse(ngaySinhDb.value, dateFormat)
                    } catch (e: Exception) {
                        null
                    }

                    if (maGVState.value.isBlank() || tenGVState.value.isBlank() ||
                        ngaySinhDb.value.isBlank() || gioiTinhState.value.isBlank() ||
                        emailState.value.isBlank()
                    ) {
                        Toast.makeText(
                            context,
                            "Vui lòng nhập đầy đủ thông tin!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (birthDate == null) {
                        Toast.makeText(
                            context,
                            "Ngày sinh không hợp lệ! (Định dạng yyyy-MM-dd)",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else if (today.year - birthDate.year < 22) {
                        Toast.makeText(
                            context,
                            "Giảng viên phải đủ 22 tuổi trở lên!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (!isValidEmail(emailState.value + suffix)) {
                        Toast.makeText(
                            context,
                            "Email phải có đuôi @caothang.edu.vn",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val daTonTai = danhSachGiangVien.any { it.MaGV == maGVState.value }
                        val emailDaTonTai =
                            danhSachGiangVien.any { it.Email == emailState.value + suffix }
                        if (daTonTai) {
                            Toast.makeText(
                                context,
                                "Mã giảng viên đã tồn tại!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (emailDaTonTai) {
                            Toast.makeText(
                                context,
                                "Email đã tồn tại!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            val giangVienMoi = GiangVien(
                                MaGV = maGVState.value,
                                TenGiangVien = tenGVState.value,
                                NgaySinh = ngaySinhDb.value,
                                GioiTinh = gioiTinhState.value,
                                Email = emailState.value + suffix,
                                MatKhau = hashPasswordMD5(maGVState.value),
                                MaLoaiTaiKhoan = 2,
                                TrangThai = 1
                            )
                            giangVienViewModel.createGiangVien(giangVienMoi)

                            Toast.makeText(
                                context,
                                "Thêm giảng viên thành công!",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.navigate(NavRoute.QUANLYGIANGVIEN.route + "?startIndex=0") {
                                popUpTo(NavRoute.ADDGIANGVIEN.route) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                            giangVienViewModel.getAllGiangVien()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0XFF1B8DDE))
            ) {
                Text("Thêm giảng viên", color = Color.White, fontWeight = FontWeight.Bold)
            }
            // Nút thêm từ file Excel bên dưới
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                        type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        putExtra(
                            Intent.EXTRA_MIME_TYPES, arrayOf(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                                "application/vnd.ms-excel"
                            )
                        )
                    }
                    excelLauncher.launch(intent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50))
            ) {
                Text("Thêm từ file Excel", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

