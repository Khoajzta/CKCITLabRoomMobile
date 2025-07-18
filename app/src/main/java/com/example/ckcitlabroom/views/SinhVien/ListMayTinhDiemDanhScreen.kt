import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.viewmodels.ChiTietSuDungMayViewModel
import com.example.ckcitlabroom.viewmodels.LichHocViewModel
import com.example.ckcitlabroom.viewmodels.MayTinhViewModel

@Composable
fun ListMayTinhDiemDanh(
    navController: NavHostController,
    malichhoc: String,
    lichHocViewModel: LichHocViewModel,
    mayTinhViewModel: MayTinhViewModel,
    chiTietSuDungMayViewModel: ChiTietSuDungMayViewModel,
    sinhVienViewModel: SinhVienViewModel
) {
    val context = LocalContext.current
    val sinhVien = sinhVienViewModel.sinhvienSet
    val lichhoc = lichHocViewModel.lichhoc
    val danhSachMayTinhTheoPhong = mayTinhViewModel.danhSachAllMayTinhtheophong
    val danhSachDiemDanh = chiTietSuDungMayViewModel.danhSachAllChiTiet

    Log.d("Lich hoc", "Lich hoc: $lichhoc")

    // Load dữ liệu
    LaunchedEffect(malichhoc) {
        lichHocViewModel.getLichHocByMaLich(malichhoc)
    }

    LaunchedEffect(Unit) {
        chiTietSuDungMayViewModel.getAllChiTietSuDungMay()
    }

    LaunchedEffect(lichhoc) {
        lichhoc?.let {
            mayTinhViewModel.getMayTinhByPhong(it.MaPhong)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        when {
            lichhoc == null -> {
                Text("Đang tải dữ liệu lịch học...")
            }

            danhSachMayTinhTheoPhong.isEmpty() -> {
                Text("Không có máy tính trong phòng ${lichhoc.MaPhong}")
            }

            else -> {
                LazyColumn {
                    items(danhSachMayTinhTheoPhong) { maytinh ->
                        CardMayTinhDiemDanh(
                            maytinh = maytinh,
                            click = {
                                navController.navigate(NavRoute.MAYTINHDETAIL.route + "?mamay=${maytinh.MaMay}&malichhoc=$malichhoc")
                            }
                        )
                    }
                }
            }
        }
    }
}

