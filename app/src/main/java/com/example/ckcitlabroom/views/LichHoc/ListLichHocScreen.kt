import android.util.Log
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.viewmodels.LichHocViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListLichHocScreen(
    maTuan: Int,
    lichHocViewModel: LichHocViewModel,
    giangVienViewModel: GiangVienViewModel,
    sinhVienViewModel: SinhVienViewModel,
    namHocViewModel: NamHocViewModel,
    tuanViewModel: TuanViewModel,
    navController: NavHostController
) {
    val giangVien = giangVienViewModel.giangvienSet
    val sinhVien = sinhVienViewModel.sinhvienSet

    val thuList = listOf("Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ Nhật")

    LaunchedEffect(Unit) {
        namHocViewModel.getAllNamHoc()
        tuanViewModel.getAllTuan()
        giangVienViewModel.getAllGiangVien()
    }

    // Danh sách từ ViewModel
    val danhsachnamhoc = namHocViewModel.danhSachAllNamHoc.firstOrNull { it.TrangThai == 1 }
    val danhsachtuantheonam = tuanViewModel.danhSachAllTuan.filter { it.MaNam == danhsachnamhoc?.MaNam }
    val danhsachgiangvien = giangVienViewModel.danhSachAllGiangVien.filter { it.TrangThai == 1 }

    // Selected states
    var selectedTuan by remember { mutableStateOf<Tuan?>(null) }
    var selectedGV by remember { mutableStateOf<GiangVien?>(null) }

    // Gán giảng viên mặc định
    LaunchedEffect(giangVien, danhsachgiangvien) {
        if (selectedGV == null && giangVien != null) {
            selectedGV = danhsachgiangvien.find { it.MaGV == giangVien.MaGV }
        }
    }

    // Gán tuần theo maTuan
    LaunchedEffect(maTuan, danhsachtuantheonam) {
        selectedTuan = danhsachtuantheonam.firstOrNull { it.MaTuan == maTuan }
    }

    // Gọi polling nếu là sinh viên
    LaunchedEffect(sinhVien?.MaLop) {
        sinhVien?.let {
            lichHocViewModel.startPollingLichHocByMaLopHoc(it.MaLop)
        }
    }

    // Gọi API lấy lịch dạy giảng viên
    LaunchedEffect(selectedGV?.MaGV) {
        selectedGV?.let {
            lichHocViewModel.getLichHocByMaGV(it.MaGV)
        }
    }

    // Danh sách hiện tại
    val danhsachlichhocSV = lichHocViewModel.danhSachLichHoctheomalop
    val danhsachlichdayGV = lichHocViewModel.danhSachLichHoctheomagv

    // Danh sách lọc theo tuần
    var danhSachLichHocSVTheoTuan by remember { mutableStateOf<List<LichHocRP>>(emptyList()) }
    var danhsachlichdayGVthuongTheoTuan by remember { mutableStateOf<List<LichHocRP>>(emptyList()) }
    var danhsachlichdayAdminTheoTuan by remember { mutableStateOf<List<LichHocRP>>(emptyList()) }

    // Lọc lịch theo tuần & giảng viên
    LaunchedEffect(maTuan, danhsachlichdayGV, danhsachlichhocSV, selectedGV?.MaGV) {
        val maGV = selectedGV?.MaGV

        danhSachLichHocSVTheoTuan = danhsachlichhocSV.filter {
            it.MaTuan == maTuan
        }

        danhsachlichdayGVthuongTheoTuan = danhsachlichdayGV.filter {
            it.MaTuan == maTuan
        }

        danhsachlichdayAdminTheoTuan = if (maGV != null) {
            danhsachlichdayGV.filter {
                it.MaTuan == maTuan  && it.MaGV == maGV
            }
        } else emptyList()
    }

    DisposableEffect(Unit) {
        onDispose {
            lichHocViewModel.stopPolling()
            lichHocViewModel.stopPollingSV()
        }
    }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Tiêu đề
        val tuan = selectedTuan
        selectedTuan?.let { tuan ->
            val tenTuan = tuan.TenTuan
            val tieuDe = when {
                giangVien != null && giangVien.MaLoaiTaiKhoan == 1 -> {
                    // Admin đang xem lịch của GV được chọn
                    "Lịch dạy tuần $tenTuan"
                }
                giangVien != null -> {
                    // Giảng viên thường
                    "Lịch dạy tuần $tenTuan"
                }
                sinhVien != null -> {
                    // Sinh viên
                    "Lịch học tuần $tenTuan lớp ${sinhVien.MaLop}"
                }
                else -> ""
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = tieuDe,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    color = Color(0xFF1B8DDE)
                )
            }
        }




        HorizontalDivider(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            thickness = 2.dp,
            color = Color(0xFF1B8DDE),
        )

        // Dropdown chọn giảng viên (admin)
        if (giangVien?.MaLoaiTaiKhoan == 1) {
            var expandedGV by remember { mutableStateOf(false) }
            val primary = Color(0xFF1B8DDE)

            ExposedDropdownMenuBox(
                expanded = expandedGV,
                onExpandedChange = { expandedGV = !expandedGV }
            ) {
                Row(
                    modifier = Modifier
                        .menuAnchor()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedGV?.TenGiangVien ?: "Chọn giảng viên",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = primary,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        if (expandedGV) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                ExposedDropdownMenu(
                    expanded = expandedGV,
                    onDismissRequest = { expandedGV = false },
                    containerColor = Color.White
                ) {
                    danhsachgiangvien.forEach { gv ->
                        DropdownMenuItem(
                            text = { Text(gv.TenGiangVien) },
                            onClick = {
                                selectedGV = gv
                                expandedGV = false
                            }
                        )
                    }
                }
            }
        }

        // Nhóm theo thứ
        fun groupLichHocByThu(danhSach: List<LichHocRP>): Map<String, List<LichHocRP>> {
            return danhSach.groupBy { it.Thu }
        }

        when (giangVien?.MaLoaiTaiKhoan) {
            1 -> {
                // ADMIN
                val grouped = remember(danhsachlichdayAdminTheoTuan) {
                    groupLichHocByThu(danhsachlichdayAdminTheoTuan)
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize().clip(shape = RoundedCornerShape(12.dp))
                ) {
                    if (grouped.isEmpty()) {
                        item {
                            Text(
                                text = "Giảng viên ${selectedGV?.TenGiangVien ?: "?"} không có lịch dạy trong tuần",
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    } else {

                        thuList.forEach { thu ->
                            val lichTrongThu = grouped[thu]
                                ?.sortedBy { it.TenCa }
                                ?: emptyList()

                            if (lichTrongThu.isNotEmpty()) {
                                item {
                                    Column {
                                        Text(
                                            text = thu,
                                            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 4.dp),
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 18.sp
                                        )

                                        val listState = rememberLazyListState()

                                        LazyRow(
                                            modifier = Modifier.fillMaxWidth(),
                                            state = listState,
                                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                                            contentPadding = PaddingValues(end = 3.dp),
                                            flingBehavior = rememberSnapFlingBehavior(listState)
                                        ) {
                                            items(lichTrongThu) { lichhoc ->
                                                CardLichHoc(lichhoc, giangVien = giangVien, navController = navController)
                                                Spacer(modifier = Modifier.width(12.dp))
                                            }
                                        }

                                        val currentPage by remember {
                                            derivedStateOf {
                                                listState.layoutInfo.visibleItemsInfo
                                                    .firstOrNull()?.index ?: 0
                                            }
                                        }


                                        val scope = rememberCoroutineScope()

                                        if (lichTrongThu.size > 1) {
                                            Row(
                                                modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                repeat(lichTrongThu.size) { index ->
                                                    val selected = index == currentPage

                                                    // 👇 Size & color có animation
                                                    val dotSize by animateDpAsState(
                                                        targetValue = if (selected) 13.dp else 8.dp,
                                                        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
                                                    )

                                                    val dotColor by animateColorAsState(
                                                        targetValue = if (selected) Color.White
                                                        else Color.White.copy(alpha = 0.3f),
                                                        animationSpec = tween(200)
                                                    )

                                                    Box(
                                                        modifier = Modifier
                                                            .size(dotSize)
                                                            .clip(CircleShape)
                                                            .background(dotColor)
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
                            }
                        }
                    }
                }
            }

            2 -> {
                // GIẢNG VIÊN
                val grouped = remember(danhsachlichdayGVthuongTheoTuan) {
                    groupLichHocByThu(danhsachlichdayGVthuongTheoTuan)
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize().clip(shape = RoundedCornerShape(12.dp))
                ) {
                    if (grouped.isEmpty()) {
                        item {
                            Text(
                                text = "Không có lịch dạy trong tuần",
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    } else {
                        thuList.forEach { thu ->

                            val lichTrongThu = grouped[thu]
                                ?.sortedBy { it.TenCa }
                                ?: emptyList()

                            if (lichTrongThu.isNotEmpty()) {
                                item {
                                    Column {
                                        Text(
                                            text = thu,
                                            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 4.dp),
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 18.sp
                                        )
                                        val listState = rememberLazyListState()

                                        LazyRow(
                                            modifier = Modifier.fillMaxWidth(),
                                            state = listState,
                                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                                            contentPadding = PaddingValues(end = 3.dp),
                                            flingBehavior = rememberSnapFlingBehavior(listState)
                                        ) {
                                            items(lichTrongThu) { lichhoc ->
                                                CardLichHoc(lichhoc, giangVien = giangVien, navController = navController)
                                                Spacer(modifier = Modifier.width(12.dp))
                                            }
                                        }

                                        val currentPage by remember {
                                            derivedStateOf {
                                                listState.layoutInfo.visibleItemsInfo
                                                    .firstOrNull()?.index ?: 0
                                            }
                                        }


                                        val scope = rememberCoroutineScope()

                                        if (lichTrongThu.size > 1) {
                                            Row(
                                                modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                repeat(lichTrongThu.size) { index ->
                                                    val selected = index == currentPage

                                                    // 👇 Size & color có animation
                                                    val dotSize by animateDpAsState(
                                                        targetValue = if (selected) 13.dp else 8.dp,
                                                        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
                                                    )

                                                    val dotColor by animateColorAsState(
                                                        targetValue = if (selected) Color.White
                                                        else Color.White.copy(alpha = 0.3f),
                                                        animationSpec = tween(200)
                                                    )

                                                    Box(
                                                        modifier = Modifier
                                                            .size(dotSize)
                                                            .clip(CircleShape)
                                                            .background(dotColor)
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
                            }
                        }
                    }
                }
            }

            else -> {
                // SINH VIÊN
                val grouped = remember(danhSachLichHocSVTheoTuan) {
                    groupLichHocByThu(danhSachLichHocSVTheoTuan)
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize().clip(shape = RoundedCornerShape(12.dp))
                ) {
                    if (grouped.isEmpty()) {
                        item {
                            Text(
                                text = "Không có lịch học trong tuần",
                                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    } else {
                        thuList.forEach { thu ->

                            val lichTrongThu = grouped[thu]
                                ?.sortedBy { it.TenCa }
                                ?: emptyList()

                            if (lichTrongThu.isNotEmpty()) {
                                item {
                                    Column {
                                        Text(
                                            text = thu,
                                            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 4.dp),
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 18.sp
                                        )
                                        val listState = rememberLazyListState()

                                        LazyRow(
                                            modifier = Modifier.fillMaxWidth(),
                                            state = listState,
                                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                                            contentPadding = PaddingValues(end = 3.dp),
                                            flingBehavior = rememberSnapFlingBehavior(listState)
                                        ) {
                                            items(lichTrongThu) { lichhoc ->
                                                CardLichHoc(lichhoc, sinhvien = sinhVien, navController = navController)
                                                Spacer(modifier = Modifier.width(12.dp))
                                            }
                                        }

                                        val currentPage by remember {
                                            derivedStateOf {
                                                listState.layoutInfo.visibleItemsInfo
                                                    .firstOrNull()?.index ?: 0
                                            }
                                        }


                                        val scope = rememberCoroutineScope()

                                        if (lichTrongThu.size > 1) {
                                            Row(
                                                modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                repeat(lichTrongThu.size) { index ->
                                                    val selected = index == currentPage

                                                    // 👇 Size & color có animation
                                                    val dotSize by animateDpAsState(
                                                        targetValue = if (selected) 13.dp else 8.dp,
                                                        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
                                                    )

                                                    val dotColor by animateColorAsState(
                                                        targetValue = if (selected) Color.White
                                                        else Color.White.copy(alpha = 0.3f),
                                                        animationSpec = tween(200)
                                                    )

                                                    Box(
                                                        modifier = Modifier
                                                            .size(dotSize)
                                                            .clip(CircleShape)
                                                            .background(dotColor)
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
                            }
                        }
                    }
                }
            }
        }
    }
}











