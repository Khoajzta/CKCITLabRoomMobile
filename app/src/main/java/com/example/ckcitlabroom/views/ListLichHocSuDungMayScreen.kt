import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.viewmodels.LichHocViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
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

    val thuList = listOf("Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ Nhật")
    val lichHocTheoThu = thuList.associateWith { thu ->
        lichHocTheoTuan.filter { it.Thu == thu }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        val isMissingData =
            danhSachNamHoc == null || danhSachTuanTheoNam.isEmpty() || selectedTuan == null

        if (isMissingData) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {

                var text = if (giangVien != null) {
                    "Chưa có lịch dạy"
                } else {
                    "Chưa có lịch học"
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = text,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
            return
        }

        var expanded by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Danh Sách Lịch Dạy ",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = Color(0xFF1B8DDE)
            )

            val primary = Color(0xFF1B8DDE)

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.width(100.dp)          // khung ngoài 200 dp
            ) {
                // ── Anchor ───────────────────────────────────────────────
                Row(
                    modifier = Modifier
                        .menuAnchor()
                        .width(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .padding(vertical = 6.dp)          // KHÔNG padding start
                        .clickable { expanded = !expanded },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // 1️⃣  Text dính trái, chiếm hết khoảng còn lại
                    BasicTextField(
                        value = selectedTuan?.TenTuan ?: "",
                        onValueChange = {},
                        readOnly = true,
                        singleLine = true,
                        textStyle = TextStyle(
                            fontSize = 20.sp,
                            color = primary,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier
                            .weight(1f)                    // đẩy icon về phải
                            .padding(start = 0.dp)
                    )

                    // 2️⃣  IconButton sát chữ
                    IconButton(
                        onClick = { expanded = !expanded },
                        modifier = Modifier
                            .size(24.dp)                   // ripple / hit-box tiêu chuẩn
                    ) {
                        Icon(
                            imageVector = if (expanded)
                                Icons.Default.KeyboardArrowUp
                            else
                                Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = primary
                        )
                    }
                }

                // ── Menu ────────────────────────────────────────────────
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    containerColor = Color.White
                ) {
                    danhSachTuanTheoNam.forEach { tuan ->
                        DropdownMenuItem(
                            text = {
                                androidx.compose.material3.Text(
                                    tuan.TenTuan,
                                    color = Color.Black
                                )
                            },
                            onClick = {
                                selectedTuan = tuan
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            thickness = 2.dp,
            color = Color(0xFF1B8DDE),
        )


        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(thuList) { thu ->
                val lichTrongThu = lichHocTheoThu[thu]?.sortedBy { it.TenCa }.orEmpty()



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
                                CardLichHocListSV(
                                    lichhoc,
                                    click = {
                                        navController.navigate(
                                            NavRoute.LISTSVSUDUNGMAYTHEOCA.route + "?malichhoc=${lichhoc.MaLichHoc}&maCa=${lichhoc.MaCaHoc}&maTuan=${lichhoc.MaTuan}&maphong=${lichhoc.MaPhong}&ngaySuDung=${lichhoc.NgayDay}"
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
