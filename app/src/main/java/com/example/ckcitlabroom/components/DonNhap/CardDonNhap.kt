import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.composables.icons.lucide.ArrowRightLeft
import com.composables.icons.lucide.CalendarDays
import com.composables.icons.lucide.ClipboardList
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.PackagePlus
import com.composables.icons.lucide.Truck
import com.composables.icons.lucide.Warehouse
import com.example.lapstore.viewmodels.ChiTietDonNhapyViewModel
import com.example.lapstore.viewmodels.DonNhapViewModel
import com.example.lapstore.viewmodels.MayTinhViewModel

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF1B8DDE),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$label: ",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp
        )
        Text(
            text = value,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Color.DarkGray
        )
    }
}


@Composable
fun CardDonNhap(
    donNhap: DonNhap,
    chiTietDonNhapyViewModel: ChiTietDonNhapyViewModel,
    mayTinhViewModel: MayTinhViewModel,
    navController: NavHostController,
    click:()->Unit
) {
    // Lấy chi tiết đơn nhập riêng theo Mã đơn
    val chiTietState = produceState<List<ChiTietDonNhap>>(initialValue = emptyList(), donNhap.MaDonNhap) {
        value = chiTietDonNhapyViewModel.getChiTietDonNhapListOnce(donNhap.MaDonNhap)
    }

    val mayTinhState = produceState<List<MayTinh>>(initialValue = emptyList()) {
        value = mayTinhViewModel.getAllMayTinhOnce()
    }

    val danhSachChiTiet = chiTietState.value
    val danhSachMayTinh = mayTinhState.value

    val danhSachMayTheoDon = remember(danhSachChiTiet, danhSachMayTinh) {
        val maMayTheoDon = danhSachChiTiet.map { it.MaMay }
        danhSachMayTinh.filter { it.MaMay in maMayTheoDon }
    }

    val soLuongMayTrongKho = danhSachMayTheoDon.count { it.MaPhong == "KHOLUUTRU" }

    Card(
        modifier = Modifier
            .padding(bottom = 12.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        onClick = {
            click()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Thông tin đơn nhập",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF1B8DDE)
            )

            Divider(color = Color(0xFFDDDDDD), thickness = 1.dp)

            InfoRow(icon = Lucide.ClipboardList, label = "Mã đơn", value = donNhap.MaDonNhap)
            InfoRow(icon = Lucide.CalendarDays, label = "Ngày nhập", value = formatNgay(donNhap.NgayNhap))
            InfoRow(icon = Lucide.Truck, label = "Nhà cung cấp", value = donNhap.NhaCungCap)
            InfoRow(icon = Lucide.PackagePlus, label = "Đã nhập", value = "${donNhap.SoLuong} máy")
            InfoRow(icon = Lucide.Warehouse, label = "Còn trong kho", value = "$soLuongMayTrongKho máy")
            InfoRow(
                icon = Lucide.ArrowRightLeft,
                label = "Đã chuyển đi",
                value = "${donNhap.SoLuong - soLuongMayTrongKho} máy"
            )
        }
    }
}


