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
import androidx.compose.runtime.snapshots.SnapshotStateList
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
fun CardMayTinhChuyenMuon2(
    maytinh: MayTinh,
    phongMayViewModel: PhongMayViewModel,
    selectedMayTinhs: SnapshotStateList<MayTinh>,
    onLongPress: () -> Unit
) {

    val isSelected = selectedMayTinhs.any { it.MaMay == maytinh.MaMay }

    var phongMayCard by remember { mutableStateOf<PhongMay?>(null) }

    LaunchedEffect(maytinh.MaPhong) {
        phongMayCard = phongMayViewModel.fetchPhongMayByMaPhong(maytinh.MaPhong)
    }

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
                onClick = { onLongPress() },
                onLongClick = { onLongPress() }
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFE3F2FD) else Color.White
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mã máy: ${maytinh.MaMay}",
                    color = Color(0xFF1B8DDE),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                val (color, statusText, statusIcon) = when (maytinh.TrangThai) {
                    1 -> Triple(Color(0xFF4CAF50), "Hoạt động", Lucide.CircleCheck)
                    0 -> Triple(Color(0xFFF44336), "Bảo trì", Lucide.CircleX)
                    else -> Triple(Color.Gray, "Không xác định", Lucide.CircleAlert)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(statusIcon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(statusText, color = color, fontWeight = FontWeight.SemiBold)
                }
            }


            Divider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = Color(0xFFCCCCCC)
            )

            InfoRow(icon = Lucide.Monitor, label = "Tên Máy", value = maytinh.TenMay)
            Spacer(Modifier.height(6.dp))
            InfoRow(icon = Lucide.Building2, label = "Phòng hiện tại", value = phongMayCard?.TenPhong ?: "Đang tải...")
        }
    }
}






