import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.AccessAlarm

import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.LaptopChromebook
import androidx.compose.material.icons.outlined.MeetingRoom
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.SupervisorAccount
import androidx.compose.material.icons.outlined.SwapHoriz
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

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
                ChucNang("Quản Lý Đơn Nhập", Icons.Outlined.ReceiptLong, Click = { navController.navigate(NavRoute.QUANLYDONNHAP.route) }),
                ChucNang("Quản Lý Phòng Máy", Icons.Outlined.MeetingRoom, Click = { navController.navigate(NavRoute.QUANLYPHONGMAY.route) }),
                ChucNang("Chuyển Máy", Icons.Outlined.SwapHoriz, Click = { navController.navigate(NavRoute.QUANLYCHUYENMAY.route) }),
                ChucNang("Quản Lý Phiếu Sửa Chữa", Icons.Outlined.Build, Click = { navController.navigate(NavRoute.QUANLYPHIEUSUACHUA.route) }),
                ChucNang("Quản Lý Phiếu Mượn May", Icons.Outlined.Article, Click = { navController.navigate(NavRoute.QUANLYPHIEUMUONMAY.route) }),

                ChucNang("Quản Lý Giảng Viên", Icons.Outlined.SupervisorAccount, Click = { navController.navigate(NavRoute.QUANLYGIANGVIEN.route) }),
                ChucNang("Quản Lý Sinh Viên", Icons.Outlined.Person, Click = { navController.navigate(NavRoute.QUANLYSINHVIEN.route) }),

                ChucNang("Quản Lý Lịch Dạy", Icons.Outlined.CalendarMonth, Click = { navController.navigate(NavRoute.QUANLYLICHHOC.route) }),
                ChucNang("Quản Lý Lớp Học", Icons.Outlined.MeetingRoom, Click = { navController.navigate(NavRoute.QUANLYLOPHOC.route) }),
                ChucNang("Quản Lý Năm Học", Icons.Outlined.DateRange, Click = { navController.navigate(NavRoute.QUANLYNAMHOC.route) }),
                ChucNang("Quản Lý Ca Học", Icons.Outlined.AccessAlarm, Click = { navController.navigate(NavRoute.QUANLYCAHOC.route) }),
                ChucNang("Quản Lý Môn Học", Icons.Outlined.MenuBook, Click = { navController.navigate(NavRoute.QUANLYMONHOC.route) }),
            )
        }
        giangVien?.MaLoaiTaiKhoan == 2 -> {
            listOf(
                ChucNang("Quản Lý Lịch Dạy", Icons.Outlined.CalendarMonth, Click = { /* TODO: Navigate */ }),
                ChucNang("Quản Lý Phiếu Sửa Chữa", Icons.Outlined.Build, Click = { navController.navigate(NavRoute.QUANLYPHIEUSUACHUA.route) }),
                ChucNang("Quản Lý Phiếu Mượn May", Icons.Outlined.Article, Click = { navController.navigate(NavRoute.QUANLYPHIEUMUONMAY.route) }),
                ChucNang("Chuyển Máy", Icons.Outlined.SwapHoriz, Click = { navController.navigate(NavRoute.QUANLYCHUYENMAY.route) }),
            )
        }
        sinhvien != null -> {
            listOf(
                ChucNang("Danh Sách Lịch Học", Icons.Outlined.CalendarMonth, Click = {  })

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