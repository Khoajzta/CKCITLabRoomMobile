import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lapstore.viewmodels.ChiTietDonNhapyViewModel
import com.example.lapstore.viewmodels.DonNhapViewModel
import com.example.lapstore.viewmodels.MayTinhViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random
import kotlinx.coroutines.*
import java.util.Calendar

@Composable
fun CreateDonNhapScreen(
    navController: NavHostController,
    mayTinhViewModel: MayTinhViewModel,
    phongMayViewModel: PhongMayViewModel,
    donNhapyViewModel: DonNhapViewModel,
    chiTietDonNhapyViewModel: ChiTietDonNhapyViewModel
) {
    val hideButtonNhap = remember { mutableStateOf(false) }

    var danhSachMayTinh = mayTinhViewModel.danhSachAllMayTinh
    val loadingState = remember { mutableStateOf(false) }

    var phonkho = phongMayViewModel.phongmay

    val openDialog = remember { mutableStateOf(false) }
    val dialogMessage = remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarData = remember { mutableStateOf<CustomSnackbarData?>(null) }
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


    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(640.dp),
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
                Text("Nhập Đơn Hàng ", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {

                item {
                    Text(
                        text = "Số lượng máy", color = Color.Black, fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        value = soluongState.value,
                        onValueChange = { soluongState.value = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        placeholder = { Text("Nhập thông tin") },
                        shape = RoundedCornerShape(12.dp),
                    )
                }

                item {
                    Text(
                        text = "Ngày Nhập",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
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
                        placeholder = { Text("Chọn ngày") },
                        shape = RoundedCornerShape(12.dp),
                    )

                }

                item {
                    Text(
                        text = "Nhà cung cấp", color = Color.Black, fontWeight = FontWeight.Bold
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
                        placeholder = { Text("Nhập thông tin") },
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
                        text = "Main", color = Color.Black, fontWeight = FontWeight.Bold
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
                        placeholder = { Text("Nhập thông tin") },
                        shape = RoundedCornerShape(12.dp),
                    )
                }

                item {
                    Text(
                        text = "CPU", color = Color.Black, fontWeight = FontWeight.Bold
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
                        placeholder = { Text("Nhập thông tin") },
                        shape = RoundedCornerShape(12.dp),
                    )
                }

                item {
                    Text(
                        text = "RAM", color = Color.Black, fontWeight = FontWeight.Bold
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
                        placeholder = { Text("Nhập thông tin") },
                        shape = RoundedCornerShape(12.dp),
                    )
                }

                item {
                    Text(
                        text = "VGA", color = Color.Black, fontWeight = FontWeight.Bold
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
                        placeholder = { Text("Nhập thông tin") },
                        shape = RoundedCornerShape(12.dp),
                    )
                }

                item {
                    Text(
                        text = "Màn Hình", color = Color.Black, fontWeight = FontWeight.Bold
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
                        placeholder = { Text("Nhập thông tin") },
                        shape = RoundedCornerShape(12.dp),
                    )
                }

                item {
                    Text(
                        text = "Bàn Phím", color = Color.Black, fontWeight = FontWeight.Bold
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
                        placeholder = { Text("Nhập thông tin") },
                        shape = RoundedCornerShape(12.dp),
                    )
                }

                item {
                    Text(
                        text = "Chuột", color = Color.Black, fontWeight = FontWeight.Bold
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
                        placeholder = { Text("Nhập thông tin") },
                        shape = RoundedCornerShape(12.dp),
                    )
                }

                item {
                    Text(
                        text = "HDD", color = Color.Black, fontWeight = FontWeight.Bold
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
                        placeholder = { Text("Nhập thông tin") },
                        shape = RoundedCornerShape(12.dp),
                    )
                }

                item {
                    Text(
                        text = "SSD", color = Color.Black, fontWeight = FontWeight.Bold
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
                        placeholder = { Text("Nhập thông tin") },
                        shape = RoundedCornerShape(12.dp),
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
                            TextButton(onClick = {
                                snackbarData.value = null
                            }) {
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

            if (loadingState.value) {
                DotLoading()
                if (openDialog.value) {
                    AlertDialog(
                        onDismissRequest = { openDialog.value = false },
                        confirmButton = {
                            TextButton(onClick = { openDialog.value = false }) {
                                Text("OK")
                            }
                        },
                        title = { Text("Thông báo") },
                        text = { Text(dialogMessage.value) }
                    )
                }
            }

            if (loadingState.value) {
                DotLoading()
            } else {
                Button(
                    onClick = {
                        val sdf = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
                        val dateStr = sdf.format(Date())
                        val randomSuffix = (1000..9999).random().toString()
                        val maDonNhap = dateStr + randomSuffix

                        val soLuong = soluongState.value.toIntOrNull() ?: 0
                        if (soLuong <= 0) {
                            dialogMessage.value = "Số lượng máy phải lớn hơn 0"
                            openDialog.value = true
                            return@Button
                        }


                        val danhSachMay = mutableListOf<MayTinh>()

                        for (i in 1..soLuong) {
                            val stt = i.toString().padStart(3, '0') // ví dụ 001, 002...
                            val maMay = "MAY${phonkho.MaPhong}$stt$randomSuffix"

                            val may = MayTinh(
                                MaMay = maMay,
                                TenMay = "",
                                ViTri = "",
                                Main = mainState.value,
                                CPU = cpuState.value,
                                RAM =  ramState.value,
                                VGA = vgaState.value,
                                ManHinh = manHinhState.value,
                                BanPhim = banPhimState.value,
                                Chuot = chuotState.value,
                                HDD = hddState.value,
                                SSD = ssdState.value,
                                QRCode = qrBase64,
                                TrangThai = 0,
                                MaPhong = phonkho.MaPhong,
                            )
                            danhSachMay.add(may)
                        }

                        // Gọi ViewModel để thêm đơn nhập và máy
                        loadingState.value = true
                        coroutineScope.launch {
                            val donNhap = DonNhap(
                                maDonNhap = maDonNhap,
                                ngayNhap = ngayNhapState.value,
                                nhaCungCap = nhacungcapState.value
                            )
                            donNhapyViewModel.createDonNhap(donNhap)
                            danhSachMay.forEach { mayTinhViewModel.createMayTinh(it) }

                            dialogMessage.value = "Tạo đơn nhập $maDonNhap với $soLuong máy thành công!"
                            openDialog.value = true
                            loadingState.value = false
                        }
                    },

                            modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(Color(0XFF1B8DDE))
                ) {
                    Text("Nhập đơn hàng", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }


            if (openDialog.value) {
                AlertDialog(
                    onDismissRequest = { openDialog.value = false },
                    confirmButton = {
                        TextButton(onClick = {
                            openDialog.value = false
                        }) {
                            Text("OK")
                        }
                    },
                    title = {
                        Text(text = "Thông báo")
                    },
                    text = {
                        Text(dialogMessage.value)
                    }
                )
            }

        }
    }
}