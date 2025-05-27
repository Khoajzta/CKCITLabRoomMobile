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
import androidx.navigation.navArgument
import com.google.ai.client.generativeai.common.server.Segment


sealed class NavRoute(val route: String) {
    object HOME : NavRoute("home_screen")
    object ACCOUNT : NavRoute("account_screen")
    object LOGIN : NavRoute("login_screen")
    object QUANLY : NavRoute("quanly_screen")
    object QUANLYCAUHINH : NavRoute("quanlycauhinh_screen")
    object QUANLYMAYTINH : NavRoute("quanlymaytinh_screen")
    object ADDCAUHINH : NavRoute("addcauhinh_screen")
    object EDITCAUHINH : NavRoute("editcauhinh_screen")
}

@Composable
fun NavgationGraph(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = NavRoute.HOME.route,

    ) {
        composable(NavRoute.HOME.route,
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
            }) {
            HomeScreen()
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
            QuanLyMayTinh(navController)
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
    }
}
