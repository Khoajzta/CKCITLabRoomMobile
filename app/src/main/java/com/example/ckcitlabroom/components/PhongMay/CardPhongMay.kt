import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.CircleX
import com.composables.icons.lucide.Cpu
import com.composables.icons.lucide.Hash
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Warehouse
import com.example.lapstore.viewmodels.MayTinhViewModel

@Composable
fun CardPhongMay(
    phongmay: PhongMay,
    navController: NavHostController,
    phongMayViewModel: PhongMayViewModel,
    mayTinhViewModel: MayTinhViewModel
) {
    val danhSachMayTinh = mayTinhViewModel.danhSachAllMayTinh
    val soLuongMay = danhSachMayTinh?.count { it.MaPhong == phongmay.MaPhong } ?: 0
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        mayTinhViewModel.getAllMayTinh()
    }

    Card(
        modifier = Modifier
            .padding(bottom = 12.dp)
            .fillMaxWidth()
            .clickable {
                val route = if (phongmay.MaPhong.contains("KHOLUUTRU", ignoreCase = true)) {
                    NavRoute.PHONGMAYDONNHAP.route
                } else {
                    NavRoute.PHONGMAYDETAIL.route
                }
                navController.navigate("$route?maphong=${phongmay.MaPhong}")
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Phòng Máy: ${phongmay.TenPhong}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF1B8DDE)
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.5.dp,
                color = Color(0xFFDDDDDD),
            )

//            InfoRow(icon = Lucide.Warehouse, label = "Tên phòng", value = phongmay.TenPhong)
//            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(icon = Lucide.Cpu, label = "Số lượng máy", value = "$soLuongMay máy")

            val (color, statusText, statusIcon) = when (phongmay.TrangThai) {
                1 -> Triple(Color(0xFF4CAF50), "Hoạt động", Lucide.CircleCheck)
                0 -> Triple(Color(0xFFF44336), "Bảo trì", Lucide.CircleX)
                else -> Triple(Color.Gray, "Không xác định", Lucide.CircleAlert)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            ) {
                Icon(statusIcon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Trạng thái: ", fontWeight = FontWeight.ExtraBold)
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(Modifier.width(4.dp))
                Text(statusText, color = color, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xff1B8DDE))
            ) {
                Text("Cập Nhật Trạng Thái", color = Color.White, fontWeight = FontWeight.Bold)
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    containerColor = Color.White,
                    title = {
                        Text("Cập nhật trạng thái", fontWeight = FontWeight.Bold, color = Color.Black)
                    },
                    text = {
                        Text("Phòng: ${phongmay.TenPhong}", color = Color.Black)
                    },
                    confirmButton = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    phongMayViewModel.updateTrangThaiPhongMay(
                                        PhongMay(phongmay.MaPhong, phongmay.TenPhong, 1)
                                    )
                                    showDialog = false
                                },
                                modifier = Modifier.weight(1f).padding(end = 8.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50))
                            ) {
                                Text("Hoạt Động", color = Color.White)
                            }

                            Button(
                                onClick = {
                                    phongMayViewModel.updateTrangThaiPhongMay(
                                        PhongMay(phongmay.MaPhong, phongmay.TenPhong, 0)
                                    )
                                    showDialog = false
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(Color(0xFFE53935))
                            ) {
                                Text("Bảo Trì", color = Color.White)
                            }
                        }
                    }
                )
            }
        }
    }
}



