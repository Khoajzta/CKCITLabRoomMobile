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
fun QuanLySinhVien(
    navController: NavHostController,
    sinhVienViewModel: SinhVienViewModel
) {
    val danhSachSinhVien = sinhVienViewModel.danhSachAllSinhVien

    LaunchedEffect(Unit) {
        sinhVienViewModel.getAllSinhVien()
    }

    DisposableEffect(Unit) {
        onDispose {
            sinhVienViewModel.stopPollingSinhVien()
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
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Quản Lý Sinh Viên",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                color = Color.White
            )
            IconButton(
                onClick = {
                navController.navigate(NavRoute.ADDSINHVIEN.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Thêm sinh viên",
                    tint = Color.White
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (danhSachSinhVien == null || danhSachSinhVien.isEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Chưa có Sinh viên nào",
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                items(danhSachSinhVien) { sinhvien ->
                    CardSinhVien(sinhvien, navController, sinhVienViewModel)
                }
            }
        }
    }
}