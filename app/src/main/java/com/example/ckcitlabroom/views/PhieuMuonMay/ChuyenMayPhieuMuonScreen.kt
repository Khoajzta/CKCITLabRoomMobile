import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.viewmodels.ChiTietDonNhapyViewModel
import com.example.ckcitlabroom.viewmodels.DonNhapViewModel
import com.example.ckcitlabroom.viewmodels.MayTinhViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChuyenMayPhieuMuonScreen(
    maphongMuon: String,
    maphieumuon: String,
    navController: NavHostController,
    mayTinhViewModel: MayTinhViewModel,
    phongMayViewModel: PhongMayViewModel,
    phieuMuonMayViewModel: PhieuMuonMayViewModel,
    donNhapViewModel: DonNhapViewModel,
    chiTietDonNhapViewModel: ChiTietDonNhapyViewModel
) {

    BackHandler {
        mayTinhViewModel.clearDanhSachMayTinhDuocChon()
        navController.popBackStack()
    }

    val phieuMuon = phieuMuonMayViewModel.phieuMuonMay

    val context = LocalContext.current
    val maxCount = phieuMuon?.SoLuong ?: 0

    var expanded by remember { mutableStateOf(false) }

    var selectedPhong by remember { mutableStateOf<PhongMay?>(null) }
    val selectedIds by mayTinhViewModel
        .selectedIdsFlow(maphieumuon)
        .collectAsState(initial = emptySet())

    val danhSachPhongMay =
        phongMayViewModel.danhSachAllPhongMay.filter { it.LoaiPhong == 1 || it.LoaiPhong == 2 }

    var danhsachallmaytinh = mayTinhViewModel.danhSachAllMayTinh

    val danhSachMayTheoPhong by remember(
        selectedPhong,
        mayTinhViewModel.danhSachAllMayTinhtheophong
    ) {
        derivedStateOf {
            val maPhong = selectedPhong?.MaPhong
            mayTinhViewModel.danhSachAllMayTinhtheophong
                .filter { it.TrangThai == 1 && it.MaPhong == maPhong }   // lọc phòng
                .sortedBy { may ->
                    Regex("""\d+""").find(may.TenMay)?.value?.toIntOrNull() ?: 0
                }
        }
    }


    Log.d("danhsachAllmaytinh", danhsachallmaytinh.toString())
    Log.d("selectedIds", selectedIds.toString())

    LaunchedEffect(Unit) {
        phongMayViewModel.getAllPhongMay()
        phieuMuonMayViewModel.getPhieuMuonByMaPhieu(maphieumuon)
        phongMayViewModel.getPhongMayByMaPhong(maphongMuon)
        mayTinhViewModel.getAllMayTinh()
    }

    LaunchedEffect(danhSachPhongMay) {
        if (selectedPhong == null && danhSachPhongMay.isNotEmpty()) {
            selectedPhong = danhSachPhongMay.first()
        }
    }

    LaunchedEffect(selectedPhong) {
        selectedPhong?.let { mayTinhViewModel.getMayTinhByPhong(it.MaPhong) }
    }

    DisposableEffect(Unit) {
        onDispose { mayTinhViewModel.stopPollingMayTinhTheoPhong() }
    }

    val selectedMayTinhs = remember(selectedIds, danhsachallmaytinh) {
        danhsachallmaytinh.filter { it.MaMay in selectedIds }
    }

    var danhsachdonnhap = donNhapViewModel.danhSachDonNhap

    LaunchedEffect(Unit) {
        donNhapViewModel.getAllDonNhap()
    }

    DisposableEffect(Unit) {
        onDispose {
            donNhapViewModel.stopPollingAllDonNhap()
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {


            val primary = Color(0xFF1B8DDE)

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                /* ---------- Anchor ---------- */
                Row(
                    modifier = Modifier
                        .menuAnchor()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .widthIn(min = 80.dp, max = 200.dp)
                        .wrapContentWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedPhong?.TenPhong ?: "Chọn phòng",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = primary,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        if (expanded) Icons.Default.KeyboardArrowUp
                        else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                /* ---------- Menu ---------- */
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    containerColor = Color.White
                ) {
                    danhSachPhongMay.forEach { phong ->
                        DropdownMenuItem(
                            text = { androidx.compose.material.Text(phong.TenPhong) },
                            onClick = {
                                selectedPhong = phong
                                expanded = false
                            }
                        )
                    }
                }
            }
        }


        if (selectedPhong != null && selectedPhong!!.LoaiPhong == 2) {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {

                if (danhsachdonnhap.isNullOrEmpty()) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Chưa có đơn nhập nào",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp
                            )
                        }
                    }
                } else {
                    items(danhsachdonnhap) { donnhap ->
                        CardDonNhapChuyen(
                            selectedPhong!!.MaPhong,
                            donnhap,
                            chiTietDonNhapViewModel,
                            mayTinhViewModel,
                            navController,
                            click = {
                                navController.navigate(
                                    NavRoute.CHITIETDONNHAPCHUYENMUON.route + "?madonnhap=${donnhap.MaDonNhap}&maphong=${selectedPhong!!.MaPhong}&maphongmuon=${maphongMuon}&maphieumuon=${maphieumuon}"
                                )
                            }
                        )
                    }
                }

            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                if (danhSachMayTheoPhong.isNullOrEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "Chưa có máy tính nào",
                                color = Color.White,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                } else {
                    items(danhSachMayTheoPhong) { maytinh ->
                        val alreadySelected = maytinh.MaMay in selectedIds
                        CardMayTinhChuyenMuon(
                            mayTinh = maytinh,
                            phongMayViewModel = phongMayViewModel,
                            isSelected = alreadySelected,
                            onToggleSelect = {
                                when {
                                    /* 1️⃣  Bỏ chọn → luôn cho phép */
                                    alreadySelected -> mayTinhViewModel.toggleMayTinh(
                                        maphieumuon,
                                        maytinh.MaMay
                                    )

                                    /* 2️⃣  Đủ số lượng rồi → Toast, không thêm */
                                    selectedIds.size >= maxCount -> {
                                        Toast.makeText(
                                            context,
                                            "Đã đủ $maxCount máy",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    else -> mayTinhViewModel.toggleMayTinh(
                                        maphieumuon,
                                        maytinh.MaMay
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }




        Card(
            modifier = Modifier
                .height(300.dp)
                .padding(top = 12.dp)
                .fillMaxWidth()
                .shadow(8.dp, shape = RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Danh sách máy được chọn:",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )

                    Text(
                        selectedIds.count().toString(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                }

                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth(),
                    thickness = 2.dp,
                    color = Color(0xFFDDDDDD),
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    items(selectedMayTinhs) { mayTinh ->

                        Column {
                            Row(
                            ) {
                                Text(
                                    text = "Mã máy:",
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Text(
                                    text = " ${mayTinh.MaMay}",
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                )
                            }

                            Row {
                                Text(
                                    text = "Tên máy:",
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = " ${mayTinh.TenMay}",
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                )
                            }

                            HorizontalDivider(
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth(),
                                thickness = 2.dp,
                                color = Color(0xFFDDDDDD),
                            )
                        }
                    }
                }
            }
        }
    }
}

