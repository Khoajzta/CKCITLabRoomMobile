import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lapstore.viewmodels.LichSuChuyenMayViewModel
import com.example.lapstore.viewmodels.MayTinhViewModel

@Composable
fun ChiTietLichSuChuyenMay(
    mamay:String,
    lichSuChuyenMayViewModel: LichSuChuyenMayViewModel,
    phongMayViewModel: PhongMayViewModel,
){

    var danhsachlichsu = lichSuChuyenMayViewModel.danhSachLichSuTheoMay

    LaunchedEffect(Unit) {
        lichSuChuyenMayViewModel.getLichSuTheoMay(mamay)
    }

    DisposableEffect(Unit) {
        onDispose {
            lichSuChuyenMayViewModel.stopPollingLichSuTheoMay()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Lịch sử chuyển phòng" , fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1B8DDE))

            Text("Số lần: ${danhsachlichsu.count()}" , fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1B8DDE))
        }

        HorizontalDivider(
            modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(),
            thickness = 2.dp,
            color = Color(0xFF1B8DDE),
        )

        LazyColumn {
            if(danhsachlichsu.isNullOrEmpty()){
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Chưa có lịch sử chuyển máy", color = Color.Black)
                    }
                }
            }

            items(danhsachlichsu){lichsu ->
                CardLichSuChuyenMay(lichsu,phongMayViewModel)
            }
        }
    }
}