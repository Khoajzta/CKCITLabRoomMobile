import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun CardDonNhap(
    donNhap: DonNhap,
    navController: NavHostController
){
    Card(
        modifier = Modifier.fillMaxSize().padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(7.dp),
        onClick = {
            navController.navigate(NavRoute.CHITIETDONNHAP.route + "?madonnhap=${donNhap.MaDonNhap}")
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Mã đơn: ${donNhap.MaDonNhap}",
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Ngày nhập: ${formatNgay(donNhap.NgayNhap)}",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Số lượng máy: ${donNhap.SoLuong}",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Nhà cung cấp: ${donNhap.NhaCungCap}",
                fontWeight = FontWeight.Bold
            )
        }
    }
}