import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.ckcitlabroom.viewmodels.CaHocViewModel
import com.example.ckcitlabroom.viewmodels.LopHocViewModel
import com.example.lapstore.viewmodels.ChiTietDonNhapyViewModel
import com.example.lapstore.viewmodels.ChiTietSuDungMayViewModel
import com.example.lapstore.viewmodels.DonNhapViewModel
import com.example.lapstore.viewmodels.LichHocViewModel
import com.example.lapstore.viewmodels.LichSuChuyenMayViewModel
import com.example.lapstore.viewmodels.MayTinhViewModel


sealed class NavRoute(val route: String) {
    object HOME : NavRoute("home_screen")
    object ACCOUNT : NavRoute("account_screen")
    object LOGINGIANGVIEN : NavRoute("logingv_screen")
    object LOGINSINHVIEN : NavRoute("loginsv_screen")
    object QUANLY : NavRoute("quanly_screen")

    object QUANLYDONNHAP : NavRoute("quanlydonnhap_screen")
    object ADDDONNHAP : NavRoute("adddonnhap_screen")
    object CHITIETDONNHAP : NavRoute("chitietdonnhap_screen")
    object CHITIETDONNHAPCHUYEN : NavRoute("chitietdonnhapchuyen_screen")


    object ADDCAUHINH : NavRoute("addcauhinh_screen")
    object EDITCAUHINH : NavRoute("editcauhinh_screen")

    object QUANLYMAYTINH : NavRoute("quanlymaytinh_screen")
    object EDITMAYTINH : NavRoute("editmaytinh_screen")
    object ADDTMAYTINH : NavRoute("addmaytinh_screen")
    object MAYTINHDETAIL : NavRoute("maytinhdetail_screen")

    object QUANLYPHONGMAY : NavRoute("quanlyphongmay_screen")
    object PHONGMAYDETAIL : NavRoute("phongmaydetail_screen")
    object ADDPHONGMAY : NavRoute("addphongmay_screen")
    object PHONGMAYCHUYEN : NavRoute("phongmaychuyen_screen")
    object PHONGMAYDONNHAP : NavRoute("phongmaydonhap_screen")
    object PHONGKHOCHUYEN : NavRoute("phongkhochuyen_screen")

    object QUANLYGIANGVIEN : NavRoute("quanlygiangvien_screen")
    object ADDGIANGVIEN : NavRoute("addgiangvien_screen")
    object EDITGIANGVIEN : NavRoute("editgiangvien_screen")

    //Chuyển Máy
    object QUANLYCHUYENMAY : NavRoute("quanlychuyenmay_screen")
    object LICHSUCHUYENMAY : NavRoute("lichsuchuyenmay_screen")
    object CHITIETLICHSUCHUYENMAY : NavRoute("chitietlichsuchuyenmay_screen")
    object LISTMAYTINHTHEODONCHUYEN : NavRoute("danhsachmaytinhtheodonchuyen_screen")
    object LISTPHONGMAYCHUYEN : NavRoute("danhsachphongmaychuyen_screen")

    object QUETQRCODE : NavRoute("quetqrcode_screen")

    object QUANLYSINHVIEN : NavRoute("quanlysinhvien_screen")
    object ADDSINHVIEN : NavRoute("addsinhvien_screen")
    object EDITSINHVIEN : NavRoute("editsinhvien_screen")

    object QUANLYLOPHOC : NavRoute("quanlylophoc_screen")
    object ADDLOPHOC : NavRoute("addlophoc_screen")
    object EDITLOPHOC : NavRoute("editlophoc_screen")


    object QUANLYCAHOC : NavRoute("quanlycahoc_screen")
    object ADDCAHOC : NavRoute("addcahoc_screen")
    object EDITCAHOC : NavRoute("editcahoc_screen")


    object ADDPHIEUSUACHUA : NavRoute("addphieusuachua_screen")
    object QUANLYPHIEUSUACHUA : NavRoute("quanlyphieusuachua_screen")
    object LICHSUSUAMAY : NavRoute("lichsusuamay_screen")
    object LISTMAYTINHLICHSUSUAMAY : NavRoute("danhsachmaytinhlichsusuamay_screen")
    object DETAILLICHSUSUAMAY : NavRoute("chitietlichsusuamay_screen")
    object LISTPHIEUCHUASUA : NavRoute("danhsachphieuchuasua_screen")
    object LISTPHIEUDASUA : NavRoute("danhsachphieudasua_screen")


    object STARTSCREEN : NavRoute("start_screen")

    //Năm học
    object QUANLYNAMHOC : NavRoute("quanlynamhoc_screen")
    object ADDNAMHOC : NavRoute("addnamhoc_screen")
    object EDITNAMHOC : NavRoute("editnamhoc_screen")
    object NAMHOCDETAIL : NavRoute("namhocdetail_screen")

    //DiemDanh
    object ADDDIEMDANH : NavRoute("adddiemdanh_screen")

    //Mượn Máy
    object QUANLYPHIEUMUONMAY : NavRoute("quanlyphieumuonmay_screen")
    object ADDPHIEUMUONMAY : NavRoute("addphieumuonmay_screen")
    object LISTPHIEUMUONMAYCHUATRA : NavRoute("danhsachphieumuonmaychuatra_screen")
    object LISTPHIEUMUONMAYCHUACHUYEN : NavRoute("danhsachphieumuonmaychuachuyen_screen")
    object CHUYENMAYPHIEUMUON : NavRoute("chuyenmayphieumuon_screen")

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
    caHocViewModel: CaHocViewModel


    namHocViewModel: NamHocViewModel,
    tuanViewModel: TuanViewModel,
    phieuSuaChuaViewModel: PhieuSuaChuaViewModel,
    chiTietSuDungMayViewModel: ChiTietSuDungMayViewModel,
    lichSuSuaMayViewModel: LichSuSuaMayViewModel,
    phieuMuonMayViewModel: PhieuMuonMayViewModel

) {

    val context = LocalContext.current.applicationContext
    val userPreferences = remember { SinhVienPreferences(context) }


    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    NavHost(navController = navController, startDestination = NavRoute.STARTSCREEN.route,

    ) {

        composable(
            route = NavRoute.STARTSCREEN.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            }
        ) {
            StartupCheckScreen(navController,sinhVienViewModel,giangVienViewModel)
        }

        composable(
            route = NavRoute.HOME.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(300)
                )
            }
        ) { navBackStackEntry ->
            HomeScreen(lichHocViewModel,giangVienViewModel,sinhVienViewModel)
        }



        composable(
            route = NavRoute.QUANLY.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            }
        ) {
            QuanLyScreen(navController,giangVienViewModel,sinhVienViewModel)
        }

        composable(
            NavRoute.ACCOUNT.route,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) }
        ) { navBackStackEntry ->
            AccountScreen(giangVienViewModel,sinhVienViewModel,navController)
        }

        composable(
            route = NavRoute.QUANLYDONNHAP.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(300)
                )
            }
        ) {
            QuanLyDonNhap(navController,donNhapViewModel,chiTietDonNhapyViewModel,mayTinhViewModel)
        }

        composable(
            route = NavRoute.ADDDONNHAP.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(200)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(200)
                )
            }
        ) {
            CreateDonNhapScreen(navController,mayTinhViewModel,phongMayViewModel,donNhapViewModel,chiTietDonNhapyViewModel)
        }

        composable(
            NavRoute.CHITIETDONNHAP.route + "?madonnhap={madonnhap}",
            arguments = listOf(
                navArgument("madonnhap") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) }
        ) { navBackStackEntry ->
            val madonnhap = navBackStackEntry.arguments?.getString("madonnhap") ?: ""
            ChiTietDonNhapScreen(madonnhap,navController,chiTietDonNhapyViewModel,mayTinhViewModel,phongMayViewModel)
        }

        composable(
            NavRoute.LISTMAYTINHTHEODONCHUYEN.route + "?madonnhap={madonnhap}",
            arguments = listOf(
                navArgument("madonnhap") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) }
        ) { navBackStackEntry ->
            val madonnhap = navBackStackEntry.arguments?.getString("madonnhap") ?: ""
            ListMayTinhTrongDonNhapScreen(madonnhap,navController,chiTietDonNhapyViewModel,mayTinhViewModel,phongMayViewModel)
        }

        composable(
            route = NavRoute.LISTPHONGMAYCHUYEN.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(300)
                )
            }
        ) {
            ListPhongMayChuyenScreen(navController,mayTinhViewModel,phongMayViewModel)
        }

        composable(
            route = NavRoute.QUANLYMAYTINH.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(300)
                )
            }
        ) {
            QuanLyMayTinh(navController,mayTinhViewModel,phongMayViewModel)
        }

        composable(
            route = NavRoute.ADDTMAYTINH.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(300)
                )
            }
        ) {
            CreateMayTinhScreen(navController,mayTinhViewModel,phongMayViewModel)
        }

        composable(
            NavRoute.EDITMAYTINH.route + "?mamay={mamay}",
            arguments = listOf(
                navArgument("mamay") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) }
        ) { navBackStackEntry ->
            val mamay = navBackStackEntry.arguments?.getString("mamay") ?: ""
            EditMayTinhScreen(mamay,phongMayViewModel)
        }

        composable(
            NavRoute.MAYTINHDETAIL.route + "?mamay={mamay}",
            arguments = listOf(
                navArgument("mamay") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) }
        ) { navBackStackEntry ->
            val mamay = navBackStackEntry.arguments?.getString("mamay") ?: ""
            MayTinhDetailScreen(mamay,phongMayViewModel,giangVienViewModel,navController)
        }

        composable(
            route = NavRoute.ADDCAUHINH.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(300)
                )
            }
        ) {
            CreateCauHinhScreen()
        }

        composable(
            route = NavRoute.EDITCAUHINH.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(300)
                )
            }
        ) {
//            EditCauHinhScreen()
        }

        composable(
            route = NavRoute.LOGINGIANGVIEN.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            }
        ) {
            LoginGVScreen(navController,giangVienViewModel)
        }

        composable(
            route = NavRoute.LOGINSINHVIEN.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            }

        ) {
            LoginSVScreen(navController,sinhVienViewModel)
        }

        composable(
            route = NavRoute.QUANLYGIANGVIEN.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(300)
                )
            }
        ) {
            QuanLyGiangVien(navController, giangVienViewModel)
        }

        composable(
            route = NavRoute.ADDGIANGVIEN.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(200)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(200)
                )
            }
        ) {
            CreateGiangVienScreen(navController, giangVienViewModel)
        }

        composable(
            NavRoute.EDITGIANGVIEN.route + "?magv={magv}",
            arguments = listOf(
                navArgument("magv") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) }
        ) { navBackStackEntry ->
            val magv = navBackStackEntry.arguments?.getString("magv") ?: ""
            EditGiangVienScreen(giangVienViewModel,magv)
        }

        composable(
            route = NavRoute.QUANLYSINHVIEN.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(300)
                )
            }
        ) {
            QuanLySinhVien(navController, sinhVienViewModel)
        }
        composable(
            route = NavRoute.ADDSINHVIEN.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(200)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(200)
                )
            }
        ) {
            CreateSinhVienScreen(navController, sinhVienViewModel, lopHocViewModel)
        }

        composable(
            NavRoute.EDITSINHVIEN.route + "?masv={masv}",
            arguments = listOf(
                navArgument("masv") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) }
        ) { navBackStackEntry ->
            val masv = navBackStackEntry.arguments?.getString("masv") ?: ""
            EditSinhVienScreen(sinhVienViewModel,lopHocViewModel, masv)
        }

        composable(
            route = NavRoute.QUANLYLOPHOC.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(300)
                )
            }
        ) {
            QuanLyLopHoc(navController, lopHocViewModel)
        }

        composable(
            route = NavRoute.ADDLOPHOC.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(300)
                )
            }
        ) {
            CreateLopHocScreen(navController,lopHocViewModel)
        }

        composable(
            NavRoute.EDITLOPHOC.route + "?malop={malop}",
            arguments = listOf(
                navArgument("malop") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) }
        ) { navBackStackEntry ->
            val malop = navBackStackEntry.arguments?.getString("malop") ?: ""
            EditLopHocScreen(lopHocViewModel,malop)
        }

        composable(
            route = NavRoute.QUANLYCAHOC.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(300)
                )
            }
        ) {
            QuanLyCaHoc(navController, caHocViewModel)
        }

        composable(
            route = NavRoute.ADDCAHOC.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(300)
                )
            }
        ) {
            CreateCaHocScreen(navController, caHocViewModel)
        }

        composable(
            NavRoute.EDITCAHOC.route + "?maca={maca}",
            arguments = listOf(
                navArgument("maca") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300))
            }
        ) { navBackStackEntry ->
            val maca = navBackStackEntry.arguments?.getString("maca") ?: ""
            EditCaHocScreen(caHocViewModel, maca)
        }



        composable(
            route = NavRoute.QUANLYPHONGMAY.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(300)
                )
            }
        ) {
            QuanLyPhongMay(navController,phongMayViewModel,mayTinhViewModel)
        }

        composable(
            NavRoute.PHONGMAYDETAIL.route + "?maphong={maphong}",
            arguments = listOf(
                navArgument("maphong") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) }
        ) { navBackStackEntry ->
            val maphong = navBackStackEntry.arguments?.getString("maphong") ?: ""
            PhongMayDetailScreen(maphong,navController,phongMayViewModel,mayTinhViewModel)
        }

        composable(
            NavRoute.PHONGMAYDONNHAP.route + "?maphong={maphong}",
            arguments = listOf(
                navArgument("maphong") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(300)
                )
            }
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
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    tween(300)
                )
            }
        ) { navBackStackEntry ->
            val maphong = navBackStackEntry.arguments?.getString("maphong") ?: ""
            PhongKhoChuyenScreen(
                maphong,
                navController,
                phongMayViewModel,
                mayTinhViewModel,
                donNhapViewModel,
                chiTietDonNhapyViewModel
            )
        }

        composable(
            NavRoute.PHONGMAYCHUYEN.route + "?maphong={maphong}",
            arguments = listOf(
                navArgument("maphong") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) }
        ) { navBackStackEntry ->
            val maphong = navBackStackEntry.arguments?.getString("maphong") ?: ""
            PhongMayChuyenScreen(maphong,navController,phongMayViewModel,mayTinhViewModel,lichSuChuyenMayViewModel)
        }

        composable(
            NavRoute.CHITIETDONNHAPCHUYEN.route + "?madonnhap={madonnhap}&maphong={maphong}",
            arguments = listOf(
                navArgument("madonnhap") { type = NavType.StringType; nullable = true; defaultValue = "" },
                navArgument("maphong") { type = NavType.StringType; nullable = true; defaultValue = "" }
            ),
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300))
            },
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300))
            }
        ) { navBackStackEntry ->
            val madonnhap = navBackStackEntry.arguments?.getString("madonnhap") ?: ""
            val maphong = navBackStackEntry.arguments?.getString("maphong") ?: ""

            ChiTietDonNhapChuyenScreen(
                maphong,
                madonnhap,
                navController,
                chiTietDonNhapyViewModel,
                phongMayViewModel,
                mayTinhViewModel,
                lichSuChuyenMayViewModel
            )
        }


        composable(
            route = NavRoute.ADDPHONGMAY.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(300)
                )
            }
        ) {
            CreatePhongMayScreen(navController,phongMayViewModel)
        }

        ///Chuyển máy

        composable(
            route = NavRoute.QUANLYCHUYENMAY.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(200)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(200)
                )
            }
        ) {
            QuanLyChuyenMayScreen(navController)
        }

        composable(
            route = NavRoute.LICHSUCHUYENMAY.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(200)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(200)
                )
            }
        ) {
            LichSuChuyenMayScreen(navController,donNhapViewModel,chiTietDonNhapyViewModel,mayTinhViewModel)
        }

        composable(
            NavRoute.CHITIETLICHSUCHUYENMAY.route + "?mamay={mamay}",
            arguments = listOf(
                navArgument("mamay") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) }
        ) { navBackStackEntry ->
            val mamay = navBackStackEntry.arguments?.getString("mamay") ?: ""
            ChiTietLichSuChuyenMay(mamay,lichSuChuyenMayViewModel,phongMayViewModel)
        }



        composable(
            route = NavRoute.QUETQRCODE.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    animationSpec = tween(200)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    animationSpec = tween(200)
                )
            }
        ) {
            QRCodeScannerScreen(
                onResult = { qrCodeValue ->
                    navController.navigate(NavRoute.MAYTINHDETAIL.route + "?mamay=${qrCodeValue}")
                }
            )
        }

        //Sửa chữa

        composable(
            NavRoute.ADDPHIEUSUACHUA.route + "?mamay={mamay}",
            arguments = listOf(
                navArgument("mamay") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) }
        ) { navBackStackEntry ->
            val mamay = navBackStackEntry.arguments?.getString("mamay") ?: ""
            CreatePhieuSuaChuaScreen(mamay,phieuSuaChuaViewModel,giangVienViewModel,sinhVienViewModel,navController)
        }

        composable(
            route = NavRoute.QUANLYPHIEUSUACHUA.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            }
        ) {
            QuanLyPhieuSuaChuaScreen(navController)
        }

        composable(
            route = NavRoute.LISTPHIEUCHUASUA.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            }
        ) {
            ListPhieuChuaSua(phieuSuaChuaViewModel,mayTinhViewModel,lichSuSuaMayViewModel)
        }

        composable(
            route = NavRoute.LISTPHIEUDASUA.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            }
        ) {
            ListPhieuDaSua(phieuSuaChuaViewModel,mayTinhViewModel,lichSuSuaMayViewModel)
        }

        composable(
            route = NavRoute.LICHSUSUAMAY.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            }
        ) {
            LichSuSuaMayScreen(navController,phongMayViewModel,mayTinhViewModel,donNhapViewModel,chiTietDonNhapyViewModel)
        }

        composable(
            NavRoute.LISTMAYTINHLICHSUSUAMAY.route + "?madonnhap={madonnhap}",
            arguments = listOf(
                navArgument("madonnhap") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) }
        ) { navBackStackEntry ->
            val madonnhap = navBackStackEntry.arguments?.getString("madonnhap") ?: ""
            ListMayTinhLichSuSuaMayScreen(madonnhap,navController,chiTietDonNhapyViewModel,mayTinhViewModel,phongMayViewModel,lichSuSuaMayViewModel)
        }

        composable(
            NavRoute.DETAILLICHSUSUAMAY.route + "?mamay={mamay}",
            arguments = listOf(
                navArgument("mamay") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) }
        ) { navBackStackEntry ->
            val mamay = navBackStackEntry.arguments?.getString("mamay") ?: ""
            ChiTietLichSuSuaMayScreen(mamay,phieuSuaChuaViewModel,lichSuSuaMayViewModel)
        }

        //Năm Học
        composable(
            route = NavRoute.QUANLYNAMHOC.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            }
        ) {
            QuanLyNamHocScreen(navController,namHocViewModel)
        }

        composable(
            route = NavRoute.ADDNAMHOC.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            }
        ) {
            CreateNamHocScreen(navController,namHocViewModel)
        }

        composable(
            NavRoute.NAMHOCDETAIL.route + "?manam={manam}",
            arguments = listOf(
                navArgument("manam") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) }
        ) { navBackStackEntry ->
            val manam = navBackStackEntry.arguments?.getString("manam") ?: ""
            NamHocDetailScreen(manam,tuanViewModel)
        }

        //DiemDanh

        composable(
            NavRoute.ADDDIEMDANH.route + "?mamay={mamay}",
            arguments = listOf(
                navArgument("mamay") { type = NavType.StringType; nullable = true }
            ),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) }
        ) { navBackStackEntry ->
            val mamay = navBackStackEntry.arguments?.getString("mamay") ?: ""
            DiemDanhScreen(mamay,namHocViewModel,tuanViewModel,chiTietSuDungMayViewModel)
        }

        //Mượn Máy
        composable(
            route = NavRoute.QUANLYPHIEUMUONMAY.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            }
        ) {
            QuanLyPhieuMuonMayScreen(navController)
        }

        composable(
            route = NavRoute.ADDPHIEUMUONMAY.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            }
        ) {
            CreatePhieuMuonMayScreen(phongMayViewModel,phieuMuonMayViewModel)
        }

        composable(
            route = NavRoute.LISTPHIEUMUONMAYCHUATRA.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            }
        ) {
            ListPhieuMuonChuaTra(navController,phieuMuonMayViewModel)
        }

        composable(
            route = NavRoute.LISTPHIEUMUONMAYCHUACHUYEN.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            }
        ) {
            ListPhieuMuonChuaChuyen(navController,phieuMuonMayViewModel)
        }

        composable(
            route = NavRoute.CHUYENMAYPHIEUMUON.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(300)
                )
            }
        ) {
            ChuyenMayPhieuMuonScreen(navController,mayTinhViewModel,phongMayViewModel)
        }
    }
}
