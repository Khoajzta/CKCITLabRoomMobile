import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.ckcitlabroom.viewmodels.LichHocViewModel
import com.example.ckcitlabroom.viewmodels.LopHocViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

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
    /* ────── STATE & VIEWMODEL DATA ────── */
    val context = LocalContext.current
    val giangvien = giangvienViewModel.giangvienSet
    val choPhepChinhSua = giangvien?.MaLoaiTaiKhoan == 1

    /** Biến hiển thị dialog lỗi */
    var showDialog by remember { mutableStateOf(false) }
    var conflictMessage by remember { mutableStateOf("") }

    /** Lịch đang chỉnh và dữ liệu gốc */
    var lichhoc by remember { mutableStateOf<LichHoc?>(null) }
    var originalMaLopHoc by remember { mutableStateOf<String?>(null) }

    /** Dropdown state */
    var selectedGiangVien by remember { mutableStateOf<GiangVien?>(null) }
    var selectedPhong by remember { mutableStateOf<PhongMay?>(null) }
    var selectedTuanTu by remember { mutableStateOf<Tuan?>(null) }
    var selectedTuanDen by remember { mutableStateOf<Tuan?>(null) }
    var selectedMonHoc by remember { mutableStateOf<MonHoc?>(null) }
    var selectedThu by remember { mutableStateOf<String?>(null) }
    var selectedLop by remember { mutableStateOf<LopHoc?>(null) }
    var selectedCaHoc by remember { mutableStateOf<CaHoc?>(null) }
    var ghiChu by remember { mutableStateOf("") }

    /** Danh sách dữ liệu */
    val danhSachGiangVien =
        giangvienViewModel.danhSachAllGiangVien.filter { it.TrangThai == 1 }?.sortedBy { gv ->
            gv.TenGiangVien.trim()
                .split("\\s+".toRegex())
                .last()
                .lowercase()
        } ?: emptyList()
    val danhSachPhong = phongMayViewModel.danhSachAllPhongMay
    val danhSachNamHoc = namHocViewModel.danhSachAllNamHoc
    val danhSachTuan = tuanViewModel.danhSachAllTuan
    val danhSachMonHoc = monHocViewModel.danhSachAllMonHoc.filter { it.TrangThai == 1 }
    val danhSachLopHoc = lopHocViewModel.danhSachAllLopHoc.filter { it.TrangThai == 1 }
    val danhSachAllLichHoc = lichhocViewModel.danhSachLichHoc.filter { it.TrangThai == 1 }

    val danhSachTokenSinhVienTheoLop by rememberUpdatedState(sinhVienViewModel.danhSachToken)
    val danhSachAllSinhVien = sinhVienViewModel.danhSachAllSinhVien
    var danhSachSinhVienTheoLop = danhSachAllSinhVien.filter { it.MaLop == lichhoc?.MaLopHoc }

    // Thứ – Offset 
    val danhSachThu = listOf("Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ Nhật")
    val thuToOffset = mapOf(
        "Thứ 2" to 0, "Thứ 3" to 1, "Thứ 4" to 2,
        "Thứ 5" to 3, "Thứ 6" to 4, "Thứ 7" to 5,
        "Chủ Nhật" to 6
    )

    // Filter ca học đơn giản: chỉ lấy những ca còn hoạt động
    val danhSachCaHoc = caHocViewModel.danhSachAllCaHoc.filter { it.TrangThai == 1 }

    // Filter lớp học chưa có ca học trong thứ và tuần được chọn (bao gồm lớp hiện tại)
    val danhsachlopchualichday = remember(
        selectedThu, selectedTuanTu, selectedTuanDen, selectedCaHoc,
        danhSachLopHoc, danhSachAllLichHoc, danhSachTuan, lichhoc
    ) {
        if (selectedThu != null && selectedTuanTu != null && selectedTuanDen != null && selectedCaHoc != null) {
            // Tính toán tất cả các ngày dạy dựa trên tuần từ - tuần đến
            val thuOffset = thuToOffset[selectedThu] ?: 0
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            val tuanBatDauIndex = danhSachTuan.indexOfFirst { it.MaTuan == selectedTuanTu?.MaTuan }
            val tuanKetThucIndex =
                danhSachTuan.indexOfFirst { it.MaTuan == selectedTuanDen?.MaTuan }

            val ngayDayList = mutableListOf<String>()

            if (tuanBatDauIndex != -1 && tuanKetThucIndex != -1 && tuanBatDauIndex <= tuanKetThucIndex) {
                for (i in tuanBatDauIndex..tuanKetThucIndex) {
                    val tuan = danhSachTuan[i]
                    try {
                        val ngayDay = LocalDate.parse(tuan.NgayBatDau, formatter)
                            .plusDays(thuOffset.toLong())
                            .format(formatter)
                        ngayDayList.add(ngayDay)
                    } catch (e: Exception) {
                        // Bỏ qua ngày không hợp lệ
                    }
                }
            }

            // Lọc ra những lớp đã có lịch học trong ca và các ngày cụ thể này
            // Loại trừ lịch học hiện tại đang được edit
            val usedLopHoc = danhSachAllLichHoc.filter { lichHoc ->
                lichHoc.MaCaHoc == selectedCaHoc?.MaCaHoc &&
                        lichHoc.NgayDay in ngayDayList &&
                        lichHoc.MaLichHoc != lichhoc?.MaLichHoc // Loại trừ lịch hiện tại
            }.map { it.MaLopHoc }.toSet()

            // Chỉ hiển thị những lớp chưa có lịch học
            danhSachLopHoc.filter { lopHoc ->
                lopHoc.MaLopHoc !in usedLopHoc
            }
        } else {
            danhSachLopHoc
        }
    }

    /** Selected năm học mặc định */
    val selectedNamHoc =
        remember(danhSachNamHoc) { danhSachNamHoc.firstOrNull { it.TrangThai == 1 } }
    val danhSachTuanTheoNam = remember(selectedNamHoc, danhSachTuan) {
        danhSachTuan.filter { it.MaNam == selectedNamHoc?.MaNam }
    }

    /* ────── LOAD DATA ONCE ────── */
    LaunchedEffect(Unit) {
        namHocViewModel.getAllNamHoc()
        tuanViewModel.getAllTuan()
        giangvienViewModel.getAllGiangVien()
        phongMayViewModel.getAllPhongMay()
        monHocViewModel.getAllMonHoc()
        lopHocViewModel.getAllLopHoc()
        caHocViewModel.getAllCaHoc()
        lichhocViewModel.getLichHocByMaLich(malichhoc)
        sinhVienViewModel.getAllSinhVien()
    }

    /* ────── KHI LỊCH ĐƯỢC TRẢ VỀ, GÁN VÀO DROPDOWN ────── */
    LaunchedEffect(
        lichhocViewModel.lichhoc, danhSachGiangVien, danhSachPhong,
        danhSachLopHoc, danhSachMonHoc, danhSachCaHoc, danhSachTuanTheoNam
    ) {
        lichhocViewModel.lichhoc?.let { data ->
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

            /** Lấy token SV lớp gốc */
            sinhVienViewModel.getTokensByMaLop(data.MaLopHoc)
        }
    }

    /* ────── TOKEN SV THAY ĐỔI KHI ĐỔI LỚP ────── */
    LaunchedEffect(selectedLop?.MaLopHoc) {
        selectedLop?.MaLopHoc?.let(sinhVienViewModel::getTokensByMaLop)
    }

    /* ────── UI ────── */
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
                "Chỉnh sửa Lịch Học",
                modifier = Modifier.padding(bottom = 16.dp),
                color = Color(0xFF1B8DDE),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )

            HorizontalDivider(
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                thickness = 2.dp,
                color = Color(0xFF1B8DDE)
            )

            /* ────── FORM ────── */
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {

                item {
                    androidx.compose.material.Text(
                        "Tuần Bắt Đầu",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    CustomDropdownSelector(
                        label = "Từ tuần",
                        items = danhSachTuanTheoNam.filter { it.MaNam == selectedNamHoc?.MaNam },
                        selectedItem = selectedTuanTu,
                        itemLabel = { it.TenTuan },
                        onItemSelected = { selectedTuanTu = it }
                    )
                }

                item {
                    androidx.compose.material.Text(
                        "Tuần Kết Thúc",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    CustomDropdownSelector(
                        label = "Đến tuần",
                        items = danhSachTuanTheoNam.filter { it.MaNam == selectedNamHoc?.MaNam },
                        selectedItem = selectedTuanDen,
                        itemLabel = { it.TenTuan },
                        onItemSelected = { selectedTuanDen = it }
                    )
                }

                item {
                    androidx.compose.material.Text(
                        "Thứ",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    CustomDropdownSelector(
                        label = "Thứ",
                        items = danhSachThu,
                        selectedItem = selectedThu,
                        itemLabel = { it },
                        onItemSelected = { selectedThu = it }
                    )
                }

                item {
                    androidx.compose.material.Text(
                        "Phòng Dạy",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    CustomDropdownSelector(
                        label = "Phòng",
                        items = danhSachPhong,
                        selectedItem = selectedPhong,
                        itemLabel = { it.TenPhong },
                        onItemSelected = { selectedPhong = it }
                    )
                }

                item {
                    androidx.compose.material.Text(
                        "Giảng Viên",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    CustomDropdownSelector(
                        label = "Giảng viên",
                        items = danhSachGiangVien,
                        selectedItem = selectedGiangVien,
                        itemLabel = { it.TenGiangVien },
                        onItemSelected = { selectedGiangVien = it }
                    )
                }

                item {
                    androidx.compose.material.Text(
                        "Ca Học",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    CustomDropdownSelector(
                        label = "Ca Học",
                        items = danhSachCaHoc,
                        selectedItem = selectedCaHoc,
                        itemLabel = { it.TenCa },
                        onItemSelected = { selectedCaHoc = it }
                    )
                }

                item {
                    androidx.compose.material.Text(
                        "Lớp",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    CustomDropdownSelector(
                        label = "Lớp",
                        items = danhsachlopchualichday,
                        selectedItem = selectedLop,
                        itemLabel = { it.TenLopHoc },
                        onItemSelected = { selectedLop = it }
                    )
                }

                item {
                    androidx.compose.material.Text(
                        "Môn",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    CustomDropdownSelector(
                        label = "Môn học",
                        items = danhSachMonHoc,
                        selectedItem = selectedMonHoc,
                        itemLabel = { it.TenMonHoc },
                        onItemSelected = { selectedMonHoc = it }
                    )
                }

                /* Ghi chú */
                item {
                    Text("Thông báo", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = ghiChu,
                        onValueChange = { ghiChu = it },
                        placeholder = { Text("Nhập thông báo (nếu có)") },
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

            /* ────── NÚT CẬP NHẬT ────── */
            Button(
                onClick = {
                    /* 1. Kiểm tra đã chọn đủ */
                    if (selectedGiangVien == null || selectedPhong == null ||
                        selectedLop == null || selectedTuanTu == null ||
                        selectedThu == null || selectedCaHoc == null ||
                        selectedMonHoc == null || selectedNamHoc == null
                    ) {
                        Toast.makeText(
                            context,
                            "Vui lòng chọn đầy đủ thông tin",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    if (ghiChu == "") {
                        Toast.makeText(context, "Vui lòng nhập thông báo", Toast.LENGTH_SHORT)
                            .show()
                        return@Button
                    }

                    /* 2. Tính ngày dạy */
                    val thuOffset = thuToOffset[selectedThu] ?: 0
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val ngayDayStr = runCatching {
                        LocalDate.parse(selectedTuanTu!!.NgayBatDau, formatter)
                            .plusDays(thuOffset.toLong())
                            .format(formatter)
                    }.getOrElse {
                        Toast.makeText(context, "Không tính được ngày dạy", Toast.LENGTH_SHORT)
                            .show()
                        return@Button
                    }

                    /* 3. Thông tin ca */
                    val caStart = LocalTime.parse(selectedCaHoc!!.GioBatDau)
                    val caEnd = LocalTime.parse(selectedCaHoc!!.GioKetThuc)
                    val ngayDate = LocalDate.parse(ngayDayStr, formatter)
                    val gioKetThuc = LocalDateTime.of(ngayDate, caEnd)

                    /* 4. Lọc lịch khác (trừ chính mình) */
                    val lichKhac =
                        danhSachAllLichHoc.filter { it.MaLichHoc != malichhoc.toIntOrNull() }

                    /* 5. Các kiểm tra */
                    // 5.1 – Quá khứ
                    if (gioKetThuc.isBefore(LocalDateTime.now())) {
                        conflictMessage =
                            "Không thể đặt lịch quá khứ (${formatNgay(ngayDayStr)} ${selectedCaHoc!!.TenCa})."
                        showDialog = true; return@Button
                    }

                    // 5.2 – Trùng phòng + ca
                    if (lichKhac.any {
                            it.NgayDay == ngayDayStr &&
                                    it.MaPhong == selectedPhong!!.MaPhong &&
                                    it.MaCaHoc == selectedCaHoc!!.MaCaHoc
                        }) {
                        conflictMessage =
                            "Phòng ${selectedPhong!!.TenPhong} đã có lịch ${selectedCaHoc!!.TenCa} ngày ${
                                formatNgay(ngayDayStr)
                            }."
                        showDialog = true; return@Button
                    }

                    // 5.3 – Trùng lớp + ca
                    if (lichKhac.any {
                            it.NgayDay == ngayDayStr &&
                                    it.MaLopHoc == selectedLop!!.MaLopHoc &&
                                    it.MaCaHoc == selectedCaHoc!!.MaCaHoc
                        }) {
                        conflictMessage =
                            "Lớp ${selectedLop!!.TenLopHoc} đã có lịch ${selectedCaHoc!!.TenCa} ngày ${
                                formatNgay(ngayDayStr)
                            }."
                        showDialog = true; return@Button
                    }

                    // 5.4 – Trùng giảng viên + ca
                    if (lichKhac.any {
                            it.NgayDay == ngayDayStr &&
                                    it.MaGV == selectedGiangVien!!.MaGV &&
                                    it.MaCaHoc == selectedCaHoc!!.MaCaHoc
                        }) {
                        conflictMessage =
                            "Giảng viên ${selectedGiangVien!!.TenGiangVien} đã có lịch ${selectedCaHoc!!.TenCa} ngày ${
                                formatNgay(ngayDayStr)
                            }."
                        showDialog = true; return@Button
                    }

                    // 5.4b – Chồng giờ giảng viên
                    val overlapGiangVien = lichKhac
                        .filter { it.NgayDay == ngayDayStr && it.MaGV == selectedGiangVien!!.MaGV }
                        .any { old ->
                            val oldCa = danhSachCaHoc.firstOrNull { it.MaCaHoc == old.MaCaHoc }
                                ?: return@any false
                            val oldStart = LocalTime.parse(oldCa.GioBatDau)
                            val oldEnd = LocalTime.parse(oldCa.GioKetThuc)
                            !(caEnd.isBefore(oldStart) || oldEnd.isBefore(caStart))
                        }
                    if (overlapGiangVien) {
                        conflictMessage =
                            "Khung giờ trùng với lịch khác của giảng viên ${selectedGiangVien!!.TenGiangVien} ngày ${
                                formatNgay(
                                    ngayDayStr
                                )
                            }."
                        showDialog = true; return@Button
                    }

                    // 5.5 – Chồng giờ trong phòng
                    val overlapTrongPhong = lichKhac
                        .filter { it.NgayDay == ngayDayStr && it.MaPhong == selectedPhong!!.MaPhong }
                        .any { old ->
                            val oldCa = danhSachCaHoc.firstOrNull { it.MaCaHoc == old.MaCaHoc }
                                ?: return@any false
                            val oldStart = LocalTime.parse(oldCa.GioBatDau)
                            val oldEnd = LocalTime.parse(oldCa.GioKetThuc)
                            !(caEnd.isBefore(oldStart) || oldEnd.isBefore(caStart))
                        }
                    if (overlapTrongPhong) {
                        conflictMessage =
                            "Trùng ca trong phòng ${selectedPhong!!.TenPhong} ngày ${
                                formatNgay(ngayDayStr)
                            }."
                        showDialog = true; return@Button
                    }

                    /* 6. Cập nhật */
                    val newLichHoc = LichHoc(
                        MaLichHoc = malichhoc.toIntOrNull() ?: 0,
                        MaGV = selectedGiangVien!!.MaGV,
                        MaPhong = selectedPhong!!.MaPhong,
                        NgayDay = ngayDayStr,
                        MaLopHoc = selectedLop!!.MaLopHoc,
                        MaCaHoc = selectedCaHoc!!.MaCaHoc!!,
                        MaMonHoc = selectedMonHoc!!.MaMonHoc,
                        Thu = selectedThu!!,
                        MaTuan = selectedTuanTu!!.MaTuan,
                        GhiChu = ghiChu,
                        TrangThai = lichhoc?.TrangThai ?: 0
                    )

                    /* Cập nhật DB */
                    lichhocViewModel.updateLichHoc(newLichHoc)

                    /* Gửi thông báo */
                    val now = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    val title = "Thông báo lịch học"
                    val body =
                        "Lịch học môn ${selectedMonHoc!!.TenMonHoc} của lớp ${selectedLop!!.TenLopHoc}\nThông báo: $ghiChu."

                    // SV cùng lớp
                    val svTokens = danhSachTokenSinhVienTheoLop.distinct()
                    if (svTokens.isNotEmpty()) notificationViewModel.sendNotificationToTokens(
                        svTokens,
                        title,
                        body
                    )

                    danhSachSinhVienTheoLop.forEach { sv ->
                        notificationViewModel.createThongBao(
                            ThongBao(
                                MaTB = 0,
                                TieuDe = title,
                                NoiDung = body,
                                MaLoaiTaiKhoan = 3,
                                MaNguoiDung = sv.MaSinhVien,
                                ThoiGian = now,
                                DaDoc = false
                            )
                        )
                    }

                    // GV nếu admin sửa lịch người khác
                    val isAdmin = giangvien?.MaLoaiTaiKhoan == 1
                    val isChinhMinh = giangvien?.MaGV == selectedGiangVien?.MaGV
                    if (isAdmin && !isChinhMinh) {
                        selectedGiangVien?.Token?.takeIf { it.isNotBlank() }?.let {
                            notificationViewModel.sendNotificationToTokens(listOf(it), title, body)
                        }
                        notificationViewModel.createThongBao(
                            ThongBao(
                                MaTB = 0,
                                TieuDe = title,
                                NoiDung = "Lịch dạy môn ${selectedMonHoc!!.TenMonHoc} của lớp ${selectedLop!!.TenLopHoc} đã được cập nhật.\nThông báo: $ghiChu.",
                                MaLoaiTaiKhoan = 2,
                                MaNguoiDung = selectedGiangVien!!.MaGV,
                                ThoiGian = now,
                                DaDoc = false
                            )
                        )
                    }

                    Toast.makeText(context, "Cập nhật lịch học thành công", Toast.LENGTH_SHORT)
                        .show()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0XFF1B8DDE))
            ) {
                Text("Cập nhật Lịch Dạy", color = Color.White, fontWeight = FontWeight.Bold)
            }

            /* ────── HỘP THOẠI LỖI ────── */
            if (showDialog) {
                AlertDialog(
                    containerColor = Color.White,
                    onDismissRequest = { showDialog = false },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Trùng lịch",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD32F2F)
                            )
                        }
                    },
                    text = {
                        Text(
                            conflictMessage,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = { showDialog = false },
                            modifier = Modifier
                                .padding(8.dp)
                                .background(Color(0xFFD32F2F), RoundedCornerShape(8.dp))
                        ) {
                            Text(
                                "Đóng",
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 8.dp
                )
            }
        }
    }
}
