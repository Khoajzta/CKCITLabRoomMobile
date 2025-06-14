import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lapstore.viewmodels.LichHocViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ChiTietLichHocScreen(
    matuan: String,
    navController: NavHostController,
    sinhVienViewModel: SinhVienViewModel,
    giangVienViewModel: GiangVienViewModel,
    lichHocViewModel: LichHocViewModel
) {
    Log.d("MaTuan", matuan)

    val giangVien = giangVienViewModel.giangvienSet
    val sinhvien = sinhVienViewModel.sinhvienSet

    Log.d("SV", sinhvien.toString())
    Log.d("GV", giangVien.toString())

    LaunchedEffect(Unit) {
        lichHocViewModel.getAllLichHoc()
    }

    val listLichHocAll = lichHocViewModel.danhSachLichHoc

    val role = when {
        giangVien != null -> "gv"
        sinhvien != null -> "sv"
        else -> null
    }

// Nếu chưa xác định được role → loading
    if (role == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            DotLoading()
        }
        return
    }

// Chuyển matuan sang Int an toàn
    val maTuanInt = matuan.toIntOrNull()

// Nếu MaTuan không hợp lệ → báo lỗi
    if (maTuanInt == null) {
        Text("Mã tuần không hợp lệ", color = Color.Red)
        return
    }

// Lọc danh sách lịch học theo role và MaTuan
    val lichHocTheoTuan = when (role) {
        "gv" -> listLichHocAll.filter {
            it.MaTuan == maTuanInt && it.MaGV == giangVien?.MaGV
        }
        "sv" -> listLichHocAll.filter {
            it.MaTuan == maTuanInt && it.MaLopHoc == sinhvien?.MaLop
        }
        else -> emptyList()
    }

    val thuList = listOf("Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật")

    val lichHocTheoThu = thuList.associateWith { thu ->
        lichHocTheoTuan.filter { it.Thu == thu }
    }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (role == "gv") "Chi tiết lịch dạy tuần" else "Chi tiết lịch học tuần",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                color = Color(0xFF1B8DDE)
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(),
            thickness = 2.dp,
            color = Color(0xFF1B8DDE),
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            thuList.forEach { thu ->
                val lichTrongThu = lichHocTheoThu[thu].orEmpty()
                if (lichTrongThu.isNotEmpty()) {
                    item {
                        Text(
                            text = thu,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF1B8DDE),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 12.dp)
                        )
                    }

                    items(lichTrongThu) { lichHoc ->
                        CardLichHoc(
                            lichHoc = lichHoc,
                            giangVien = if (role == "gv") giangVien else null,
                            sinhvien = if (role == "sv") sinhvien else null,
                            navController = navController
                        )
                    }
                }
            }

            // Nếu toàn bộ không có lịch
            if (lichHocTheoTuan.isEmpty()) {
                item {
                    Text(
                        text = "Không có lịch học nào trong tuần này",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp, start = 12.dp)
                    )
                }
            }
        }

    }

    // Hiển thị UI

}


