import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CardTuan(tuan: Tuan) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = {

        }

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = tuan.TenTuan,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF1B8DDE)
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
                thickness = 2.dp,
                color = Color(0xFFDDDDDD),
            )

            InfoRow(Icons.Default.CalendarToday, "Ngày bắt đầu", formatNgay(tuan.NgayBatDau))
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(Icons.Default.CalendarToday, "Ngày kết thúc", formatNgay(tuan.NgayKetThuc))
        }
    }
}
