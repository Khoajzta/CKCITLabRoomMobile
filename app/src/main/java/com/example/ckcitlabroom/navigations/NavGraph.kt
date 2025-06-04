import android.transition.Transition
import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.*
import androidx.compose.animation.core.spring
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
import com.example.ckcitlabroom.viewmodels.LopHocViewModel
import com.example.lapstore.viewmodels.ChiTietDonNhapyViewModel
import com.example.lapstore.viewmodels.DonNhapyViewModel
import com.example.lapstore.viewmodels.LichHocViewModel
import com.example.lapstore.viewmodels.LichSuChuyenMayViewModel
import com.example.lapstore.viewmodels.MayTinhViewModel
import com.google.ai.client.generativeai.common.server.Segment


sealed class NavRoute(val route: String) {
    object HOME : NavRoute("home_screen")
    object ACCOUNT : NavRoute("account_screen")
    object LOGINGIANGVIEN : NavRoute("logingv_screen")
    object LOGINSINHVIEN : NavRoute("loginsv_screen")
    object QUANLY : NavRoute("quanly_screen")

    object QUANLYDONNHAP : NavRoute("quanlydonnhap_screen")
    object ADDDONNHAP : NavRoute("adddonnhap_screen")
    object CHITIETDONNHAP : NavRoute("chitietdonnhap_screen")


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

    object QUANLYGIANGVIEN : NavRoute("quanlygiangvien_screen")
    object ADDGIANGVIEN : NavRoute("addgiangvien_screen")
    object EDITGIANGVIEN : NavRoute("editgiangvien_screen")


    object QUANLYCHUYENMAY : NavRoute("quanlychuyenmay_screen")
    object LICHSUCHUYENMAY : NavRoute("lichsuchuyenmay_screen")
    object CHITIETLICHSUCHUYENMAY : NavRoute("chitietlichsuchuyenmay_screen")


    object QUETQRCODE : NavRoute("quetqrcode_screen")

    object QUANLYSINHVIEN : NavRoute("quanlysinhvien_screen")
    object ADDSINHVIEN : NavRoute("addsinhvien_screen")
    object EDITSINHVIEN : NavRoute("editsinhvien_screen")


    object QUANLYLOPHOC : NavRoute("quanlylophoc_screen")
    object ADDLOPHOC : NavRoute("addlophoc_screen")
    object EDITLOPHOC : NavRoute("editlophoc_screen")


    object ADDPHIEUSUACHUA : NavRoute("addphieusuachua_screen")
    object STARTSCREEN : NavRoute("start_screen")

}

@Composable
fun NavgationGraph(
    navController: NavHostController,
    lichHocViewModel: LichHocViewModel,
    giangVienViewModel: GiangVienViewModel,
    mayTinhViewModel: MayTinhViewModel,
    phongMayViewModel: PhongMayViewModel,
    sinhVienViewModel: SinhVienViewModel,
    lopHocViewModel: LopHocViewModel

    lichSuChuyenMayViewModel: LichSuChuyenMayViewModel,
    donNhapyViewModel: DonNhapyViewModel,
    chiTietDonNhapyViewModel: ChiTietDonNhapyViewModel,
    sinhVienViewModel: SinhVienViewModel,

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
            QuanLyDonNhap(navController,donNhapyViewModel)
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
            CreateDonNhapScreen(navController,mayTinhViewModel,phongMayViewModel,donNhapyViewModel,chiTietDonNhapyViewModel)
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
            MayTinhDetailScreen(mamay,phongMayViewModel,navController)
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
            CreateSinhVienScreen(navController, sinhVienViewModel)
        }

        composable(
            route = NavRoute.EDITSINHVIEN.route,
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
//            EditSinhVienScreen()
        }

        composable(
            route = NavRoute.QUANLYLOPHOC.route,
        ) {
            QuanLyLopHoc(navController, lopHocViewModel)
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
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) }
        ) { navBackStackEntry ->
            val maphong = navBackStackEntry.arguments?.getString("maphong") ?: ""
            PhongMayDonNhapScreen(maphong,navController,phongMayViewModel,mayTinhViewModel,donNhapyViewModel)
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
            QuanLyChuyenMayScreen(navController,mayTinhViewModel,phongMayViewModel)
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
            LichSuChuyenMayScreen(navController,mayTinhViewModel,phongMayViewModel)
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
            ChiTietLichSuChuyenMay(mamay,lichSuChuyenMayViewModel,phongMayViewModel,mayTinhViewModel)
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

        composable(
            route = NavRoute.ADDPHIEUSUACHUA.route,
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
            CreatePhieuSuaChua()
        }

    }
}
