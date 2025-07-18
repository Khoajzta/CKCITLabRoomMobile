import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Hash
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPin
import com.example.ckcitlabroom.viewmodels.ChiTietSuDungMayViewModel
import com.example.ckcitlabroom.viewmodels.LichHocViewModel
import kotlinx.coroutines.delay

@Composable
fun ListSinhVienTheoCa(
    malichhoc: String,
    maCa: String,
    maTuan: String,
    maphong: String,
    ngaySuDung: String,
    chiTietSuDungMayViewModel: ChiTietSuDungMayViewModel,
    sinhVienViewModel: SinhVienViewModel,
    lichHocViewModel: LichHocViewModel
) {
    val context = LocalContext.current
    var showConfirmDialog by remember { mutableStateOf(false) }
    var selectedChiTiet by remember { mutableStateOf<ChiTietSuDungMayRP?>(null) }
    var pendingDelete by remember { mutableStateOf(false) }

    var lichhoc = lichHocViewModel.lichhoc


    LaunchedEffect(Unit) {
        chiTietSuDungMayViewModel.getAllChiTietSuDungMay()
        lichHocViewModel.getLichHocByMaLich(malichhoc)
    }

    DisposableEffect(Unit) {
        onDispose {
            chiTietSuDungMayViewModel.stopPollingAllChiTiet()
        }
    }

    val listAllchitiet = chiTietSuDungMayViewModel.danhSachAllChiTiet

    val listchitiettheolich = listAllchitiet.filter {
        it.MaCa == maCa.toInt() &&
                it.MaPhong == maphong &&
                it.NgaySuDung == ngaySuDung &&
                it.MaTuan == maTuan.toInt()
    }?.sortedBy { sv ->
        sv.TenSinhVien.trim()
            .split("\\s+".toRegex())
            .last()
            .lowercase()
    } ?: emptyList()

    var sinhvienChinh = emptyList<ChiTietSuDungMayRP>()
    var sinhvienHocGhep = emptyList<ChiTietSuDungMayRP>()

    if (lichhoc != null) {
        sinhvienChinh =
            listchitiettheolich.filter { it.MaLop == lichhoc.MaLopHoc }?.sortedBy { sv ->
                sv.TenSinhVien.trim()
                    .split("\\s+".toRegex())
                    .last()
                    .lowercase()
            } ?: emptyList()
        sinhvienHocGhep =
            listchitiettheolich.filter { it.MaLop != lichhoc.MaLopHoc }?.sortedBy { sv ->
                sv.TenSinhVien.trim()
                    .split("\\s+".toRegex())
                    .last()
                    .lowercase()
            } ?: emptyList()
    }


    val visibleMap = remember { mutableStateMapOf<Int, Boolean>() }

    LaunchedEffect(listchitiettheolich) {
        listchitiettheolich.forEach {
            if (visibleMap[it.MaChiTietSuDung] == null) {
                visibleMap[it.MaChiTietSuDung] = true
            }
        }
    }

    // Thực hiện xóa sau khi xác nhận
    LaunchedEffect(pendingDelete) {
        if (pendingDelete && selectedChiTiet != null) {
            delay(300)
            chiTietSuDungMayViewModel.deleteChiTietSuDungMay(selectedChiTiet!!.MaChiTietSuDung) { success, message ->
                Toast.makeText(
                    context,
                    if (success) "Xóa thành công" else message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            pendingDelete = false
            selectedChiTiet = null
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Danh Sách Sinh Viên",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = Color(0xFF1B8DDE),
            )
            Text(
                text = "Số lượng ${listchitiettheolich.count()}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = Color(0xFF1B8DDE),
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            thickness = 2.dp,
            color = Color(0xFF1B8DDE),
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (listchitiettheolich.isEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Không có sinh viên nào điểm danh.",
                            color = Color.Black,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                items(sinhvienChinh, key = { it.MaChiTietSuDung }) { chitiet ->
                    val visible = visibleMap[chitiet.MaChiTietSuDung] ?: true
                    var offsetX by remember { mutableStateOf(0f) }
                    val animatedOffsetX by animateDpAsState(
                        targetValue = offsetX.dp,
                        label = "offset animation"
                    )
                    val maxOffset = with(LocalDensity.current) { -60.dp.toPx() }

                    AnimatedVisibility(
                        visible = visible,
                        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { -it })
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            // Nền đỏ phía sau với nút Xóa
                            Card(
                                modifier = Modifier.matchParentSize(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.Red),
                                elevation = CardDefaults.cardElevation(0.dp)
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    IconButton(
                                        onClick = {
                                            selectedChiTiet = chitiet
                                            showConfirmDialog = true
                                        },
                                        modifier = Modifier.padding(end = 8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Xóa",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }

                            // Card nội dung có thể trượt
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
                                                val newOffset =
                                                    (offsetX + dragAmount).coerceIn(maxOffset, 0f)
                                                offsetX = newOffset
                                            }
                                        )
                                    }
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = chitiet.TenSinhVien,
                                        color = Color(0xFF1B8DDE),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )

                                    HorizontalDivider(
                                        modifier = Modifier
                                            .padding(vertical = 8.dp)
                                            .fillMaxWidth(),
                                        thickness = 2.dp,
                                        color = Color(0xFFDDDDDD),
                                    )

                                    InfoRow(
                                        icon = Lucide.Hash,
                                        label = "MSSV",
                                        value = chitiet.MaSV
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    InfoRow(
                                        icon = Lucide.Hash,
                                        label = "Mã Máy",
                                        value = chitiet.MaMay
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    InfoRow(
                                        icon = Lucide.MapPin,
                                        label = "Vị Trí Máy",
                                        value = chitiet.ViTri.toString()
                                    )
                                }
                            }
                        }
                    }
                }

                if (!sinhvienHocGhep.isEmpty()) {
                    item {
                        Text(
                            modifier = Modifier.padding(12.dp),
                            text = "Sinh viên học ghép",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }

                    items(sinhvienHocGhep, key = { it.MaChiTietSuDung }) { chitiet ->
                        val visible = visibleMap[chitiet.MaChiTietSuDung] ?: true
                        var offsetX by remember { mutableStateOf(0f) }
                        val animatedOffsetX by animateDpAsState(
                            targetValue = offsetX.dp,
                            label = "offset animation"
                        )
                        val maxOffset = with(LocalDensity.current) { -60.dp.toPx() }

                        AnimatedVisibility(
                            visible = visible,
                            exit = fadeOut() + slideOutHorizontally(targetOffsetX = { -it })
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            ) {
                                // Nền đỏ phía sau với nút Xóa
                                Card(
                                    modifier = Modifier.matchParentSize(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.Red),
                                    elevation = CardDefaults.cardElevation(0.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        IconButton(
                                            onClick = {
                                                selectedChiTiet = chitiet
                                                showConfirmDialog = true
                                            },
                                            modifier = Modifier.padding(end = 8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Xóa",
                                                tint = Color.White
                                            )
                                        }
                                    }
                                }

                                // Card nội dung có thể trượt
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
                                                    val newOffset =
                                                        (offsetX + dragAmount).coerceIn(
                                                            maxOffset,
                                                            0f
                                                        )
                                                    offsetX = newOffset
                                                }
                                            )
                                        }
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            text = chitiet.TenSinhVien,
                                            color = Color(0xFF1B8DDE),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        )

                                        HorizontalDivider(
                                            modifier = Modifier
                                                .padding(vertical = 8.dp)
                                                .fillMaxWidth(),
                                            thickness = 2.dp,
                                            color = Color(0xFFDDDDDD),
                                        )

                                        InfoRow(
                                            icon = Lucide.Hash,
                                            label = "MSSV",
                                            value = chitiet.MaSV
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        InfoRow(
                                            icon = Lucide.Hash,
                                            label = "Mã Máy",
                                            value = chitiet.MaMay
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        InfoRow(
                                            icon = Lucide.MapPin,
                                            label = "Vị Trí Máy",
                                            value = chitiet.ViTri.toString()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Dialog xác nhận xóa
        if (showConfirmDialog && selectedChiTiet != null) {
            AlertDialog(
                onDismissRequest = {
                    showConfirmDialog = false
                    selectedChiTiet = null
                },
                shape = RoundedCornerShape(12.dp),
                containerColor = Color.White,
                titleContentColor = Color.Black,
                textContentColor = Color.Black,
                title = {
                    Text(
                        text = "Xác nhận xóa",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                text = {
                    Text("Bạn có chắc chắn muốn xóa sinh viên này khỏi danh sách điểm danh?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            selectedChiTiet?.let {
                                visibleMap[it.MaChiTietSuDung] = false
                                pendingDelete = true
                            }
                            showConfirmDialog = false
                        }
                    ) {
                        Text("Xóa", color = Color.Red, fontWeight = FontWeight.SemiBold)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showConfirmDialog = false
                            selectedChiTiet = null
                        }
                    ) {
                        Text("Hủy", color = Color.Gray)
                    }
                }
            )
        }
    }
}




