import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.viewmodels.CaHocViewModel
import com.example.ckcitlabroom.viewmodels.LichHocViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun LichDayGVAllTuan(
    lichHocViewModel: LichHocViewModel,
    giangVienViewModel: GiangVienViewModel,
    sinhVienViewModel: SinhVienViewModel,
    namHocViewModel: NamHocViewModel,
    tuanViewModel: TuanViewModel,
    phongMayViewModel: PhongMayViewModel,
    navController: NavHostController
) {
    val danhSachPhongMay = phongMayViewModel.danhSachAllPhongMay.filter { it.LoaiPhong == 1 }

    LaunchedEffect(Unit) {
        namHocViewModel.getAllNamHoc()
        tuanViewModel.getAllTuan()
        phongMayViewModel.getAllPhongMay()
        lichHocViewModel.getAllLichHoc()
    }


    val danhSachNamHoc = namHocViewModel.danhSachAllNamHoc.firstOrNull { it.TrangThai == 1 }
    val danhSachTuanTheoNam =
        tuanViewModel.danhSachAllTuan.filter { it.MaNam == danhSachNamHoc?.MaNam }

    var selectedTuan by remember { mutableStateOf<Tuan?>(null) }

    // Chọn tuần hiện tại nếu chưa chọn
    LaunchedEffect(danhSachTuanTheoNam) {
        if (selectedTuan == null && danhSachTuanTheoNam.isNotEmpty()) {
            val today = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            val tuanHienTai = danhSachTuanTheoNam.find { tuan ->
                try {
                    val ngayBD = LocalDate.parse(tuan.NgayBatDau, formatter)
                    val ngayKT = LocalDate.parse(tuan.NgayKetThuc, formatter)
                    !today.isBefore(ngayBD) && !today.isAfter(ngayKT)
                } catch (_: Exception) {
                    false
                }
            }

            selectedTuan = tuanHienTai ?: danhSachTuanTheoNam.first()
        }
    }



    LaunchedEffect(selectedTuan?.MaTuan) {
        selectedTuan?.let {
            lichHocViewModel.startPollingLichHocTheoThuVaPhong(it.MaTuan.toString())
        }
    }

    var danhSachPhongMayCoTKB = lichHocViewModel.danhSachLichHocTheoThuVaPhong


    val danhSachPhongDayDu by remember(danhSachPhongMayCoTKB, danhSachPhongMay) {
        derivedStateOf {
            danhSachPhongMay.map { phong ->
                danhSachPhongMayCoTKB.find { it.MaPhong == phong.MaPhong }
                    ?: LichHocTheoThuVaPhongItem(
                        MaPhong = phong.MaPhong,
                        TenPhong = phong.TenPhong,
                        Thu2 = emptyList(),
                        Thu3 = emptyList(),
                        Thu4 = emptyList(),
                        Thu5 = emptyList(),
                        Thu6 = emptyList(),
                        Thu7 = emptyList(),
                        ChuNhat = emptyList()
                    )
            }
        }
    }


    LaunchedEffect(selectedTuan) {
        selectedTuan?.let {
            lichHocViewModel.startPollingLichHocTheoThuVaPhong(it.MaTuan.toString())
        }
    }


    DisposableEffect(Unit) {
        onDispose {
            lichHocViewModel.stopPollingaALLGV()
        }
    }


    // Giao diện
    Column(modifier = Modifier.fillMaxSize()) {
        val isMissingData =
            danhSachNamHoc == null || danhSachTuanTheoNam.isEmpty() || selectedTuan == null || danhSachPhongMay.isEmpty()

        if(giangVienViewModel.giangvienSet?.MaLoaiTaiKhoan == 1){
            if (isMissingData) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Chưa có lịch phòng máy",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tạo năm học mới để có thể tạo lịch dạy",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        )
                    }
                }
                return
            }
        }else{
            if (isMissingData) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Chưa có lịch phòng máy",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                return
            }
        }


        CustomDropdownSelector(
            modifier = Modifier.fillMaxWidth(),
            label = "Tuần",
            items = danhSachTuanTheoNam,
            selectedItem = selectedTuan,
            itemLabel = { it.TenTuan },
            onItemSelected = { selectedTuan = it })

        if (selectedTuan != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Lịch Phòng Máy Tuần ${selectedTuan!!.TenTuan}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = Color(0xFF1B8DDE)
                )
            }
        } else {
            DotLoading()
        }

        if (danhSachPhongDayDu.isEmpty()) {
            DotLoading()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(12.dp))
            ) {
                item {
                    CardThoiKhoaBieuTheoTuan(
                        danhSachPhongDayDu, click = {
                            navController.navigate("${NavRoute.LISTLICHHOC.route}/${selectedTuan!!.MaTuan}")
                        })
                }
            }
        }

    }
}


















