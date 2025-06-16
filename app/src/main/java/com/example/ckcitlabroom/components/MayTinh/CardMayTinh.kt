import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.composables.icons.lucide.*
import com.example.lapstore.viewmodels.MayTinhViewModel
import androidx.compose.material3.HorizontalDivider

@Composable
fun CardMayTinh(
    maytinh: MayTinh,
    navController: NavHostController,
    maytinhViewModel: MayTinhViewModel,
    phongMayViewModel: PhongMayViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    var phongMayCard by remember { mutableStateOf<PhongMay?>(null) }

    LaunchedEffect(maytinh.MaPhong) {
        phongMayCard = phongMayViewModel.fetchPhongMayByMaPhong(maytinh.MaPhong)
    }

    Card(
        modifier = Modifier
            .padding(bottom = 12.dp)
            .fillMaxWidth()
            .border(1.dp, Color.White, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .clickable { expanded = !expanded }
            .animateContentSize(tween(300)),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Mã máy: ${maytinh.MaMay}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1B8DDE)
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

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = Color(0xFFDDDDDD),
            )

            InfoRow(icon = Lucide.QrCode, label = "Tên máy", value = maytinh.TenMay)
            InfoRow(icon = Lucide.MapPin, label = "Vị trí", value = maytinh.ViTri)
            InfoRow(icon = Lucide.Building2, label = "Phòng", value = phongMayCard?.TenPhong ?: "Đang tải...")

            if (expanded) {
                Spacer(Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    InfoRow(icon = Lucide.Cpu, label = "Main", value = maytinh.Main)
                    InfoRow(icon = Lucide.Cpu, label = "CPU", value = maytinh.CPU)
                    InfoRow(icon = Lucide.MemoryStick, label = "RAM", value = maytinh.RAM)
                    InfoRow(icon = Lucide.HardDrive, label = "HDD", value = maytinh.HDD)
                    InfoRow(icon = Lucide.HardDrive, label = "SSD", value = maytinh.SSD)
                    InfoRow(icon = Lucide.Monitor, label = "Màn hình", value = maytinh.ManHinh)
                    InfoRow(icon = Lucide.Keyboard, label = "Bàn phím", value = maytinh.BanPhim)
                    InfoRow(icon = Lucide.Mouse, label = "Chuột", value = maytinh.Chuot)

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            navController.navigate(NavRoute.EDITMAYTINH.route + "?mamay=${maytinh.MaMay}")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B8DDE))
                    ) {
                        Text("Chỉnh sửa", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}



//    val qrText = maytinh.QRCode
//
//    val qrBitmap = remember(qrText) {
//        generateQRCode(qrText, 300)  // kích thước 300x300 px
//    }

