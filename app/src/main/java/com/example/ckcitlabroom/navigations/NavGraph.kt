import android.widget.Toast
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ckcitlabroom.viewmodels.CaHocViewModel
import com.example.ckcitlabroom.viewmodels.LopHocViewModel
import com.example.ckcitlabroom.views.SinhVien.CreateSinhVienScreen
import com.example.ckcitlabroom.views.SinhVien.EditSinhVienScreen
import com.example.ckcitlabroom.viewmodels.ChiTietDonNhapyViewModel
import com.example.ckcitlabroom.viewmodels.ChiTietPhieuMuonViewModel
import com.example.ckcitlabroom.viewmodels.ChiTietSuDungMayViewModel
import com.example.ckcitlabroom.viewmodels.DonNhapViewModel
import com.example.ckcitlabroom.viewmodels.LichHocViewModel
import com.example.ckcitlabroom.viewmodels.LichSuChuyenMayViewModel
import com.example.ckcitlabroom.viewmodels.MayTinhViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

sealed class NavRoute(val route: String) {
    object HOME : NavRoute("home_screen")
    object ACCOUNT : NavRoute("thongtin_screen")
    object LOGINGIANGVIEN : NavRoute("logingv_screen")
    object LOGINSINHVIEN : NavRoute("loginsv_screen")
    object QUANLY : NavRoute("quanly_screen")

    //Đơn Nhập
    object QUANLYDONNHAP : NavRoute("quanlydonnhap_screen")
    object ADDDONNHAP : NavRoute("adddonnhap_screen")
    object CHITIETDONNHAP : NavRoute("chitietdonnhap_screen")
    object CHITIETDONNHAPCHUYEN : NavRoute("chitietdonnhapchuyen_screen")
    object CHITIETDONNHAPCHUYENMUON : NavRoute("chitietdonnhapchuyenmuon_screen")

    //Máy Tinh
    object QUANLYMAYTINH : NavRoute("quanlymaytinh_screen")
    object EDITMAYTINH : NavRoute("editmaytinh_screen")
    object ADDTMAYTINH : NavRoute("addmaytinh_screen")
    object MAYTINHDETAIL : NavRoute("maytinhdetail_screen")

    //PhongMay
    object QUANLYPHONGMAY : NavRoute("quanlyphongmay_screen")
    object PHONGMAYDETAIL : NavRoute("phongmaydetail_screen")
    object ADDPHONGMAY : NavRoute("addphongmay_screen")
    object PHONGMAYCHUYEN : NavRoute("phongmaychuyen_screen")
    object PHONGMAYCHUYENMUON : NavRoute("phongmaychuyenmuon_screen")
    object PHONGMAYDONNHAP : NavRoute("phongmaydonhap_screen")
    object PHONGKHOCHUYEN : NavRoute("phongkhochuyen_screen")
    object PHONGKHOCHUYENMUON : NavRoute("phongkhochuyenmuon_screen")

    //GiangVien
    object QUANLYGIANGVIEN : NavRoute("quanlygiangvien_screen")
    object ADDGIANGVIEN : NavRoute("addgiangvien_screen")
    object EDITGIANGVIEN : NavRoute("editgiangvien_screen")
    object LISTGIANGVIENCONGTAC : NavRoute("danhsachgiangvien_screen")
    object LISTGIANGVIENNGUNGCONGTAC : NavRoute("danhsachgiangvienngungcongtac_screen")
    object PHANQUYENADMIN : NavRoute("phanquyenadmin_screen")
    object PHANQUYENGIANGVIEN : NavRoute("phanquyengiangvien_screen")
    object DOIMATKHAUGV : NavRoute("doimatkhaugv_screen")

    //Chuyển Máy
    object QUANLYCHUYENMAY : NavRoute("quanlychuyenmay_screen")
    object LICHSUCHUYENMAY : NavRoute("lichsuchuyenmay_screen")
    object CHITIETLICHSUCHUYENMAY : NavRoute("chitietlichsuchuyenmay_screen")
    object LISTMAYTINHTHEODONCHUYEN : NavRoute("danhsachmaytinhtheodonchuyen_screen")
    object LISTPHONGMAYCHUYEN : NavRoute("danhsachphongmaychuyen_screen")

    //Quet QR
    object QUETQRCODE : NavRoute("quetqrcode_screen")

    //Sinh Viên
    object QUANLYSINHVIEN : NavRoute("quanlysinhvien_screen")
    object ADDSINHVIEN : NavRoute("addsinhvien_screen")
    object EDITSINHVIEN : NavRoute("editsinhvien_screen")
    object LISTSINHVIENTHEOLOP : NavRoute("sinhviendanghoc_screen")
    object LISTSINHVIENDINHCHI : NavRoute("sinhvientheolop_screen")
    object DOIMATKHAUSV : NavRoute("doimatkhausv_screen")

    //Lớp Học
    object QUANLYLOPHOC : NavRoute("quanlylophoc_screen")
    object ADDLOPHOC : NavRoute("addlophoc_screen")
    object EDITLOPHOC : NavRoute("editlophoc_screen")

    //CaHoc
    object QUANLYCAHOC : NavRoute("quanlycahoc_screen")
    object ADDCAHOC : NavRoute("addcahoc_screen")
    object EDITCAHOC : NavRoute("editcahoc_screen")

    //Sua May
    object ADDPHIEUSUACHUA : NavRoute("addphieusuachua_screen")
    object QUANLYPHIEUSUACHUA : NavRoute("quanlyphieusuachua_screen")
    object LICHSUSUAMAY : NavRoute("lichsusuamay_screen")
    object LISTMAYTINHLICHSUSUAMAY : NavRoute("danhsachmaytinhlichsusuamay_screen")
    object DETAILLICHSUSUAMAY : NavRoute("chitietlichsusuamay_screen")
    object LISTPHIEUCHUASUA : NavRoute("danhsachphieuchuasua_screen")
    object LISTPHIEUDASUA : NavRoute("danhsachphieudasua_screen")
    object LISTPHIEUBYSINHVIEN : NavRoute("danhsachphieubysinhvien_screen")

    //Màn hình bắt đầu
    object STARTSCREEN : NavRoute("start_screen")

    //Năm học
    object QUANLYNAMHOC : NavRoute("quanlynamhoc_screen")
    object ADDNAMHOC : NavRoute("addnamhoc_screen")
    object NAMHOCDETAIL : NavRoute("namhocdetail_screen")

    //DiemDanh
    object ADDDIEMDANH : NavRoute("adddiemdanh_screen")
    object LISTMAYTINHDIEMDANH : NavRoute("iistmaytinhdiemdanh_screen")

    //Mượn Máy
    object QUANLYPHIEUMUONMAY : NavRoute("quanlyphieumuonmay_screen")
    object ADDPHIEUMUONMAY : NavRoute("addphieumuonmay_screen")
    object LISTPHIEUMUONMAYCHUATRA : NavRoute("danhsachphieumuonmaychuatra_screen")
    object LISTPHIEUMUONMAYDATRA : NavRoute("danhsachphieumuonmaydatra_screen")
    object LISTPHIEUMUONMAYCHUACHUYEN : NavRoute("danhsachphieumuonmaychuachuyen_screen")
    object CHUYENMAYPHIEUMUON : NavRoute("chuyenmayphieumuon_screen")
    object CHITIETPHIEUMUON : NavRoute("chitietphieumuon_screen")
    object UPDATETRAMAY : NavRoute("updatetramay_screen")

    //Lịch Học
    object QUANLYLICHHOC : NavRoute("quanlylichhoc_screen")
    object ADDLICHHOC : NavRoute("addlichhoc_screen")
    object EDITLICHHOC : NavRoute("editlichhoc_screen")
    object LISTLICHHOC : NavRoute("danhsachlichhoc_screen")
    object LISTLICHHOCDADAY : NavRoute("danhsachlichhocdaday_screen")
    object CHITIETLICHHOCTHEOTUAN : NavRoute("chitietlichhoctheotuuan_screen")
    object LICHDAYTEOTUANALLGV : NavRoute("lichdaytheotuanallgv_screen")

    //Môn Học
    object QUANLYMONHOC : NavRoute("quanlymonhoc_screen")
    object ADDMONHOC : NavRoute("addmonhoc_screen")
    object EDITMONHOC : NavRoute("editmonhoc_screen")
    object LISTMONHOCDANGDAY : NavRoute("listmonhocdangday_screen")
    object LISTMONHOCNGUNGDAY : NavRoute("listmonhocngungday_screen")

    //Chi tiết sử dụng máy
    object LISTLICHHOCSUDUNGMAY : NavRoute("listlichhocsudungmay_screen")
    object LISTSVSUDUNGMAYTHEOCA : NavRoute("listSinhViensudungmaytheoca_screen")

    //Thông báo
    object ListThongBaoSinhVien : NavRoute("listthongbaosinhvien_screen")
}

@Composable
fun NavgationGraph(
    navController: NavHostController,
    lichHocViewModel: LichHocViewModel,
    giangVienViewModel: GiangVienViewModel,
    mayTinhViewModel: MayTinhViewModel,
    phongMayViewModel: PhongMayViewModel,
    lopHocViewModel: LopHocViewModel,
    lichSuChuyenMayViewModel: LichSuChuyenMayViewModel,
    donNhapViewModel: DonNhapViewModel,
    chiTietDonNhapyViewModel: ChiTietDonNhapyViewModel,
    sinhVienViewModel: SinhVienViewModel,

    namHocViewModel: NamHocViewModel,
    tuanViewModel: TuanViewModel,
    phieuSuaChuaViewModel: PhieuSuaChuaViewModel,
    chiTietSuDungMayViewModel: ChiTietSuDungMayViewModel,
    lichSuSuaMayViewModel: LichSuSuaMayViewModel,
    phieuMuonMayViewModel: PhieuMuonMayViewModel,
    chitetPhieuMuonViewModel: ChiTietPhieuMuonViewModel,
    caHocViewModel: CaHocViewModel,
    monhocViewModel: MonHocViewModel,
    notificationViewModel: NotificationViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.STARTSCREEN.route,
    ) {

        composable(
            route = NavRoute.STARTSCREEN.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            StartupCheckScreen(navController,sinhVienViewModel,giangVienViewModel)
        }

        composable(
            route = NavRoute.HOME.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            HomeScreen(lichHocViewModel,giangVienViewModel,sinhVienViewModel,navController,namHocViewModel,tuanViewModel)
        }

        composable(
            route = NavRoute.QUANLY.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            QuanLyScreen(navController,giangVienViewModel,sinhVienViewModel)
        }

        composable(
            route = NavRoute.ACCOUNT.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            AccountScreen(giangVienViewModel,sinhVienViewModel,navController)

        }

        composable(
            route = NavRoute.QUANLYDONNHAP.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            QuanLyDonNhap(navController,donNhapViewModel,chiTietDonNhapyViewModel,mayTinhViewModel)
        }

        composable(
            route = NavRoute.ADDDONNHAP.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            CreateDonNhapScreen(navController,mayTinhViewModel,phongMayViewModel,donNhapViewModel,chiTietDonNhapyViewModel)
        }

        composable(
            NavRoute.CHITIETDONNHAP.route + "?madonnhap={madonnhap}",
            arguments = listOf(
                navArgument("madonnhap") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val madonnhap = navBackStackEntry.arguments?.getString("madonnhap") ?: ""
            ChiTietDonNhapScreen(madonnhap,navController,chiTietDonNhapyViewModel,mayTinhViewModel,phongMayViewModel)
        }

        composable(
            NavRoute.LISTMAYTINHTHEODONCHUYEN.route + "?madonnhap={madonnhap}",
            arguments = listOf(
                navArgument("madonnhap") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val madonnhap = navBackStackEntry.arguments?.getString("madonnhap") ?: ""
            ListMayTinhTrongDonNhapScreen(madonnhap,navController,chiTietDonNhapyViewModel,mayTinhViewModel,phongMayViewModel)
        }

        composable(
            route = NavRoute.LISTPHONGMAYCHUYEN.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            ListPhongMayChuyenScreen(navController,mayTinhViewModel,phongMayViewModel)
        }

        composable(
            route = NavRoute.QUANLYMAYTINH.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            QuanLyMayTinh(navController,mayTinhViewModel,phongMayViewModel)
        }

        composable(
            route = NavRoute.ADDTMAYTINH.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            CreateMayTinhScreen(navController,mayTinhViewModel,phongMayViewModel)
        }

        composable(
            NavRoute.EDITMAYTINH.route + "?mamay={mamay}",
            arguments = listOf(
                navArgument("mamay") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val mamay = navBackStackEntry.arguments?.getString("mamay") ?: ""
            EditMayTinhScreen(mamay,phongMayViewModel)
        }

        composable(
            NavRoute.MAYTINHDETAIL.route + "?mamay={mamay}",
            arguments = listOf(
                navArgument("mamay") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val mamay = navBackStackEntry.arguments?.getString("mamay") ?: ""
            MayTinhDetailScreen(mamay,phongMayViewModel,giangVienViewModel,sinhVienViewModel,navController,caHocViewModel,chiTietSuDungMayViewModel,lichHocViewModel)
        }

        composable(
            route = NavRoute.LOGINGIANGVIEN.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            LoginGVScreen(navController,giangVienViewModel)
        }

        composable(
            route = NavRoute.LOGINSINHVIEN.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)

        ) {
            LoginSVScreen(navController,sinhVienViewModel)
        }

        composable(
            route = NavRoute.QUANLYGIANGVIEN.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            QuanLyGiangVien(navController)
        }

        composable(
            route = NavRoute.LISTGIANGVIENCONGTAC.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            ListGiangVienCongTac(navController,giangVienViewModel)
        }

        composable(
            route = NavRoute.LISTGIANGVIENNGUNGCONGTAC.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            ListGiangVienNgungCongTac(navController,giangVienViewModel)
        }

        composable(
            route = NavRoute.PHANQUYENADMIN.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            PhanQuyenAdminGVScreen(navController,giangVienViewModel)
        }

        composable(
            route = NavRoute.PHANQUYENGIANGVIEN.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            PhanQuyenGiangVienScreen(navController,giangVienViewModel)
        }

        composable(
            route = NavRoute.ADDGIANGVIEN.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            CreateGiangVienScreen(navController, giangVienViewModel)
        }

        composable(
            NavRoute.DOIMATKHAUGV.route + "?magv={magv}",
            arguments = listOf(
                navArgument("magv") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val magv = navBackStackEntry.arguments?.getString("magv") ?: ""
            DoiMatKhauGVScreen(magv,giangVienViewModel,navController)
        }

        composable(
            NavRoute.EDITGIANGVIEN.route + "?magv={magv}",
            arguments = listOf(
                navArgument("magv") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val magv = navBackStackEntry.arguments?.getString("magv") ?: ""
            EditGiangVienScreen(giangVienViewModel,magv)
        }

        composable(
            route = NavRoute.QUANLYSINHVIEN.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            QuanLySinhVien(navController)
        }
        composable(
            route = NavRoute.ADDSINHVIEN.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            CreateSinhVienScreen(navController, sinhVienViewModel, lopHocViewModel)
        }


        composable(
            route = NavRoute.LISTSINHVIENDINHCHI.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            ListSinhVienDinhCHi(navController,sinhVienViewModel, lopHocViewModel)
        }

        composable(
            route = NavRoute.LISTSINHVIENTHEOLOP.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            ListSinhVienDangHoc(navController,sinhVienViewModel, lopHocViewModel)
        }


        composable(
            NavRoute.EDITSINHVIEN.route + "?masv={masv}",
            arguments = listOf(
                navArgument("masv") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val masv = navBackStackEntry.arguments?.getString("masv") ?: ""
            EditSinhVienScreen(sinhVienViewModel,lopHocViewModel, masv)
        }

        composable(
            NavRoute.DOIMATKHAUSV.route + "?masv={masv}",
            arguments = listOf(
                navArgument("masv") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val masv = navBackStackEntry.arguments?.getString("masv") ?: ""
            DoiMatKhauSvScreen(masv,sinhVienViewModel,navController)
        }

        composable(
            route = NavRoute.QUANLYLOPHOC.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            QuanLyLopHoc(navController, lopHocViewModel)
        }

        composable(
            route = NavRoute.ADDLOPHOC.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            CreateLopHocScreen(navController,lopHocViewModel)
        }

        composable(
            NavRoute.EDITLOPHOC.route + "?malop={malop}",
            arguments = listOf(
                navArgument("malop") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val malop = navBackStackEntry.arguments?.getString("malop") ?: ""
            EditLopHocScreen(lopHocViewModel,malop)
        }

        composable(
            route = NavRoute.QUANLYCAHOC.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            QuanLyCaHoc(navController, caHocViewModel)
        }

        composable(
            route = NavRoute.ADDCAHOC.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            CreateCaHocScreen(navController, caHocViewModel)
        }

        composable(
            NavRoute.EDITCAHOC.route + "?maca={maca}",
            arguments = listOf(
                navArgument("maca") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val maca = navBackStackEntry.arguments?.getString("maca") ?: ""
            EditCaHocScreen(caHocViewModel, maca)
        }

        composable(
            route = NavRoute.QUANLYPHONGMAY.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            QuanLyPhongMay(navController,phongMayViewModel,mayTinhViewModel)
        }

        composable(
            NavRoute.PHONGMAYDETAIL.route + "?maphong={maphong}",
            arguments = listOf(
                navArgument("maphong") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val maphong = navBackStackEntry.arguments?.getString("maphong") ?: ""
            PhongMayDetailScreen(maphong,navController,phongMayViewModel,mayTinhViewModel)
        }

        composable(
            NavRoute.PHONGMAYDONNHAP.route + "?maphong={maphong}",
            arguments = listOf(
                navArgument("maphong") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val maphong = navBackStackEntry.arguments?.getString("maphong") ?: ""
            PhongMayDonNhapScreen(
                maphong,
                navController,
                phongMayViewModel,
                mayTinhViewModel,
                donNhapViewModel,
                chiTietDonNhapyViewModel
            )
        }

        composable(
            NavRoute.PHONGKHOCHUYEN.route + "?maphong={maphong}",
            arguments = listOf(
                navArgument("maphong") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val maphong = navBackStackEntry.arguments?.getString("maphong") ?: ""
            PhongKhoChuyenScreen(maphong, navController, phongMayViewModel, mayTinhViewModel, donNhapViewModel, chiTietDonNhapyViewModel)
        }

        composable(
            NavRoute.PHONGKHOCHUYENMUON.route + "?maphong={maphong}&maphongmuon={maphongmuon}&maphieumuon={maphieumuon}",
            arguments = listOf(
                navArgument("maphong") { type = NavType.StringType; nullable = true },
                navArgument("maphongmuon") { type = NavType.StringType; nullable = true },
                navArgument("maphieumuon") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val maphong = navBackStackEntry.arguments?.getString("maphong") ?: ""
            val maphongmuon = navBackStackEntry.arguments?.getString("maphongmuon") ?: ""
            val maphieumuon = navBackStackEntry.arguments?.getString("maphieumuon") ?: ""
            PhongKhoChuyenMuonScreen(maphieumuon,maphong, maphongmuon, navController,phongMayViewModel, mayTinhViewModel, donNhapViewModel, chiTietDonNhapyViewModel)
        }

        composable(
            NavRoute.PHONGMAYCHUYEN.route + "?maphong={maphong}",
            arguments = listOf(
                navArgument("maphong") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val maphong = navBackStackEntry.arguments?.getString("maphong") ?: ""
            PhongMayChuyenScreen(maphong,phongMayViewModel,mayTinhViewModel,lichSuChuyenMayViewModel)
        }

        composable(
            NavRoute.PHONGMAYCHUYENMUON.route + "?maphong={maphong}&maphongmuon={maphongmuon}&maphieumuon={maphieumuon}",
            arguments = listOf(
                navArgument("maphong") { type = NavType.StringType; nullable = true },
                navArgument("maphongmuon") { type = NavType.StringType; nullable = true },
                navArgument("maphieumuon") { type = NavType.StringType; nullable = true },
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val maphong = navBackStackEntry.arguments?.getString("maphong") ?: ""
            val maphongmuon = navBackStackEntry.arguments?.getString("maphongmuon") ?: ""
            val maphieumuon = navBackStackEntry.arguments?.getString("maphieumuon") ?: ""
            PhongMayChuyenMuonScreen(maphieumuon,maphong,maphongmuon,phongMayViewModel,mayTinhViewModel,lichSuChuyenMayViewModel,phieuMuonMayViewModel,chitetPhieuMuonViewModel)
        }


        composable(
            NavRoute.CHITIETDONNHAPCHUYEN.route + "?madonnhap={madonnhap}&maphong={maphong}",
            arguments = listOf(
                navArgument("madonnhap") { type = NavType.StringType; nullable = true; defaultValue = "" },
                navArgument("maphong") { type = NavType.StringType; nullable = true; defaultValue = "" }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val madonnhap = navBackStackEntry.arguments?.getString("madonnhap") ?: ""
            val maphong = navBackStackEntry.arguments?.getString("maphong") ?: ""

            ChiTietDonNhapChuyenScreen(
                madonnhap,
                maphong,
                chiTietDonNhapyViewModel,
                phongMayViewModel,
                mayTinhViewModel,
                lichSuChuyenMayViewModel
            )
        }

        composable(
            NavRoute.CHITIETDONNHAPCHUYENMUON.route + "?madonnhap={madonnhap}&maphong={maphong}&maphongmuon={maphongmuon}&maphieumuon={maphieumuon}",
            arguments = listOf(
                navArgument("madonnhap") { type = NavType.StringType; nullable = true; defaultValue = "" },
                navArgument("maphong") { type = NavType.StringType; nullable = true; defaultValue = "" },
                navArgument("maphongmuon") { type = NavType.StringType; nullable = true; defaultValue = "" },
                navArgument("maphieumuon") { type = NavType.StringType; nullable = true; defaultValue = "" }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val madonnhap = navBackStackEntry.arguments?.getString("madonnhap") ?: ""
            val maphong = navBackStackEntry.arguments?.getString("maphong") ?: ""
            val maphongmuon = navBackStackEntry.arguments?.getString("maphongmuon") ?: ""
            val maphieumuon = navBackStackEntry.arguments?.getString("maphieumuon") ?: ""

            ChiTietDonNhapChuyenMuonScreen(
                maphieumuon,
                maphong,
                maphongmuon,
                madonnhap,
                chiTietDonNhapyViewModel,
                phongMayViewModel,
                mayTinhViewModel,
                lichSuChuyenMayViewModel,
                phieuMuonMayViewModel,
                chitetPhieuMuonViewModel,
            )
        }

        composable(
            route = NavRoute.ADDPHONGMAY.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            CreatePhongMayScreen(navController,phongMayViewModel)
        }

        ///Chuyển máy
        composable(
            route = NavRoute.QUANLYCHUYENMAY.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            QuanLyChuyenMayScreen(navController,mayTinhViewModel)
        }

        composable(
            route = NavRoute.LICHSUCHUYENMAY.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            LichSuChuyenMayScreen(navController,donNhapViewModel,chiTietDonNhapyViewModel,mayTinhViewModel)
        }

        composable(
            NavRoute.CHITIETLICHSUCHUYENMAY.route + "?mamay={mamay}",
            arguments = listOf(
                navArgument("mamay") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val mamay = navBackStackEntry.arguments?.getString("mamay") ?: ""
            ChiTietLichSuChuyenMay(mamay,lichSuChuyenMayViewModel,phongMayViewModel)
        }

        composable(
            route = NavRoute.QUETQRCODE.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            val context = LocalContext.current

            /* 1. Thu thập Flow thành State */
            val danhSachMay by mayTinhViewModel
                .danhSachAllMayTinh2           // Flow<List<MayTinh>>
                .collectAsState(initial = emptyList())

            /* 2. Gọi API một lần duy nhất khi composable được đưa vào composition */
            LaunchedEffect(Unit) {
                mayTinhViewModel.getAllMayTinh2()
            }

            /* 3. Chờ dữ liệu xong rồi mới hiển thị QR scanner */
            if (danhSachMay.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    DotLoading()
                }
            } else {
                QRCodeScannerScreen { qrCodeValue ->
                    val tonTai = danhSachMay.any { it.MaMay == qrCodeValue }
                    if (tonTai) {
                        navController.navigate(
                            NavRoute.MAYTINHDETAIL.route + "?mamay=$qrCodeValue"
                        )
                    } else {
                        Toast.makeText(context, "Mã QR không hợp lệ", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }



        //Sửa chữa
        composable(
            NavRoute.ADDPHIEUSUACHUA.route + "?mamay={mamay}",
            arguments = listOf(
                navArgument("mamay") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val mamay = navBackStackEntry.arguments?.getString("mamay") ?: ""
            CreatePhieuSuaChuaScreen(mamay,phieuSuaChuaViewModel,giangVienViewModel,sinhVienViewModel,navController,notificationViewModel)
        }

        composable(
            route = NavRoute.QUANLYPHIEUSUACHUA.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            QuanLyPhieuSuaChuaScreen(navController)
        }

        composable(
            route = NavRoute.LISTPHIEUCHUASUA.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            ListPhieuChuaSua(phieuSuaChuaViewModel,mayTinhViewModel,lichSuSuaMayViewModel,giangVienViewModel)
        }

        composable(
            route = NavRoute.LISTPHIEUDASUA.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            ListPhieuDaSua(phieuSuaChuaViewModel,mayTinhViewModel,lichSuSuaMayViewModel,giangVienViewModel)
        }

        composable(
            route = NavRoute.LISTPHIEUBYSINHVIEN.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            ListPhieuSuaBySinhVienScreen(phieuSuaChuaViewModel,mayTinhViewModel,lichSuSuaMayViewModel,sinhVienViewModel,giangVienViewModel)
        }

        composable(
            route = NavRoute.LICHSUSUAMAY.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            LichSuSuaMayScreen(navController,phongMayViewModel,mayTinhViewModel,donNhapViewModel,chiTietDonNhapyViewModel)
        }

        composable(
            NavRoute.LISTMAYTINHLICHSUSUAMAY.route + "?madonnhap={madonnhap}",
            arguments = listOf(
                navArgument("madonnhap") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val madonnhap = navBackStackEntry.arguments?.getString("madonnhap") ?: ""
            ListMayTinhLichSuSuaMayScreen(madonnhap,navController,chiTietDonNhapyViewModel,mayTinhViewModel,phongMayViewModel,lichSuSuaMayViewModel)
        }

        composable(
            NavRoute.DETAILLICHSUSUAMAY.route + "?mamay={mamay}",
            arguments = listOf(
                navArgument("mamay") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val mamay = navBackStackEntry.arguments?.getString("mamay") ?: ""
            ChiTietLichSuSuaMayScreen(mamay,phieuSuaChuaViewModel,lichSuSuaMayViewModel)
        }

        //Năm Học
        composable(
            route = NavRoute.QUANLYNAMHOC.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            QuanLyNamHocScreen(navController,namHocViewModel)
        }

        composable(
            route = NavRoute.ADDNAMHOC.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            CreateNamHocScreen(navController,namHocViewModel)
        }

        composable(
            NavRoute.NAMHOCDETAIL.route + "?manam={manam}",
            arguments = listOf(
                navArgument("manam") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val manam = navBackStackEntry.arguments?.getString("manam") ?: ""
            NamHocDetailScreen(manam,tuanViewModel)
        }

        //DiemDanh

        composable(
            NavRoute.ADDDIEMDANH.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            DiemDanhScreen(chiTietSuDungMayViewModel,lichHocViewModel,sinhVienViewModel,caHocViewModel,navController)
        }

        composable(
            NavRoute.LISTMAYTINHDIEMDANH.route + "?malichhoc={malichhoc}",
            arguments = listOf(
                navArgument("malichhoc") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val malichhoc = navBackStackEntry.arguments?.getString("malichhoc") ?: ""
            ListMayTinhDiemDanh(navController,malichhoc,lichHocViewModel,mayTinhViewModel,chiTietSuDungMayViewModel,sinhVienViewModel)
        }

        //Mượn Máy
        composable(
            route = NavRoute.QUANLYPHIEUMUONMAY.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            QuanLyPhieuMuonMayScreen(navController)
        }

        composable(
            route = NavRoute.ADDPHIEUMUONMAY.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            CreatePhieuMuonMayScreen(phongMayViewModel,phieuMuonMayViewModel)
        }

        composable(
            route = NavRoute.LISTPHIEUMUONMAYCHUATRA.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            ListPhieuMuonChuaTra(navController,phieuMuonMayViewModel,phongMayViewModel,chitetPhieuMuonViewModel,mayTinhViewModel,lichSuChuyenMayViewModel)
        }

        composable(
            route = NavRoute.LISTPHIEUMUONMAYDATRA.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            ListPhieuMuonDaTra(navController,phongMayViewModel,phieuMuonMayViewModel,chitetPhieuMuonViewModel,mayTinhViewModel,lichSuChuyenMayViewModel)
        }

        composable(
            route = NavRoute.LISTPHIEUMUONMAYCHUACHUYEN.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            ListPhieuMuonChuaChuyen(navController,phongMayViewModel,phieuMuonMayViewModel,chitetPhieuMuonViewModel,mayTinhViewModel,lichSuChuyenMayViewModel)
        }

        composable(
            NavRoute.CHUYENMAYPHIEUMUON.route + "?maphong={maphong}&maphieumuon={maphieumuon}",
            arguments = listOf(
                navArgument("maphong") { type = NavType.StringType; nullable = true },
                navArgument("maphieumuon") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val maphong = navBackStackEntry.arguments?.getString("maphong") ?: ""
            val maphieumuon = navBackStackEntry.arguments?.getString("maphieumuon") ?: ""
            ChuyenMayPhieuMuonScreen(maphong,maphieumuon,navController,mayTinhViewModel,phongMayViewModel)
        }

        composable(
            NavRoute.CHITIETPHIEUMUON.route + "?maphieumuon={maphieumuon}",
            arguments = listOf(
                navArgument("maphieumuon") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val maphieumuon = navBackStackEntry.arguments?.getString("maphieumuon") ?: ""
            ChiTietPhieuMuonMay(maphieumuon,chitetPhieuMuonViewModel,mayTinhViewModel,phongMayViewModel)
        }

        composable(
            NavRoute.UPDATETRAMAY.route + "?maphieumuon={maphieumuon}",
            arguments = listOf(
                navArgument("maphieumuon") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val maphieumuon = navBackStackEntry.arguments?.getString("maphieumuon") ?: ""
            UpdateTraMayScreen(navController,maphieumuon,mayTinhViewModel,phieuMuonMayViewModel,chitetPhieuMuonViewModel,lichSuChuyenMayViewModel)
        }

        composable(
            route = NavRoute.QUANLYLICHHOC.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            QuanLyLichHocScreen(navController,giangVienViewModel)
        }

        composable(
            route = NavRoute.ADDLICHHOC.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            CreateLichHocScreen(
                navController,
                lichHocViewModel,
                namHocViewModel,
                monhocViewModel,
                tuanViewModel,
                giangVienViewModel,
                lopHocViewModel,
                phongMayViewModel,
                caHocViewModel
            )
        }

        composable(
            NavRoute.EDITLICHHOC.route + "?malichhoc={malichhoc}",
            arguments = listOf(
                navArgument("malichhoc") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val malichhoc = navBackStackEntry.arguments?.getString("malichhoc") ?: ""
            EditLichHocScreen(
                malichhoc,
                navController,
                lichHocViewModel,
                namHocViewModel,
                monhocViewModel,
                tuanViewModel,
                giangVienViewModel,
                lopHocViewModel,
                phongMayViewModel,
                caHocViewModel,
                sinhVienViewModel,
                notificationViewModel
            )
        }

        composable(
            route = NavRoute.LICHDAYTEOTUANALLGV.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            LichDayGVAllTuan(lichHocViewModel,giangVienViewModel,sinhVienViewModel,namHocViewModel,tuanViewModel,phongMayViewModel,navController)
        }

        composable(
            route = "${NavRoute.LISTLICHHOC.route}/{maTuan}",
            arguments = listOf(navArgument("maTuan") { type = NavType.IntType }),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { backStackEntry ->
            val maTuan = backStackEntry.arguments?.getInt("maTuan") ?: 0

            ListLichHocScreen(
                maTuan = maTuan,
                lichHocViewModel = lichHocViewModel,
                giangVienViewModel = giangVienViewModel,
                sinhVienViewModel = sinhVienViewModel,
                namHocViewModel = namHocViewModel,
                tuanViewModel = tuanViewModel,
                navController = navController
            )
        }


        composable(
            route = NavRoute.LISTLICHHOCDADAY.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            ListLichHocDaDayScreen(lichHocViewModel,giangVienViewModel,sinhVienViewModel,namHocViewModel,tuanViewModel,navController)
        }

        composable(
            NavRoute.CHITIETLICHHOCTHEOTUAN.route + "?matuan={matuan}",
            arguments = listOf(
                navArgument("matuan") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val matuan = navBackStackEntry.arguments?.getString("matuan") ?: ""
            ChiTietLichHocScreen(matuan,navController,sinhVienViewModel, giangVienViewModel,lichHocViewModel,tuanViewModel)
        }


        //Môn Học
        composable(
            route = NavRoute.QUANLYMONHOC.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            QuanLyMonHocScreen(navController)
        }

        composable(
            route = NavRoute.ADDMONHOC.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            CreateMonHocScreen(navController,monhocViewModel)
        }

        composable(
            NavRoute.EDITMONHOC.route + "?mamonhoc={mamonhoc}",
            arguments = listOf(
                navArgument("mamonhoc") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val mamonhoc = navBackStackEntry.arguments?.getString("mamonhoc") ?: ""
            EditMonHocScreen(mamonhoc,monhocViewModel)
        }

        composable(
            route = NavRoute.LISTMONHOCDANGDAY.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            ListMonHocDangDayScreen(navController,monhocViewModel)
        }

        composable(
            route = NavRoute.LISTMONHOCNGUNGDAY.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            ListMonHocNgungDayScreen(navController,monhocViewModel)
        }

        //Chi tiết sử dụng máy
        composable(
            route = NavRoute.LISTLICHHOCSUDUNGMAY.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            ListLichHocSuDungMay(lichHocViewModel,giangVienViewModel,namHocViewModel,tuanViewModel,navController)
        }

        composable(
            NavRoute.LISTSVSUDUNGMAYTHEOCA.route + "?maCa={maCa}&maTuan={maTuan}&maphong={maphong}&ngaySuDung={ngaySuDung}",
            arguments = listOf(
                navArgument("maCa") { type = NavType.StringType; nullable = true },
                navArgument("maTuan") { type = NavType.StringType; nullable = true },
                navArgument("maphong") { type = NavType.StringType; nullable = true },
                navArgument("ngaySuDung") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) { navBackStackEntry ->
            val maCa = navBackStackEntry.arguments?.getString("maCa") ?: ""
            val maTuan = navBackStackEntry.arguments?.getString("maTuan") ?: ""
            val maphong = navBackStackEntry.arguments?.getString("maphong") ?: ""
            val ngaySuDung = navBackStackEntry.arguments?.getString("ngaySuDung") ?: ""

            ListSinhVienTheoCa(maCa,maTuan,maphong,ngaySuDung,chiTietSuDungMayViewModel)
        }

        composable(
            route = NavRoute.ListThongBaoSinhVien.route,
            enterTransition = defaultEnterTransition(AnimatedContentTransitionScope.SlideDirection.Start),
            exitTransition = defaultExitTransition(AnimatedContentTransitionScope.SlideDirection.End)
        ) {
            ListThongBao(notificationViewModel,sinhVienViewModel,giangVienViewModel)
        }
    }
}
