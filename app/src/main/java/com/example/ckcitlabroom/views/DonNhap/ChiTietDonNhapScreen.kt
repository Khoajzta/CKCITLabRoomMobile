import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lapstore.viewmodels.ChiTietDonNhapyViewModel
import com.example.lapstore.viewmodels.MayTinhViewModel

@Composable
fun ChiTietDonNhapScreen(
    madonnhap: String,
    navController: NavHostController,
    chiTietDonNhapyViewModel: ChiTietDonNhapyViewModel,
    mayTinhViewModel: MayTinhViewModel,
    phongMayViewModel: PhongMayViewModel
) {
    val context = LocalContext.current
    val danhsachchitietdonnhap = chiTietDonNhapyViewModel.danhSachChiTietDonNhaptheoMaDonNhap
    val danhSachMayTinh = mayTinhViewModel.danhSachAllMayTinh

    val danhSachMayTheoDon = remember(danhsachchitietdonnhap, danhSachMayTinh) {
        val maMayTheoDon = danhsachchitietdonnhap.map { it.MaMay }
        danhSachMayTinh.filter { it.MaMay in maMayTheoDon }
    }

    LaunchedEffect(Unit) {
        mayTinhViewModel.getAllMayTinh()
        chiTietDonNhapyViewModel.getChiTietDonNhapTheoMaDonNhap(madonnhap)
    }

    DisposableEffect(Unit) {
        onDispose {
            mayTinhViewModel.stopPollingAllMayTinh()
            chiTietDonNhapyViewModel.stopPollingChiTietTheoMaDonNhap()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Danh sách máy tính theo đơn",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = Color(0xFF1B8DDE)
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(),
            thickness = 2.dp,
            color = Color(0xFF1B8DDE),
        )

        LazyColumn(
            modifier = Modifier.height(530.dp).fillMaxWidth()
        ) {
            if (danhSachMayTheoDon.isEmpty()) {
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
                items(danhSachMayTheoDon) { maytinh ->
                    CardMayTinh(maytinh, navController, mayTinhViewModel, phongMayViewModel)
                }
            }
        }

        Button(
            modifier = Modifier.padding(top = 12.dp).height(45.dp).fillMaxWidth(),
            onClick = {
                createPdfWithQRCodeBase64(context, madonnhap , danhSachMayTheoDon,"QR_Don_Nhap_${madonnhap}.pdf")
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
        ) {
            Text("In QR", color = Color.Black, fontWeight = FontWeight.ExtraBold)
        }
    }
}
