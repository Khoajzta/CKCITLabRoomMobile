import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(7.dp)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text("Máy: ${lichSuChuyenMay.MaMay }", fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
            Text("Ngày chuyển: ${formatNgay(lichSuChuyenMay.NgayChuyen)}", fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Từ phòng ${phongMayCu.TenPhong.ifBlank { lichSuChuyenMay.MaPhongCu }}", fontWeight = FontWeight.Bold)
                Text("đến phòng ${phongMayMoi.TenPhong.ifBlank { lichSuChuyenMay.MaPhongMoi }}", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 6.dp))
            }
        }
    }
}
