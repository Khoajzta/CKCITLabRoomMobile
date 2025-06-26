import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.ckcitlabroom.viewmodels.MayTinhViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CardPhieuSuaChua(
    phieuSuaChuarp: PhieuSuaChuaRp,
    phieuSuaChuaViewModel: PhieuSuaChuaViewModel,
    lichSuSuaMayViewModel: LichSuSuaMayViewModel,
    mayTinhViewModel: MayTinhViewModel,
    giangVienViewModel: GiangVienViewModel
) {
    val giangVien = giangVienViewModel.giangvienSet
    var showDialog by remember { mutableStateOf(false) }

    val lichSuSuaMay = lichSuSuaMayViewModel.lichSuSuaMayMap[phieuSuaChuarp.MaPhieuSuaChua.toString()]

    LaunchedEffect(phieuSuaChuarp.MaPhieuSuaChua) {
        lichSuSuaMayViewModel.getLichSuTheoMaPhieu(phieuSuaChuarp.MaPhieuSuaChua.toString())
    }

    val ngaySuaXong = remember(lichSuSuaMay) {
        lichSuSuaMay?.NgaySuaXong?.takeIf { it.isNotBlank() }?.let { formatNgay(it) }
            ?: "Chưa sửa xong"
    }

    val (statusColor, statusText, statusIcon) = when (phieuSuaChuarp.TrangThai) {
        1 -> Triple(Color(0xFF4CAF50), "Đã Sửa Chữa", Lucide.CircleCheck)
        0 -> Triple(Color(0xFF1B8DDE), "Đang Sửa Chữa", Lucide.Clock)
        else -> Triple(Color.Gray, "Không xác định", Lucide.CircleAlert)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Phiếu Sửa Chữa: ${phieuSuaChuarp.MaPhieuSuaChua}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B8DDE)
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.5.dp,
                color = Color(0xFFDDDDDD),
            )

            InfoRow(Lucide.Cpu, "Mã Máy", phieuSuaChuarp.MaMay)
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(Lucide.Monitor, "Tên Máy", phieuSuaChuarp.TenMay ?: "")
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(Lucide.MapPin, "Vị Trí", phieuSuaChuarp.ViTri ?: "")
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(Lucide.LayoutTemplate, "Phòng", phieuSuaChuarp.TenPhong ?: "")
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(Lucide.CalendarDays, "Ngày Báo Hỏng", formatNgay(phieuSuaChuarp.NgayBaoHong))
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(Lucide.CircleAlert, "Mô Tả Lỗi", phieuSuaChuarp.MoTaLoi)
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(Lucide.User, "Người Báo Hỏng", phieuSuaChuarp.TenNguoiBaoHong ?: "")
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(Lucide.User, "Mã Người Báo Hỏng", phieuSuaChuarp.MaNguoiBaoHong)
            Spacer(modifier = Modifier.height(8.dp))
            if(phieuSuaChuarp.TenNguoiSua!= null){
                InfoRow(Lucide.BadgeInfo, "Người Sửa", phieuSuaChuarp.TenNguoiSua)
                Spacer(modifier = Modifier.height(8.dp))
            }
            InfoRow(Lucide.CalendarDays, "Ngày Sửa Xong", ngaySuaXong)

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(statusIcon, contentDescription = null, tint = statusColor, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text("Trạng thái: ", fontWeight = FontWeight.Medium)
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                )
                Spacer(Modifier.width(4.dp))
                Text(statusText, color = statusColor, fontWeight = FontWeight.Bold)
            }

            if (phieuSuaChuarp.TrangThai == 0 && giangVien != null) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B8DDE))
                ) {
                    Text("Cập Nhật Trạng Thái", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    containerColor = Color.White,
                    title = {
                        Text(
                            "Cập nhật trạng thái",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    },
                    text = {
                        Text("Bạn có chắc muốn đánh dấu phiếu này là đã sửa chữa?", color = Color.Black)
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                val updatedPhieu = PhieuSuaChua(
                                    MaPhieuSuaChua = phieuSuaChuarp.MaPhieuSuaChua,
                                    MaMay = phieuSuaChuarp.MaMay,
                                    NgayBaoHong = phieuSuaChuarp.NgayBaoHong,
                                    MoTaLoi = phieuSuaChuarp.MoTaLoi,
                                    MaPhong = phieuSuaChuarp.MaPhong,
                                    MaNguoiBaoHong = phieuSuaChuarp.MaNguoiBaoHong,
                                    MaGV = giangVien!!.MaGV,
                                    TrangThai = 1
                                )
                                phieuSuaChuaViewModel.updatePhieuSuaChua(updatedPhieu)

                                val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)

                                val lichSu = LichSuSuaMay(
                                    MaPhieuSuaChua = phieuSuaChuarp.MaPhieuSuaChua,
                                    NgaySuaXong = today,
                                    MaMay = phieuSuaChuarp.MaMay
                                )
                                lichSuSuaMayViewModel.createLichSuSuaMay(lichSu)

                                mayTinhViewModel.updateTrangThaiMayTinh(
                                    MayTinhTrangThaiUpdateRequest(phieuSuaChuarp.MaMay, 1)
                                )

                                showDialog = false
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50))
                        ) {
                            Text("Xác nhận", color = Color.White)
                        }
                    }
                )
            }
        }
    }
}

