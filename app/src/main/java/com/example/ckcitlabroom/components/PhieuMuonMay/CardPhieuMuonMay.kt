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
import androidx.navigation.NavHostController
import com.composables.icons.lucide.BadgeInfo
import com.composables.icons.lucide.CalendarDays
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.Clock
import com.composables.icons.lucide.Cpu
import com.composables.icons.lucide.LayoutTemplate
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPin
import com.composables.icons.lucide.Monitor
import com.composables.icons.lucide.User
import com.example.lapstore.viewmodels.ChiTietPhieuMuonViewModel
import com.example.lapstore.viewmodels.LichSuChuyenMayViewModel
import com.example.lapstore.viewmodels.MayTinhViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@Composable
fun CardPhieuMuonMay(
    phieuMuonMay: PhieuMuonMay,
    phieuMuonMayViewModel: PhieuMuonMayViewModel,
    phongMayViewModel: PhongMayViewModel,
    navController: NavHostController,
    chiTietPhieuMuonViewModel: ChiTietPhieuMuonViewModel,
    mayTinhViewModel: MayTinhViewModel,
    lichSuChuyenMayViewModel: LichSuChuyenMayViewModel
){
    var showDialog by remember { mutableStateOf(false) }

    var phongMayCard by remember { mutableStateOf<PhongMay?>(null) }

    val danhSachChiTiet by remember { derivedStateOf { chiTietPhieuMuonViewModel.danhSachChiTietPhieuMuonTheoMaPhieu } }
    val danhSachMayTinh by remember { derivedStateOf { mayTinhViewModel.danhSachAllMayTinh } }

    val mayTinhDaMuon = remember(danhSachChiTiet, danhSachMayTinh) {
        danhSachMayTinh.filter { may ->
            danhSachChiTiet.any { ct -> ct.MaMay == may.MaMay }
        }
    }

    LaunchedEffect(phieuMuonMay.MaPhieuMuon) {
        chiTietPhieuMuonViewModel.getChiTietPhieuMuonTheoMaPhieuOnce(phieuMuonMay.MaPhieuMuon.toString())
        mayTinhViewModel.getAllMayTinh()
    }

    LaunchedEffect(phieuMuonMay.MaPhong) {
        phongMayCard = phongMayViewModel.fetchPhongMayByMaPhong(phieuMuonMay.MaPhong)
    }

    Card(
        modifier = Modifier
            .padding(bottom = 12.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 7.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = {
            navController.navigate(NavRoute.CHITIETPHIEUMUON.route + "?maphieumuon=${phieuMuonMay.MaPhieuMuon}")
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            InfoRow(icon = Lucide.Cpu, label = "Tên Người Mượn", value = phieuMuonMay.NguoiMuon)
            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(icon = Lucide.Monitor, label = "Ngày Mượn", value = formatNgay(phieuMuonMay.NgayMuon))
            Spacer(modifier = Modifier.height(8.dp))

            if(phieuMuonMay.NgayTra != "0000-00-00" && phieuMuonMay.TrangThai == 2){
                InfoRow(icon = Lucide.CalendarDays, label = "Ngày Trả", value = formatNgay(phieuMuonMay.NgayTra))
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (phongMayCard != null) {
                InfoRow(
                    icon = Lucide.MapPin,
                    label = "Khoa Mượn",
                    value = phongMayCard?.TenPhong.orEmpty()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }



            InfoRow(icon = Lucide.CalendarDays, label = "Số lượng", value = phieuMuonMay.SoLuong.toString())

            val (color, statusText, statusIcon) = when (phieuMuonMay.TrangThai) {
                0 -> Triple(Color(0xFF1B8DDE), "Chưa Chuyển Máy", Lucide.CircleCheck)
                1 -> Triple(Color(0xFF1B8DDE), "Chưa Trả Máy", Lucide.Clock)
                2 -> Triple(Color(0xFF4CAF50), "Đã Trả Máy", Lucide.Clock)
                else -> Triple(Color.Gray, "Không xác định", Lucide.CircleAlert)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(
                    statusIcon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text("Trạng thái: ", fontWeight = FontWeight.ExtraBold)
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(Modifier.width(4.dp))
                Text(text = statusText, color = color, fontWeight = FontWeight.Bold)
            }

            if(phieuMuonMay.TrangThai == 0){
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate(NavRoute.CHUYENMAYPHIEUMUON.route + "?maphong=${phieuMuonMay.MaPhong}&maphieumuon=${phieuMuonMay.MaPhieuMuon}")
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xff0b9adc))
                ) {
                    Text("Chuyển Máy", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            if(phieuMuonMay.TrangThai == 1){
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate(NavRoute.UPDATETRAMAY.route + "?maphieumuon=${phieuMuonMay.MaPhieuMuon}")
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xff0b9adc))
                ) {
                    Text("Cập nhật trả máy", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }



            if (showDialog) {
                AlertDialog(
                    containerColor = Color.White,
                    onDismissRequest = { showDialog = false },
                    title = {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ){
                            Text("Cập nhật trạng thái", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    },
                    confirmButton = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                modifier = Modifier.weight(1f).padding(end = 8.dp),
                                onClick = {
                                    val ngayHienTai = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                                    val phieuMuonCapNhat = phieuMuonMay.copy(NgayTra = ngayHienTai, TrangThai = 2)

                                    phieuMuonMayViewModel.updatePhieuMuonMay(phieuMuonCapNhat)

                                    mayTinhDaMuon.forEach { mayTinh ->
                                        val mayTinhCapNhat = mayTinh.copy(MaPhong = "KHOLUUTRU", TenMay = "MAYKHOLUUTRU")
                                        mayTinhViewModel.updateMayTinh(mayTinhCapNhat)

                                        val lichSu = LichSuChuyenMay(
                                            MaLichSu = 0,
                                            MaMay = mayTinh.MaMay,
                                            MaPhongCu = phieuMuonMay.MaPhong,
                                            MaPhongMoi = "KHOLUUTRU",
                                            NgayChuyen = ngayHienTai
                                        )
                                        lichSuChuyenMayViewModel.createLichSuChuyenMay(lichSu)
                                    }

                                    showDialog = false
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50))
                            ) {
                                Text("Đã Trả Máy", color = Color.White)
                            }
                        }
                    }
                )
            }
        }
    }
}