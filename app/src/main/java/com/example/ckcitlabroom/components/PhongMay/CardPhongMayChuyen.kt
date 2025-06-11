import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.CircleX
import com.composables.icons.lucide.Hash
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Warehouse
import com.example.lapstore.viewmodels.MayTinhViewModel

@Composable
fun CardPhongMayChuyen(
    phongmay: PhongMay,
    navController: NavHostController,
    mayTinhViewModel: MayTinhViewModel,
    click:()-> Unit
) {
    val danhsachmaytinh = mayTinhViewModel.danhSachAllMayTinh
    val soLuongMay = danhsachmaytinh?.count { it.MaPhong == phongmay.MaPhong } ?: 0

    LaunchedEffect(Unit) {
        mayTinhViewModel.getAllMayTinh()
    }

    val (color, statusText, statusIcon) = when (phongmay.TrangThai) {
        1 -> Triple(Color(0xFF4CAF50), "Hoạt động", Lucide.CircleCheck)
        0 -> Triple(Color(0xFFF44336), "Bảo trì", Lucide.CircleX)
        else -> Triple(Color.Gray, "Không xác định", Lucide.CircleAlert)
    }

    Card(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .width(300.dp)
            .shadow(7.dp, shape = RoundedCornerShape(12.dp)),
        onClick = {
            click()
        },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Thông tin phòng máy",
                color = Color(0xFF1B8DDE),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
                thickness = 2.dp,
                color = Color(0xFFDDDDDD),
            )

            InfoRow(
                icon = Lucide.Warehouse, label = "Phòng", value = phongmay.TenPhong
            )

            InfoRow(
                icon = Lucide.Hash, label = "Số lượng máy", value = soLuongMay.toString()
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = statusIcon,
                    contentDescription = "Trạng thái",
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Trạng thái: ", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = statusText, color = color, fontWeight = FontWeight.Bold, fontSize = 16.sp
                )
            }
        }
    }
}



