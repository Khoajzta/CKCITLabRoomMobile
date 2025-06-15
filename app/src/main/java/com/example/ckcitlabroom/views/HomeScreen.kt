import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.lapstore.viewmodels.LichHocViewModel
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

    // Gọi API lịch học
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
            danhSach.filter {it.MaTuan == tuan.MaTuan }
        } ?: emptyList()
    }

    val lichHocTheoNgay = remember(danhSachLichGV, danhSachLichSV, todayDate) {
        val danhSach = if (giangVien != null) danhSachLichGV else danhSachLichSV
        danhSach
            .filter {
                try {
                    LocalDate.parse(it.NgayDay, formatterDate) == todayDate
                } catch (e: Exception) {
                    false
                }
            }
            .sortedBy { it.MaCaHoc }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Tiêu đề
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = if (giangVien != null) "Lịch Dạy" else "Lịch Học",
                color = Color(0xFF1B8DDE),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                modifier = Modifier.padding(16.dp)
            )
        }

        // Tuần hiện tại đang được hiển thị
        selectedTuan?.let {
            Text(
                "Thời khóa biểu ${it.TenTuan}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // --- Lịch học theo tuần ---
//        CardLichHocTheoTuan(lichHocTheoTuan)
        CardThoiKhoaBieuTuan(navController,lichHocList = lichHocTheoTuan)

        Spacer(modifier = Modifier.height(16.dp))

        // --- Lịch học hôm nay ---
        Text(
            text = if (giangVien != null) {
                "Lịch dạy hôm nay (${todayDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))})"
            } else {
                "Lịch học hôm nay (${todayDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))})"
            },
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 8.dp)
        )


        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.Top
        ) {
            if(lichHocTheoNgay.isEmpty()){
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if(giangVien!= null){
                            Text(
                                text = "Không có lịch dạy hôm nay",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }else{
                            Text(
                                text = "Không có lịch học hôm nay",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }
                }

            }

            items(lichHocTheoNgay) { lichhoc->
                CardLichHoc(lichHoc = lichhoc, giangVien = giangVien, sinhvien = sinhvien, navController = navController)
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}










