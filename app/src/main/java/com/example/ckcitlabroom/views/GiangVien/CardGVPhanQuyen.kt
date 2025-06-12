package com.example.ckcitlabroom.views.GiangVien
import GiangVien
import GiangVienViewModel
import InfoRow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.BadgeCheck
import com.composables.icons.lucide.Key
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Mail
import com.composables.icons.lucide.Phone
import com.composables.icons.lucide.ShieldCheck
import com.composables.icons.lucide.User

@Composable
fun CardGiangVienPhanQuyen(
    giangVien: GiangVien,
    giangVienViewModel: GiangVienViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            InfoRow(icon = Lucide.User, label = "Mã GV", value = giangVien.MaGV.toString())
            InfoRow(icon = Lucide.BadgeCheck, label = "Họ tên", value = giangVien.TenGiangVien)
            InfoRow(icon = Lucide.Mail, label = "Email", value = giangVien.Email)
            InfoRow(icon = Lucide.Key, label = "Mật Khẩu", value = giangVien.MatKhau)
            InfoRow(
                icon = Lucide.ShieldCheck,
                label = "Vai trò",
                value = if (giangVien.MaLoaiTaiKhoan == 1) "Admin" else "Giảng viên"
            )


            Spacer(modifier = Modifier.height(12.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { showDialog = true },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5))
            ) {
                Text("Phân Quyền", color = Color.White, fontWeight = FontWeight.Bold)
            }
            if (showDialog) {
                AlertDialog(
                    containerColor = Color.White,
                    onDismissRequest = { showDialog = false },
                    title = {
                        Text("Phân quyền", color = Color.Black, fontWeight = FontWeight.Bold)
                    },
                    text = {
                        Text("Giảng viên: ${giangVien.TenGiangVien}", color = Color.Black)
                    },
                    confirmButton = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp),
                                onClick = {
                                    giangVienViewModel.updateLoaiTKGiangVien(
                                        giangVien.copy(MaLoaiTaiKhoan = 1) // 1 = Admin
                                    )
                                    showDialog = false
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50))
                            ) {
                                Text("Admin", color = Color.White)
                            }

                            Button(
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    giangVienViewModel.updateLoaiTKGiangVien(
                                        giangVien.copy(MaLoaiTaiKhoan = 2) // 2 = Giảng viên
                                    )
                                    showDialog = false
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(Color(0xFF2196F3))
                            ) {
                                Text("Giảng viên", color = Color.White)
                            }
                        }
                    }
                )
            }
        }
    }
}
