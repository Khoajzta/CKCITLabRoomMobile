import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
            modifier = Modifier.fillMaxWidth().padding(bottom = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Lịch sử chuyển phòng" , fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)

            Text("Số lần: ${danhsachlichsu.count()}" , fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
        }


        LazyColumn {
            if(danhsachlichsu.isNullOrEmpty()){
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Chưa có lịch sử chuyển máy", color = Color.White, fontSize = 20.sp)
                    }
                }
            }

            items(danhsachlichsu){lichsu ->
                CardLichSu(lichsu,phongMayViewModel)
            }
        }
    }
}