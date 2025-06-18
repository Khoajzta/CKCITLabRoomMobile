import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.CircleX
import com.composables.icons.lucide.Hash
import com.composables.icons.lucide.Lucide
import com.example.ckcitlabroom.viewmodels.MayTinhViewModel

@Composable
fun CardPhongMayChuyen(
    phongmay: PhongMay,
    mayTinhViewModel: MayTinhViewModel,
    onClick: () -> Unit
) {
    LaunchedEffect(Unit) {
        mayTinhViewModel.getAllMayTinh()
    }

    val danhSachMayTinh = mayTinhViewModel.danhSachAllMayTinh
    val soLuongMay by remember(danhSachMayTinh) {
        mutableStateOf(danhSachMayTinh?.count { it.MaPhong == phongmay.MaPhong } ?: 0)
    }

    val (color, statusText, statusIcon) = when (phongmay.TrangThai) {
        1 -> Triple(Color(0xFF4CAF50), "Hoạt động", Lucide.CircleCheck)
        0 -> Triple(Color(0xFFF44336), "Bảo trì", Lucide.CircleX)
        else -> Triple(Color.Gray, "Không xác định", Lucide.CircleAlert)
    }

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(6.dp, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Phòng Máy: ${phongmay.TenPhong}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF1B8DDE)
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                thickness = 1.dp,
                color = Color(0xFFCCCCCC)
            )

            InfoRow(icon = Lucide.Hash, label = "Số lượng máy", value = soLuongMay.toString())

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(
                    imageVector = statusIcon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text("Trạng thái:", fontWeight = FontWeight.ExtraBold)
                Spacer(Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(Modifier.width(4.dp))
                Text(statusText, color = color, fontWeight = FontWeight.Bold)
            }
        }
    }
}





