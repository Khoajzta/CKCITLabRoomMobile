import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.composables.icons.lucide.*

@Composable
fun QuanLyLichHocScreen(
    navController: NavHostController,
    giangVienViewModel: GiangVienViewModel
){
    val giangvien = giangVienViewModel.giangvienSet

    val dsChucNang = buildList {
        if (giangvien != null) {
            add(ChucNang("Danh Sách Lịch Dạy Theo Tuần", Lucide.CalendarRange) {
                navController.navigate(NavRoute.LISTLICHHOC.route)
            })
            add(ChucNang("Danh Sách Lịch Đã Dạy", Lucide.ClipboardCheck) {
                navController.navigate(NavRoute.LISTLICHHOCDADAY.route)
            })

            if (giangvien.MaLoaiTaiKhoan == 1) {
                add(ChucNang("Thêm Mới Lịch Dạy", Lucide.CalendarPlus) {
                    navController.navigate(NavRoute.ADDLICHHOC.route)
                })
            }
        } else {
            add(ChucNang("Danh Sách Lịch Học Theo Tuần", Lucide.CalendarRange) {
                navController.navigate(NavRoute.LISTLICHHOC.route)
            })
            add(ChucNang("Danh Sách Lịch Đã Học", Lucide.ClipboardCheck) {
                navController.navigate(NavRoute.LISTLICHHOCDADAY.route)
            })
        }

    }

    var text = if(giangvien != null) "Quản Lý Lịch Dạy" else "Quản Lý Lịch Học"

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