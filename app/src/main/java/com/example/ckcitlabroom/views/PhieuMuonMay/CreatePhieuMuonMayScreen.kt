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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePhieuMuonMayScreen(
    phongMayViewModel: PhongMayViewModel,
    phieuMuonMayViewModel: PhieuMuonMayViewModel,
) {
    var mayTinhViewModel: MayTinhViewModel = viewModel()
    var maytinh = mayTinhViewModel.maytinh


    val loadingState = remember { mutableStateOf(false) }

    val openDialog = remember { mutableStateOf(false) }
    val dialogMessage = remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }

    val danhSachPhongMay = phongMayViewModel.danhSachAllPhongMay
    var selectdMaPhong by remember { mutableStateOf("") }
    var phongMayCard by remember { mutableStateOf<PhongMay?>(null) }
    val maPhongState = remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarData = remember { mutableStateOf<CustomSnackbarData?>(null) }
    val coroutineScope = rememberCoroutineScope()

    var showDatePicker by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val ngayMuonState = remember { mutableStateOf("") }
    val tenNguoiMuonState = remember { mutableStateOf("") }
    val soluongState = remember { mutableStateOf("") }

    val selectedTenPhong = danhSachPhongMay.find { it.MaPhong == selectdMaPhong }?.TenPhong ?: ""

    LaunchedEffect(Unit) {
        phongMayViewModel.getAllPhongMay()
    }

    LaunchedEffect(selectdMaPhong) {
        maPhongState.value = selectdMaPhong
    }

    LaunchedEffect(danhSachPhongMay) {
        if (danhSachPhongMay.isNotEmpty() && selectdMaPhong.isEmpty()) {
            selectdMaPhong = danhSachPhongMay[0].MaPhong
        }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth(),
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
                Text("Tạo Phiếu Sửa Chữa ", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            }

            Text(
                text = "Tên Người Mượn", color = Color.Black, fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                value = tenNguoiMuonState.value,
                onValueChange = { tenNguoiMuonState.value = it },
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

            Text(
                text = "Ngày Mươn", color = Color.Black, fontWeight = FontWeight.Bold
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                value = formatNgay(ngayMuonState.value),
                onValueChange = { ngayMuonState.value = it },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Chọn ngày",
                            tint = Color.Black
                        )
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

            Text(
                text = "Số Lượng Máy", color = Color.Black, fontWeight = FontWeight.Bold
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
                placeholder = { Text("Nhập thông tin") },
                shape = RoundedCornerShape(12.dp),
            )

            Text(
                text = "Chuyển Đến Phòng", color = Color.Black, fontWeight = FontWeight.Bold
            )

            ExposedDropdownMenuBox(
                expanded = isExpanded, onExpandedChange = { isExpanded = !isExpanded }) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .padding(vertical = 8.dp),
                    value = selectedTenPhong,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
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
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    danhSachPhongMay.forEach { phongMay ->
                        DropdownMenuItem(text = {
                            Text(
                                phongMay.TenPhong,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                        }, onClick = {
                            selectdMaPhong = phongMay.MaPhong
                            isExpanded = false
                        })
                    }
                }
            }

            SnackbarHost(
                hostState = snackbarHostState, modifier = Modifier.padding(16.dp)
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
                        }) {
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
                    val tenNguoiMuon = tenNguoiMuonState.value.trim()
                    val ngayMuon = ngayMuonState.value.trim()
                    val soLuongText = soluongState.value.trim()
                    val maPhong = selectdMaPhong.trim()

                    val soLuong = soLuongText.toIntOrNull()

                    if (tenNguoiMuon.isEmpty() || ngayMuon.isEmpty() || soLuongText.isEmpty() || soLuong == null || soLuong <= 0 || maPhong.isEmpty()) {
                        coroutineScope.launch {
                            snackbarData.value = CustomSnackbarData(
                                message = "Vui lòng nhập đầy đủ thông tin!",
                                type = SnackbarType.ERROR
                            )
                            snackbarHostState.showSnackbar("Thiếu thông tin")
                        }
                    } else {
                        val phieumuonmay = PhieuMuonMay(
                            MaPhieuMuon = 0,
                            NgayMuon = ngayMuon,
                            NgayTra = "",
                            NguoiMuon = tenNguoiMuon,
                            MaPhong = maPhong,
                            SoLuong = soLuongText,
                            TrangThai = 0
                        )

                        phieuMuonMayViewModel.createPhieuMuonMay(phieumuonmay)

                        coroutineScope.launch {
                            snackbarData.value = CustomSnackbarData(
                                message = "Tạo phiếu mượn thành công!",
                                type = SnackbarType.SUCCESS
                            )
                            snackbarHostState.showSnackbar("Thành công")
                        }

                        // Reset input
                        tenNguoiMuonState.value = ""
                        ngayMuonState.value = ""
                        soluongState.value = ""
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0XFF1B8DDE))
            ) {
                Text("Tạo Phiếu Mượn Máy", color = Color.White, fontWeight = FontWeight.Bold)
            }

        }




        if (loadingState.value) {
            DotLoading()
            if (openDialog.value) {
                AlertDialog(onDismissRequest = { openDialog.value = false }, confirmButton = {
                    TextButton(onClick = { openDialog.value = false }) {
                        Text("OK")
                    }
                }, title = { Text("Thông báo") }, text = { Text(dialogMessage.value) })
            }
        }


        if (openDialog.value) {
            AlertDialog(onDismissRequest = { openDialog.value = false }, confirmButton = {
                TextButton(onClick = {
                    openDialog.value = false
                }) {
                    Text("OK")
                }
            }, title = {
                Text(text = "Thông báo")
            }, text = {
                Text(dialogMessage.value)
            })
        }

        if (showDatePicker) {
            val datePickerDialog = DatePickerDialog(
                context,
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    ngayMuonState.value = sdf.format(calendar.time)
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