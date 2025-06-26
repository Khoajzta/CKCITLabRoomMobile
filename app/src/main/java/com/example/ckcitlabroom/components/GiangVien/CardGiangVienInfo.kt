import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.R

@Composable
fun CardGiangVienInfo(
    giangVien: GiangVien,
    navController: NavHostController,
    giangVienViewModel: GiangVienViewModel
) {
    var showLogoutConfirmDialog by remember { mutableStateOf(false) }

    if (showLogoutConfirmDialog) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showLogoutConfirmDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutConfirmDialog = false

                    // Đăng xuất
                    val giangVienNew = giangVien.copy(Token = "")
                    giangVienViewModel.updateGiangVien(giangVienNew)

                    giangVienViewModel.setGV(null)
                    giangVienViewModel.resetLoginResult()
                    giangVienViewModel.logout()

                    navController.navigate(NavRoute.LOGINSINHVIEN.route) {
                        popUpTo(NavRoute.HOME.route) { inclusive = true }
                    }
                }) {
                    Text("Đăng xuất", fontWeight = FontWeight.Bold, color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutConfirmDialog = false }) {
                    Text("Hủy",color = Color.Black)
                }
            },
            title = { Text("Xác nhận đăng xuất", color = Color.Red) },
            text = { Text("Bạn có chắc chắn muốn đăng xuất khỏi tài khoản này?",color = Color.Black) },
            shape = RoundedCornerShape(16.dp)
        )
    }

    Card(
        modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(20.dp)),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {

            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(135.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = giangVien.TenGiangVien,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (giangVien.MaLoaiTaiKhoan != 1) "Giảng Viên" else "Admin",
                fontSize = 20.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = giangVien.MaGV,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Ngày Sinh:",
                modifier = Modifier.align(Alignment.Start),
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.White)
                    .border(1.dp, Color.Black, RoundedCornerShape(12.dp)),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        formatNgay(giangVien.NgaySinh),
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Trạng Thái",
                modifier = Modifier.align(Alignment.Start),
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.White)
                    .border(1.dp, Color.Black, RoundedCornerShape(12.dp)),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (giangVien.TrangThai == 1) "Đang Giảng Dạy" else "Ngừng Dạy",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    modifier = Modifier.width(170.dp),
                    onClick = {
                        navController.navigate(NavRoute.DOIMATKHAUGV.route + "?magv=${giangVien.MaGV}")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B8DDE)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Key, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Đổi mật khẩu", color = Color.White, fontSize = 15.sp)
                }

                Button(
                    modifier = Modifier.width(170.dp),
                    onClick = {
                        showLogoutConfirmDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B8DDE)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Logout, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Đăng xuất", color = Color.White, fontSize = 15.sp)
                }
            }
        }
    }
}
