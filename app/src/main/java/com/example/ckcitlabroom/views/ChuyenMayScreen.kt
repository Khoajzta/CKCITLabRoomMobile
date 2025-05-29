import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.navigation.NavHostController
import com.composables.icons.lucide.History
import com.composables.icons.lucide.Lucide
import com.example.lapstore.viewmodels.LichSuChuyenMayViewModel
import com.example.lapstore.viewmodels.MayTinhViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuChuyenMayScreen(
    navController: NavHostController,
    mayTinhViewModel: MayTinhViewModel,
    lichSuChuyenMayViewModel: LichSuChuyenMayViewModel,
    phongMayViewModel: PhongMayViewModel
) {
    val danhSachPhongMay = phongMayViewModel.danhSachAllPhongMay



    LaunchedEffect(Unit) {
        phongMayViewModel.getAllPhongMay()
        mayTinhViewModel.getAllMayTinh()
    }

    DisposableEffect(Unit) {
        onDispose {
            mayTinhViewModel.stopPollingAllMayTinh()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Chuyển máy",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 25.sp
            )

            IconButton(onClick = { /* Có thể mở lịch sử tại đây nếu cần */ }) {
                Icon(
                    modifier = Modifier.size(30.dp),
                    imageVector = Lucide.History,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if(danhSachPhongMay.isNullOrEmpty()){
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Chưa có phòng máy",
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }else{
                items(danhSachPhongMay) { phongmay ->
                   CardPhongMayChuyen(phongmay,navController,phongMayViewModel,mayTinhViewModel)
                }
            }
        }


    }
}
