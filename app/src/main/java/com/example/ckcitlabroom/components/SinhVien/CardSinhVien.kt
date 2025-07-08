import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.composables.icons.lucide.CalendarClock
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.CircleX
import com.composables.icons.lucide.Hash
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Mail
import com.composables.icons.lucide.School
import com.composables.icons.lucide.User
import com.composables.icons.lucide.Users

@Composable
fun CardSinhVien(
    sinhVien: SinhVien,
    navController: NavHostController,
    sinhVienViewModel: SinhVienViewModel,
    maLop: String
) {
    var expanded by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    var context = LocalContext.current


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
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${sinhVien.TenSinhVien}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1B8DDE)
                )

                val (color, statusText, statusIcon) = when (sinhVien.TrangThai) {
                    1 -> Triple(Color(0xFF4CAF50), "Đang học", Lucide.CircleCheck)
                    0 -> Triple(Color(0xFFF44336), "Đình chỉ", Lucide.CircleX)
                    else -> Triple(Color.Gray, "Không xác định", Lucide.CircleAlert)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        statusIcon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(statusText, color = color, fontWeight = FontWeight.SemiBold)
                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                thickness = 2.dp,
                color = Color(0xFFDDDDDD),
            )

            InfoRow(icon = Lucide.Hash, label = "Mã SV", value = sinhVien.MaSinhVien)
            Spacer(Modifier.height(8.dp))

            InfoRow(icon = Lucide.User, label = "Tên", value = sinhVien.TenSinhVien)
            Spacer(Modifier.height(8.dp))

            InfoRow(
                icon = Lucide.CalendarClock,
                label = "Ngày sinh",
                value = formatNgay(sinhVien.NgaySinh)
            )
            Spacer(Modifier.height(8.dp))

            InfoRow(icon = Lucide.Users, label = "Giới tính", value = sinhVien.GioiTinh)
            Spacer(Modifier.height(8.dp))

            InfoRow(icon = Lucide.Mail, label = "Email", value = sinhVien.Email)
            Spacer(Modifier.height(8.dp))

            InfoRow(icon = Lucide.School, label = "Mã lớp", value = sinhVien.MaLop)
            Spacer(Modifier.height(8.dp))


            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    if (sinhVien.TrangThai == 1) {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                var sinhviennew = sinhVien.copy(TrangThai = 0)
                                sinhVienViewModel.updateSinhVien(sinhviennew)
                                Toast.makeText(context, "Đình chỉ thành công", Toast.LENGTH_SHORT)
                                    .show()
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFFE53935))
                        ) {
                            Text("Đình Chỉ", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                var sinhviennew = sinhVien.copy(TrangThai = 1)
                                sinhVienViewModel.updateSinhVien(sinhviennew)
                                Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT)
                                    .show()
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50))
                        ) {
                            Text("Đang Học", color = Color.White)
                        }
                    }

                    Button(
                        onClick = {
                            navController.navigate(NavRoute.EDITSINHVIEN.route + "?masv=${sinhVien.MaSinhVien}")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xff1B8DDE)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Chỉnh Sửa", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Button(
                        onClick = {
                            var sinhviewnew =
                                sinhVien.copy(MatKhau = hashPasswordMD5(sinhVien.MaSinhVien))
                            sinhVienViewModel.updateSinhVien(sinhviewnew)
                            Toast.makeText(context, "Reset mật khẩu thành công", Toast.LENGTH_SHORT)
                                .show()
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

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Xác nhận") },
            text = {
                Text(
                    "Bạn có chắc chắn muốn xóa sinh viên này không?",
                    fontWeight = FontWeight.Bold
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        sinhVienViewModel.deleteSinhVien(sinhVien.MaSinhVien)
                        showConfirmDialog = false
                        sinhVienViewModel.getSinhVienByMaLop(maLop)
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

