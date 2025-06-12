import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.composables.icons.lucide.Badge
import com.composables.icons.lucide.Calendar
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.CircleX
import com.composables.icons.lucide.Hash
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Mail
import com.composables.icons.lucide.User
import com.composables.icons.lucide.Users
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.sinh

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

    val (color, statusText, statusIcon) = when (sinhVien.TrangThai) {
        1 -> Triple(Color(0xFF4CAF50), "Đang học", Lucide.CircleCheck)
        0 -> Triple(Color(0xFFF44336), "Đình chỉ", Lucide.CircleX)
        else -> Triple(Color.Gray, "Không xác định", Lucide.CircleAlert)
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
                Icon(imageVector = Lucide.Hash, contentDescription = "Mã SV", tint = Color.Black, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text("Mã SV: ${sinhVien.MaSinhVien}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Lucide.User, contentDescription = "Tên SV", tint = Color.Black, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text("Tên: ${sinhVien.TenSinhVien}", fontSize = 16.sp)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Lucide.Calendar, contentDescription = "Ngày sinh", tint = Color.Black, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

                val ngaySinhFormatted = try {
                    LocalDate.parse(sinhVien.NgaySinh, inputFormatter).format(outputFormatter)
                } catch (e: Exception) {
                    sinhVien.NgaySinh
                }

                Text("Ngày sinh: $ngaySinhFormatted", fontSize = 16.sp)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Lucide.Users , contentDescription = "Giới tính", tint = Color.Black, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text("Giới tính: ${sinhVien.GioiTinh}")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Lucide.Mail, contentDescription = "Email", tint = Color.Black, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text("Email: ${sinhVien.Email}")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Lucide.Badge, contentDescription = "Mã lớp", tint = Color.Black, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text("Mã lớp: ${sinhVien.MaLop}")
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

            Spacer(Modifier.height(8.dp))

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { showDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
                    ) {
                        Text("Cập Nhật Trạng Thái", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                    if (showDialog) {
                        AlertDialog(
                            containerColor = Color.White,
                            onDismissRequest = { showDialog = false },
                            title = {
                                Text("Cập nhật trạng thái", color = Color.Black, fontWeight = FontWeight.Bold)
                            },
                            text = {
                                Text("Sinh viên: ${sinhVien.TenSinhVien}", color = Color.Black)
                            },
                            confirmButton = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Button(
                                        modifier = Modifier.weight(1f).padding(end = 8.dp),
                                        onClick = {
                                            sinhVienViewModel.updateTrangThaiSinhVien(
                                                SinhVien(sinhVien.MaSinhVien, sinhVien.TenSinhVien, sinhVien.NgaySinh, sinhVien.GioiTinh,
                                                    sinhVien.Email, sinhVien.MatKhau, sinhVien.MaLop, sinhVien.MaLoaiTaiKhoan, 1),
                                                maLop
                                            )
                                            showDialog = false
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50))
                                    ) {
                                        Text("Đang học", color = Color.White)
                                    }

                                    Button(
                                        modifier = Modifier.weight(1f),
                                        onClick = {
                                            sinhVienViewModel.updateTrangThaiSinhVien(
                                                SinhVien(sinhVien.MaSinhVien, sinhVien.TenSinhVien, sinhVien.NgaySinh, sinhVien.GioiTinh,
                                                    sinhVien.Email, sinhVien.MatKhau, sinhVien.MaLop, sinhVien.MaLoaiTaiKhoan, 0),
                                                maLop
                                            )
                                            showDialog = false
                                            sinhVienViewModel.getSinhVienByMaLop(maLop)
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(Color(0xFFE53935))
                                    ) {
                                        Text("Đình chỉ", color = Color.White)
                                    }
                                }
                            }
                        )
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
            text = { Text("Bạn có chắc chắn muốn xóa sinh viên này không?", fontWeight = FontWeight.Bold) },
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

