import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.viewmodels.MayTinhViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePhieuMuonMayScreen(
    navController: NavHostController,
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

    val danhSachPhongMay = phongMayViewModel.danhSachAllPhongMay.filter { it.LoaiPhong == 3 }
    var selectdMaPhong by remember { mutableStateOf("") }
    var phongMayCard by remember { mutableStateOf<PhongMay?>(null) }
    val maPhongState = remember { mutableStateOf("") }

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
                Text("Tạo Phiếu Mượn ", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
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
                placeholder = { Text("Nhập tên người mượn") },
                shape = RoundedCornerShape(12.dp),
            )

            Text(
                text = "Ngày Mượn", color = Color.Black, fontWeight = FontWeight.Bold
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
                placeholder = { Text("Chọn ngày ->") },
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

            Text(
                text = "Chuyển Đến Phòng", color = Color.Black, fontWeight = FontWeight.Bold
            )

            ExposedDropdownMenuBox(
                expanded = isExpanded, onExpandedChange = { isExpanded = !isExpanded }) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .padding(bottom = 8.dp),
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


            Button(
                onClick = {
                    val tenNguoiMuon = tenNguoiMuonState.value.trim()
                    val ngayMuon = ngayMuonState.value.trim()
                    val soLuongText = soluongState.value.trim()
                    val maPhong = selectdMaPhong.trim()

                    val soLuong = soLuongText.toIntOrNull()

                    if (tenNguoiMuon.isEmpty() || ngayMuon.isEmpty() || soLuongText.isEmpty() || soLuong == null || soLuong <= 0 || maPhong.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Vui lòng nhập đầy đủ thông tin",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val phieumuonmay = PhieuMuonMay(
                            MaPhieuMuon = 0,
                            NgayMuon = ngayMuon,
                            NgayTra = "",
                            NguoiMuon = tenNguoiMuon,
                            MaPhong = maPhong,
                            SoLuong = soLuong,
                            TrangThai = 0
                        )

                        phieuMuonMayViewModel.createPhieuMuonMay(phieumuonmay)
                        Toast.makeText(context, "Tạo Phiếu Mượn Máy Thành Công", Toast.LENGTH_SHORT)
                        navController.navigate(NavRoute.QUANLYPHIEUMUONMAY.route + "?startIndex=0")

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