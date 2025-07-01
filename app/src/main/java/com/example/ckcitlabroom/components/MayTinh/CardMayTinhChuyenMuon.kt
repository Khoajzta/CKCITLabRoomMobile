import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material.TabRowDefaults.Divider
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Building2
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.CircleX
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Monitor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardMayTinhChuyenMuon(
    mayTinh: MayTinh,
    phongMayViewModel: PhongMayViewModel,
    isSelected: Boolean,          // ← cờ đã chọn
    onToggleSelect: () -> Unit    // ← callback
) {
    /* ------ Lấy phòng hiện tại của máy ------ */
    var phongMay by remember { mutableStateOf<PhongMay?>(null) }

    LaunchedEffect(mayTinh.MaPhong) {
        phongMay = phongMayViewModel.fetchPhongMayByMaPhong(mayTinh.MaPhong)
    }

    /* ------ Card ------ */
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .shadow(6.dp, shape = RoundedCornerShape(16.dp))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Color(0xFF1B8DDE) else Color(0xFFE0E0E0),
                shape = RoundedCornerShape(16.dp)
            )
            .combinedClickable(
                onClick = { onToggleSelect() },   // 1 chạm đổi trạng thái
                onLongClick = { onToggleSelect() }    // giữ lâu cũng vậy
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFE3F2FD) else Color.White
        ),
        shape = RoundedCornerShape(16.dp)
    ) {

        /* ---------- Nội dung ---------- */
        Column(Modifier.padding(16.dp)) {

            /* --- Dòng tiêu đề --- */
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Mã máy: ${mayTinh.MaMay}",
                    color = Color(0xFF1B8DDE),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                val (color, status, icon) = when (mayTinh.TrangThai) {
                    1 -> Triple(Color(0xFF4CAF50), "Hoạt động", Lucide.CircleCheck)
                    0 -> Triple(Color(0xFFF44336), "Bảo trì", Lucide.CircleX)
                    else -> Triple(Color.Gray, "Không rõ", Lucide.CircleAlert)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(status, color = color, fontWeight = FontWeight.SemiBold)
                }
            }

            Divider(
                Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = Color(0xffdddcdc)
            )

            InfoRow(icon = Lucide.Monitor, label = "Tên Máy", value = mayTinh.TenMay)
            Spacer(Modifier.height(6.dp))
            InfoRow(
                icon = Lucide.Building2,
                label = "Phòng hiện tại",
                value = phongMay?.TenPhong ?: "Đang tải…"
            )
        }
    }
}




