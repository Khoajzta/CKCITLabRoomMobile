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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.composables.icons.lucide.Building2
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.CircleX
import com.composables.icons.lucide.Cpu
import com.composables.icons.lucide.HardDrive
import com.composables.icons.lucide.Keyboard
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPin
import com.composables.icons.lucide.MemoryStick
import com.composables.icons.lucide.Monitor
import com.composables.icons.lucide.MousePointer2
import com.example.lapstore.viewmodels.MayTinhViewModel
import android.graphics.Paint
import androidx.compose.ui.graphics.vector.ImageVector



@Composable
fun CardMayTinh(
    maytinh: MayTinh,
    navController: NavHostController,
    maytinhViewModel: MayTinhViewModel,
    phongMayViewModel: PhongMayViewModel
) {


//    val qrText = maytinh.QRCode
//
//    val qrBitmap = remember(qrText) {
//        generateQRCode(qrText, 300)  // kích thước 300x300 px
//    }




    var expanded by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    var phongMayCard by remember { mutableStateOf<PhongMay?>(null) }

    LaunchedEffect(maytinh.MaPhong) {
        phongMayCard = phongMayViewModel.fetchPhongMayByMaPhong(maytinh.MaPhong)
    }

    Card(
        modifier = Modifier
            .padding(bottom = 15.dp)
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { expanded = !expanded }
            .animateContentSize(animationSpec = tween(20, easing = FastOutSlowInEasing)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // QR Code
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                QRCodeImage(base64Str = maytinh.QRCode, modifier = Modifier.size(100.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Basic info
            InfoRow(icon = Lucide.Monitor, label = "Tên Máy", value = maytinh.TenMay)
            InfoRow(icon = Lucide.MapPin, label = "Vị Trí", value = maytinh.ViTri)
            InfoRow(icon = Lucide.Building2, label = "Phòng", value = phongMayCard?.TenPhong ?: "Đang tải...")

            val (color, statusText, statusIcon) = when (maytinh.TrangThai) {
                1 -> Triple(Color(0xFF4CAF50), "Hoạt động", Lucide.CircleCheck)
                0 -> Triple(Color(0xFFF44336), "Không hoạt động", Lucide.CircleX)
                else -> Triple(Color.Gray, "Không xác định", Lucide.CircleAlert)
            }

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                Icon(statusIcon, contentDescription = "Trạng thái", tint = color, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Trạng thái:", fontWeight = FontWeight.Medium)
                Spacer(Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(Modifier.width(4.dp))
                Text(statusText, color = color, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    InfoRow(icon = Lucide.Cpu, label = "Main", value = maytinh.Main)
                    InfoRow(icon = Lucide.Cpu, label = "CPU", value = maytinh.CPU)
                    InfoRow(icon = Lucide.MemoryStick, label = "RAM", value = maytinh.RAM)
                    InfoRow(icon = Lucide.Monitor, label = "VGA", value = maytinh.VGA)
                    InfoRow(icon = Lucide.Monitor, label = "Màn Hình", value = maytinh.ManHinh)
                    InfoRow(icon = Lucide.Keyboard, label = "Bàn Phím", value = maytinh.BanPhim)
                    InfoRow(icon = Lucide.MousePointer2, label = "Chuột", value = maytinh.Chuot)
                    InfoRow(icon = Lucide.HardDrive, label = "HDD", value = maytinh.HDD)
                    InfoRow(icon = Lucide.HardDrive, label = "SSD", value = maytinh.SSD)

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            navController.navigate(NavRoute.EDITMAYTINH.route + "?mamay=${maytinh.MaMay}")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xff1B8DDE)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Chỉnh Sửa", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }


    // Dialog xác nhận xóa
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Xác nhận", color = Color.Black, fontWeight = FontWeight.Bold) },
            text = { Text("Bạn có chắc chắn muốn xóa máy tính này không?", fontWeight = FontWeight.Bold, color = Color.Black) },
            confirmButton = {
                TextButton(
                    onClick = {
                        maytinhViewModel.deleteMayTinh(maytinh.MaMay)
                        showConfirmDialog = false
                    }
                ) {
                    Text("Xóa", color = Color.Red)
                }
            },
            dismissButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                    onClick = { showConfirmDialog = false }
                ) {
                    Text("Hủy", color = Color.White)
                }
            },
            containerColor = Color.White
        )
    }
}


//                    Button(
//                        modifier = Modifier.fillMaxWidth(),
//                        onClick = {
//                            showConfirmDialog = true
//                        },
//                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336)),
//                        shape = RoundedCornerShape(12.dp)
//                    ) {
//                        Text("Xóa", fontWeight = FontWeight.Bold, color = Color.White)
//                    }

