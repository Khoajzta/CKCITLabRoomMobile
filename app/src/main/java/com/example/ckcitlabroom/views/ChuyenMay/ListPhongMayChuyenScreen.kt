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
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.viewmodels.MayTinhViewModel

@Composable
fun ListPhongMayChuyenScreen(
    navController: NavHostController,
    mayTinhViewModel: MayTinhViewModel,
    phongMayViewModel: PhongMayViewModel,
) {
    val danhSachPhongMay = phongMayViewModel.danhSachAllPhongMay
        .filter { it.LoaiPhong == 1 || it.LoaiPhong == 2 }
        .sortedByDescending { it.LoaiPhong == 2 }


    LaunchedEffect(Unit) {
        phongMayViewModel.getAllPhongMay()
        mayTinhViewModel.getAllMayTinh()
    }

    DisposableEffect(Unit) {
        onDispose {
            mayTinhViewModel.stopPollingAllMayTinh()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (danhSachPhongMay.isNullOrEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Chưa có phòng máy",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                items(danhSachPhongMay) { phongmay ->
                    CardPhongMayChuyen(
                        phongmay,
                        mayTinhViewModel,
                        onClick = {
                            val route =
                                if (phongmay.LoaiPhong == 2) {
                                    NavRoute.PHONGKHOCHUYEN.route
                                } else {
                                    NavRoute.PHONGMAYCHUYEN.route
                                }
                            navController.navigate("$route?maphong=${phongmay.MaPhong}")
                        }
                    )
                }
            }
        }
    }
}