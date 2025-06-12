import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
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
import kotlin.math.sinh

@Composable
fun EditLichHocScreen(
    malichhoc: String,
    navController: NavHostController,
    lichhocViewModel: LichHocViewModel,
    namHocViewModel: NamHocViewModel,
    monHocViewModel: MonHocViewModel,
    tuanViewModel: TuanViewModel,
    giangvienViewModel: GiangVienViewModel,
    lopHocViewModel: LopHocViewModel,
    phongMayViewModel: PhongMayViewModel,
    caHocViewModel: CaHocViewModel,
    sinhVienViewModel: SinhVienViewModel,
    notificationViewModel: NotificationViewModel
) {
    val context = LocalContext.current

    var lichhoc by remember { mutableStateOf<LichHoc?>(null) }
    var originalMaLopHoc by remember { mutableStateOf<String?>(null) }

    var selectedGiangVien by remember { mutableStateOf<GiangVien?>(null) }
    var selectedPhong by remember { mutableStateOf<PhongMay?>(null) }
    var selectedTuanTu by remember { mutableStateOf<Tuan?>(null) }
    var selectedTuanDen by remember { mutableStateOf<Tuan?>(null) }
    var selectedMonHoc by remember { mutableStateOf<MonHoc?>(null) }
    var selectedThu by remember { mutableStateOf<String?>(null) }
    var selectedLop by remember { mutableStateOf<LopHoc?>(null) }
    var selectedCaHoc by remember { mutableStateOf<CaHoc?>(null) }
    var ghiChu by remember { mutableStateOf("") }

    val danhSachGiangVien = giangvienViewModel.danhSachAllGiangVien
    val danhSachPhong = phongMayViewModel.danhSachAllPhongMay
    val danhSachNamHoc = namHocViewModel.danhSachAllNamHoc
    val danhSachTuan = tuanViewModel.danhSachAllTuan
    val danhSachMonHoc = monHocViewModel.danhSachAllMonHoc.filter { it.TrangThai == 1 }
    val danhSachLopHoc = lopHocViewModel.danhSachAllLopHoc.filter { it.TrangThai == 1 }
    val danhSachCaHoc = caHocViewModel.danhSachAllCaHoc.filter { it.TrangThai == 1 }
    val danhSachTokenSinhVienTheoLop by rememberUpdatedState(newValue = sinhVienViewModel.danhSachToken)


    val danhSachThu = listOf("Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ Nhật")
    val thuToOffset = mapOf(
        "Thứ 2" to 0, "Thứ 3" to 1, "Thứ 4" to 2,
        "Thứ 5" to 3, "Thứ 6" to 4, "Thứ 7" to 5,
        "Chủ Nhật" to 6
    )

    val selectedNamHoc = remember(danhSachNamHoc) {
        danhSachNamHoc.firstOrNull { it.TrangThai == 1 }
    }

    val danhSachTuanTheoNam = remember(selectedNamHoc, danhSachTuan) {
        danhSachTuan.filter { it.MaNam == selectedNamHoc?.MaNam }
    }

    // Load dữ liệu ban đầu
    LaunchedEffect(Unit) {
        namHocViewModel.getAllNamHoc()
        tuanViewModel.getAllTuan()
        giangvienViewModel.getAllGiangVien()
        phongMayViewModel.getAllPhongMay()
        monHocViewModel.getAllMonHoc()
        lopHocViewModel.getAllLopHoc()
        caHocViewModel.getAllCaHoc()
        lichhocViewModel.getLichHocByMaLich(malichhoc)
    }

    // Gán dữ liệu vào dropdown khi lichhoc được load
    LaunchedEffect(lichhocViewModel.lichhoc, danhSachGiangVien, danhSachPhong, danhSachLopHoc, danhSachMonHoc, danhSachCaHoc, danhSachTuanTheoNam) {
        val data = lichhocViewModel.lichhoc
        if (data != null) {
            lichhoc = data
            originalMaLopHoc = data.MaLopHoc
            selectedGiangVien = danhSachGiangVien.find { it.MaGV == data.MaGV }
            selectedPhong = danhSachPhong.find { it.MaPhong == data.MaPhong }
            selectedLop = danhSachLopHoc.find { it.MaLopHoc == data.MaLopHoc }
            selectedMonHoc = danhSachMonHoc.find { it.MaMonHoc == data.MaMonHoc }
            selectedCaHoc = danhSachCaHoc.find { it.MaCaHoc == data.MaCaHoc }
            selectedTuanTu = danhSachTuanTheoNam.find { it.MaTuan == data.MaTuan }
            selectedTuanDen = selectedTuanTu
            selectedThu = danhSachThu.find { it == data.Thu }

            // Lấy danh sách token theo lớp gốc
            sinhVienViewModel.getTokensByMaLop(data.MaLopHoc)
        }
    }

    // Cập nhật token khi đổi lớp
    LaunchedEffect(selectedLop?.MaLopHoc) {
        selectedLop?.MaLopHoc?.let { maLop ->
            sinhVienViewModel.getTokensByMaLop(maLop)
        }
    }

    Log.d("DanhSachToken", "Danh sách token: $danhSachTokenSinhVienTheoLop")



    // UI hiển thị
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
                text = "Chỉnh sửa Lịch Học",
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
                // Ở trong LazyColumn:

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
                        items = danhSachLopHoc,
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
                        items = danhSachCaHoc,
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

                item {
                    Text("Ghi chú", fontWeight = FontWeight.Bold, color = Color.Black)
                    OutlinedTextField(
                        value = ghiChu,
                        onValueChange = { ghiChu = it },
                        placeholder = { Text("Nhập ghi chú (nếu có)") },
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black
                        )
                    )
                }

            }

            Button(
                onClick = {
                    if (
                        selectedGiangVien != null &&
                        selectedPhong != null &&
                        selectedLop != null &&
                        selectedTuanTu != null &&
                        selectedTuanDen != null &&
                        selectedThu != null &&
                        selectedCaHoc != null &&
                        selectedMonHoc != null &&
                        selectedNamHoc != null
                    ) {
                        val tuan = selectedTuanTu!!
                        val thu = selectedThu!!
                        val offset = thuToOffset[thu] ?: 0
                        val ngayDay = try {
                            val ngayBatDau = LocalDate.parse(tuan.NgayBatDau, DateTimeFormatter.ISO_DATE)
                            ngayBatDau.plusDays(offset.toLong()).format(DateTimeFormatter.ISO_DATE)
                        } catch (e: Exception) {
                            ""
                        }

                        val newLichHoc = LichHoc(
                            MaLichHoc = malichhoc.toIntOrNull() ?: 0,
                            MaGV = selectedGiangVien!!.MaGV,
                            MaPhong = selectedPhong!!.MaPhong,
                            NgayDay = ngayDay,
                            MaLopHoc = selectedLop!!.MaLopHoc,
                            MaCaHoc = selectedCaHoc!!.MaCaHoc!!,
                            MaMonHoc = selectedMonHoc!!.MaMonHoc,
                            Thu = thu,
                            MaTuan = tuan.MaTuan,
                            GhiChu = ghiChu,
                            TrangThai = lichhoc?.TrangThai ?: 0
                        )


                        // Cập nhật lịch học
                        lichhocViewModel.updateLichHoc(newLichHoc)

                        // Gửi thông báo FCM
                        val uniqueTokens = danhSachTokenSinhVienTheoLop.distinct()
                        if (uniqueTokens.isNotEmpty()) {
                            val title = "Thông báo lịch học"
                            val body = "Lịch học môn ${selectedMonHoc!!.TenMonHoc} của lớp ${selectedLop!!.TenLopHoc}\n Thông báo: ${ghiChu}."
                            notificationViewModel.sendNotificationToTokens(uniqueTokens, title, body)
                        }


                        Toast.makeText(context, "Cập nhật lịch học thành công", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    } else {
                        Toast.makeText(context, "Vui lòng chọn đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                    }
                }

                ,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0XFF1B8DDE))
            ) {
                Text("Cập nhật Lịch Dạy", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}







