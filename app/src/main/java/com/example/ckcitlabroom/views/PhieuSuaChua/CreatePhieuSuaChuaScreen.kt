import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.lapstore.viewmodels.MayTinhViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@Composable
fun CreatePhieuSuaChuaScreen(
    mamay:String,
    phieuSuaChuaViewModel: PhieuSuaChuaViewModel,
    giaoDichViewModel: GiangVienViewModel,
    sinhVienViewModel: SinhVienViewModel,
    navController: NavHostController
){

    var sinhvien = sinhVienViewModel.sinhvienSet
    var giangvien = giaoDichViewModel.giangvienSet

    val maNguoiBaoHong = remember {
        mutableStateOf(
            when {
                sinhvien != null -> sinhvien.MaSinhVien
                giangvien != null -> giangvien.MaGV
                else -> ""
            }
        )
    }

    var danhsachphieusuachua = phieuSuaChuaViewModel.danhSachAllPhieuSuaChua

    var mayTinhViewModel: MayTinhViewModel = viewModel()
    var maytinh = mayTinhViewModel.maytinh

    LaunchedEffect(Unit) {
        mayTinhViewModel.getMayTinhByMaMay(mamay)
        phieuSuaChuaViewModel.getAllPhieuSuaChua()
    }

    DisposableEffect(Unit) {
        onDispose {
            phieuSuaChuaViewModel.stopPollingPhieuSuaChua()
        }
    }

    val loadingState = remember { mutableStateOf(false) }

    val openDialog = remember { mutableStateOf(false) }
    val dialogMessage = remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarData = remember { mutableStateOf<CustomSnackbarData?>(null) }
    val coroutineScope = rememberCoroutineScope()

    var showDatePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val ngayBaoHongState = remember { mutableStateOf("") }
    val tenMayState = remember { mutableStateOf("") }
    val viTriState = remember { mutableStateOf("") }
    val mainState = remember { mutableStateOf("") }
    val cpuState = remember { mutableStateOf("") }
    val ramState = remember { mutableStateOf("") }
    val vgaState = remember { mutableStateOf("") }
    val manHinhState = remember { mutableStateOf("") }
    val banPhimState = remember { mutableStateOf("") }
    val chuotState = remember { mutableStateOf("") }
    val hddState = remember { mutableStateOf("") }
    val ssdState = remember { mutableStateOf("") }
    val moTaLoiState = remember { mutableStateOf("") }

    val trangThaiState = remember { mutableStateOf("") }

    LaunchedEffect(maytinh) {
        maytinh?.let {
            tenMayState.value = it.TenMay
            viTriState.value = it.ViTri
            mainState.value = it.Main
            cpuState.value = it.CPU
            ramState.value = it.RAM
            vgaState.value = it.VGA
            manHinhState.value = it.ManHinh
            banPhimState.value = it.BanPhim
            chuotState.value = it.Chuot
            hddState.value = it.HDD
            ssdState.value = it.SSD
            trangThaiState.value = it.TrangThai.toString()
        }
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
                Text("Tạo Phiếu Sửa Chữa ", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f).height(550.dp)
                    .fillMaxWidth()
            ) {


                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Điền Thông Tin Cấu Hình Hiện Tại",
                            color = Color.Black,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                }

                item {
                    Text(
                        text = "Tên Máy",
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
                                maytinh.TenMay,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Vị Trí",
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
                                maytinh.ViTri,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )
                        }
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

                item {
                    Text(
                        text = "Mô Tả Lỗi", color = Color.Black, fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        value = moTaLoiState.value,
                        onValueChange = { moTaLoiState.value = it },
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

            val context = LocalContext.current

            Button(
                onClick = {
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val today = LocalDate.now().format(formatter)

                    val isEmpty = mamay.isBlank() ||
                            moTaLoiState.value.isBlank() ||
                            maNguoiBaoHong.value.isBlank()

                    if (isEmpty) {
                        Toast.makeText(context, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
                    } else {
                        val hasOpenPhieu = danhsachphieusuachua.any {
                            it.MaMay == mamay && it.TrangThai == 0
                        }

                        if (hasOpenPhieu) {
                            Toast.makeText(context, "Máy này đang có phiếu sửa chữa chưa hoàn tất", Toast.LENGTH_SHORT).show()
                        } else {
                            val phieuSuaChua = PhieuSuaChua(
                                MaPhieuSuaChua = 0,
                                MaMay = mamay,
                                NgayBaoHong = today,
                                MoTaLoi = moTaLoiState.value,
                                MaPhong = maytinh.MaPhong,
                                MaNguoiBaoHong = maNguoiBaoHong.value,
                                TrangThai = 0
                            )

                            phieuSuaChuaViewModel.createPhieuSuaChua(phieuSuaChua)

                            val maytinhnew = maytinh.copy(TrangThai = 0)
                            mayTinhViewModel.updateMayTinh(maytinhnew)

                            coroutineScope.launch {
                                snackbarData.value = CustomSnackbarData(
                                    message = "Thêm phòng máy thành công",
                                    type = SnackbarType.SUCCESS
                                )
                                snackbarHostState.showSnackbar("Thông báo")
                                delay(500)
                                navController.popBackStack()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0XFF1B8DDE))
            ) {
                Text("Tạo Phiếu Sửa Chữa", color = Color.White, fontWeight = FontWeight.Bold)
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

            if (showDatePicker) {
                val datePickerDialog = DatePickerDialog(
                    context,
                    { _, year, month, day ->
                        calendar.set(year, month, day)
                        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        ngayBaoHongState.value = sdf.format(calendar.time)
                        showDatePicker = false
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                datePickerDialog.setOnDismissListener { showDatePicker = false }
                datePickerDialog.show()
            }

        }
    }
}