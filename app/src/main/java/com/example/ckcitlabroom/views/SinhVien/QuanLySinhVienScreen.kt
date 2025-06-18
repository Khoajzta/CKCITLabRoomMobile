import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.HorizontalDivider
import com.composables.icons.lucide.*

@Composable
fun QuanLySinhVien(
    navController: NavHostController,
) {

    val dsChucNang =
        listOf(
            ChucNang("Sinh Viên Theo Lớp", Lucide.Users, Click = {
                navController.navigate(NavRoute.LISTSINHVIENTHEOLOP.route)
            }),
            ChucNang("Sinh Viên Đình Chỉ", Lucide.UserX, Click = {
                navController.navigate(NavRoute.LISTSINHVIENDINHCHI.route)
            }),
            ChucNang("Thêm Sinh Viên", Lucide.UserPlus, Click = {
                navController.navigate(NavRoute.ADDSINHVIEN.route)
            }),
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
