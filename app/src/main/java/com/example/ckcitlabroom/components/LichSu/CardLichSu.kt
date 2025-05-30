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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lapstore.viewmodels.LichSuChuyenMayViewModel
import com.example.lapstore.viewmodels.MayTinhViewModel

import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun CardLichSu(
    lichSuChuyenMay: LichSuChuyenMay,
    lichSuChuyenMayViewModel: LichSuChuyenMayViewModel,
    phongMayViewModel: PhongMayViewModel,
    mayTinhViewModel: MayTinhViewModel
){
    var maytinh = mayTinhViewModel.maytinh
    var phongmaycu = phongMayViewModel.phongmay
    var phongmaymoi = phongMayViewModel.phongmaymoi

    LaunchedEffect(Unit) {
        mayTinhViewModel.getMayTinhByMaMay(lichSuChuyenMay.MaMay)
        phongMayViewModel.getPhongMayByMaPhong(lichSuChuyenMay.MaPhongCu)
        phongMayViewModel.getPhongMayMoi(lichSuChuyenMay.MaPhongMoi)

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
            Text("Máy: ${lichSuChuyenMay.MaMay}", fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
            Text("Ngày chuyển: ${formatNgay(lichSuChuyenMay.NgayChuyen)}", fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Từ phòng ${phongmaycu.TenPhong}", fontWeight = FontWeight.Bold)
                Text(" đến phòng ${phongmaymoi.TenPhong}", fontWeight = FontWeight.Bold)
            }
        }

    }
}