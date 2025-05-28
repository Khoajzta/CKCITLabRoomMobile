import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun QuanLyGiangVien(navController: NavHostController) {
    val danhSachGiangVien = listOf(
        GiangVien(
            MaGV = "GV01",
            TenGiangVien = "Nguyê Văn A",
            NgaySinh = "1999-03-15",
            GioiTinh = "Nam",
            MatKhau = "12345678",
            MaLoaiTaiKhoan = 1,
            TrangThai = 1
        ),
        GiangVien(
            MaGV = "GV01",
            TenGiangVien = "Trần Thị B",
            NgaySinh = "1996-06-27",
            GioiTinh = "Nữ",
            MatKhau = "12345678",
            MaLoaiTaiKhoan = 1,
            TrangThai = 0
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Quản Lý Giảng Viên",
                        fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                color = Color.White
            )
            IconButton(
                onClick = {
                    navController.navigate(NavRoute.ADDGIANGVIEN.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Thêm giảng viên",
                    tint = Color.White
                )
            }
        }

        // Danh sách cấu hình
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(danhSachGiangVien) { giangvien ->
                CardGiangVien(giangvien)
            }
        }
    }
}
