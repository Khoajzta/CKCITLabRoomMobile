import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.items

@Composable
fun QuanLyGiangVien(
    navController: NavHostController,
) {

    val dsChucNang =
        listOf(
            ChucNang("Giảng Viên Ngừng Công Tác", Icons.Outlined.Article, Click = {
                navController.navigate(NavRoute.LISTGIANGVIENNGUNGCONGTAC.route)
            }),
            ChucNang("Giảng Viên Đang Dạy", Icons.Outlined.Article, Click = {
                navController.navigate(NavRoute.LISTGIANGVIENCONGTAC.route)
            }),
            ChucNang("Phân Quyền Giảng Viên", Icons.Outlined.ManageAccounts, Click = {
                navController.navigate(NavRoute.PHANQUYEN.route)
            }),
            ChucNang("Thêm Giảng Viên", Icons.Outlined.AddCircle, Click = {
                navController.navigate(NavRoute.ADDGIANGVIEN.route)
            }),
        )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Quản Lý Giảng Viên",
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
