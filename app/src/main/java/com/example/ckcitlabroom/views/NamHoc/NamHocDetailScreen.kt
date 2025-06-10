import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NamHocDetailScreen(
    manam: String,
    tuanViewModel: TuanViewModel
) {

    val danhSachTuan = tuanViewModel.danhSachTuanTheoNamMap[manam] ?: emptyList()

    // Gọi API khi màn hình mở
    LaunchedEffect(manam) {
        tuanViewModel.getTuanByMaNam(manam)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Danh sách tuần",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = Color(0xFF1B8DDE)
            )

            Text(
                text = "Số Tuần: ${danhSachTuan.size}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = Color(0xFF1B8DDE)
            )
        }


        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(danhSachTuan) { tuan ->
                CardTuan(tuan)
            }
        }
    }
}

