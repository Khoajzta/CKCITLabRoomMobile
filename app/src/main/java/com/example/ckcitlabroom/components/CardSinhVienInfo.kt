import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.R

@Composable
fun CardSinhVienInfo(
    sinhvien: SinhVien,
    navController: NavHostController,
    sinhVienViewModel: SinhVienViewModel
) {

    val context = LocalContext.current
    val sinhVienPreferences = remember { SinhVienPreferences(context) }

    val loginState by sinhVienPreferences.loginStateFlow.collectAsState(initial = LoginSinhVienState())
    Card(
        modifier = Modifier
            .fillMaxWidth().height(640.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
        ) {
            // Avatar
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(135.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = sinhvien.TenSinhVien,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Sinh Viên",
                fontSize = 20.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = sinhvien.MaSinhVien,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(15.dp))

            // Ngày sinh
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
                    .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(12.dp)),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 10.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        formatNgay(sinhvien.NgaySinh),
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }

            }

            Spacer(modifier = Modifier.height(15.dp))

            // Lớp
            Text(
                text = "Lớp:",
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
                    .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(12.dp)),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 10.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = sinhvien.MaLop,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }

            }

            Spacer(modifier = Modifier.height(15.dp))

            // Trạng thái
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
                    .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(12.dp)),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 10.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text =
                        if (sinhvien.TrangThai == 1) {
                            "Đang Học"
                        } else {
                            "Đình Chỉ"
                        },

                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }

            }

            Spacer(modifier = Modifier.height(15.dp))

            // Nút hành động
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    modifier = Modifier.width(170.dp),
                    onClick = { /* TODO */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B8DDE)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Key, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Đổi mật khẩu", color = Color.White)
                }

                Button(
                    modifier = Modifier.width(170.dp),
                    onClick = {
                        var sinhviennew = sinhvien.copy(Token = "")
                        sinhVienViewModel.updateSinhVien(sinhviennew)

                        sinhVienViewModel.setSV(null)
                        sinhVienViewModel.resetLoginResult()
                        sinhVienViewModel.logout()

                        Log.d("Thông báo","Xóa thành công")
                        Log.d("SV",loginState.tenSinhVien.toString())
                        navController.navigate(NavRoute.LOGINSINHVIEN.route) {
                            popUpTo(NavRoute.HOME.route) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B8DDE)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Logout, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Đăng xuất", color = Color.White)
                }

            }
        }
    }
}