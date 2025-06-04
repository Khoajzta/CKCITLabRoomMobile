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

    var danhsachmaytinh = mayTinhViewModel.danhSachAllMayTinh
    val soLuongMay = danhsachmaytinh?.count { it.MaPhong == phongmay.MaPhong } ?: 0

    var showDialog by remember { mutableStateOf(false) }

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
                if (phongmay.MaPhong.contains("KHO", ignoreCase = true)) {
                    navController.navigate(NavRoute.PHONGMAYDONNHAP.route + "?maphong=${phongmay.MaPhong}")
                } else {
                    navController.navigate(NavRoute.PHONGMAYDETAIL.route + "?maphong=${phongmay.MaPhong}")
                }
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                Text(text = "Số lượng máy: ${soLuongMay}", fontSize = 16.sp)
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
                Text(statusText, color = color)
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { showDialog = true },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
            ) {
                Text("Cập Nhật Trạng Thái", color = Color.White)
            }

            if (showDialog) {
                AlertDialog(
                    containerColor = Color.White,
                    onDismissRequest = { showDialog = false },
                    title = { Text(text = "Cập nhật", color = Color.Black) },
                    text = { Text("Trạng thái phòng ${phongmay.TenPhong}", color = Color.Black) },
                    confirmButton = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                modifier = Modifier.width(130.dp),
                                onClick = {
                                    var phongmaynewTrangThai =
                                        PhongMay(phongmay.MaPhong, phongmay.TenPhong, 1)
                                    phongMayViewModel.updateTrangThaiPhongMay(phongmaynewTrangThai)
                                    showDialog = false
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50))
                            ) {
                                Text(
                                    text = "Hoạt Động", color = Color.White
                                )
                            }
                            Button(
                                modifier = Modifier.width(130.dp),
                                onClick = {
                                    var phongmaynewTrangThai = PhongMay(phongmay.MaPhong, phongmay.TenPhong, 0)
                                    phongMayViewModel.updateTrangThaiPhongMay(phongmaynewTrangThai)
                                    showDialog = false
                                    showDialog = false
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(Color(0xffE20C0C))
                            ) {
                                Text(
                                    text = "Bảo Trì", color = Color.White
                                )
                            }
                        }
                    },
                )
            }
        }
    }
}


