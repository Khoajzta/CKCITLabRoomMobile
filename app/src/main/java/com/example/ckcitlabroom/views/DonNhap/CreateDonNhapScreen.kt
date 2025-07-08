import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.viewmodels.DonNhapViewModel
import com.example.ckcitlabroom.viewmodels.MayTinhViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CreateDonNhapScreen(
    navController: NavHostController,
    mayTinhViewModel: MayTinhViewModel,
    phongMayViewModel: PhongMayViewModel,
    donNhapyViewModel: DonNhapViewModel,
) {

    var maDonNhapNew = remember { mutableStateOf("") }

    val isSuccess = remember { mutableStateOf(false) }   // ← NEW

    val loadingState = remember { mutableStateOf(false) }

    var phonkho = phongMayViewModel.phongmay

    val openDialog = remember { mutableStateOf(false) }
    val dialogMessage = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    var showDatePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    LaunchedEffect(Unit) {
        mayTinhViewModel.getAllMayTinh()
        phongMayViewModel.getPhongMayByMaPhong("KHOLUUTRU")
    }

    val soluongState = remember { mutableStateOf("") }
    val nhacungcapState = remember { mutableStateOf("") }
    val ngayNhapState = remember { mutableStateOf("") }
    val mainState = remember { mutableStateOf("") }
    val cpuState = remember { mutableStateOf("") }
    val ramState = remember { mutableStateOf("") }
    val vgaState = remember { mutableStateOf("") }
    val manHinhState = remember { mutableStateOf("") }
    val banPhimState = remember { mutableStateOf("") }
    val chuotState = remember { mutableStateOf("") }
    val hddState = remember { mutableStateOf("") }
    val ssdState = remember { mutableStateOf("") }

    if (showDatePicker) {
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                ngayNhapState.value = sdf.format(calendar.time)
                showDatePicker = false // Reset khi chọn xong
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Xử lý khi người dùng đóng dialog mà không chọn gì
        datePickerDialog.setOnDismissListener {
            showDatePicker = false
        }

        datePickerDialog.show()
    }

    val density = LocalDensity.current
    val ime = WindowInsets.ime

    val imeBottomPx by remember {             // recomposition khi ime thay đổi
        derivedStateOf { ime.getBottom(density) }
    }

    val rawBottomDp = with(density) { (imeBottomPx * 0.7f).toDp() }
    val targetBottomDp = rawBottomDp.coerceAtMost(300.dp)

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = targetBottomDp)
            .heightIn(max = 640.dp),
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
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Nhập Đơn Hàng ", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            }

            HorizontalDivider(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                thickness = 2.dp,
                color = Color.Gray,
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {

                item {
                    Text(
                        text = "Số lượng máy(*)", color = Color.Black, fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        value = soluongState.value,
                        onValueChange = { newText ->
                            if (newText.isEmpty() || newText.all { it.isDigit() }) {
                                soluongState.value = newText
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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        placeholder = { Text("Nhập số lượng") },
                        shape = RoundedCornerShape(12.dp),
                    )
                }

                item {
                    Text(
                        text = "Ngày Nhập(*)",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .clickable { showDatePicker = true },
                        value = ngayNhapState.value,
                        onValueChange = { ngayNhapState.value = it },
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
                        placeholder = { Text("Chọn ngày ->") },
                        shape = RoundedCornerShape(12.dp),
                    )

                }

                item {
                    Text(
                        text = "Nhà cung cấp(*)", color = Color.Black, fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        value = nhacungcapState.value,
                        onValueChange = { nhacungcapState.value = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        placeholder = { Text("Nhập tên nhà cung cấp") },
                        shape = RoundedCornerShape(12.dp),
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Thông tin cấu hình",
                            color = Color.Black,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                }

                item {
                    Text(
                        text = "Main(*)", color = Color.Black, fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        value = mainState.value,
                        onValueChange = { mainState.value = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        placeholder = { Text("Nhập thông tin main") },
                        shape = RoundedCornerShape(12.dp),
                    )
                }

                item {
                    Text(
                        text = "CPU(*)", color = Color.Black, fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        value = cpuState.value,
                        onValueChange = { cpuState.value = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        placeholder = { Text("Nhập thông tin CPU") },
                        shape = RoundedCornerShape(12.dp),
                    )
                }

                item {
                    Text(
                        text = "RAM(*)", color = Color.Black, fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        value = ramState.value,
                        onValueChange = { ramState.value = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        placeholder = { Text("Nhập thông tin RAM") },
                        shape = RoundedCornerShape(12.dp),
                    )
                }

                item {
                    Text(
                        text = "VGA(*)", color = Color.Black, fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        value = vgaState.value,
                        onValueChange = { vgaState.value = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        placeholder = { Text("Nhập thông tin VGA") },
                        shape = RoundedCornerShape(12.dp),
                    )
                }

                item {
                    Text(
                        text = "Màn Hình(*)", color = Color.Black, fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        value = manHinhState.value,
                        onValueChange = { manHinhState.value = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        placeholder = { Text("Nhập thông tin màn hình") },
                        shape = RoundedCornerShape(12.dp),
                    )
                }

                item {
                    Text(
                        text = "Bàn Phím(*)", color = Color.Black, fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        value = banPhimState.value,
                        onValueChange = { banPhimState.value = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        placeholder = { Text("Nhập thông tin bàn phím") },
                        shape = RoundedCornerShape(12.dp),
                    )
                }

                item {
                    Text(
                        text = "Chuột(*)", color = Color.Black, fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        value = chuotState.value,
                        onValueChange = { chuotState.value = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        placeholder = { Text("Nhập thông tin chuột") },
                        shape = RoundedCornerShape(12.dp),
                    )
                }

                item {
                    Text(
                        text = "HDD(*)", color = Color.Black, fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        value = hddState.value,
                        onValueChange = { hddState.value = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        placeholder = { Text("Nhập thông tin HDD") },
                        shape = RoundedCornerShape(12.dp),
                    )
                }

                item {
                    Text(
                        text = "SSD(*)", color = Color.Black, fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        value = ssdState.value,
                        onValueChange = { ssdState.value = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        placeholder = { Text("Nhập thông tin SSD") },
                        shape = RoundedCornerShape(12.dp),
                    )
                }
            }


            if (loadingState.value) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    contentAlignment = Alignment.Center
                ) {
                    DotLoading()
                }
            }

            Button(
                onClick = {
                    val sdfInput = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val sdfNgayNhap = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                    val parsedDate = try {
                        sdfInput.parse(ngayNhapState.value)
                    } catch (e: Exception) {
                        null
                    }

                    val soLuong = soluongState.value.toIntOrNull() ?: 0
                    val nhaCungCap = nhacungcapState.value.trim()

                    val fields = listOf(
                        mainState.value,
                        cpuState.value,
                        ramState.value,
                        vgaState.value,
                        manHinhState.value,
                        banPhimState.value,
                        chuotState.value,
                        hddState.value,
                        ssdState.value
                    )

                    if (parsedDate == null) {
                        dialogMessage.value = "Ngày nhập không hợp lệ!"
                        openDialog.value = true
                    } else if (soLuong <= 0) {
                        dialogMessage.value = "Số lượng máy phải lớn hơn 0!"
                        openDialog.value = true
                    } else if (nhaCungCap.isBlank()) {
                        dialogMessage.value = "Vui lòng nhập nhà cung cấp!"
                        openDialog.value = true
                    } else if (fields.any { it.isBlank() }) {
                        dialogMessage.value =
                            "Vui lòng nhập đầy đủ thông tin linh kiện. Nếu không có thì nhập NONE"
                        openDialog.value = true
                    } else {
                        loadingState.value = true

                        coroutineScope.launch {
                            try {
                                /* 1. Gọi API trong IO dispatcher */
                                val maDon = withContext(Dispatchers.IO) {
                                    val ngayNhapStr = sdfNgayNhap.format(parsedDate!!)
                                    val request = DonNhapRequest(
                                        NgayNhap = ngayNhapStr,
                                        SoLuong = soLuong,
                                        NhaCungCap = nhaCungCap,
                                        MaPhong = phonkho.MaPhong,
                                        LinhKien = LinhKien(
                                            main = mainState.value,
                                            cpu = cpuState.value,
                                            ram = ramState.value,
                                            vga = vgaState.value,
                                            manHinh = manHinhState.value,
                                            banPhim = banPhimState.value,
                                            chuot = chuotState.value,
                                            hdd = hddState.value,
                                            ssd = ssdState.value
                                        )
                                    )
                                    donNhapyViewModel.createDonNhapAsync(request)   // suspend -> String?
                                }

                                /* 2. Xử lý kết quả (trên Main thread) */
                                if (maDon != null) {
                                    maDonNhapNew.value = maDon
                                    isSuccess.value = true
                                    dialogMessage.value =
                                        "Tạo đơn nhập $maDon với $soLuong máy thành công!"
                                } else {
                                    isSuccess.value = false
                                    dialogMessage.value = "Tạo đơn nhập thất bại!"
                                }

                            } catch (e: Exception) {
                                isSuccess.value = false
                                dialogMessage.value = "Lỗi khi gửi đơn nhập. Vui lòng thử lại!"
                            } finally {
                                loadingState.value = false
                                openDialog.value = true
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0XFF1B8DDE))
            ) {
                Text("Nhập đơn hàng", color = Color.White, fontWeight = FontWeight.Bold)
            }

            if (openDialog.value) {
                AlertDialog(
                    onDismissRequest = { openDialog.value = false },
                    containerColor = Color.White,
                    shape = RoundedCornerShape(20.dp),
                    tonalElevation = 8.dp,
                    title = {
                        Text(
                            "Thông báo",
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0XFF1B8DDE)
                        )
                    },
                    text = { Text(dialogMessage.value, color = Color.Black) },
                    confirmButton = {
                        Button(
                            onClick = {
                                openDialog.value = false
                                if (isSuccess.value) {
                                    navController.navigate(NavRoute.CHITIETDONNHAP.route + "?madonnhap=${maDonNhapNew.value}") {
                                        popUpTo(NavRoute.ADDDONNHAP.route) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                    isSuccess.value = false
                                }
                            },
                            colors = ButtonDefaults.buttonColors(Color(0xFF1B8DDE))
                        ) { Text("OK", color = Color.White) }
                    }
                )
            }
        }
    }
}
