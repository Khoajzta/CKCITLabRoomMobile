import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.lapstore.viewmodels.MayTinhViewModel

@Composable
fun QuanLyMayTinh(
    navController: NavHostController,
    mayTinhViewModel:MayTinhViewModel,
    phongMayViewModel: PhongMayViewModel
) {
    val danhSachMayTinh = mayTinhViewModel.danhSachAllMayTinh

    LaunchedEffect(Unit) {
        mayTinhViewModel.getAllMayTinh()
    }

    DisposableEffect(Unit) {
        onDispose {
            mayTinhViewModel.stopPollingAllMayTinh()
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
                "Quản Lý Máy Tính",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                color = Color.White
            )
            IconButton(
                onClick = {
                    navController.navigate(NavRoute.ADDTMAYTINH.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Thêm cấu hình",
                    tint = Color.White
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (danhSachMayTinh == null || danhSachMayTinh.isEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Chưa có máy tính nào",
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                }
            } else {
                items(danhSachMayTinh) { maytinh ->
                    CardMayTinh(maytinh, navController,mayTinhViewModel,phongMayViewModel)
                }
            }
        }
    }
}
