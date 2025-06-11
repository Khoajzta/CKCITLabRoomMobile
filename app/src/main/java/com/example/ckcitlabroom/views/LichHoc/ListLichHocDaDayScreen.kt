import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.components.CardMonHoc
import com.example.lapstore.viewmodels.LichHocViewModel

@Composable
fun ListLichHocDaDayScreen(
    lichHocViewModel: LichHocViewModel,
    giangVienViewModel: GiangVienViewModel,
    sinhVienViewModel: SinhVienViewModel,
    namHocViewModel: NamHocViewModel,
    tuanViewModel: TuanViewModel,
    navController: NavHostController
) {
    val giangVien = giangVienViewModel.giangvienSet
    val sinhVien = sinhVienViewModel.sinhvienSet

    // Gọi API ban đầu và bắt đầu polling
    LaunchedEffect(Unit) {
        namHocViewModel.getAllNamHoc()
        tuanViewModel.getAllTuan()

        giangVien?.let {
            lichHocViewModel.startPollingLichHocByMaGV(it.MaGV)
        }

        sinhVien?.let {
            lichHocViewModel.startPollingLichHocByMaLopHoc(it.MaLop)
        }
    }

    // Ngừng polling khi rời màn hình
    DisposableEffect(Unit) {
        onDispose {
            lichHocViewModel.stopPolling()       // Dừng polling theo giảng viên
            lichHocViewModel.stopPollingSV()     // Dừng polling theo sinh viên
        }
    }

    val danhSachNamHoc = namHocViewModel.danhSachAllNamHoc
    val danhSachTuan = tuanViewModel.danhSachAllTuan

    val danhSachLichGV = lichHocViewModel.danhSachLichHoctheomagv
    val danhSachLichSV = lichHocViewModel.danhSachLichHoctheomalop

    val selectedNamHoc = remember(danhSachNamHoc) {
        danhSachNamHoc.firstOrNull { it.TrangThai == 1 }
    }

    val danhSachTuanTheoNam = remember(selectedNamHoc, danhSachTuan) {
        danhSachTuan.filter { it.MaNam == selectedNamHoc?.MaNam }
    }

    var selectedTuan by remember { mutableStateOf<Tuan?>(null) }

    LaunchedEffect(danhSachTuanTheoNam) {
        if (selectedTuan == null && danhSachTuanTheoNam.isNotEmpty()) {
            selectedTuan = danhSachTuanTheoNam.first()
        }
    }

    // Chọn đúng danh sách lịch tuỳ vai trò
    val lichHocTheoTuan = remember(danhSachLichGV, danhSachLichSV, selectedTuan) {
        selectedTuan?.let { tuan ->
            val danhSach = when {
                giangVien != null -> danhSachLichGV
                sinhVien != null -> danhSachLichSV
                else -> emptyList()
            }
            danhSach.filter { it.TrangThai == 0 && it.MaTuan == tuan.MaTuan }
        } ?: emptyList()
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // Tiêu đề
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = if (giangVien != null) "Danh Sách Lịch Dạy" else "Danh Sách Lịch Học",
                fontWeight = FontWeight.ExtraBold,
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

        // Dropdown chọn tuần
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tuần", fontWeight = FontWeight.Bold)
                CustomDropdownSelector(
                    label = "Tuần",
                    items = danhSachTuanTheoNam,
                    selectedItem = selectedTuan,
                    itemLabel = { it.TenTuan },
                    onItemSelected = { selectedTuan = it }
                )
            }
        }

        // Danh sách lịch
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (lichHocTheoTuan.isEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Không có lịch.",
                            color = Color.Black,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                items(lichHocTheoTuan) { lichhoc ->
                    when {
                        giangVien != null -> CardLichHoc(lichhoc, giangVien = giangVien, sinhvien = null,navController)
                        sinhVien != null -> CardLichHoc(lichhoc, giangVien = null ,sinhvien = sinhVien, navController)
                    }
                }
            }
        }
    }
}





