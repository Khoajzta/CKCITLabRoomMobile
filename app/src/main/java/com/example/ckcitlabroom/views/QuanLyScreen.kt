import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DesktopWindows
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SupervisorAccount
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


@Composable
fun QuanLyScreen(navController: NavHostController) {


    val dsChucNang = listOf(
        ChucNang("Quản Lý Máy Tính", Icons.Outlined.DesktopWindows, Click = {navController.navigate(NavRoute.QUANLYMAYTINH.route)}),
        ChucNang("Quản Lý Đơn Nhập", iconComputer, Click = { navController.navigate(NavRoute.QUANLYCAUHINH.route) }),
        ChucNang("Quản Lý Phòng Máy", iconComputer, Click = { navController.navigate(NavRoute.QUANLYPHONGMAY.route) }),
        ChucNang("Quản Lý Giảng Viên", Icons.Outlined.SupervisorAccount, Click = {navController.navigate(NavRoute.QUANLYGIANGVIEN.route)}),
        ChucNang("Quản Lý Sinh Viên", Icons.Outlined.Person, {}),
        ChucNang("Quản Lý Lịch", Icons.Outlined.CalendarMonth, {})
    )


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
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