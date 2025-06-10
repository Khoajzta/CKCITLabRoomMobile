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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.composables.icons.lucide.BookOpen
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.Clock
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Tag

@Composable
fun CardNamHoc(
    namHoc: NamHoc,
    navController: NavHostController,
    namHocViewModel: NamHocViewModel,
) {

    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(bottom = 12.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate(NavRoute.NAMHOCDETAIL.route + "?manam=${namHoc.MaNam}")
            },
        shape = RoundedCornerShape(12.dp), // Bo góc 12.dp
        elevation = CardDefaults.cardElevation(defaultElevation = 7.dp), // Bóng đổ
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            InfoRow(icon = Lucide.Tag, label = "Mã Năm Học", value = namHoc.MaNam)
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(icon = Lucide.BookOpen, label = "Tên Năm Học", value = namHoc.TenNam)
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(icon = Icons.Default.CalendarToday, label = "Ngày Bắt Đầu", value = formatNgay(namHoc.NgayBatDau))
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(icon = Icons.Default.CalendarToday, label = "Ngày Kết Thúc", value = formatNgay(namHoc.NgayKetThuc))
            val (color, statusText, statusIcon) = when (namHoc.TrangThai) {
                1 -> Triple(Color(0xFF4CAF50), "Đã Kết Thúc", Lucide.CircleCheck)
                0 -> Triple(Color(0xFF1B8DDE), "Đang Thực Hiện", Lucide.Clock)
                else -> Triple(Color.Gray, "Không xác định", Lucide.CircleAlert)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(statusIcon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
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


//            Button(
//                modifier = Modifier.fillMaxWidth(),
//                onClick = { showDialog = true },
//                shape = RoundedCornerShape(12.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
//            ) {
//                Text("Cập Nhật Trạng Thái", color = Color.White, fontWeight = FontWeight.Bold)
//            }

//            if (showDialog) {
//                AlertDialog(
//                    containerColor = Color.White,
//                    onDismissRequest = { showDialog = false },
//                    title = {
//                        Text("Cập nhật trạng thái", color = Color.Black, fontWeight = FontWeight.Bold)
//                    },
//                    text = {
//                        Text("Phòng: ${namHoc.TenNam}", color = Color.Black)
//                    },
//                    confirmButton = {
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            Button(
//                                modifier = Modifier.weight(1f).padding(end = 8.dp),
//                                onClick = {
//                                    namHocViewModel.updateTrangThaiPhongMay(
//                                        PhongMay(phongmay.MaPhong, phongmay.TenPhong, 1)
//                                    )
//                                    showDialog = false
//                                },
//                                shape = RoundedCornerShape(12.dp),
//                                colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50))
//                            ) {
//                                Text("Hoạt Động", color = Color.White)
//                            }
//
//                            Button(
//                                modifier = Modifier.weight(1f),
//                                onClick = {
//                                    phongMayViewModel.updateTrangThaiPhongMay(
//                                        PhongMay(phongmay.MaPhong, phongmay.TenPhong, 0)
//                                    )
//                                    showDialog = false
//                                },
//                                shape = RoundedCornerShape(12.dp),
//                                colors = ButtonDefaults.buttonColors(Color(0xFFE53935))
//                            ) {
//                                Text("Bảo Trì", color = Color.White)
//                            }
//                        }
//                    }
//                )
//            }
        }
    }

}