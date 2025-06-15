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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.Clock
import com.composables.icons.lucide.Lucide

@Composable
fun CardLichHocDiemDanh(
    lichHoc: LichHocRP,
    click:() -> Unit
){
    Card(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth(),
        onClick = {click()},
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Thông tin lịch học",
                    color = Color(0xFF1B8DDE),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                thickness = 2.dp,
                color = Color(0xFFDDDDDD),
            )

            InfoRow(icon = Icons.Filled.MenuBook, label = "Môn học", value = lichHoc.TenMonHoc.toString())
            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(icon = Icons.Filled.MeetingRoom, label = "Phòng", value = lichHoc.TenPhong.toString())
            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(icon = Icons.Filled.Schedule, label = "Ca học", value = lichHoc.TenCa.toString())
            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(icon = Icons.Filled.Today, label = "Thứ", value = lichHoc.Thu)
            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(icon = Icons.Filled.Event, label = "Ngày học", value = formatNgay(lichHoc.NgayDay))

            val (color, statusText, statusIcon) = when (lichHoc.TrangThai) {
                0 -> Triple(Color(0xFF1B8DDE), "Đã Kết Thúc", Lucide.CircleCheck)
                1 -> Triple(Color(0xFF4CAF50), "Đang Diễn Ra", Lucide.Clock)
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