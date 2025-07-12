import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ckcitlabroom.viewmodels.ChiTietDonNhapyViewModel
import com.example.ckcitlabroom.viewmodels.ChiTietPhieuMuonViewModel
import com.example.ckcitlabroom.viewmodels.LichSuChuyenMayViewModel
import com.example.ckcitlabroom.viewmodels.MayTinhViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChiTietDonNhapChuyenMuonScreen(
    maphieumuon: String,
    maphong: String,
    maphongmuon: String,
    maDonNhap: String,
    chiTietDonNhapyViewModel: ChiTietDonNhapyViewModel,
    phongMayViewModel: PhongMayViewModel,
    mayTinhViewModel: MayTinhViewModel,
    lichSuChuyenMayViewModel: LichSuChuyenMayViewModel,
    phieuMuonMayViewModel: PhieuMuonMayViewModel,
    chiTietPhieuMuonViewModel: ChiTietPhieuMuonViewModel
) {

    val coroutineScope = rememberCoroutineScope()

    var context = LocalContext.current

    val danhSachChiTiet by produceState(initialValue = emptyList<ChiTietDonNhap>(), maDonNhap) {
        value = chiTietDonNhapyViewModel.getChiTietDonNhapListOnce(maDonNhap)
    }

    val danhSachMayTinh by produceState(initialValue = emptyList<MayTinh>()) {
        value = mayTinhViewModel.getAllMayTinhOnce()
    }

    val phongmaymuon = phongMayViewModel.phongmay

    LaunchedEffect(Unit) {
        phongMayViewModel.getPhongMayByMaPhong(maphongmuon)
        phieuMuonMayViewModel.getPhieuMuonByMaPhieu(maphieumuon)
    }

    val phieuMuon = phieuMuonMayViewModel.phieuMuonMay
    val maxCount = phieuMuon?.SoLuong ?: 0


    val danhSachMayTinhTheoDon = remember(danhSachChiTiet, danhSachMayTinh) {
        val maMayTheoDon = danhSachChiTiet.map { it.MaMay }
        danhSachMayTinh.filter { it.MaMay in maMayTheoDon }.sortedBy { may ->
            // Trích số từ tên máy, mặc định 0 nếu không trích được
            Regex("""\d+""").find(may.TenMay)?.value?.toIntOrNull() ?: 0
        }
    }

    LaunchedEffect(maphieumuon) {
        maphieumuon.let { mayTinhViewModel.loadMayTinhDangChonOPhieuKhac(it) }
    }

    val mayDangChonOPhieuKhac by mayTinhViewModel.mayDangDuocChonOPhieuKhac.observeAsState(emptySet())

    val danhSachMayTinhTrongKhoTheoDon = remember(danhSachMayTinhTheoDon) {
        danhSachMayTinhTheoDon.filter {
            it.MaPhong.equals(
                maphong,
                ignoreCase = true
            ) && it.TrangThai == 1
        }
            .filterNot { it.MaMay in mayDangChonOPhieuKhac }
    }

    val selectedIds by mayTinhViewModel
        .selectedIdsFlow(maphieumuon)
        .collectAsState(initial = emptySet())

    LaunchedEffect(maDonNhap) {
        chiTietDonNhapyViewModel.getChiTietDonNhapTheoMaDonNhap(maDonNhap)
        mayTinhViewModel.getAllMayTinh()
    }

    DisposableEffect(Unit) {
        onDispose {
            mayTinhViewModel.stopPollingMayTinhTheoPhong()
        }
    }

    val selectedMayTinhs = remember(selectedIds, danhSachMayTinh) {
        danhSachMayTinh.filter { it.MaMay in selectedIds }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Danh Sách Máy Tính",
                fontWeight = FontWeight.SemiBold,
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

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            if (danhSachMayTinhTrongKhoTheoDon.isNullOrEmpty()) {
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
                items(danhSachMayTinhTrongKhoTheoDon) { maytinh ->
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
