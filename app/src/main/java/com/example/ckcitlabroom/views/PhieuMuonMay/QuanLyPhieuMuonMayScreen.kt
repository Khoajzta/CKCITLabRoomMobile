import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun QuanLyPhieuMuonMayScreen(
    navController: NavHostController,
) {

    val dsChucNang =
        listOf(
            ChucNang("Phiếu Đã Trả", Icons.Outlined.Article, Click = {navController.navigate(NavRoute.LISTPHIEUMUONMAYDATRA.route)}),
            ChucNang("Phiếu Chưa Trả", Icons.Outlined.Article, Click = {navController.navigate(NavRoute.LISTPHIEUMUONMAYCHUATRA.route)}),
            ChucNang("Phiếu Chưa Chuyển Máy", Icons.Outlined.Article, Click = {navController.navigate(NavRoute.LISTPHIEUMUONMAYCHUACHUYEN.route)}),
            ChucNang("Tạo Phiếu Mượn May", Icons.Outlined.AddCircle, Click = {navController.navigate(NavRoute.ADDPHIEUMUONMAY.route)}),
        )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
            thickness = 2.dp,
            color = Color(0xFF1B8DDE),
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