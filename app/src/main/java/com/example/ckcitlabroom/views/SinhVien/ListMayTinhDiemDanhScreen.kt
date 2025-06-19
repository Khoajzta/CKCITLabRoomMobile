import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.viewmodels.ChiTietSuDungMayViewModel
import com.example.ckcitlabroom.viewmodels.LichHocViewModel
import com.example.ckcitlabroom.viewmodels.MayTinhViewModel
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
    val context = LocalContext.current
    val sinhVien = sinhVienViewModel.sinhvienSet
    val lichhoc = lichHocViewModel.lichhoc
    val danhSachMayTinhTheoPhong = mayTinhViewModel.danhSachAllMayTinhtheophong
    val danhSachDiemDanh = chiTietSuDungMayViewModel.danhSachAllChiTiet

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
        Text(
            text = "Chọn máy tính đang sử dụng để điểm danh",
            color = Color(0xFF1B8DDE),
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp
        )

        HorizontalDivider(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            thickness = 2.dp,
            color = Color(0xFF1B8DDE),
        )

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
                                if (sinhVien == null) {
                                    Toast.makeText(
                                        context,
                                        "Không tìm thấy thông tin sinh viên",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@CardMayTinhDiemDanh
                                }

                                val today = LocalDate.now().toString()

                                val daDiemDanh = danhSachDiemDanh.any { chiTiet ->
                                    chiTiet.MaSV == sinhVien.MaSinhVien &&
                                            chiTiet.MaCa == lichhoc.MaCaHoc &&
                                            chiTiet.NgaySuDung == today
                                }

                                if (daDiemDanh) {
                                    Toast.makeText(
                                        context,
                                        "Bạn đã điểm danh trước đó rồi",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    val chiTietSuDung = ChiTietSuDungMay(
                                        MaChiTietSuDung = 0,
                                        MaSV = sinhVien.MaSinhVien,
                                        MaCa = lichhoc.MaCaHoc,
                                        MaTuan = lichhoc.MaTuan,
                                        NgaySuDung = today,
                                        MaMay = maytinh.MaMay,
                                        MaPhong = lichhoc.MaPhong
                                    )

                                    chiTietSuDungMayViewModel.createChiTietSuDungMay(chiTietSuDung)
                                    Toast.makeText(context, "Điểm danh thành công", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

