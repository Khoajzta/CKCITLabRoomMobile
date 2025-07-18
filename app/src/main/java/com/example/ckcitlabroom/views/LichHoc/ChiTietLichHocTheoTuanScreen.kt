import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.viewmodels.LichHocViewModel
import kotlinx.coroutines.launch

@Composable
fun ChiTietLichHocScreen(
    matuan: String,
    navController: NavHostController,
    sinhVienViewModel: SinhVienViewModel,
    giangVienViewModel: GiangVienViewModel,
    lichHocViewModel: LichHocViewModel,
    tuanViewModel: TuanViewModel
) {
    val giangVien = giangVienViewModel.giangvienSet
    val sinhvien = sinhVienViewModel.sinhvienSet
    val danhsachalltuan = tuanViewModel.danhSachAllTuan


    LaunchedEffect(Unit) {
        lichHocViewModel.startPollingAllLichHoc()
        tuanViewModel.getAllTuan()
    }

    DisposableEffect(Unit) {
        onDispose {
            lichHocViewModel.stopPollingAllLichHoc()
        }
    }

    val listLichHocAll = lichHocViewModel.danhSachLichHoc

    val role = when {
        giangVien != null -> "gv"
        sinhvien != null -> "sv"
        else -> null
    }

    if (role == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            DotLoading()
        }
        return
    }

    val maTuanInt = matuan.toIntOrNull()

    val tuan = danhsachalltuan.find { it.MaTuan == maTuanInt }

    if (tuan == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = "Không tìm thấy thông tin tuần học",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = Color.Black
            )
        }
        return
    }

    val lichHocTheoTuan = when (role) {
        "gv" -> listLichHocAll.filter { it.MaTuan == maTuanInt && it.MaGV == giangVien?.MaGV }
        "sv" -> listLichHocAll.filter { it.MaTuan == maTuanInt && it.MaLopHoc == sinhvien?.MaLop }
        else -> emptyList()
    }

    val thuList = listOf("Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ Nhật")
    val lichHocTheoThu = thuList.associateWith { thu ->
        lichHocTheoTuan.filter { it.Thu == thu }
    }

    if (danhsachalltuan.isEmpty() || listLichHocAll.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Chưa có lịch",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = Color(0xFF1B8DDE)
            )
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (role == "gv") "Chi tiết lịch dạy tuần ${tuan.TenTuan}" else "Chi tiết lịch học tuần ${tuan.TenTuan}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = Color(0xFF1B8DDE)
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                thickness = 2.dp,
                color = Color(0xFF1B8DDE)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 8.dp)
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
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )

                            val listState = rememberLazyListState()
                            val currentPage by remember {
                                derivedStateOf { listState.firstVisibleItemIndex }
                            }

                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                state = listState,
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(end = 3.dp),
                                flingBehavior = rememberSnapFlingBehavior(listState)
                            ) {
                                items(lichTrongThu) { lichHoc ->
                                    CardLichHoc(
                                        lichHoc = lichHoc,
                                        giangVien = if (role == "gv") giangVien else null,
                                        sinhvien = if (role == "sv") sinhvien else null,
                                        navController = navController
                                    )
                                }
                            }


                            val scope = rememberCoroutineScope()

                            if (lichTrongThu.size > 1) {
                                Row(
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(
                                        8.dp,
                                        Alignment.CenterHorizontally
                                    ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    repeat(lichTrongThu.size) { index ->
                                        val selected = index == currentPage

                                        // 👇 Size & color có animation
                                        val dotSize by animateDpAsState(
                                            targetValue = if (selected) 13.dp else 8.dp,
                                            animationSpec = tween(
                                                durationMillis = 300,
                                                easing = FastOutSlowInEasing
                                            )
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
                                                    scope.launch {
                                                        listState.animateScrollToItem(
                                                            index
                                                        )
                                                    }
                                                }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }


                if (lichHocTheoTuan.isEmpty()) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (role == "gv") "Không có lịch dạy trong tuần này" else "Không có lịch học trong tuần này",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 16.dp, start = 12.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}




