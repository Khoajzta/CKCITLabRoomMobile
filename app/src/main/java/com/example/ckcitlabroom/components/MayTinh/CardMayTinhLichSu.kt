import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Building2
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.CircleX
import com.composables.icons.lucide.ClipboardList
import com.composables.icons.lucide.Cpu
import com.composables.icons.lucide.Lucide


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardMayTinhLichSu(
    maytinh: MayTinh,
    phongMayViewModel: PhongMayViewModel,
    click:() -> Unit
) {
    val danhSachPhongMay = phongMayViewModel.danhSachAllPhongMay
    var selectdMaPhong by remember { mutableStateOf("") }
    var phongMayCard by remember { mutableStateOf<PhongMay?>(null) }
    val maPhongState = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        phongMayViewModel.getAllPhongMay()
    }

    LaunchedEffect(maytinh.MaPhong) {
        phongMayCard = phongMayViewModel.fetchPhongMayByMaPhong(maytinh.MaPhong)
    }

    LaunchedEffect(selectdMaPhong) {
        maPhongState.value = selectdMaPhong
    }

    LaunchedEffect(danhSachPhongMay) {
        if (danhSachPhongMay.isNotEmpty() && selectdMaPhong.isEmpty()) {
            selectdMaPhong = danhSachPhongMay[0].MaPhong
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable {
                click()
            }
            .shadow(6.dp, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Header: chỉ còn Mã máy
            InfoRow(
                icon = Lucide.ClipboardList,
                label = "Mã Máy",
                value = maytinh?.MaMay ?: "Đang tải..."
            )

            Spacer(Modifier.height(12.dp))

            InfoRow(
                icon = Lucide.Cpu,
                label = "Tên Máy",
                value = maytinh?.TenMay ?: "Đang tải..."
            )

            Spacer(Modifier.height(6.dp))

            InfoRow(
                icon = Lucide.Building2,
                label = "Phòng hiện tại",
                value = phongMayCard?.TenPhong ?: "Đang tải..."
            )

            Spacer(Modifier.height(12.dp))

            // Trạng thái cuối cùng
            val (color, statusText, statusIcon) = when (maytinh.TrangThai) {
                1 -> Triple(Color(0xFF4CAF50), "Hoạt động", Lucide.CircleCheck)
                0 -> Triple(Color(0xFFF44336), "Đang bảo trì", Lucide.CircleX)
                else -> Triple(Color.Gray, "Không xác định", Lucide.CircleAlert)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = statusIcon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "Trạng thái:",
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = statusText,
                    color = color,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }
    }


}


