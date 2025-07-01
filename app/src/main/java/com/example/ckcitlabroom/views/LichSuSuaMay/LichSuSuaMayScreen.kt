import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.viewmodels.ChiTietDonNhapyViewModel
import com.example.ckcitlabroom.viewmodels.DonNhapViewModel
import com.example.ckcitlabroom.viewmodels.MayTinhViewModel

@Composable
fun LichSuSuaMayScreen(
    navController: NavHostController,
    phongMayViewModel: PhongMayViewModel,
    mayTinhViewModel: MayTinhViewModel,
    donNhapyViewModel: DonNhapViewModel,
    chiTietDonNhapyViewModel: ChiTietDonNhapyViewModel
) {
    var danhsachdonnhap = donNhapyViewModel.danhSachDonNhap
    val danhSachMayTinh = mayTinhViewModel.danhSachAllMayTinhtheophong

    val phongmay = phongMayViewModel.phongmay


    DisposableEffect(Unit) {
        onDispose {
            mayTinhViewModel.stopPollingMayTinhTheoPhong()
        }
    }

    val tenPhongState = remember { mutableStateOf(phongmay.TenPhong) }

    LaunchedEffect(phongmay) {
        tenPhongState.value = phongmay.TenPhong
    }

    LaunchedEffect(Unit) {
        donNhapyViewModel.getAllDonNhap()
    }

    DisposableEffect(Unit) {
        onDispose {
            donNhapyViewModel.stopPollingAllDonNhap()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // Danh sách đơn nhập
        LazyColumn(
            modifier = Modifier.height(600.dp)
        ) {

            if (danhsachdonnhap.isNullOrEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Chưa có đơn nhập nào",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 17.sp
                        )
                    }
                }
            } else {
                items(danhsachdonnhap) { donnhap ->
                    CardDonNhap(
                        donnhap,
                        chiTietDonNhapyViewModel,
                        mayTinhViewModel,
                        navController,
                        click = { navController.navigate(NavRoute.LISTMAYTINHLICHSUSUAMAY.route + "?madonnhap=${donnhap.MaDonNhap}") })
                }
            }
        }
    }
}

