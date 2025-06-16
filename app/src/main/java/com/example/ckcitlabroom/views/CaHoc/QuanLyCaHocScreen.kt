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
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.HorizontalDivider
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
import com.example.ckcitlabroom.components.CardCaHoc
import com.example.ckcitlabroom.viewmodels.CaHocViewModel

@Composable
fun QuanLyCaHoc(
    navController: NavHostController,
    caHocViewModel: CaHocViewModel
) {
    val danhSachCaHoc = caHocViewModel.danhSachAllCaHoc

    LaunchedEffect(Unit) {
        caHocViewModel.getAllCaHoc()
    }

    DisposableEffect(Unit) {
        onDispose {
            caHocViewModel.stopPollingCaHoc()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Danh Sách Ca Học",
                fontWeight = FontWeight.SemiBold,
                fontSize = 22.sp,
                color = Color(0xFF1B8DDE)
            )
            IconButton(
                onClick = {
                    navController.navigate(NavRoute.ADDCAHOC.route)
                }
            ) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Icons.Outlined.AddCircle,
                    contentDescription = "Thêm ca học",
                    tint = Color(0xFF1B8DDE)
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
            thickness = 2.dp,
            color = Color(0xFF1B8DDE),
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (danhSachCaHoc == null || danhSachCaHoc.isEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Chưa có ca học nào",
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                items(danhSachCaHoc) { cahoc ->
                    CardCaHoc(cahoc, navController, caHocViewModel)
                }
            }
        }
    }
}
