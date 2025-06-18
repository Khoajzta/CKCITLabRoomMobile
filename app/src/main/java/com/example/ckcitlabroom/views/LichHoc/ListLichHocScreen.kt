import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.viewmodels.LichHocViewModel

@Composable
fun ListLichHocScreen(
    lichHocViewModel: LichHocViewModel,
    giangVienViewModel: GiangVienViewModel,
    sinhVienViewModel: SinhVienViewModel,
    namHocViewModel: NamHocViewModel,
    tuanViewModel: TuanViewModel,
    navController: NavHostController
) {
    val giangVien = giangVienViewModel.giangvienSet
    val sinhVien = sinhVienViewModel.sinhvienSet

    val thuList = listOf("Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ Nhật")

    LaunchedEffect(Unit) {
        namHocViewModel.getAllNamHoc()
        tuanViewModel.getAllTuan()
        giangVienViewModel.getAllGiangVien()
    }

// Danh sách
    val danhsachnamhoc = namHocViewModel.danhSachAllNamHoc.firstOrNull { it.TrangThai == 1 }
    val danhsachtuantheonam = tuanViewModel.danhSachAllTuan.filter { it.MaNam == danhsachnamhoc?.MaNam }
    val danhsachgiangvien = giangVienViewModel.danhSachAllGiangVien.filter { it.TrangThai == 1 }

// Selected states
    var selectedTuan by remember { mutableStateOf<Tuan?>(null) }
    var selectedGV by remember { mutableStateOf<GiangVien?>(null) }

// Gán giảng viên mặc định
    LaunchedEffect(giangVien, danhsachgiangvien) {
        if (selectedGV == null && giangVien != null) {
            selectedGV = danhsachgiangvien.find { it.MaGV == giangVien.MaGV }
        }
    }

// Gán tuần mặc định
    LaunchedEffect(danhsachtuantheonam) {
        if (selectedTuan == null && danhsachtuantheonam.isNotEmpty()) {
            selectedTuan = danhsachtuantheonam.first()
        }
    }

// Gọi polling nếu là sinh viên
    LaunchedEffect(sinhVien?.MaLop) {
        sinhVien?.let {
            lichHocViewModel.startPollingLichHocByMaLopHoc(it.MaLop)
        }
    }

// 🔥 GỌI API lấy lịch học theo giảng viên mỗi khi chọn giảng viên mới
    LaunchedEffect(selectedGV?.MaGV) {
        selectedGV?.let {
            lichHocViewModel.getLichHocByMaGV(it.MaGV)
        }
    }

// Danh sách hiện tại
    val danhsachlichhocSV = lichHocViewModel.danhSachLichHoctheomalop
    val danhsachlichdayGV = lichHocViewModel.danhSachLichHoctheomagv

// Danh sách lọc theo tuần
    var danhSachLichHocSVTheoTuan by remember { mutableStateOf<List<LichHocRP>>(emptyList()) }
    var danhsachlichdayGVthuongTheoTuan by remember { mutableStateOf<List<LichHocRP>>(emptyList()) }
    var danhsachlichdayAdminTheoTuan by remember { mutableStateOf<List<LichHocRP>>(emptyList()) }

// Lọc theo tuần và giảng viên
    LaunchedEffect(selectedTuan?.MaTuan, danhsachlichdayGV, danhsachlichhocSV, selectedGV?.MaGV) {
        selectedTuan?.let { tuan ->
            val maTuan = tuan.MaTuan
            val maGV = selectedGV?.MaGV

            // Lịch học sinh viên
            danhSachLichHocSVTheoTuan = danhsachlichhocSV.filter {
                it.MaTuan == maTuan && it.TrangThai == 1
            }

            // Lịch dạy thường (giảng viên tự xem)
            danhsachlichdayGVthuongTheoTuan = danhsachlichdayGV.filter {
                it.MaTuan == maTuan && it.TrangThai == 1
            }

            // Lịch dạy admin chọn theo GV
            danhsachlichdayAdminTheoTuan = if (maGV != null) {
                danhsachlichdayGV.filter {
                    it.MaTuan == maTuan && it.TrangThai == 1 && it.MaGV == maGV
                }
            } else emptyList()
        }
    }


    DisposableEffect(Unit) {
        onDispose {
            lichHocViewModel.stopPolling()
            lichHocViewModel.stopPollingSV()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Tiêu đề
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (giangVien != null) "Danh Sách Lịch Dạy" else "Danh Sách Lịch Học",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = Color(0xFF1B8DDE)
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            thickness = 2.dp,
            color = Color(0xFF1B8DDE),
        )

        // Dropdown chọn tuần (và giảng viên nếu là admin)
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (giangVien?.MaLoaiTaiKhoan == 1) {
                CustomDropdownSelector(
                    modifier = Modifier.width(180.dp),
                    label = "Tuần",
                    items = danhsachtuantheonam,
                    selectedItem = selectedTuan,
                    itemLabel = { it.TenTuan },
                    onItemSelected = { selectedTuan = it }
                )

                CustomDropdownSelector(
                    modifier = Modifier.width(190.dp),
                    label = "Giảng viên",
                    items = danhsachgiangvien,
                    selectedItem = selectedGV,
                    itemLabel = { it.TenGiangVien },
                    onItemSelected = { selectedGV = it }
                )
            } else {
                CustomDropdownSelector(
                    modifier = Modifier.fillMaxWidth(),
                    label = "Tuần",
                    items = danhsachtuantheonam,
                    selectedItem = selectedTuan,
                    itemLabel = { it.TenTuan },
                    onItemSelected = { selectedTuan = it }
                )
            }
        }

        // Hàm nhóm lịch học theo thứ

        fun groupLichHocByThu(danhSach: List<LichHocRP>): Map<String, List<LichHocRP>> {
            return danhSach.groupBy { it.Thu }
        }

        when (giangVien?.MaLoaiTaiKhoan) {
            1 -> {
                // ADMIN
                val grouped = remember(danhsachlichdayAdminTheoTuan) {
                    groupLichHocByThu(danhsachlichdayAdminTheoTuan)
                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    if (grouped.isEmpty()) {
                        item {
                            Text(
                                text = "Giảng viên ${selectedGV?.TenGiangVien ?: "?"} không có lịch dạy trong tuần ${selectedTuan?.TenTuan ?: ""}",
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                        }
                    } else {
                        thuList.forEach { thu ->
                            val lichTrongThu = grouped[thu] ?: emptyList()
                            if (lichTrongThu.isNotEmpty()) {
                                item {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = thu,
                                            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 4.dp),
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 18.sp,
                                            color = Color.Black
                                        )

                                        LazyRow(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(end = 12.dp)
                                        ) {
                                            items(lichTrongThu) { lichhoc ->
                                                CardLichHoc(
                                                    lichhoc,
                                                    giangVien = giangVien,
                                                    navController = navController
                                                )
                                                Spacer(modifier = Modifier.width(12.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // GIẢNG VIÊN
                val grouped = remember(danhsachlichdayGVthuongTheoTuan) {
                    groupLichHocByThu(danhsachlichdayGVthuongTheoTuan)
                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    if (grouped.isEmpty()) {
                        item {
                            Text(
                                text = "Không có lịch dạy trong tuần ${selectedTuan?.TenTuan ?: ""}",
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                        }
                    } else {
                        thuList.forEach { thu ->
                            val lichTrongThu = grouped[thu] ?: emptyList()
                            if (lichTrongThu.isNotEmpty()) {
                                item {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = thu,
                                            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 4.dp),
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 18.sp,
                                            color = Color.Black
                                        )

                                        LazyRow(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(end = 12.dp)
                                        ) {
                                            items(lichTrongThu) { lichhoc ->
                                                CardLichHoc(
                                                    lichhoc,
                                                    giangVien = giangVien,
                                                    navController = navController
                                                )
                                                Spacer(modifier = Modifier.width(12.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            else -> {
                // SINH VIÊN
                val grouped = remember(danhSachLichHocSVTheoTuan) {
                    groupLichHocByThu(danhSachLichHocSVTheoTuan)
                }

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    if (grouped.isEmpty()) {
                        item {
                            Text(
                                text = "Không có lịch học trong tuần ${selectedTuan?.TenTuan ?: ""}",
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                        }
                    } else {
                        thuList.forEach { thu ->
                            val lichTrongThu = grouped[thu] ?: emptyList()
                            if (lichTrongThu.isNotEmpty()) {
                                item {
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = thu,
                                            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 4.dp),
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 18.sp,
                                            color = Color.Black
                                        )

                                        LazyRow(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(end = 12.dp)
                                        ) {
                                            items(lichTrongThu) { lichhoc ->
                                                CardLichHoc(
                                                    lichhoc,
                                                    sinhvien = sinhVien,
                                                    navController = navController
                                                )
                                                Spacer(modifier = Modifier.width(12.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}










