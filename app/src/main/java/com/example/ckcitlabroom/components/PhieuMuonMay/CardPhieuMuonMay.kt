import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.composables.icons.lucide.CalendarCheck
import com.composables.icons.lucide.CalendarX2
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.CircleHelp
import com.composables.icons.lucide.Clock
import com.composables.icons.lucide.Cpu
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPin
import com.composables.icons.lucide.Truck
import com.composables.icons.lucide.User
import com.example.ckcitlabroom.viewmodels.ChiTietPhieuMuonViewModel
import com.example.ckcitlabroom.viewmodels.LichSuChuyenMayViewModel
import com.example.ckcitlabroom.viewmodels.MayTinhViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CardPhieuMuonMay(
    phieu: PhieuMuonMay,
    navController: NavHostController,
    mayTinhVM: MayTinhViewModel,
    phongMayVM: PhongMayViewModel,
    chiTietVM: ChiTietPhieuMuonViewModel,
    lichSuVM: LichSuChuyenMayViewModel,
    phieuVM: PhieuMuonMayViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    var showConfirm by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var context = LocalContext.current

    LaunchedEffect(phieu.MaPhieuMuon) {
        chiTietVM.startPollingChiTietPhieu(phieu.MaPhieuMuon.toString())
    }

    DisposableEffect(Unit) {
        onDispose { chiTietVM.stopPollingChiTietPhieu() }
    }


    var chitietphieumuon = chiTietVM.danhSachChiTietPhieuMuonTheoMaPhieu


    var danhsachAllMay = mayTinhVM.danhSachAllMayTinh

    LaunchedEffect(Unit) {
        mayTinhVM.getAllMayTinh()
    }

    val phong by produceState<PhongMay?>(null, phieu.MaPhong) {
        value = phongMayVM.fetchPhongMayByMaPhong(phieu.MaPhong)
    }

    val selectedIds by mayTinhVM
        .selectedIdsFlow(phieu.MaPhieuMuon.toString())
        .collectAsState(emptySet())

    val (color, statusText, statusIcon) = when (phieu.TrangThai) {
        0 -> Triple(Color(0xFFFF9800), "Chưa Chuyển Máy", Lucide.Truck)
        1 -> Triple(Color(0xFF03A9F4), "Đang Mượn", Lucide.Clock)
        2 -> Triple(Color(0xFF4CAF50), "Đã Trả Máy", Lucide.CircleCheck)
        else -> Triple(Color.Gray, "Không xác định", Lucide.CircleHelp)
    }

    /* -------- CARD -------- */
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(7.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = {
            navController.navigate(NavRoute.CHITIETPHIEUMUON.route + "?maphieumuon=${phieu.MaPhieuMuon}")
        }
    ) {

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Phiếu Mượn Máy",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B8DDE)
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 2.dp,
                color = Color(0xFFE0E0E0)
            )

            InfoRow(icon = Lucide.User, label = "Người Mượn", value = phieu.NguoiMuon)
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(icon = Lucide.MapPin, label = "Phòng", value = phong?.TenPhong.orEmpty())
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(
                icon = Lucide.Cpu,
                label = "Số lượng máy",
                value = phieu.SoLuong.toString()
            )
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(
                icon = Lucide.CalendarCheck,
                label = "Ngày Mượn",
                value = formatNgay(phieu.NgayMuon)
            )

            if (phieu.NgayTra != "0000-00-00" && phieu.TrangThai == 2) {
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow(
                    icon = Lucide.CalendarX2,
                    label = "Ngày Trả",
                    value = formatNgay(phieu.NgayTra)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Trạng thái
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    statusIcon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Trạng thái:", fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.width(4.dp))
                Text(statusText, color = color, fontWeight = FontWeight.Bold)
            }

            when (phieu.TrangThai) {
                0 -> {
                    Column {
                        Spacer(Modifier.height(8.dp))

                        val isFull = selectedIds.size == phieu.SoLuong          // đủ số lượng?

                        Button(
                            onClick = {
                                navController.navigate(
                                    "${NavRoute.CHUYENMAYPHIEUMUON.route}" +
                                            "?maphong=${phieu.MaPhong}&maphieumuon=${phieu.MaPhieuMuon}"
                                )
                            },
                            Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFF1B8DDE))
                        ) {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    if (isFull) Icons.Filled.Visibility else Icons.Filled.AddCircle,
                                    null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(Modifier.width(8.dp))

                                /* ---- Nhãn động ---- */
                                Text(
                                    text = if (isFull) "Xem Máy Đã Chọn" else "Chọn Máy Để Chuyển",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "${selectedIds.size}/${phieu.SoLuong}",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }


                        Spacer(Modifier.height(6.dp))

                        Button(
                            onClick = {
                                when {
                                    selectedIds.isEmpty() ->
                                        errorMsg = "Vui lòng chọn ít nhất một máy tính để chuyển."

                                    selectedIds.size > phieu.SoLuong ->
                                        errorMsg = "Phiếu mượn chỉ mượn ${phieu.SoLuong} máy."

                                    selectedIds.size < phieu.SoLuong ->
                                        errorMsg =
                                            "Cần ${phieu.SoLuong} máy, bạn mới chọn ${selectedIds.size}."

                                    else -> showConfirm = true
                                }
                            },
                            Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFF1B8DDE))
                        ) {
                            Icon(
                                Lucide.Truck,
                                null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Chuyển Máy", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                1 -> {
                    Button(
                        onClick = {
                            navController.navigate(NavRoute.UPDATETRAMAY.route + "?maphieumuon=${phieu.MaPhieuMuon}")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B8DDE)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cập Nhật Trả Máy", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

    }

    /* -------- Alert: xác nhận -------- */
    if (showConfirm) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showConfirm = false },
            title = {
                Text(
                    "Xác nhận",
                    color = Color(0xFF1B8DDE),
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = {
                Text(
                    "Chuyển ${selectedIds.size} máy tới phòng ${phong!!.TenPhong}?",
                    color = Color.Black
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    coroutineScope.launch {
                        val ngayChuyen = SimpleDateFormat(
                            "yyyy-MM-dd", Locale.getDefault()
                        ).format(Date())
                        val chiTietList = mutableListOf<ChiTietPhieuMuon>()
                        val listMay = danhsachAllMay
                            .filter { it.MaMay in selectedIds }


                        listMay.forEach { may ->
                            // 1️⃣  Cập nhật máy sang phòng đích
                            mayTinhVM.updateMayTinh(
                                may.copy(
                                    MaPhong = phong!!.MaPhong,            // ← phòng đích
                                    TenMay = "MAY${phong!!.MaPhong}"     // string template đúng
                                )
                            )

                            // 2️⃣  Ghi lịch sử
                            lichSuVM.createLichSuChuyenMay(
                                LichSuChuyenMay(
                                    MaLichSu = 0,
                                    MaPhongCu = may.MaPhong,
                                    MaPhongMoi = phong!!.MaPhong,
                                    NgayChuyen = ngayChuyen,
                                    MaMay = may.MaMay
                                )
                            )

                            // 3️⃣  Thêm chi tiết phiếu (giữ Int)
                            chiTietList.add(
                                ChiTietPhieuMuon(
                                    MaChiTiet = 0,
                                    MaPhieuMuon = phieu.MaPhieuMuon.toString(),   // Int
                                    MaMay = may.MaMay,
                                    TinhTrangMuon = "Hoạt động",
                                    TinhTrangTra = ""
                                )
                            )
                        }

                        // Gọi API thêm danh sách chi tiết phiếu mượn
                        val result = chiTietVM.createNhieuChiTietPhieuMuon(
                            chiTietList
                        )
                        result.onSuccess {
                            // Cập nhật trạng thái phiếu mượn nếu thêm thành công
                            phieuVM.updateTrangThaiPhieuMuon(phieu.MaPhieuMuon.toString(), 1)

                            // Xoá danh sách máy đã chọn và đóng dialog
                            mayTinhVM.clearMayTinhOfPhieu(phieu.MaPhieuMuon.toString())
                            phieuVM.getAllPhieuMuonMay()
                            Toast.makeText(
                                context,
                                "Chuyển máy thành công",
                                Toast.LENGTH_SHORT
                            ).show()
                            showConfirm = false
                        }.onFailure {
                            // Hiển thị lỗi nếu cần
                            Log.e(
                                "ChiTietPhieuMuon",
                                "Lỗi khi thêm chi tiết phiếu mượn: ${it.message}"
                            )
                        }
                    }
                }) { Text("Đồng ý", color = Color(0xFF1B8DDE)) }
            },
            dismissButton = {
                TextButton(onClick = { showConfirm = false }) {
                    Text(
                        "Hủy",
                        color = Color(0xFF1B8DDE)
                    )
                }
            }
        )
    }

    /* -------- Alert lỗi -------- */
    errorMsg?.let { msg ->
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { errorMsg = null },
            title = {
                Text(
                    "Thông báo",
                    color = Color(0xFF1B8DDE),
                    fontWeight = FontWeight.SemiBold
                )
            },
            text = { Text(msg, color = Color.Black) },
            confirmButton = {
                TextButton(onClick = { errorMsg = null }) { Text("OK", color = Color(0xFF1B8DDE)) }
            }
        )
    }
}

