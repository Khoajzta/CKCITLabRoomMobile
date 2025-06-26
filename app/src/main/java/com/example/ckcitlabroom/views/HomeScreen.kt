import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.viewmodels.LichHocViewModel
import kotlinx.coroutines.launch
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


    Log.d("Home", "HOME")

    LaunchedEffect(Unit) {
        namHocViewModel.getAllNamHoc()
        tuanViewModel.getAllTuan()
    }

    val sinhvien = sinhVienViewModel.sinhvienSet
    val giangVien = giangVienViewModel.giangvienSet



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
            danhSach.filter { it.MaTuan == tuan.MaTuan }
        } ?: emptyList()
    }

    val lichHocTheoNgay = remember(danhSachLichGV, danhSachLichSV, todayDate) {
        val danhSach = if (giangVien != null) danhSachLichGV else danhSachLichSV
        danhSach.filter {
            try {
                LocalDate.parse(it.NgayDay, formatterDate) == todayDate
            } catch (e: Exception) {
                false
            }
        }.sortedBy { it.MaCaHoc }
    }

    val today = todayDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

//===============================================================================================================

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {

        if(danhSachNamHoc.isNullOrEmpty()){
            DotLoadingLight()
            return@Box
        }

        if(selectedNamHoc == null){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = if (giangVien != null)
                            "Xin chào, ${giangVien.TenGiangVien}"
                        else
                            "Xin chào, ${sinhvien?.TenSinhVien}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1B8DDE),
                        modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
                    )

                    Text(
                        text = "Hôm nay: $today",
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                    )
                }



                val text = when {
                    sinhvien != null ->
                        "🎉 Chào mừng bạn đã đến với CKCITLab!\n📚 Vui lòng chờ Admin tạo lịch học nhé."

                    giangVien?.MaLoaiTaiKhoan == 1 ->
                        "👋 Chào mừng bạn đã đến với CKCITLab!\n🛠️ Hãy tạo năm học mới trong phần chức năng nhé."

                    giangVien?.MaLoaiTaiKhoan == 2 ->
                        "👋 Chào mừng bạn đã đến với CKCITLab!\n📅 Vui lòng chờ Admin tạo lịch dạy nhé."

                    else -> "⚠️ Bạn chưa có năm học nào được tạo."
                }


                val styledText = buildAnnotatedString {
                    val lines = text.lines()
                    if (lines.isNotEmpty()) {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(lines[0])                                       // dòng 1 – in đậm
                        }
                        if (lines.size > 1) {
                            append("\n")
                            append(lines.drop(1).joinToString("\n"))               // phần còn lại
                        }
                    }
                }

                Text(
                    text = styledText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFFF8E1))                             // vàng kem
                        .padding(16.dp),
                    lineHeight = 22.sp,
                    color = Color(0xFFEF6C00),
                    fontSize = 16.sp
                )


            }

            return@Box
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {

            Column(
            ) {
                Text(
                    text = if (giangVien != null)
                        "Xin chào, ${giangVien.TenGiangVien}"
                    else
                        "Xin chào, ${sinhvien?.TenSinhVien}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1B8DDE),
                    modifier = Modifier.padding(start = 8.dp,bottom = 4.dp)
                )
                Text(
                    text = "Hôm nay: $today",
                    fontSize = 16.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 8.dp,bottom = 4.dp)
                )


                Spacer(modifier = Modifier.height(20.dp))
                selectedTuan?.let {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, RoundedCornerShape(24.dp))
                            .clip(RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB))
                    ) {
                        Column {
                            Text(
                                text = "Thời khóa biểu: ${it.TenTuan}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1B8DDE),
                                modifier = Modifier.padding(start = 15.dp, top = 15.dp)
                            )

                            HorizontalDivider(
                                modifier = Modifier
                                    .padding(vertical = 6.dp, horizontal = 12.dp)
                                    .fillMaxWidth(),
                                thickness = 1.dp,
                                color = Color(0xFF1B8DDE),
                            )

                            CardThoiKhoaBieuTuan(
                                tuanHienTai = it,
                                navController = navController,
                                lichHocList = lichHocTheoTuan
                            )
                        }

                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp)),
                ) {

                    val listState = rememberLazyListState()
                    val currentPage by remember {
                        derivedStateOf { listState.firstVisibleItemIndex }
                    }

                    Text(
                        text = if (giangVien != null) "Lịch dạy hôm nay" else "Lịch học hôm nay",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = Color.White,
                        modifier = Modifier.padding(start = 15.dp, top = 12.dp)
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {

                        HorizontalDivider(
                            modifier = Modifier
                                .padding(vertical = 6.dp, horizontal = 12.dp)
                                .fillMaxWidth(),
                            thickness = 1.dp,
                            color = Color.White,
                        )


                        LazyRow(
                            state = listState,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(end = 3.dp),
                            flingBehavior = rememberSnapFlingBehavior(listState)
                        ) {
                            if (lichHocTheoNgay.isEmpty()) {
                                item {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = if (giangVien != null)
                                                "Không có lịch dạy hôm nay"
                                            else
                                                "Không có lịch học hôm nay",
                                            color = Color.White,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            } else {
                                items(lichHocTheoNgay) { lichHoc ->
                                    CardLichHoc(
                                        lichHoc = lichHoc,
                                        giangVien = giangVien,
                                        sinhvien = sinhvien,
                                        navController = navController
                                    )
                                }
                            }
                        }

                        /* ---------- Dấu chấm chuyển card ---------- */
                        val scope = rememberCoroutineScope()

                        if (lichHocTheoNgay.size > 1) {
                            Row(
                                modifier = Modifier.padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(lichHocTheoNgay.size) { index ->
                                    val selected = index == currentPage

                                    // 👇 Size & color có animation
                                    val dotSize by animateDpAsState(
                                        targetValue = if (selected) 13.dp else 8.dp,
                                        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
                                    )

                                    val dotColor by animateColorAsState(
                                        targetValue = if (selected) Color.White
                                        else Color.White.copy(alpha = 0.3f),
                                        animationSpec = tween(300)          // cùng tốc độ với size
                                    )

                                    Box(
                                        modifier = Modifier
                                            .size(dotSize)                  // dùng size đã animate
                                            .clip(CircleShape)
                                            .background(dotColor)           // dùng màu đã animate
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = null
                                            ) {
                                                scope.launch { listState.animateScrollToItem(index) }
                                            }
                                    )
                                }
                            }
                        }


                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}