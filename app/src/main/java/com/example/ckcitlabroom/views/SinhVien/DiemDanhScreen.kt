import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.viewmodels.CaHocViewModel
import com.example.lapstore.viewmodels.ChiTietSuDungMayViewModel
import com.example.lapstore.viewmodels.LichHocViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.sinh

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DiemDanhScreen(
    chiTietSuDungMayViewModel: ChiTietSuDungMayViewModel,
    lichhocViewModel: LichHocViewModel,
    sinhVienViewModel: SinhVienViewModel,
    caHocViewModel: CaHocViewModel,
    navController: NavHostController,
) {
    val sinhvien = sinhVienViewModel.sinhvienSet

    LaunchedEffect(Unit) {
        sinhvien?.let {
            lichhocViewModel.getLichHocByMaLopHoc(it.MaLop)
            caHocViewModel.getAllCaHoc()
        }
    }

    val danhSachCaHoc = caHocViewModel.danhSachAllCaHoc
    val danhSachLichHoc = lichhocViewModel.danhSachLichHoctheomalop

    // Lấy giờ hiện tại
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    val gioHienTai = LocalTime.now().format(formatter)

    // Tìm ca học hiện tại
    val caHienTai = danhSachCaHoc.firstOrNull {
        it.GioBatDau <= gioHienTai && it.GioKetThuc >= gioHienTai
    }

    // Lấy danh sách lịch học khớp với ca hiện tại
    val danhSachLichHocHienTai = remember(caHienTai, danhSachLichHoc) {
        if (caHienTai != null) {
            val ngayHomNay = LocalDate.now().toString() // yyyy-MM-dd
            danhSachLichHoc.filter {
                it.MaCaHoc == caHienTai.MaCaHoc && it.NgayDay == ngayHomNay
            }
        } else emptyList()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Điểm danh hôm nay",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        if (caHienTai == null) {
            Text("Hiện tại không có ca học nào đang diễn ra", color = Color.Red)
        } else if (danhSachLichHocHienTai.isEmpty()) {
            Text("Không có lịch học nào trong ca hiện tại")
        } else {
            LazyColumn {
                items(danhSachLichHocHienTai) { lichhoc ->
                    CardLichHocDiemDanh(
                        lichhoc,
                        click = {
                            navController.navigate(NavRoute.LISTMAYTINHDIEMDANH.route + "?malichhoc=${lichhoc.MaLichHoc}")
                        }
                    )
                }
            }
        }
    }
}



