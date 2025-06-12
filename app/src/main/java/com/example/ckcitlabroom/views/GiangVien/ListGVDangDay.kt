import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@Composable
fun ListGiangVienCongTac(
    navController: NavHostController,
    giangVienViewModel: GiangVienViewModel
) {
    val danhSachGiangVien = giangVienViewModel.danhSachAllGiangVien

    LaunchedEffect(Unit) {
        giangVienViewModel.getAllGiangVien()
    }

    DisposableEffect(Unit) {
        onDispose {
            giangVienViewModel.stopPollingGiangVien()
        }
    }

    // 👉 Lọc trước: chỉ lấy giảng viên có TrangThai == 1
    val giangVienHoatDong = danhSachGiangVien?.filter { it.TrangThai == 1 } ?: emptyList()

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
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (giangVienHoatDong.isEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Chưa có giảng viên nào hoạt động",
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                items(giangVienHoatDong) { giangvien ->
                    CardGiangVien(giangvien, navController, giangVienViewModel)
                }
            }
        }
    }
}
