import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.components.GiangVien.CardGiangVienPhanQuyen

@Composable
fun PhanQuyenAdminGVScreen(
    navController: NavHostController,
    giangVienViewModel: GiangVienViewModel
) {
    val danhSachGiangVien = giangVienViewModel.danhSachAllGiangVien.filter { it.MaLoaiTaiKhoan == 2}

    LaunchedEffect(Unit) {
        giangVienViewModel.getAllGiangVien()
    }

    DisposableEffect(Unit) {
        onDispose {
            giangVienViewModel.stopPollingGiangVien()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Phân Quyền Giảng Viên",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                color = Color(0xFF1B8DDE)
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(),
            thickness = 2.dp,
            color = Color(0xFF1B8DDE),
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (danhSachGiangVien.isEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Chưa có giảng viên nào hoạt động",
                            color = Color.Black,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                items(danhSachGiangVien) { giangvien ->
                    CardGiangVienPhanQuyen(giangvien, giangVienViewModel)
                }
            }
        }
    }
}