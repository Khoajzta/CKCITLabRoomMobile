import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.CircleX
import com.composables.icons.lucide.Hash
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Mail
import com.composables.icons.lucide.Repeat
import com.composables.icons.lucide.Users

@Composable
fun CardGiangVien(
    giangVien: GiangVien,
    navController: NavHostController,
    giangVienViewModel: GiangVienViewModel
) {
    var giangvienhientai = giangVienViewModel.giangvienSet

    var expanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    var context = LocalContext.current

    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp) // padding BÊN NGOÀI card
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)) // quan trọng để áp dụng bo góc cho phần nền trắng
            .clickable { expanded = !expanded }
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "GV: ${giangVien.TenGiangVien}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1B8DDE)
                )

                val (color, statusText, statusIcon) = when (giangVien.TrangThai) {
                    1 -> Triple(Color(0xFF4CAF50), "Đang Công Tác", Lucide.CircleCheck)
                    0 -> Triple(Color(0xFFF44336), "Ngừng Công Tác", Lucide.CircleX)
                    else -> Triple(Color.Gray, "Không xác định", Lucide.CircleAlert)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(statusIcon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(statusText, color = color, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(thickness = 1.dp, color = Color(0xFFDDDDDD))
            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(icon = Lucide.Hash, label = "Mã GV", value = giangVien.MaGV)
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(icon = Lucide.Calendar, label = "Ngày sinh", value = formatNgay(giangVien.NgaySinh))
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(icon = Lucide.Users, label = "Giới tính", value = giangVien.GioiTinh)
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(icon = Lucide.Mail, label = "Email", value = giangVien.Email)

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    if(giangVien.MaGV != giangvienhientai!!.MaGV){
                        Button(
                            onClick = { showDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                        ) {
                            Icon(Lucide.Repeat, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Cập Nhật Trạng Thái", color = Color.White)
                        }
                    }
                    Button(
                        onClick = {
                            navController.navigate(NavRoute.EDITGIANGVIEN.route + "?magv=${giangVien.MaGV}")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B8DDE))
                    ) {
                        Spacer(Modifier.width(8.dp))
                        Text("Chỉnh Sửa", color = Color.White)
                    }

                    Button(
                        onClick = {
                            var giangviennew = giangVien.copy(MatKhau = giangVien.MaGV)
                            giangVienViewModel.updateGiangVien(giangviennew)
                            Toast.makeText(context, "Reset mật khẩu thành công", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xff1B8DDE)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Reset Mật Khẩu", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Cập nhật trạng thái", fontWeight = FontWeight.Bold, color = Color.Black) },
            text = { Text("Giảng viên: ${giangVien.TenGiangVien}",color = Color.Black) },
            confirmButton = {
                val newTrangThai = if (giangVien.TrangThai == 0) 1 else 0
                val label = if (newTrangThai == 1) "Công tác lại" else "Ngừng công tác"
                val buttonColor = if (newTrangThai == 1) Color(0xFF4CAF50) else Color(0xFFE53935)

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        giangVienViewModel.updateTrangThaiGiangVien(giangVien.copy(TrangThai = newTrangThai))
                        showDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(buttonColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(label, color = Color.White)
                }
            },
            containerColor = Color.White
        )
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Xác nhận xóa") },
            text = { Text("Bạn có chắc chắn muốn xóa giảng viên này không?", fontWeight = FontWeight.Bold) },
            confirmButton = {
                TextButton(onClick = {
                    giangVienViewModel.deleteGiangVien(giangVien.MaGV)
                    showConfirmDialog = false
                }) {
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
