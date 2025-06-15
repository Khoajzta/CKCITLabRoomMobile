import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.Clock
import com.composables.icons.lucide.Cpu
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPin
import com.composables.icons.lucide.Monitor
import com.example.lapstore.viewmodels.MayTinhViewModel

@Composable
fun CardMayTinhMuon(
    chiTietPhieuMuon: ChiTietPhieuMuon,
    phongMayViewModel: PhongMayViewModel,
    mayTinhViewModel: MayTinhViewModel,
    click: () -> Unit
) {
    var mayTinh by remember { mutableStateOf<MayTinh?>(null) }
    var phongMay by remember { mutableStateOf<PhongMay?>(null) }

    LaunchedEffect(chiTietPhieuMuon.MaMay) {
        mayTinhViewModel.getMayTinhByMaMay(chiTietPhieuMuon.MaMay) {
            mayTinh = it
            it?.let { mt ->
                phongMayViewModel.getPhongMayByMaPhong(mt.MaPhong) { pm ->
                    phongMay = pm
                }
            }
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .shadow(6.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp)),
        onClick = { click() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Thông tin máy tính", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF1B8DDE))
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 2.dp)

            InfoRow(Lucide.Cpu, "Mã Máy", chiTietPhieuMuon.MaMay)
            Spacer(Modifier.height(6.dp))

            InfoRow(Lucide.Monitor, "Tên Máy", mayTinh?.TenMay ?: "Đang tải...")
            Spacer(Modifier.height(6.dp))

            InfoRow(Lucide.MapPin, "Phòng hiện tại", phongMay?.TenPhong ?: "Đang tải...")
            Spacer(Modifier.height(6.dp))

            InfoRow(Lucide.Clock, "Tình trạng mượn", chiTietPhieuMuon.TinhTrangMuon ?: "Chưa cập nhật")
            Spacer(Modifier.height(6.dp))

            if (chiTietPhieuMuon.TinhTrangTra != "") {
                InfoRow(Lucide.CircleCheck, "Tình trạng trả", chiTietPhieuMuon.TinhTrangTra.toString())
                Spacer(Modifier.height(6.dp))
            }

        }
    }
}
