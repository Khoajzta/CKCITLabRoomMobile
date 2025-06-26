import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.R
import kotlinx.coroutines.delay

@Composable
fun StartupCheckScreen(
    navController: NavHostController,
    sinhVienViewModel: SinhVienViewModel,
    giangVienViewModel: GiangVienViewModel
) {
    val context = LocalContext.current
    var isConnected by remember { mutableStateOf(isInternetAvailable(context)) }
    var isNavigated by remember { mutableStateOf(false) }

    // Theo dõi mạng
    NetworkStatusHandler(
        onAvailable = {
            isConnected = true
        },
        onLost = {
            isConnected = false
            Toast.makeText(context, "Mất kết nối Internet", Toast.LENGTH_LONG).show()
        }
    )

    val sinhVienPreferences = remember(context) { SinhVienPreferences(context) }
    val giangVienPreferences = remember(context) { GiangVienPreferences(context) }

    val loginSinhVienState by sinhVienPreferences.loginStateFlow.collectAsState(initial = LoginSinhVienState())
    val loginGiangVienState by giangVienPreferences.loginStateFlow.collectAsState(initial = LoginGiangVienState())

    // Hiệu ứng splash
    val infiniteTransition = rememberInfiniteTransition(label = "startup_transition")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(tween(1000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "scale"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1200), RepeatMode.Reverse),
        label = "alpha"
    )

    if (!isNavigated) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!isConnected) NoInternetBanner()

            Spacer(modifier = Modifier.height(100.dp))
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .padding(top = 100.dp)
                    .size(160.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    }
                    .clip(CircleShape)
                    .shadow(8.dp, shape = CircleShape)
            )
            Spacer(modifier = Modifier.height(32.dp))
            DotLoadingLight()
        }
    }


    LaunchedEffect(isConnected, loginGiangVienState, loginSinhVienState) {
        if (!isConnected || isNavigated) return@LaunchedEffect
        delay(500)

        when {
            loginGiangVienState.maGiangVien != null -> {
                val gv = giangVienViewModel.getGiangVienByMaGOrEmailNow(loginGiangVienState.maGiangVien!!)
                if (gv != null && gv.Token == loginGiangVienState.token) {
                    giangVienViewModel.setGV(gv)
                    isNavigated = true
                    navController.navigate(NavRoute.HOME.route) {
                        popUpTo(NavRoute.STARTSCREEN.route) { inclusive = true }
                    }
                } else {
                    Toast.makeText(context, "Tài khoản đã đăng nhập trên thiết bị khác", Toast.LENGTH_SHORT).show()
                    giangVienPreferences.logout()
                    isNavigated = true
                    navController.navigate(NavRoute.LOGINSINHVIEN.route) {
                        popUpTo(NavRoute.STARTSCREEN.route) { inclusive = true }
                    }

                }
            }

            loginSinhVienState.maSinhVien != null -> {
                val sv = sinhVienViewModel.getSinhVienByMaGOrEmailNow(loginSinhVienState.maSinhVien!!)
                if (sv != null && sv.Token == loginSinhVienState.token) {
                    sinhVienViewModel.setSV(sv)
                    isNavigated = true
                    navController.navigate(NavRoute.HOME.route) {
                        popUpTo(NavRoute.STARTSCREEN.route) { inclusive = true }
                    }
                } else {
                    Toast.makeText(context, "Tài khoản đã đăng nhập trên thiết bị khác", Toast.LENGTH_SHORT).show()
                    sinhVienPreferences.logout()
                    isNavigated = true
                    navController.navigate(NavRoute.LOGINSINHVIEN.route) {
                        popUpTo(NavRoute.STARTSCREEN.route) { inclusive = true }
                    }
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






















