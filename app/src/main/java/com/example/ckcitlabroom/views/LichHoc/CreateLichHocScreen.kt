import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.models.CaHoc
import com.example.ckcitlabroom.models.LopHoc
import com.example.ckcitlabroom.viewmodels.CaHocViewModel
import com.example.ckcitlabroom.viewmodels.LopHocViewModel
import com.example.lapstore.viewmodels.LichHocViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CreateLichHocScreen(
    navController: NavHostController,
    lichhocViewModel: LichHocViewModel,
    namHocViewModel: NamHocViewModel,
    monHocViewModel: MonHocViewModel,
    tuanViewModel: TuanViewModel,
    giangvienViewModel: GiangVienViewModel,
    lopHocViewModel: LopHocViewModel,
    phongMayViewModel: PhongMayViewModel,
    caHocViewModel: CaHocViewModel
) {
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var conflictMessage by remember { mutableStateOf("") }


    // Load dữ liệu khi khởi chạy
    LaunchedEffect(Unit) {
        namHocViewModel.getAllNamHoc()
        tuanViewModel.getAllTuan()
        giangvienViewModel.getAllGiangVien()
        phongMayViewModel.getAllPhongMay()
        monHocViewModel.getAllMonHoc()
        lopHocViewModel.getAllLopHoc()
        caHocViewModel.getAllCaHoc()
        lichhocViewModel.getAllLichHoc()
    }

    // State dropdown
    var selectedGiangVien by remember { mutableStateOf<GiangVien?>(null) }
    var selectedPhong by remember { mutableStateOf<PhongMay?>(null) }
    var selectedTuanTu by remember { mutableStateOf<Tuan?>(null) }
    var selectedTuanDen by remember { mutableStateOf<Tuan?>(null) }
    var selectedMonHoc by remember { mutableStateOf<MonHoc?>(null) }
    var selectedThu by remember { mutableStateOf<String?>(null) }
    var selectedLop by remember { mutableStateOf<LopHoc?>(null) }
    var selectedCaHoc by remember { mutableStateOf<CaHoc?>(null) }
    var ghiChu by remember { mutableStateOf("") }

    // Dữ liệu nguồn
    val danhSachGiangVien = giangvienViewModel.danhSachAllGiangVien.filter { it.TrangThai == 1 }
    val danhSachPhong = phongMayViewModel.danhSachAllPhongMay.filter { it.TrangThai == 1 }
    val danhSachNamHoc = namHocViewModel.danhSachAllNamHoc.filter { it.TrangThai == 1 }
    val danhSachTuan = tuanViewModel.danhSachAllTuan
    val danhSachMonHoc = monHocViewModel.danhSachAllMonHoc.filter { it.TrangThai == 1 }
    val danhsachlophoc = lopHocViewModel.danhSachAllLopHoc.filter { it.TrangThai == 1 }
    val danhsachcahoc = caHocViewModel.danhSachAllCaHoc.filter { it.TrangThai == 1 }
    val danhsachAllLichHoc = lichhocViewModel.danhSachLichHoc.filter { it.TrangThai == 1 }

    val danhSachThu  = listOf(
        "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ Nhật"
    )

    val thuToOffset = mapOf(
        "Thứ 2" to 0,
        "Thứ 3" to 1,
        "Thứ 4" to 2,
        "Thứ 5" to 3,
        "Thứ 6" to 4,
        "Thứ 7" to 5,
        "Chủ Nhật" to 6
    )

    val selectedNamHoc = remember(danhSachNamHoc) {
        danhSachNamHoc.firstOrNull { it.TrangThai == 1 }
    }

    val danhSachTuanTheoNam = remember(selectedNamHoc, danhSachTuan) {
        danhSachTuan.filter { it.MaNam == selectedNamHoc?.MaNam }
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                text = "Thêm Lịch Học",
                color = Color(0xFF1B8DDE),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )

            HorizontalDivider(
                modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(),
                thickness = 2.dp,
                color = Color(0xFF1B8DDE),
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                item {
                    Text("Giảng Viên", fontWeight = FontWeight.Bold, color = Color.Black)
                    CustomDropdownSelector(
                        label = "Giảng viên",
                        items = danhSachGiangVien,
                        selectedItem = selectedGiangVien,
                        itemLabel = { it.TenGiangVien },
                        onItemSelected = { selectedGiangVien = it }
                    )
                }

                item {
                    Text("Phòng Dạy", fontWeight = FontWeight.Bold, color = Color.Black)
                    CustomDropdownSelector(
                        label = "Phòng",
                        items = danhSachPhong,
                        selectedItem = selectedPhong,
                        itemLabel = { it.TenPhong },
                        onItemSelected = { selectedPhong = it }
                    )
                }

                item {
                    Text("Lớp", fontWeight = FontWeight.Bold, color = Color.Black)
                    CustomDropdownSelector(
                        label = "Lớp",
                        items = danhsachlophoc,
                        selectedItem = selectedLop,
                        itemLabel = { it.TenLopHoc },
                        onItemSelected = { selectedLop = it }
                    )
                }

                item {
                    Text("Tuần Bắt Đầu", fontWeight = FontWeight.Bold, color = Color.Black)
                    CustomDropdownSelector(
                        label = "Từ tuần",
                        items = danhSachTuanTheoNam.filter { it.MaNam == selectedNamHoc?.MaNam },
                        selectedItem = selectedTuanTu,
                        itemLabel = { it.TenTuan },
                        onItemSelected = { selectedTuanTu = it }
                    )
                }

                item {
                    Text("Tuần Kết Thúc", fontWeight = FontWeight.Bold, color = Color.Black)
                    CustomDropdownSelector(
                        label = "Đến tuần",
                        items = danhSachTuanTheoNam.filter { it.MaNam == selectedNamHoc?.MaNam },
                        selectedItem = selectedTuanDen,
                        itemLabel = { it.TenTuan },
                        onItemSelected = { selectedTuanDen = it }
                    )
                }

                item {
                    Text("Thứ", fontWeight = FontWeight.Bold, color = Color.Black)

                    CustomDropdownSelector(
                        label = "Thứ",
                        items = danhSachThu,
                        selectedItem = selectedThu,
                        itemLabel = { it },
                        onItemSelected = { selectedThu = it }
                    )
                }

                item {
                    Text("Ca Học", fontWeight = FontWeight.Bold, color = Color.Black)

                    CustomDropdownSelector(
                        label = "Ca Học",
                        items = danhsachcahoc,
                        selectedItem = selectedCaHoc,
                        itemLabel = { it.TenCa },
                        onItemSelected = { selectedCaHoc = it }
                    )
                }

                item {
                    Text("Môn", fontWeight = FontWeight.Bold, color = Color.Black)
                    CustomDropdownSelector(
                        label = "Môn học",
                        items = danhSachMonHoc,
                        selectedItem = selectedMonHoc,
                        itemLabel = { it.TenMonHoc },
                        onItemSelected = { selectedMonHoc = it }
                    )
                }

//                item {
//                    Text("Ghi chú", fontWeight = FontWeight.Bold, color = Color.Black)
//                    OutlinedTextField(
//                        value = ghiChu,
//                        onValueChange = { ghiChu = it },
//                        placeholder = { Text("Nhập ghi chú (nếu có)") },
//                        modifier = Modifier
//                            .padding(bottom = 16.dp)
//                            .fillMaxWidth(),
//                        shape = RoundedCornerShape(12.dp),
//                        colors = OutlinedTextFieldDefaults.colors(
//                            focusedTextColor = Color.Black,
//                            unfocusedTextColor = Color.Black,
//                            unfocusedContainerColor = Color.White,
//                            focusedContainerColor = Color.White,
//                            focusedBorderColor = Color.Black,
//                            unfocusedBorderColor = Color.Black
//                        )
//                    )
//                }
            }


            Button(
                onClick = {
                    if (
                        selectedGiangVien != null && selectedPhong != null &&
                        selectedTuanTu != null && selectedTuanDen != null && selectedThu != null &&
                        selectedLop != null && selectedCaHoc != null && selectedMonHoc != null
                    ) {
                        val thuOffset = thuToOffset[selectedThu] ?: 0
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                        val tuanBatDauIndex = danhSachTuan.indexOfFirst { it.MaTuan == selectedTuanTu!!.MaTuan }
                        val tuanKetThucIndex = danhSachTuan.indexOfFirst { it.MaTuan == selectedTuanDen!!.MaTuan }

                        if (tuanBatDauIndex != -1 && tuanKetThucIndex != -1 && tuanBatDauIndex <= tuanKetThucIndex) {
                            val lichHocList = mutableListOf<LichHoc>()

                            for (i in tuanBatDauIndex..tuanKetThucIndex) {
                                val tuan = danhSachTuan[i]
                                val ngayDay = try {
                                    LocalDate.parse(tuan.NgayBatDau, formatter).plusDays(thuOffset.toLong())
                                        .format(formatter)
                                } catch (e: Exception) {
                                    null
                                }

                                if (ngayDay != null) {
                                    val isTrungCaTrongPhong = danhsachAllLichHoc.any {
                                        it.NgayDay == ngayDay &&
                                                it.MaPhong == selectedPhong!!.MaPhong &&
                                                it.MaCaHoc == selectedCaHoc!!.MaCaHoc
                                    }

                                    if (isTrungCaTrongPhong) {
                                        conflictMessage =
                                            "Trùng lịch:\nTuần ${tuan.TenTuan}\nNgày ${formatNgay(ngayDay)}, phòng ${selectedPhong?.TenPhong}, ca ${selectedCaHoc?.TenCa} đã có lịch dạy!"
                                        showDialog = true
                                        return@Button
                                    }



                                    val lichHoc = LichHoc(
                                        MaLichHoc = 0,
                                        MaGV = selectedGiangVien!!.MaGV,
                                        MaPhong = selectedPhong!!.MaPhong,
                                        MaLopHoc = selectedLop!!.MaLopHoc,
                                        MaMonHoc = selectedMonHoc!!.MaMonHoc,
                                        MaCaHoc = selectedCaHoc!!.MaCaHoc ?: 0,
                                        MaTuan = tuan.MaTuan.toInt(),
                                        Thu = selectedThu!!,
                                        NgayDay = ngayDay,
                                        GhiChu = "",
                                        TrangThai = 1
                                    )
                                    lichHocList.add(lichHoc)
                                }

                            }

                            lichhocViewModel.createListLichHoc(lichHocList)

                            Toast.makeText(context, "Đã tạo ${lichHocList.size} lịch học", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()

                        } else {
                            Toast.makeText(context, "Tuần bắt đầu/kết thúc không hợp lệ", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Vui lòng chọn đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0XFF1B8DDE))
            ) {
                Text("Thêm Lịch Dạy", color = Color.White, fontWeight = FontWeight.Bold)
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFD32F2F), // Red color
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Trùng lịch học",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFD32F2F)
                                )
                            )
                        }
                    },
                    text = {
                        Text(
                            text = conflictMessage,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = { showDialog = false },
                            modifier = Modifier
                                .padding(8.dp)
                                .background(Color(0xFFD32F2F), shape = RoundedCornerShape(8.dp))
                        ) {
                            Text(
                                "Đóng",
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> CustomDropdownSelector(
    label: String,
    items: List<T>,
    selectedItem: T?,
    itemLabel: (T) -> String,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true // ✅ thêm tham số enabled
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = !expanded }, // ✅ chỉ cho mở khi enabled
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedItem?.let { itemLabel(it) } ?: "",
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            trailingIcon = {
                if (enabled) {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            },
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            placeholder = { Text(label) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                disabledBorderColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                disabledTextColor = Color.Black
            )
        )


        if (enabled) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(itemLabel(item)) },
                        onClick = {
                            onItemSelected(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}





