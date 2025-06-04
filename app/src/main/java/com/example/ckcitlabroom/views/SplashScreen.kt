import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

@Composable
fun StartupCheckScreen(
    navController: NavHostController,
    sinhVienViewModel: SinhVienViewModel,
    giangVienViewModel: GiangVienViewModel
) {
    val context = LocalContext.current

    val sinhVienPreferences = remember(context) { SinhVienPreferences(context) }
    val giangVienPreferences = remember(context) { GiangVienPreferences(context) }

    val loginSinhVienState by sinhVienPreferences.loginStateFlow.collectAsState(initial = LoginSinhVienState())
    val loginGiangVienState by giangVienPreferences.loginStateFlow.collectAsState(initial = LoginGiangVienState())

    var isNavigated by remember { mutableStateOf(false) }

    // 1. Show loading circle
    if (!isNavigated) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    // 2. Navigate only ONCE
    LaunchedEffect(loginSinhVienState, loginGiangVienState) {
        if (isNavigated) return@LaunchedEffect

        // Cho phép DataStore kịp emit dữ liệu
        delay(200)

        when {
            loginGiangVienState.isLoggedIn -> {
                isNavigated = true
                navController.navigate(NavRoute.LOGINGIANGVIEN.route) {
                    popUpTo(NavRoute.STARTSCREEN.route) { inclusive = true }
                }
            }
            loginSinhVienState.isLoggedIn -> {
                isNavigated = true
                navController.navigate(NavRoute.LOGINSINHVIEN.route) {
                    popUpTo(NavRoute.STARTSCREEN.route) { inclusive = true }
                }
            }
            else -> {
                isNavigated = true
                navController.navigate(NavRoute.LOGINSINHVIEN.route) {
                    popUpTo(NavRoute.STARTSCREEN.route) { inclusive = true }
                }
            }
        }
    }
}

















