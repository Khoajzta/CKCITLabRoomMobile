import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.CalendarDays
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Monitor
import com.composables.icons.lucide.MoveLeft
import com.composables.icons.lucide.MoveRight
import com.example.lapstore.viewmodels.LichSuChuyenMayViewModel
import com.example.lapstore.viewmodels.MayTinhViewModel
import kotlinx.coroutines.launch

import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun CardLichSu(
    lichSuChuyenMay: LichSuChuyenMay,
    phongMayViewModel: PhongMayViewModel,
) {
    val coroutineScope = rememberCoroutineScope()

    var phongMayCu by remember { mutableStateOf(PhongMay("","",1)) }
    var phongMayMoi by remember { mutableStateOf(PhongMay("","",1)) }

    LaunchedEffect(lichSuChuyenMay) {
        coroutineScope.launch {
            phongMayCu = phongMayViewModel.fetchPhongMayByMaPhong(lichSuChuyenMay.MaPhongCu)
            phongMayMoi = phongMayViewModel.fetchPhongMayByMaPhong(lichSuChuyenMay.MaPhongMoi)
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            InfoRow(
                icon = Lucide.Monitor,
                label = "Mã Máy",
                value = lichSuChuyenMay.MaMay
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(
                icon = Lucide.CalendarDays,
                label = "Ngày chuyển",
                value = formatNgay(lichSuChuyenMay.NgayChuyen)
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(
                icon = Lucide.MoveRight,
                label = "Từ phòng",
                value = phongMayCu.TenPhong.ifBlank { lichSuChuyenMay.MaPhongCu }
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(
                icon = Lucide.MoveLeft,
                label = "Đến phòng",
                value = phongMayMoi.TenPhong.ifBlank { lichSuChuyenMay.MaPhongMoi }
            )
        }
    }
}
