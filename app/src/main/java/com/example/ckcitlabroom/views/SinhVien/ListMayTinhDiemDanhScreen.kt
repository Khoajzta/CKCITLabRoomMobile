import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lapstore.viewmodels.ChiTietSuDungMayViewModel
import com.example.lapstore.viewmodels.LichHocViewModel
import com.example.lapstore.viewmodels.MayTinhViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ListMayTinhDiemDanh(
    navController: NavHostController,
    malichhoc: String,
    lichHocViewModel: LichHocViewModel,
    mayTinhViewModel: MayTinhViewModel,
    chiTietSuDungMayViewModel: ChiTietSuDungMayViewModel,
    sinhVienViewModel: SinhVienViewModel
) {

    var context = LocalContext.current
    var sinhVien = sinhVienViewModel.sinhvienSet

    val lichhoc = lichHocViewModel.lichhoc
    val danhSachMayTinhTheoPhong = mayTinhViewModel.danhSachAllMayTinhtheophong

    // Lấy lịch học theo mã
    LaunchedEffect(malichhoc) {
        lichHocViewModel.getLichHocByMaLich(malichhoc)
    }

    // Khi lichhoc != null thì gọi lấy máy tính theo phòng
    LaunchedEffect(lichhoc) {
        lichhoc?.let {
            mayTinhViewModel.getMayTinhByPhong(it.MaPhong)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Chọn máy tính đang sử dụng để điểm danh",
            color = Color(0xFF1B8DDE),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        )

        if (lichhoc == null) {
            Text("Đang tải dữ liệu lịch học...")
        } else if (danhSachMayTinhTheoPhong.isEmpty()) {
            Text("Không có máy tính trong phòng ${lichhoc.MaPhong}")
        } else {
            LazyColumn {
                items(danhSachMayTinhTheoPhong) { maytinh ->
                    CardMayTinhDiemDanh(
                        maytinh = maytinh,
                        click = {
                            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                            val today = LocalDate.now().format(formatter)

                            var chitietSuDungMay = ChiTietSuDungMay(sinhVien!!.MaSinhVien,lichhoc.MaCaHoc,lichhoc.MaTuan,today,maytinh.MaMay,lichhoc.MaPhong)

                            chiTietSuDungMayViewModel.createChiTietSuDungMay(chitietSuDungMay)
                            navController.popBackStack()
                            Toast.makeText(context, "Điểm danh thành công", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}
