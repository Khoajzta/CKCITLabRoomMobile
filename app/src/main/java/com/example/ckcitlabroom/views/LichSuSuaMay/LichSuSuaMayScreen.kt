import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lapstore.viewmodels.ChiTietDonNhapyViewModel
import com.example.lapstore.viewmodels.DonNhapViewModel
import com.example.lapstore.viewmodels.MayTinhViewModel

@Composable
fun LichSuSuaMayScreen(
    navController: NavHostController,
    phongMayViewModel: PhongMayViewModel,
    mayTinhViewModel: MayTinhViewModel,
    donNhapyViewModel: DonNhapViewModel,
    chiTietDonNhapyViewModel: ChiTietDonNhapyViewModel
){
    var danhsachdonnhap = donNhapyViewModel.danhSachDonNhap
    val danhSachMayTinh = mayTinhViewModel.danhSachAllMayTinhtheophong

    val phongmay = phongMayViewModel.phongmay

    var showDialog by remember { mutableStateOf(false) }
    var showDeleteWarning by remember { mutableStateOf(false) }
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }




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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Danh Sách Đơn Nhập",
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

        // Danh sách đơn nhập
        LazyColumn(
            modifier = Modifier.height(600.dp)
        ) {

            if(danhsachdonnhap.isNullOrEmpty()){
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Chưa có đơn nhập nào",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }
                }
            }else{
                items(danhsachdonnhap) { donnhap ->
                    CardDonNhap(donnhap,chiTietDonNhapyViewModel,mayTinhViewModel,navController, click = {navController.navigate(NavRoute.LISTMAYTINHLICHSUSUAMAY.route + "?madonnhap=${donnhap.MaDonNhap}")})
                }
            }
        }
    }
}

