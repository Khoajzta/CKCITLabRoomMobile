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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun CardGiangVien(giangvien: GiangVien) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .width(300.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                expanded = !expanded
            }
            .animateContentSize(
                animationSpec = tween(durationMillis = 20, easing = FastOutSlowInEasing)
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Thông tin chính - luôn hiển thị
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 4.dp)) {
                Icon(Icons.Default.Code, contentDescription = "Mã GV", tint = Color(0xFF3F51B5), modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text(text = "Mã Giảng Viên: ${giangvien.MaGV}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 2.dp)) {
                Icon(Icons.Default.PersonOutline, contentDescription = "Tên GV", tint = Color(0xFF3F51B5), modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text(text = "Tên Giảng Viên: ${giangvien.TenGiangVien}", fontSize = 16.sp)
            }

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 2.dp)) {
                Icon(Icons.Default.Transgender, contentDescription = "Giới Tính", tint = Color(0xFF3F51B5), modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text(text = "Giới Tính: ${giangvien.GioiTinh}", fontSize = 16.sp)
            }

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 2.dp)) {
                Icon(Icons.Default.DateRange, contentDescription = "Ngày Sinh", tint = Color(0xFF3F51B5), modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text(text = "Ngày Sinh: ${giangvien.NgaySinh}", fontSize = 16.sp)
            }

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 2.dp)) {
                Icon(Icons.Default.Email, contentDescription = "Email", tint = Color(0xFF3F51B5), modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text(text = "Email: ${giangvien.Email}", fontSize = 16.sp)
            }

            // Phần mở rộng ẩn/hiện
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    val infoItems = listOf(
                        Icons.Default.AdminPanelSettings to "Mã Loại Tài khoản: ${giangvien.MaLoaiTaiKhoan}",
                        Icons.Default.Password to "Mật Khẩu: ${giangvien.MatKhau}"
                    )

                    infoItems.forEach { (icon, text) ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 2.dp)) {
                            Icon(icon, contentDescription = null, tint = Color(0xFF3F51B5), modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(6.dp))
                            Text(text = text, fontSize = 16.sp)
                        }
                    }

                    // Trạng thái
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                        Icon(Icons.Default.Info, contentDescription = "Trạng thái", tint = Color(0xFF3F51B5), modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(text = "Trạng thái: ", fontWeight = FontWeight.SemiBold)
                        val (color, statusText) = when (giangvien.TrangThai) {
                            1 -> Color(0xFF4CAF50) to "Đang dạy"
                            0 -> Color(0xFFF44336) to "Không hoạt động"
                            else -> Color(0xFF9E9E9E) to "Không xác định"
                        }

                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(color)
                                .align(Alignment.CenterVertically)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = statusText, color = color)
                    }

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {}
                    ) {
                        Text("Chỉnh Sửa")
                    }
                }
            }
        }
    }
}


