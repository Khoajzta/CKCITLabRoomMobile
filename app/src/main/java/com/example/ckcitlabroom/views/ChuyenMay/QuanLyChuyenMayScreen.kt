import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
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
import com.example.lapstore.viewmodels.DonNhapViewModel
import com.example.lapstore.viewmodels.MayTinhViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuanLyChuyenMayScreen(
    navController: NavHostController,
) {
    val dsChucNang =
        listOf(
            ChucNang("Danh Sách Phòng Máy", iconComputer, Click = { navController.navigate(NavRoute.LISTPHONGMAYCHUYEN.route) }),
            ChucNang("Lịch Sử Chuyển Máy", Icons.Filled.History, Click = { navController.navigate(NavRoute.LICHSUCHUYENMAY.route) }),
        )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(bottom = 16.dp),
            text = "Quản Lý Chuyển Máy",
            color = Color(0xFF1B8DDE),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(dsChucNang) { chucNang ->
                CardChucNang(chucNang)
            }
        }
    }
}
