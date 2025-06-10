import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
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
fun QuanLyPhongMay(
    navController: NavHostController,
    phongMayViewModel: PhongMayViewModel,
    mayTinhViewModel: MayTinhViewModel
) {

    val danhSachPhongMay = phongMayViewModel.danhSachAllPhongMay

    LaunchedEffect(Unit) {
        phongMayViewModel.getAllPhongMay()
    }

    DisposableEffect(Unit) {
        onDispose {
            phongMayViewModel.stopPollingPhongMay()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Quản Lý Phòng Máy",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = Color(0xFF1B8DDE)
            )
            IconButton(
                onClick = {
                    navController.navigate(NavRoute.ADDPHONGMAY.route)
                }
            ) {
                Icon(
                    modifier = Modifier.size(35.dp),
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Thêm cấu hình",
                    tint = Color(0xFF1B8DDE),
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (danhSachPhongMay == null || danhSachPhongMay.isEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Chưa có phòng nào",
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                }
            } else {
                items(danhSachPhongMay) { phongmay ->
                    CardPhongMay(phongmay,navController,phongMayViewModel,mayTinhViewModel)
                }
            }
        }
    }
}
