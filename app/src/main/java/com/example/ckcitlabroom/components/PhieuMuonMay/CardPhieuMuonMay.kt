import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.composables.icons.lucide.*
import com.composables.icons.lucide.User
import com.example.lapstore.viewmodels.ChiTietPhieuMuonViewModel
import com.example.lapstore.viewmodels.LichSuChuyenMayViewModel
import com.example.lapstore.viewmodels.MayTinhViewModel

@Composable
fun CardPhieuMuonMay(
    phieuMuonMay: PhieuMuonMay,
    phieuMuonMayViewModel: PhieuMuonMayViewModel,
    phongMayViewModel: PhongMayViewModel,
    navController: NavHostController,
    chiTietPhieuMuonViewModel: ChiTietPhieuMuonViewModel,
    mayTinhViewModel: MayTinhViewModel,
    lichSuChuyenMayViewModel: LichSuChuyenMayViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    var phongMayCard by remember { mutableStateOf<PhongMay?>(null) }

    val danhSachChiTiet by remember { derivedStateOf { chiTietPhieuMuonViewModel.danhSachChiTietPhieuMuonTheoMaPhieu } }
    val danhSachMayTinh by remember { derivedStateOf { mayTinhViewModel.danhSachAllMayTinh } }

    val mayTinhDaMuon = remember(danhSachChiTiet, danhSachMayTinh) {
        danhSachMayTinh.filter { may -> danhSachChiTiet.any { ct -> ct.MaMay == may.MaMay } }
    }

    LaunchedEffect(phieuMuonMay.MaPhieuMuon) {
        chiTietPhieuMuonViewModel.getChiTietPhieuMuonTheoMaPhieuOnce(phieuMuonMay.MaPhieuMuon.toString())
        mayTinhViewModel.getAllMayTinh()
    }

    LaunchedEffect(phieuMuonMay.MaPhong) {
        phongMayCard = phongMayViewModel.fetchPhongMayByMaPhong(phieuMuonMay.MaPhong)
    }

    val (color, statusText, statusIcon) = when (phieuMuonMay.TrangThai) {
        0 -> Triple(Color(0xFFFF9800), "Chưa Chuyển Máy", Lucide.Truck)
        1 -> Triple(Color(0xFF03A9F4), "Đang Mượn", Lucide.Clock)
        2 -> Triple(Color(0xFF4CAF50), "Đã Trả Máy", Lucide.CircleCheck)
        else -> Triple(Color.Gray, "Không xác định", Lucide.CircleHelp)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(7.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = {
            navController.navigate(NavRoute.CHITIETPHIEUMUON.route + "?maphieumuon=${phieuMuonMay.MaPhieuMuon}")
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

            InfoRow(icon = Lucide.User, label = "Người Mượn", value = phieuMuonMay.NguoiMuon)
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(icon = Lucide.MapPin, label = "Phòng", value = phongMayCard?.TenPhong.orEmpty())
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(icon = Lucide.Cpu, label = "Số lượng máy", value = phieuMuonMay.SoLuong.toString())
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(icon = Lucide.CalendarCheck, label = "Ngày Mượn", value = formatNgay(phieuMuonMay.NgayMuon))

            if (phieuMuonMay.NgayTra != "0000-00-00" && phieuMuonMay.TrangThai == 2) {
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow(icon = Lucide.CalendarX2, label = "Ngày Trả", value = formatNgay(phieuMuonMay.NgayTra))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Trạng thái
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(statusIcon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Trạng thái:", fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.width(4.dp))
                Text(statusText, color = color, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nút chức năng
            when (phieuMuonMay.TrangThai) {
                0 -> Button(
                    onClick = {
                        navController.navigate(NavRoute.CHUYENMAYPHIEUMUON.route +
                                "?maphong=${phieuMuonMay.MaPhong}&maphieumuon=${phieuMuonMay.MaPhieuMuon}")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B8DDE)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Lucide.Truck, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Chuyển Máy", color = Color.White, fontWeight = FontWeight.Bold)
                }

                1 -> Button(
                    onClick = {
                        navController.navigate(NavRoute.UPDATETRAMAY.route + "?maphieumuon=${phieuMuonMay.MaPhieuMuon}")
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
