import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.viewmodels.LichHocViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun HomeScreen(
    lichhocviewmodel: LichHocViewModel,
    giangVienViewModel: GiangVienViewModel,
    sinhVienViewModel: SinhVienViewModel,
    navController: NavHostController,
    namHocViewModel: NamHocViewModel,
    tuanViewModel: TuanViewModel
) {
    BackHandler {}

    LaunchedEffect(Unit) {
        namHocViewModel.getAllNamHoc()
        tuanViewModel.getAllTuan()
    }

    val sinhvien = sinhVienViewModel.sinhvienSet
    val giangVien = giangVienViewModel.giangvienSet

    LaunchedEffect(giangVien?.MaGV, sinhvien?.MaLop) {
        giangVien?.MaGV?.let { lichhocviewmodel.startPollingLichHocByMaGV(it) }
        sinhvien?.MaLop?.let { lichhocviewmodel.startPollingLichHocByMaLopHoc(it) }
    }

    DisposableEffect(Unit) {
        onDispose {
            lichhocviewmodel.stopPolling()
            lichhocviewmodel.stopPollingSV()
        }
    }

    val danhSachNamHoc = namHocViewModel.danhSachAllNamHoc
    val danhSachTuan = tuanViewModel.danhSachAllTuan
    val danhSachLichGV = lichhocviewmodel.danhSachLichHoctheomagv
    val danhSachLichSV = lichhocviewmodel.danhSachLichHoctheomalop

    val formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val todayDate = LocalDate.now()

    val selectedNamHoc = remember(danhSachNamHoc) {
        danhSachNamHoc.firstOrNull { it.TrangThai == 1 }
    }

    val danhSachTuanTheoNam = remember(selectedNamHoc, danhSachTuan) {
        danhSachTuan.filter { it.MaNam == selectedNamHoc?.MaNam }
    }

    var selectedTuan by remember { mutableStateOf<Tuan?>(null) }

    LaunchedEffect(danhSachTuanTheoNam) {
        if (selectedTuan == null && danhSachTuanTheoNam.isNotEmpty()) {
            val tuanHienTai = danhSachTuanTheoNam.firstOrNull { tuan ->
                val ngayBatDau = LocalDate.parse(tuan.NgayBatDau, formatterDate)
                val ngayKetThuc = LocalDate.parse(tuan.NgayKetThuc, formatterDate)
                todayDate in ngayBatDau..ngayKetThuc
            }
            selectedTuan = tuanHienTai ?: danhSachTuanTheoNam.first()
        }
    }

    val lichHocTheoTuan = remember(danhSachLichGV, danhSachLichSV, selectedTuan) {
        selectedTuan?.let { tuan ->
            val danhSach = if (giangVien != null) danhSachLichGV else danhSachLichSV
            danhSach.filter { it.MaTuan == tuan.MaTuan }
        } ?: emptyList()
    }

    val lichHocTheoNgay = remember(danhSachLichGV, danhSachLichSV, todayDate) {
        val danhSach = if (giangVien != null) danhSachLichGV else danhSachLichSV
        danhSach.filter {
            try {
                LocalDate.parse(it.NgayDay, formatterDate) == todayDate
            } catch (e: Exception) {
                false
            }
        }.sortedBy { it.MaCaHoc }
    }

    val today = todayDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

    val scrollState = rememberScrollState()

    val dynamicTextColor by remember {
        derivedStateOf {
            if (scrollState.value > 200) Color(0xFF1B8DDE) else Color.White
        }
    }

//===============================================================================================================

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = if (giangVien != null) "Xin chào, ${giangVien.TenGiangVien}" else "Xin chào, ${sinhvien?.TenSinhVien}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B8DDE),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = "Hôm nay: $today",
                fontSize = 16.sp,
                color = Color(0xFF555555),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            selectedTuan?.let {
                Text(
                    text = "Thời khóa biểu: ${it.TenTuan}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(6.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.95f)),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                CardThoiKhoaBieuTuan(navController, lichHocList = lichHocTheoTuan)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = if (giangVien != null) "Lịch dạy hôm nay" else "Lịch học hôm nay",
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = dynamicTextColor, // ← đổi màu theo scroll
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(end = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (lichHocTheoNgay.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (giangVien != null) "Không có lịch dạy hôm nay" else "Không có lịch học hôm nay",
                                color = Color.LightGray,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                } else {
                    items(lichHocTheoNgay) { lichHoc ->
                        CardLichHoc(
                            lichHoc = lichHoc,
                            giangVien = giangVien,
                            sinhvien = sinhvien,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}










