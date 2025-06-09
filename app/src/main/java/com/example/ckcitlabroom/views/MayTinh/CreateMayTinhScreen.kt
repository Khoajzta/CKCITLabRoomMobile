import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lapstore.viewmodels.MayTinhViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateMayTinhScreen(
    navController: NavHostController,
    mayTinhViewModel: MayTinhViewModel,
    phongMayViewModel: PhongMayViewModel
) {
//    val danhSachPhongMay = phongMayViewModel.danhSachAllPhongMay
//    var danhSachMayTinh = mayTinhViewModel.danhSachAllMayTinh
//
//    var isExpanded by remember { mutableStateOf(false) }
//
//    var selectdMaPhong by remember { mutableStateOf("") }
//
//    // Đồng bộ giá trị selectdMaPhong khi danhSachPhongMay load xong (có dữ liệu)
//    LaunchedEffect(danhSachPhongMay) {
//        if (danhSachPhongMay.isNotEmpty() && selectdMaPhong.isEmpty()) {
//            selectdMaPhong = danhSachPhongMay[0].MaPhong
//        }
//    }
//
//    // Đồng bộ maPhongState với selectdMaPhong
//    val maPhongState = remember { mutableStateOf("") }
//    LaunchedEffect(selectdMaPhong) {
//        maPhongState.value = selectdMaPhong
//    }
//
//    val selectedTenPhong = danhSachPhongMay.find { it.MaPhong == selectdMaPhong }?.TenPhong ?: ""
//
//    // Các state khác...
//    val maMayState = remember { mutableStateOf("") }
//    val tenMayState = remember { mutableStateOf("") }
//    val viTriState = remember { mutableStateOf("") }
//    val mainState = remember { mutableStateOf("") }
//    val cpuState = remember { mutableStateOf("") }
//    val ramState = remember { mutableStateOf("") }
//    val vgaState = remember { mutableStateOf("") }
//    val manHinhState = remember { mutableStateOf("") }
//    val banPhimState = remember { mutableStateOf("") }
//    val chuotState = remember { mutableStateOf("") }
//    val hddState = remember { mutableStateOf("") }
//    val ssdState = remember { mutableStateOf("") }
//    var qrCodeBitmap by remember { mutableStateOf<Bitmap?>(null) }
//    val sizeQR = 256
//
//    val snackbarHostState = remember { SnackbarHostState() }
//    val snackbarData = remember { mutableStateOf<CustomSnackbarData?>(null) }
//    val coroutineScope = rememberCoroutineScope()
//
//    LaunchedEffect(Unit) {
//        mayTinhViewModel.getAllMayTinh()
//        phongMayViewModel.getAllPhongMay()
//    }
//
//    DisposableEffect(Unit) {
//        onDispose {
//            mayTinhViewModel.stopPollingMayTinhTheoPhong()
//        }
//    }
//
//    DisposableEffect(Unit) {
//        onDispose {
//            phongMayViewModel.stopPollingPhongMay()
//        }
//    }
//
//    Card(
//        colors = CardDefaults.cardColors(containerColor = Color.White),
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(640.dp),
//        elevation = CardDefaults.cardElevation(8.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 8.dp),
//                horizontalArrangement = Arrangement.Center
//            ) {
//                Text("Thêm Máy Tính", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
//            }
//
//            LazyColumn(
//                modifier = Modifier
//                    .weight(1f)
//                    .fillMaxWidth()
//            ) {
//
//                item {
//                    Text(
//                        text = "Mã Máy", color = Color.Black, fontWeight = FontWeight.Bold
//                    )
//
//                    OutlinedTextField(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 12.dp),
//                        value = maMayState.value,
//                        onValueChange = { maMayState.value = it },
//                        colors = OutlinedTextFieldDefaults.colors(
//                            unfocusedContainerColor = Color.White,
//                            focusedContainerColor = Color.White,
//                            focusedBorderColor = Color.Black,
//                            unfocusedBorderColor = Color.Black,
//                            focusedTextColor = Color.Black,
//                            unfocusedTextColor = Color.Black
//                        ),
//                        placeholder = { Text("Nhập thông tin") },
//                        shape = RoundedCornerShape(12.dp),
//                    )
//                }
//
//                item {
//                    Text(
//                        text = "Tên Máy", color = Color.Black, fontWeight = FontWeight.Bold
//                    )
//
//                    OutlinedTextField(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 12.dp),
//                        value = tenMayState.value,
//                        onValueChange = { tenMayState.value = it },
//                        colors = OutlinedTextFieldDefaults.colors(
//                            unfocusedContainerColor = Color.White,
//                            focusedContainerColor = Color.White,
//                            focusedBorderColor = Color.Black,
//                            unfocusedBorderColor = Color.Black,
//                            focusedTextColor = Color.Black,
//                            unfocusedTextColor = Color.Black
//                        ),
//                        placeholder = { Text("Nhập thông tin") },
//                        shape = RoundedCornerShape(12.dp),
//                    )
//                }
//
//                item {
//                    Text(
//                        text = "Vị Trí", color = Color.Black, fontWeight = FontWeight.Bold
//                    )
//
//                    OutlinedTextField(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 12.dp),
//                        value = viTriState.value,
//                        onValueChange = { viTriState.value = it },
//                        colors = OutlinedTextFieldDefaults.colors(
//                            unfocusedContainerColor = Color.White,
//                            focusedContainerColor = Color.White,
//                            focusedBorderColor = Color.Black,
//                            unfocusedBorderColor = Color.Black,
//                            focusedTextColor = Color.Black,
//                            unfocusedTextColor = Color.Black
//                        ),
//                        placeholder = { Text("Nhập thông tin") },
//                        shape = RoundedCornerShape(12.dp),
//                    )
//                }
////
//                item {
//                    Text(
//                        text = "Main", color = Color.Black, fontWeight = FontWeight.Bold
//                    )
//
//                    OutlinedTextField(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 12.dp),
//                        value = mainState.value,
//                        onValueChange = { mainState.value = it },
//                        colors = OutlinedTextFieldDefaults.colors(
//                            unfocusedContainerColor = Color.White,
//                            focusedContainerColor = Color.White,
//                            focusedBorderColor = Color.Black,
//                            unfocusedBorderColor = Color.Black,
//                            focusedTextColor = Color.Black,
//                            unfocusedTextColor = Color.Black
//                        ),
//                        placeholder = { Text("Nhập thông tin") },
//                        shape = RoundedCornerShape(12.dp),
//                    )
//                }
//
//                item {
//                    Text(
//                        text = "CPU", color = Color.Black, fontWeight = FontWeight.Bold
//                    )
//
//                    OutlinedTextField(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 12.dp),
//                        value = cpuState.value,
//                        onValueChange = { cpuState.value = it },
//                        colors = OutlinedTextFieldDefaults.colors(
//                            unfocusedContainerColor = Color.White,
//                            focusedContainerColor = Color.White,
//                            focusedBorderColor = Color.Black,
//                            unfocusedBorderColor = Color.Black,
//                            focusedTextColor = Color.Black,
//                            unfocusedTextColor = Color.Black
//                        ),
//                        placeholder = { Text("Nhập thông tin") },
//                        shape = RoundedCornerShape(12.dp),
//                    )
//                }
//
//                item {
//                    Text(
//                        text = "RAM", color = Color.Black, fontWeight = FontWeight.Bold
//                    )
//
//                    OutlinedTextField(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 12.dp),
//                        value = ramState.value,
//                        onValueChange = { ramState.value = it },
//                        colors = OutlinedTextFieldDefaults.colors(
//                            unfocusedContainerColor = Color.White,
//                            focusedContainerColor = Color.White,
//                            focusedBorderColor = Color.Black,
//                            unfocusedBorderColor = Color.Black,
//                            focusedTextColor = Color.Black,
//                            unfocusedTextColor = Color.Black
//                        ),
//                        placeholder = { Text("Nhập thông tin") },
//                        shape = RoundedCornerShape(12.dp),
//                    )
//                }
////
//                item {
//                    Text(
//                        text = "VGA", color = Color.Black, fontWeight = FontWeight.Bold
//                    )
//
//                    OutlinedTextField(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 12.dp),
//                        value = vgaState.value,
//                        onValueChange = { vgaState.value = it },
//                        colors = OutlinedTextFieldDefaults.colors(
//                            unfocusedContainerColor = Color.White,
//                            focusedContainerColor = Color.White,
//                            focusedBorderColor = Color.Black,
//                            unfocusedBorderColor = Color.Black,
//                            focusedTextColor = Color.Black,
//                            unfocusedTextColor = Color.Black
//                        ),
//                        placeholder = { Text("Nhập thông tin") },
//                        shape = RoundedCornerShape(12.dp),
//                    )
//                }
////
//                item {
//                    Text(
//                        text = "Màn Hình", color = Color.Black, fontWeight = FontWeight.Bold
//                    )
//
//                    OutlinedTextField(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 12.dp),
//                        value = manHinhState.value,
//                        onValueChange = { manHinhState.value = it },
//                        colors = OutlinedTextFieldDefaults.colors(
//                            unfocusedContainerColor = Color.White,
//                            focusedContainerColor = Color.White,
//                            focusedBorderColor = Color.Black,
//                            unfocusedBorderColor = Color.Black,
//                            focusedTextColor = Color.Black,
//                            unfocusedTextColor = Color.Black
//                        ),
//                        placeholder = { Text("Nhập thông tin") },
//                        shape = RoundedCornerShape(12.dp),
//                    )
//                }
////
//                item {
//                    Text(
//                        text = "Bàn Phím", color = Color.Black, fontWeight = FontWeight.Bold
//                    )
//
//                    OutlinedTextField(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 12.dp),
//                        value = banPhimState.value,
//                        onValueChange = { banPhimState.value = it },
//                        colors = OutlinedTextFieldDefaults.colors(
//                            unfocusedContainerColor = Color.White,
//                            focusedContainerColor = Color.White,
//                            focusedBorderColor = Color.Black,
//                            unfocusedBorderColor = Color.Black,
//                            focusedTextColor = Color.Black,
//                            unfocusedTextColor = Color.Black
//                        ),
//                        placeholder = { Text("Nhập thông tin") },
//                        shape = RoundedCornerShape(12.dp),
//                    )
//                }
////
//                item {
//                    Text(
//                        text = "Chuột", color = Color.Black, fontWeight = FontWeight.Bold
//                    )
//
//                    OutlinedTextField(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 12.dp),
//                        value = chuotState.value,
//                        onValueChange = { chuotState.value = it },
//                        colors = OutlinedTextFieldDefaults.colors(
//                            unfocusedContainerColor = Color.White,
//                            focusedContainerColor = Color.White,
//                            focusedBorderColor = Color.Black,
//                            unfocusedBorderColor = Color.Black,
//                            focusedTextColor = Color.Black,
//                            unfocusedTextColor = Color.Black
//                        ),
//                        placeholder = { Text("Nhập thông tin") },
//                        shape = RoundedCornerShape(12.dp),
//                    )
//                }
////
//                item {
//                    Text(
//                        text = "HDD", color = Color.Black, fontWeight = FontWeight.Bold
//                    )
//
//                    OutlinedTextField(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 12.dp),
//                        value = hddState.value,
//                        onValueChange = { hddState.value = it },
//                        colors = OutlinedTextFieldDefaults.colors(
//                            unfocusedContainerColor = Color.White,
//                            focusedContainerColor = Color.White,
//                            focusedBorderColor = Color.Black,
//                            unfocusedBorderColor = Color.Black,
//                            focusedTextColor = Color.Black,
//                            unfocusedTextColor = Color.Black
//                        ),
//                        placeholder = { Text("Nhập thông tin") },
//                        shape = RoundedCornerShape(12.dp),
//                    )
//                }
////
//                item {
//                    Text(
//                        text = "SSD", color = Color.Black, fontWeight = FontWeight.Bold
//                    )
//
//                    OutlinedTextField(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(bottom = 12.dp),
//                        value = ssdState.value,
//                        onValueChange = { ssdState.value = it },
//                        colors = OutlinedTextFieldDefaults.colors(
//                            unfocusedContainerColor = Color.White,
//                            focusedContainerColor = Color.White,
//                            focusedBorderColor = Color.Black,
//                            unfocusedBorderColor = Color.Black,
//                            focusedTextColor = Color.Black,
//                            unfocusedTextColor = Color.Black
//                        ),
//                        placeholder = { Text("Nhập thông tin") },
//                        shape = RoundedCornerShape(12.dp),
//                    )
//                }
////
//                item {
//                    Text(
//                        text = "Phòng", color = Color.Black, fontWeight = FontWeight.Bold
//                    )
//                    Column {
//                        ExposedDropdownMenuBox(
//                            expanded = isExpanded,
//                            onExpandedChange = { isExpanded = !isExpanded },
//                        ) {
//                            OutlinedTextField(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .menuAnchor()
//                                    .padding(bottom = 12.dp),
//                                value = selectedTenPhong,
//                                onValueChange = { },
//                                readOnly = true,
//                                colors = OutlinedTextFieldDefaults.colors(
//                                    unfocusedContainerColor = Color.White,
//                                    focusedContainerColor = Color.White,
//                                    focusedBorderColor = Color.Black,
//                                    unfocusedBorderColor = Color.Black,
//                                    focusedTextColor = Color.Black,
//                                    unfocusedTextColor = Color.Black
//                                ),
//                                shape = RoundedCornerShape(12.dp),
//                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) }
//                            )
//
//                            ExposedDropdownMenu(
//                                modifier = Modifier.background(Color.White).height(300.dp).padding(bottom = 8.dp),
//                                expanded = isExpanded,
//                                onDismissRequest = { isExpanded = false },
//                                shape = RoundedCornerShape(12.dp),
//                            ) {
//                                danhSachPhongMay.forEach { phongMay ->
//                                    androidx.compose.material3.DropdownMenuItem(
//                                        text = { Text(phongMay.TenPhong, fontWeight = FontWeight.Bold)},
//                                        onClick = {
//                                            selectdMaPhong = phongMay.MaPhong
//                                            isExpanded = false
//                                        }
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//
//            }
//
//            SnackbarHost(
//                hostState = snackbarHostState,
//                modifier = Modifier.padding(16.dp)
//            ) { data ->
//                snackbarData.value?.let { customData ->
//                    Snackbar(
//                        containerColor = Color(0xFF1B8DDE),
//                        contentColor = Color.White,
//                        shape = RoundedCornerShape(12.dp),
//                        action = {
//                            TextButton(onClick = {
//                                snackbarData.value = null
//                            }) {
//                                Text("Đóng", color = Color.White)
//                            }
//                        }
//                    ) {
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            Icon(
//                                imageVector = if (customData.type == SnackbarType.SUCCESS) Icons.Default.Info else Icons.Default.Warning,
//                                contentDescription = null,
//                                tint = if (customData.type == SnackbarType.SUCCESS) Color.Cyan else Color.Yellow,
//                                modifier = Modifier.size(20.dp)
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(text = customData.message)
//                        }
//                    }
//                }
//            }
//
//
//            Button(
//                onClick = {
//                    val maMayMoi = maMayState.value
//                    val daTonTai = danhSachMayTinh.any { it.MaMay == maMayMoi }
//
//                    val qrBitmap = generateQRCode(maMayMoi, 512)
//                    val qrBase64 = bitmapToBase64(qrBitmap)
//
//                    if(maMayState.value == "" ){
//                        coroutineScope.launch {
//                            snackbarData.value = CustomSnackbarData(
//                                message = "Mã máy không được để trống!", type = SnackbarType.ERROR
//                            )
//                            snackbarHostState.showSnackbar("Thông báo")
//                        }
//                    }
//                    else if (daTonTai) {
//                        // Hiện snackbar lỗi
//                        coroutineScope.launch {
//                            snackbarData.value = CustomSnackbarData(
//                                message = "Mã máy đã tồn tại!", type = SnackbarType.ERROR
//                            )
//                            snackbarHostState.showSnackbar("Thông báo")
//                        }
//
//                    } else {
//                        val mayTinhMoi = MayTinh(
//                            MaMay = maMayMoi,
//                            TenMay = tenMayState.value,
//                            ViTri = viTriState.value,
//                            Main = mainState.value,
//                            CPU = cpuState.value,
//                            RAM = ramState.value,
//                            VGA = vgaState.value,
//                            ManHinh = manHinhState.value,
//                            BanPhim = banPhimState.value,
//                            Chuot = chuotState.value,
//                            HDD = hddState.value,
//                            SSD = ssdState.value,
//                            MaPhong = maPhongState.value,
//                            QRCode = qrBase64,
//                            TrangThai = 1
//                        )
//
//
//                        viewModelScope.launch {
//                            mayTinhViewModel.createMayTinh(mayTinhMoi)
//                        }
//
//
//
//                        coroutineScope.launch {
//                            snackbarData.value = CustomSnackbarData(
//                                message = "Thêm máy tính thành công",
//                                type = SnackbarType.SUCCESS
//                            )
//                            snackbarHostState.showSnackbar("Thông báo") // Trigger để SnackbarHost hiện
//                            delay(1000)
//                            navController.popBackStack()
//                        }
//                    }
//                },
//
//                modifier = Modifier.fillMaxWidth(),
//                shape = RoundedCornerShape(12.dp),
//                colors = ButtonDefaults.buttonColors(Color(0XFF1B8DDE))
//            ) {
//                Text("Thêm máy tính")
//            }
//        }
//    }
}





