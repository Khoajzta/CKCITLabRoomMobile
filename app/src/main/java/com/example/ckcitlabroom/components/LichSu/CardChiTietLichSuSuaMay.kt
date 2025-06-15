import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.composables.icons.lucide.BadgeInfo
import com.composables.icons.lucide.BookOpen
import com.composables.icons.lucide.CalendarDays
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.Clock
import com.composables.icons.lucide.Cpu
import com.composables.icons.lucide.LayoutTemplate
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPin
import com.composables.icons.lucide.Monitor
import com.composables.icons.lucide.Tag
import com.composables.icons.lucide.User
import com.example.lapstore.viewmodels.MayTinhViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CardChiTietLichSuSuaMay(
    phieuSuaChuarp: PhieuSuaChuaRp,
    lichSuSuaMayViewModel: LichSuSuaMayViewModel,
) {
    val lichSuSuaMay = lichSuSuaMayViewModel.lichSuSuaMayMap[phieuSuaChuarp.MaPhieuSuaChua.toString()]

    LaunchedEffect(phieuSuaChuarp.MaPhieuSuaChua) {
        lichSuSuaMayViewModel.getLichSuTheoMaPhieu(phieuSuaChuarp.MaPhieuSuaChua.toString())
    }

    val ngaysuaxong = remember(lichSuSuaMay) {
        if (lichSuSuaMay == null || lichSuSuaMay.NgaySuaXong.isNullOrBlank()) {
            "Chưa sửa xong"
        } else {
            formatNgay(lichSuSuaMay.NgaySuaXong)
        }
    }


    val nguoiBaoHongLabel = when (phieuSuaChuarp.MaLoaiTaiKhoan) {
        1 -> "MaGV"     // Admin
        2 -> "MaGV"    // Giảng viên
        3 -> "MSSV"    // Sinh viên
        else -> "Mã Người Báo Hỏng"
    }

    Card(
        modifier = Modifier
            .padding(bottom = 12.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 7.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Thông tin lịch sử",
                color = Color(0xFF1B8DDE),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
                thickness = 2.dp,
                color = Color(0xFFDDDDDD),
            )

            InfoRow(icon = Lucide.Cpu, label = "Mã Máy", value = phieuSuaChuarp.MaMay)
            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(icon = Lucide.Monitor, label = "Tên Máy", value = phieuSuaChuarp.TenMay ?: "")
            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(icon = Lucide.MapPin, label = "Vị Trí", value = phieuSuaChuarp.ViTri ?: "")
            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(
                icon = Lucide.CalendarDays,
                label = "Ngày Báo Hỏng",
                value = formatNgay(phieuSuaChuarp.NgayBaoHong)
            )
            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(
                icon = Lucide.CircleAlert,
                label = "Mô Tả Lỗi",
                value = phieuSuaChuarp.MoTaLoi
            )
            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(icon = Lucide.LayoutTemplate, label = "Phòng", value = phieuSuaChuarp.TenPhong ?: "")
            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(icon = Lucide.User, label = "Người Báo Hỏng", value = phieuSuaChuarp.TenNguoiBaoHong ?: "")
            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(icon = Lucide.BadgeInfo, label = nguoiBaoHongLabel, value = phieuSuaChuarp.MaNguoiBaoHong)
            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(icon = Lucide.CalendarDays, label = "Ngày Sửa Xong", value = ngaysuaxong)

            val (color, statusText, statusIcon) = when (phieuSuaChuarp.TrangThai) {
                1 -> Triple(Color(0xFF4CAF50), "Đã Sửa Chữa", Lucide.CircleCheck)
                0 -> Triple(Color(0xFF1B8DDE), "Đang Sửa Chữa", Lucide.Clock)
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
        }
    }
}

