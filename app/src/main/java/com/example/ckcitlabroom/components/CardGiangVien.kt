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
import com.composables.icons.lucide.Badge
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.Circle
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.CircleX
import com.composables.icons.lucide.Hash
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Mail
import com.composables.icons.lucide.PersonStanding
import com.composables.icons.lucide.User
import com.composables.icons.lucide.Users
import com.example.ckcitlabroom.models.LopHoc
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun CardGiangVien(
    giangVien: GiangVien,
    navController: NavHostController,
    giangVienViewModel: GiangVienViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    val (color, statusText, statusIcon) = when (giangVien.TrangThai) {
        1 -> Triple(Color(0xFF4CAF50), "Đang Công Tác", Lucide.CircleCheck)
        0 -> Triple(Color(0xFFF44336), "Ngừng Công Tác", Lucide.CircleX)
        else -> Triple(Color.Gray, "Không xác định", Lucide.CircleAlert)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable {
                expanded = !expanded
            }
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(7.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Thông tin giảng viên",
                color = Color(0xFF1B8DDE),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
                thickness = 2.dp,
                color = Color(0xFFDDDDDD),
            )

            InfoRow(icon = Lucide.Hash, label = "Mã GV", value = giangVien.MaGV)
            Spacer(Modifier.height(8.dp))
            InfoRow(icon = Lucide.User, label = "Tên", value = giangVien.TenGiangVien)
            Spacer(Modifier.height(8.dp))
            InfoRow(icon = Lucide.Calendar, label = "Ngày sinh", value = formatNgay(giangVien.NgaySinh))
            Spacer(Modifier.height(8.dp))
            InfoRow(icon = Lucide.Users, label = "Giới tính", value = giangVien.GioiTinh)
            Spacer(Modifier.height(8.dp))
            InfoRow(icon = Lucide.Mail, label = "Email", value = giangVien.Email)
            Spacer(Modifier.height(8.dp))

            // Trạng thái
            Row(verticalAlignment = Alignment.CenterVertically) {
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

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
                ) {
                    Text("Cập Nhật Trạng Thái", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        navController.navigate(NavRoute.EDITGIANGVIEN.route + "?magv=${giangVien.MaGV}")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xff1B8DDE))
                ) {
                    Text("Chỉnh Sửa", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }

    // Dialog trạng thái
    if (showDialog) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showDialog = false },
            title = {
                Text("Cập nhật trạng thái", color = Color.Black, fontWeight = FontWeight.Bold)
            },
            text = {
                Text("Giảng viên: ${giangVien.TenGiangVien}", color = Color.Black)
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    if(giangVien.TrangThai == 0){
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                giangVienViewModel.updateTrangThaiGiangVien(giangVien.copy(TrangThai = 1))
                                showDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Công tác lại", color = Color.White)
                        }
                    }else{
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                giangVienViewModel.updateTrangThaiGiangVien(giangVien.copy(TrangThai = 0))
                                showDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(Color(0xFFE53935)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Ngừng công tác", color = Color.White)
                        }
                    }
                }
            }
        )
    }

    // Dialog xác nhận xóa
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Xác nhận") },
            text = { Text("Bạn có chắc chắn muốn xóa giảng viên này không?", fontWeight = FontWeight.Bold) },
            confirmButton = {
                TextButton(
                    onClick = {
                        giangVienViewModel.deleteGiangVien(giangVien.MaGV)
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
                    Text("Hủy")
                }
            },
            containerColor = Color.White
        )
    }
}
