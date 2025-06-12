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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    navController: NavHostController
) {
    BackHandler {}

    val sinhvien = sinhVienViewModel.sinhvienSet
    val giangVien = giangVienViewModel.giangvienSet

    // Gọi API chỉ khi đã có dữ liệu
    LaunchedEffect(giangVien?.MaGV, sinhvien?.MaLop) {
        giangVien?.MaGV?.let {
            lichhocviewmodel.startPollingLichHocByMaGV(it)
        }
        sinhvien?.MaLop?.let {
            lichhocviewmodel.startPollingLichHocByMaLopHoc(it)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            lichhocviewmodel.stopPolling()
            lichhocviewmodel.stopPollingSV()
        }
    }

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val today = LocalDate.now().format(formatter)

    val danhSachGV = lichhocviewmodel.danhSachLichHoctheomagv
    val danhSachSV = lichhocviewmodel.danhSachLichHoctheomalop

    val lichhocSinhVien = danhSachSV.filter {
        it.MaLopHoc == sinhvien?.MaLop && it.TrangThai == 1 && it.NgayDay == today
    }

    val lichDangDienRa = danhSachGV.filter {
        it.TrangThai == 1 && it.NgayDay == today
    }

    val lichHienThi = when {
        sinhvien != null -> lichhocSinhVien
        giangVien != null -> lichDangDienRa
        else -> emptyList()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = if (giangVien != null) "Lịch Dạy" else "Lịch Học",
                color = Color(0xFF1B8DDE),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (lichHienThi.isEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Không có lịch ${if (giangVien != null) "dạy" else "học"}.",
                            color = Color.Black,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                items(lichHienThi) { lichhoc ->
                    CardLichHoc(
                        lichhoc,
                        giangVien,
                        sinhvien,
                        navController
                    )
                }
            }
        }
    }
}








