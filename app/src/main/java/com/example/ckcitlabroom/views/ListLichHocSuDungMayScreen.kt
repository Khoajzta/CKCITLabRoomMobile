import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
fun ListLichHocSuDungMay(
    lichHocViewModel: LichHocViewModel,
    giangVienViewModel: GiangVienViewModel,
    namHocViewModel: NamHocViewModel,
    tuanViewModel: TuanViewModel,
    navController: NavHostController
) {
    val giangVien = giangVienViewModel.giangvienSet

    LaunchedEffect(Unit) {
        namHocViewModel.getAllNamHoc()
        tuanViewModel.getAllTuan()

        giangVien?.let {
            lichHocViewModel.startPollingLichHocByMaGV(it.MaGV)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            lichHocViewModel.stopPolling()
            lichHocViewModel.stopPollingSV()
        }
    }

    val danhSachNamHoc = namHocViewModel.danhSachAllNamHoc
    val danhSachTuan = tuanViewModel.danhSachAllTuan

    val danhSachLichGV = lichHocViewModel.danhSachLichHoctheomagv

    val selectedNamHoc = remember(danhSachNamHoc) {
        danhSachNamHoc.firstOrNull { it.TrangThai == 1 }
    }

    val danhSachTuanTheoNam = remember(selectedNamHoc, danhSachTuan) {
        danhSachTuan.filter { it.MaNam == selectedNamHoc?.MaNam }
    }

    var selectedTuan by remember { mutableStateOf<Tuan?>(null) }

    LaunchedEffect(danhSachTuanTheoNam) {
        if (selectedTuan == null && danhSachTuanTheoNam.isNotEmpty()) {
            val today = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            selectedTuan = danhSachTuanTheoNam.find { tuan ->
                try {
                    val start = LocalDate.parse(tuan.NgayBatDau, formatter)
                    val end = LocalDate.parse(tuan.NgayKetThuc, formatter)
                    today in start..end
                } catch (e: Exception) {
                    false
                }
            } ?: danhSachTuanTheoNam.first()
        }
    }

    val lichHocTheoTuan = remember(danhSachLichGV, selectedTuan) {
        selectedTuan?.let { tuan ->
            danhSachLichGV.filter { it.MaTuan == tuan.MaTuan }
        } ?: emptyList()
    }

    val thuList = listOf("Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật")
    val lichHocTheoThu = thuList.associateWith { thu ->
        lichHocTheoTuan.filter { it.Thu == thu }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Danh Sách Lịch Dạy",
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

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                CustomDropdownSelector(
                    label = "Tuần",
                    items = danhSachTuanTheoNam,
                    selectedItem = selectedTuan,
                    itemLabel = { it.TenTuan },
                    onItemSelected = { selectedTuan = it }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(thuList) { thu ->
                val lichTrongThu = lichHocTheoThu[thu].orEmpty()

                if (lichTrongThu.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = thu,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(lichTrongThu) { lichhoc ->
                                CardLichHocListSV(lichhoc,
                                    click = {
                                        navController.navigate(
                                            NavRoute.LISTSVSUDUNGMAYTHEOCA.route + "?maCa=${lichhoc.MaCaHoc}&maTuan=${lichhoc.MaTuan}&maphong=${lichhoc.MaPhong}&ngaySuDung=${lichhoc.NgayDay}"
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }

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
            }
        }
    }
}
