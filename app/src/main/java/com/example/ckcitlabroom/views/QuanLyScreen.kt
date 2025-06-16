import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessAlarm
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.MeetingRoom
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.SupervisorAccount
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.composables.icons.lucide.*


@Composable
fun QuanLyScreen(
    navController: NavHostController,
    giangVienViewModel: GiangVienViewModel,
    sinhVienViewModel: SinhVienViewModel
) {
    val giangVien = giangVienViewModel.giangvienSet
    val sinhvien = sinhVienViewModel.sinhvienSet

    val dsChucNang = when {
        giangVien?.MaLoaiTaiKhoan == 1 -> {
            listOf(
//                ChucNang("Quản Lý Máy Tính", Icons.Outlined.DesktopWindows, Click = { navController.navigate(NavRoute.QUANLYMAYTINH.route) }),
                ChucNang("Quản Lý Đơn Nhập", Lucide.ClipboardList, Click = { navController.navigate(NavRoute.QUANLYDONNHAP.route) }),
                ChucNang("Quản Lý Phòng Máy", Lucide.LayoutGrid, Click = { navController.navigate(NavRoute.QUANLYPHONGMAY.route) }),
                ChucNang("Chuyển Máy", Lucide.MoveRight, Click = { navController.navigate(NavRoute.QUANLYCHUYENMAY.route) }),

                ChucNang("Quản Lý Phiếu Sửa Chữa", Icons.Outlined.Build, Click = { navController.navigate(NavRoute.QUANLYPHIEUSUACHUA.route) }),
                ChucNang("Quản Lý Phiếu Mượn Máy", Lucide.FileCheck, Click = { navController.navigate(NavRoute.QUANLYPHIEUMUONMAY.route) }),

                ChucNang("Quản Lý Giảng Viên", Lucide.Users, Click = { navController.navigate(NavRoute.QUANLYGIANGVIEN.route) }),
                ChucNang("Quản Lý Sinh Viên", Lucide.User, Click = { navController.navigate(NavRoute.QUANLYSINHVIEN.route) }),
                ChucNang("Quản Lý Điểm Danh", Lucide.ListChecks, Click = { navController.navigate(NavRoute.LISTLICHHOCSUDUNGMAY.route) }),

                ChucNang("Quản Lý Lịch Dạy", Lucide.CalendarDays, Click = { navController.navigate(NavRoute.QUANLYLICHHOC.route) }),
                ChucNang("Quản Lý Lớp Học", Lucide.School, Click = { navController.navigate(NavRoute.QUANLYLOPHOC.route) }),
                ChucNang("Quản Lý Năm Học", Lucide.CalendarCheck, Click = { navController.navigate(NavRoute.QUANLYNAMHOC.route) }),
                ChucNang("Quản Lý Ca Học", Lucide.Clock, Click = { navController.navigate(NavRoute.QUANLYCAHOC.route) }),
                ChucNang("Quản Lý Môn Học", Lucide.BookOpenText, Click = { navController.navigate(NavRoute.QUANLYMONHOC.route) }),


            )
        }
        giangVien?.MaLoaiTaiKhoan == 2 -> {
            listOf(
                ChucNang(
                    "Quản Lý Lịch Dạy",
                    Lucide.CalendarClock,
                    Click = { navController.navigate(NavRoute.QUANLYLICHHOC.route) }
                ),
                ChucNang(
                    "Quản Lý Phiếu Sửa Chữa",
                    Icons.Outlined.Build,
                    Click = { navController.navigate(NavRoute.QUANLYPHIEUSUACHUA.route) }
                ),
                ChucNang(
                    "Quản Lý Điểm Danh",
                    Lucide.ListChecks,
                    Click = { navController.navigate(NavRoute.LISTLICHHOCSUDUNGMAY.route) }
                )
            )
        }
        sinhvien != null -> {
            listOf(
                ChucNang(
                    "Danh Sách Lịch Học",
                    Lucide.CalendarDays,
                    Click = { navController.navigate(NavRoute.QUANLYLICHHOC.route) }
                ),
                ChucNang(
                    "Lịch Sử Báo Hỏng",
                    Lucide.History,
                    Click = { navController.navigate(NavRoute.LISTPHIEUBYSINHVIEN.route) }
                ),
                ChucNang(
                    "Điểm Danh",
                    Lucide.CircleCheck,
                    Click = { navController.navigate(NavRoute.ADDDIEMDANH.route) }
                )
            )
        }
        else -> emptyList()
    }



    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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