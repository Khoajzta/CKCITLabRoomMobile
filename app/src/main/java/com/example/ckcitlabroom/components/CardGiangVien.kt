import android.util.Log
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
import androidx.navigation.NavHostController
import com.composables.icons.lucide.Circle
import com.composables.icons.lucide.Lucide
import com.example.lapstore.viewmodels.MayTinhViewModel

@Composable
fun CardGiangVien(
    giangVien: GiangVien,
    navController: NavHostController,
    giangVienViewModel: GiangVienViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    // Chọn màu trạng thái dựa trên giangVien.TrangThai (1: hoạt động, 0: không hoạt động)
    val (color, statusText) = when (giangVien.TrangThai) {
        1 -> Color(0xFF4CAF50) to "Hoạt động"
        0 -> Color(0xFFF44336) to "Không hoạt động"
        else -> Color.Gray to "Không xác định"
    }

    Card(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
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
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Lucide.Circle, contentDescription = "Mã GV", tint = Color.Black, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text("Mã GV: ${giangVien.MaGV}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Lucide.Circle, contentDescription = "Tên GV", tint = Color.Black, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text("Tên: ${giangVien.TenGiangVien}", fontSize = 16.sp)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Lucide.Circle, contentDescription = "Ngày sinh", tint = Color.Black, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text("Ngày sinh: ${giangVien.NgaySinh}", fontSize = 16.sp)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Lucide.Circle, contentDescription = "Giới tính", tint = Color.Black, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text("Giới tính: ${giangVien.GioiTinh}")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Lucide.Circle, contentDescription = "Email", tint = Color.Black, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text("Email: ${giangVien.Email}")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Lucide.Circle, contentDescription = "Trạng thái", tint = color, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text("Trạng thái: ", fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(4.dp))
                Text(statusText, color = color)
            }

            Spacer(Modifier.height(8.dp))

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    // Nếu bạn muốn show thêm thông tin khác, có thể thêm ở đây

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            navController.navigate("edit_giangvien?magv=${giangVien.MaGV}")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xff1B8DDE)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Chỉnh Sửa", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { showConfirmDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xffAC0808)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Xóa", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Xác nhận") },
            text = { Text("Bạn có chắc chắn muốn xóa giảng viên này không?", fontWeight = FontWeight.Bold) },
            confirmButton = {
                TextButton(
                    onClick = {
//                        giangVienViewModel.deleteGiangVien(giangVien.MaGV)
//                        showConfirmDialog = false
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
                    Text("Hủy")
                }
            },
            containerColor = Color.White
        )
    }
}