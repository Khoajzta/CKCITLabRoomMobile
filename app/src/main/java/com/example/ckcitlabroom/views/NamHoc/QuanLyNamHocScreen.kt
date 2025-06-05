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

@Composable
fun QuanLyNamHocScreen(
    navController: NavHostController,
    namHocViewModel: NamHocViewModel
){
    var danhSachNamHoc = namHocViewModel.danhSachAllNamHoc

    LaunchedEffect(Unit) {
        namHocViewModel.getAllNamHoc()
    }

    DisposableEffect(Unit) {
        onDispose {
            namHocViewModel.stopPollingAllNamHoc()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Quản Lý Năm Học ",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = Color(0xFF1B8DDE)
            )
            IconButton(
                onClick = {
                    navController.navigate(NavRoute.ADDNAMHOC.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Thêm năm học",
                    tint = Color(0xFF1B8DDE),
                    modifier = Modifier.size(35.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (danhSachNamHoc == null || danhSachNamHoc.isEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Chưa có năm học nào",
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                }
            } else {
                items(danhSachNamHoc) { namhoc ->
                    CardNamHoc(namhoc, navController, namHocViewModel)
                }
            }
        }

    }
}