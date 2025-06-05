import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
    phongMayViewModel: PhongMayViewModel,
    mayTinhViewModel: MayTinhViewModel
) {

    var danhsachmaytinh = mayTinhViewModel.danhSachAllMayTinh
    val soLuongMay = danhsachmaytinh?.count { it.MaPhong == phongmay.MaPhong } ?: 0

    LaunchedEffect(Unit) {
        mayTinhViewModel.getAllMayTinh()
    }

    Card(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .width(300.dp)
            .clickable(
            ) {
                val route = if (phongmay.MaPhong.contains("KHO", ignoreCase = true)) {
                    NavRoute.PHONGKHOCHUYEN.route
                } else {
                    NavRoute.PHONGMAYCHUYEN.route
                }
                navController.navigate("$route?maphong=${phongmay.MaPhong}")
            }.shadow(7.dp, shape = RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 2.dp)
            ) {
                Icon(
                    imageVector = Lucide.Warehouse, // hoặc Icons.Default.RoomPreferences
                    contentDescription = "Tên Phòng",
                    tint = Color(0xFF3F51B5),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(text = "Phòng: ${phongmay.TenPhong}", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 2.dp)
            ) {
                Icon(
                    imageVector = Lucide.Hash, // hoặc Icons.Default.RoomPreferences
                    contentDescription = "Tên Phòng",
                    tint = Color(0xFF3F51B5),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(text = "Số lượng máy: ${soLuongMay}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            val (color, statusText, statusIcon) = when (phongmay.TrangThai) {
                1 -> Triple(Color(0xFF4CAF50), "Hoạt động", Lucide.CircleCheck)
                0 -> Triple(Color(0xFFF44336), "Bảo trì", Lucide.CircleX)
                else -> Triple(Color.Gray, "Không xác định", Lucide.CircleAlert)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = statusIcon,
                    contentDescription = "Trạng thái",
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text("Trạng thái: ")
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


