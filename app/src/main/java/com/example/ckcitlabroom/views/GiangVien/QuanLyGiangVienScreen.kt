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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.HorizontalDivider
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Presentation
import com.composables.icons.lucide.UserPlus
import com.composables.icons.lucide.*

@Composable
fun QuanLyGiangVien(
    navController: NavHostController,
) {

    val dsChucNang =
        listOf(
            // Giảng Viên Đang Dạy
            ChucNang(
                "Giảng Viên Đang Công Tác",
                Lucide.UserCheck ,
                Click = { navController.navigate(NavRoute.LISTGIANGVIENCONGTAC.route) }
            ),

// Giảng Viên Ngừng Công Tác
            ChucNang(
                "Giảng Viên Ngừng Công Tác",
                Lucide.UserX,
                Click = { navController.navigate(NavRoute.LISTGIANGVIENNGUNGCONGTAC.route) }
            ),

// Phân Quyền Admin
            ChucNang(
                "Phân Quyền Admin",
                Lucide.ShieldCheck,
                Click = { navController.navigate(NavRoute.PHANQUYENADMIN.route) }
            ),

// Phân Quyền Giảng Viên
            ChucNang(
                "Xóa Quyền Admin",
                Lucide.UserCog,
                Click = { navController.navigate(NavRoute.PHANQUYENGIANGVIEN.route) }
            ),

// Thêm Giảng Viên
            ChucNang(
                "Thêm Giảng Viên",
                Lucide.UserPlus,
                Click = { navController.navigate(NavRoute.ADDGIANGVIEN.route) }
            ),

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
