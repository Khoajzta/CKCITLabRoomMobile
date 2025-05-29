import android.transition.Transition
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.*
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.lapstore.viewmodels.LichHocViewModel
import com.example.lapstore.viewmodels.MayTinhViewModel
import com.google.ai.client.generativeai.common.server.Segment


sealed class NavRoute(val route: String) {
    object HOME : NavRoute("home_screen")
    object ACCOUNT : NavRoute("account_screen")
    object LOGINGIANGVIEN : NavRoute("logingv_screen")
    object LOGINSINHVIEN : NavRoute("loginsv_screen")
    object QUANLY : NavRoute("quanly_screen")

    object QUANLYCAUHINH : NavRoute("quanlycauhinh_screen")
    object ADDCAUHINH : NavRoute("addcauhinh_screen")
    object EDITCAUHINH : NavRoute("editcauhinh_screen")

    object QUANLYMAYTINH : NavRoute("quanlymaytinh_screen")
    object EDITMAYTINH : NavRoute("editmaytinh_screen")
    object ADDTMAYTINH : NavRoute("addmaytinh_screen")

    object QUANLYPHONGMAY : NavRoute("quanlyphongmay_screen")

    object QUANLYGIANGVIEN : NavRoute("quanlygiangvien_screen")
    object ADDGIANGVIEN : NavRoute("addgiangvien_screen")
    object EDITGIANGVIEN : NavRoute("editgiangvien_screen")
}

@Composable
fun NavgationGraph(
    navController: NavHostController,
    lichHocViewModel: LichHocViewModel,
    giangVienViewModel: GiangVienViewModel,
    mayTinhViewModel: MayTinhViewModel,
    phongMayViewModel: PhongMayViewModel
) {

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    NavHost(navController = navController, startDestination = NavRoute.HOME.route,

    ) {
        composable(
            NavRoute.HOME.route,
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
        ) { navBackStackEntry ->
            val giangvien = navBackStackEntry.savedStateHandle.get<GiangVien>("giangvien")
            HomeScreen(lichHocViewModel, giangvien)
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
            QuanLyScreen(navController)
        }

        composable(
            route = NavRoute.ACCOUNT.route,
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
            AccountScreen()
        }

        composable(
            route = NavRoute.QUANLYCAUHINH.route,
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
            QuanLyCauHinh(navController)
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
            QuanLyMayTinh(navController,mayTinhViewModel)
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
            route = NavRoute.QUANLYGIANGVIEN.route,
        ) {
            QuanLyGiangVien(navController)
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
                val direction = if (currentRoute == NavRoute.LOGINGIANGVIEN.route)
                    AnimatedContentTransitionScope.SlideDirection.Start
                else
                    AnimatedContentTransitionScope.SlideDirection.End

                slideIntoContainer(
                    direction,
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                val direction = if (currentRoute == NavRoute.LOGINGIANGVIEN.route)
                    AnimatedContentTransitionScope.SlideDirection.Start
                else
                    AnimatedContentTransitionScope.SlideDirection.End

                slideOutOfContainer(
                    direction,
                    animationSpec = tween(300)
                )
            }

        ) {
            LoginSVScreen(navController)
        }

        composable(
            route = NavRoute.QUANLYGIANGVIEN.route,
        ) {
            QuanLyGiangVien(navController)
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
            CreateGiangVienScreen()
        }

        composable(
            route = NavRoute.EDITGIANGVIEN.route,
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
//            EditGiangVienScreen()
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
            CreateGiangVienScreen()
        }

        composable(
            route = NavRoute.EDITGIANGVIEN.route,
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
//            EditGiangVienScreen()
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
            QuanLyPhongMay(navController,phongMayViewModel)
        }
    }
}
