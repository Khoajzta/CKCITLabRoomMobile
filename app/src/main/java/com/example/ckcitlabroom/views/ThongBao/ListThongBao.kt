import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ListThongBao(
    notificationViewModel: NotificationViewModel,
    sinhVienViewModel: SinhVienViewModel,
    giangVienViewModel: GiangVienViewModel
) {
    val sinhVien = sinhVienViewModel.sinhvienSet
    val giangVien = giangVienViewModel.giangvienSet

    val thongBaoList = notificationViewModel.danhSachAllThongBao
        .filter {
            when {
                sinhVien != null -> it.MaNguoiDung == sinhVien.MaSinhVien
                giangVien != null -> it.MaNguoiDung == giangVien.MaGV
                else -> false
            }
        }
        .sortedByDescending { it.ThoiGian }


    // Trạng thái visible của từng thông báo (true = hiển thị)
    val visibleMap = remember { mutableStateMapOf<Int, Boolean>() }

    // Khởi tạo tất cả visible là true
    LaunchedEffect(thongBaoList) {
        thongBaoList.forEach {
            if (visibleMap[it.MaTB] == null) {
                visibleMap[it.MaTB] = true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {

        if (thongBaoList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Bạn chưa có thông báo nào.", color = Color.Gray, fontSize = 16.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(thongBaoList, key = { it.MaTB }) { thongBao ->
                    val visible = visibleMap[thongBao.MaTB] ?: true
                    AnimatedVisibility(
                        visible = visible,
                        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { -it })
                    ) {
                        SwipeableThongBaoCard(
                            thongBao = thongBao,
                            notificationViewModel = notificationViewModel,
                            onDeleteSwipe = { visibleMap[thongBao.MaTB] = false }
                        )
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (thongBaoList.isNotEmpty()) {
                            TextButton(
                                onClick = {
                                    thongBaoList.forEachIndexed { index, thongBao ->
                                        CoroutineScope(Dispatchers.Main).launch {
                                            delay(index * 80L)
                                            visibleMap[thongBao.MaTB] = false
                                        }
                                    }

                                    // Sau cùng mới xóa khỏi ViewModel (trễ hơn animation)
                                    CoroutineScope(Dispatchers.Main).launch {
                                        delay(thongBaoList.size * 80L + 300) // đợi tất cả animation xong
                                        thongBaoList.forEach {
                                            notificationViewModel.deleteThongBao(it.MaTB)
                                        }
                                    }
                                }) {
                                Text(
                                    text = "Xóa tất cả",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 17.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun SwipeableThongBaoCard(
    thongBao: ThongBao,
    notificationViewModel: NotificationViewModel,
    onDeleteSwipe: (() -> Unit)? = null
) {
    var offsetX by remember { mutableStateOf(0f) }
    val animatedOffsetX by animateDpAsState(targetValue = offsetX.dp, label = "offset animation")
    val maxOffset = with(LocalDensity.current) { -60.dp.toPx() }
    var pendingDelete by remember { mutableStateOf(false) }

    LaunchedEffect(pendingDelete) {
        if (pendingDelete) {
            delay(300)
            notificationViewModel.deleteThongBao(thongBao.MaTB)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        // Nền đỏ phía sau
        Card(
            modifier = Modifier
                .matchParentSize(), // ← dùng matchParentSize thay vì fillMaxWidth + padding
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFD32F2F)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(
                    onClick = {
                        onDeleteSwipe?.invoke()
                        pendingDelete = true
                    },
                    modifier = Modifier.padding(end = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Xóa thông báo",
                        tint = Color.White
                    )
                }
            }
        }

        // Nội dung thông báo
        Card(
            modifier = Modifier
                .offset(x = animatedOffsetX)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            offsetX = if (offsetX < -30f) -60f else 0f
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            val newOffset = (offsetX + dragAmount).coerceIn(maxOffset, 0f)
                            offsetX = newOffset
                        }
                    )
                }
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable(enabled = !thongBao.DaDoc) {
                    if (!thongBao.DaDoc) {
                        notificationViewModel.updateThongBao(thongBao.copy(DaDoc = true))
                    }
                },
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (thongBao.DaDoc) Color.White else Color(0xFFE3F2FD)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // Tiêu đề + trạng thái đọc
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = thongBao.TieuDe,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = if (thongBao.DaDoc) Color(0xFF1B8DDE) else Color(0xFF0A4C8A),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (thongBao.DaDoc) "Đã đọc" else "Chưa đọc",
                        fontSize = 13.sp,
                        color = if (thongBao.DaDoc) Color.Gray else Color(0xFF1B8DDE),
                        fontWeight = if (thongBao.DaDoc) FontWeight.Normal else FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Nội dung
                Text(
                    text = thongBao.NoiDung,
                    fontSize = 15.sp,
                    color = if (thongBao.DaDoc) Color.DarkGray else Color.Black,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Thời gian có icon
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = thongBao.ThoiGian,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}



