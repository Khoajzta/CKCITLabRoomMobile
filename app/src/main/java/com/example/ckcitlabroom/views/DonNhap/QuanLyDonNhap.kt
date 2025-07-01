import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.viewmodels.ChiTietDonNhapyViewModel
import com.example.ckcitlabroom.viewmodels.DonNhapViewModel
import com.example.ckcitlabroom.viewmodels.MayTinhViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuanLyDonNhap(
    navController: NavHostController,
    donNhapyViewModel: DonNhapViewModel,
    chiTietDonNhapyViewModel: ChiTietDonNhapyViewModel,
    mayTinhViewModel: MayTinhViewModel
) {
    BackHandler {
        navController.navigate(NavRoute.QUANLY.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
        }
    }
    val danhSachDonNhap = donNhapyViewModel.danhSachDonNhap

    /* -- Lấy dữ liệu + dừng polling -- */
    LaunchedEffect(Unit) { donNhapyViewModel.getAllDonNhap() }
    DisposableEffect(Unit) {
        onDispose { donNhapyViewModel.stopPollingAllDonNhap() }
    }

    /* ------------------- UI ------------------- */
    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButtonCustom(
                onClick = { navController.navigate(NavRoute.ADDDONNHAP.route) }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 0.dp,
                    bottom = 0.dp,
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                )   // tránh FAB che nội dung
        ) {
            /* --------- Danh sách --------- */
            LazyColumn(Modifier.fillMaxSize()) {
                if (danhSachDonNhap.isNullOrEmpty()) {
                    item {
                        Row(
                            Modifier.fillMaxWidth(),
                            Arrangement.Center,
                            Alignment.CenterVertically
                        ) {
                            Text(
                                "Chưa có đơn nhập nào",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 17.sp
                            )
                        }
                    }
                } else {
                    items(danhSachDonNhap) { donNhap ->
                        CardDonNhap(
                            donNhap,
                            chiTietDonNhapyViewModel,
                            mayTinhViewModel,
                            navController,
                            click = {
                                navController.navigate(
                                    NavRoute.CHITIETDONNHAP.route +
                                            "?madonnhap=${donNhap.MaDonNhap}"
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

