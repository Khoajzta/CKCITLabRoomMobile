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
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun QuanLyLichHocScreen(
    navController: NavHostController,
    sinhVienViewModel: SinhVienViewModel
){

    val sinhvien = sinhVienViewModel.sinhvienSet

    val dsChucNang = if (sinhvien != null) {
        listOf(
            ChucNang("Danh Sách Lịch Học Theo Tuần", Icons.Outlined.CalendarMonth, Click = { navController.navigate(NavRoute.LISTLICHHOC.route) }),
            ChucNang("Danh Sách Lịch Đã Học", Icons.Outlined.CalendarMonth, Click = { navController.navigate(NavRoute.LISTLICHHOCDADAY.route) })
            // Không có nút thêm
        )
    } else {
        listOf(
            ChucNang("Danh Sách Lịch Dạy Theo Tuần", Icons.Outlined.CalendarMonth, Click = { navController.navigate(NavRoute.LISTLICHHOC.route) }),
            ChucNang("Danh Sách Lịch Đã Dạy", Icons.Outlined.CalendarMonth, Click = { navController.navigate(NavRoute.LISTLICHHOCDADAY.route) }),
            ChucNang("Thêm Mới Lịch Dạy", Icons.Outlined.AddCircle, Click = { navController.navigate(NavRoute.ADDLICHHOC.route) })
        )
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Quản Lý Lịch Dạy",
            color = Color(0xFF1B8DDE),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        )

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