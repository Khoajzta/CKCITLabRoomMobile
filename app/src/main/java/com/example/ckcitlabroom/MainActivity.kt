package com.example.ckcitlabroom

import AnimatedNavigationBar
import ButtonData
import DotLoadingOverlay
import GiangVienViewModel
import LoginSVScreen
import NavRoute
import NavgationGraph
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ckcitlabroom.ui.theme.CKCITLabRoomTheme
import com.example.lapstore.viewmodels.LichHocViewModel
import com.example.lapstore.viewmodels.MayTinhViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CKCITLabRoomTheme {
                MainScreen()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val lichHocViewModel: LichHocViewModel = viewModel()
    val giangVienViewModel: GiangVienViewModel = viewModel()
    val mayTinhViewModel: MayTinhViewModel = viewModel()

    var isLoading by remember { mutableStateOf(false) }

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // Danh sách các button cho Bottom Navigation Bar
    val buttons = listOf(
        ButtonData("Home", Icons.Default.Home, click = {
            isLoading = true
            CoroutineScope(Dispatchers.Main).launch {
                delay(500)
                isLoading = false
                navController.navigate(NavRoute.HOME.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }),
        ButtonData("Quản Lý", Icons.Default.Apps, click = {
            isLoading = true
            CoroutineScope(Dispatchers.Main).launch {
                delay(500)
                isLoading = false
                navController.navigate(NavRoute.QUANLY.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }),
        ButtonData("Quét Mã", Icons.Default.QrCodeScanner, click = {

        }),
        ButtonData("Thông Báo", Icons.Default.Notifications, click = {

        }),
        ButtonData("Thông Tin", Icons.Default.AccountCircle, click = {
//            navController.navigate(NavRoute.ACCOUNT.route){
//                popUpTo(0) { inclusive = true }
//            }

            navController.navigate(NavRoute.ACCOUNT.route) {
//                popUpTo(0) { inclusive = true }
            }

        }),
    )

    val topAppBars = listOf<@Composable () -> Unit>({
        TopAppBar(colors = TopAppBarDefaults.topAppBarColors(Color(0XFF1B8DDE)),
            title = { Text("Trở lại", fontWeight = FontWeight.Bold, color = Color.White) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            })
    }, {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0XFF1B8DDE)),
            title = {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        "IT LabRoom",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 25.sp,
                        color = Color.White
                    )
                }
            },
        )
    }, {

    })

    Scaffold(topBar = {
        when (currentRoute) {
            NavRoute.LOGINSINHVIEN.route,
            NavRoute.LOGINGIANGVIEN.route -> {}

            NavRoute.HOME.route,
            NavRoute.QUANLY.route,
            NavRoute.ACCOUNT.route -> topAppBars[1]()

            else -> topAppBars[0]()
        }

    }, bottomBar = {
        when (currentRoute) {
            NavRoute.LOGINSINHVIEN.route, NavRoute.LOGINGIANGVIEN.route -> {
            }

            else -> {
                AnimatedNavigationBar(
                    buttons = buttons,
                    barColor = Color.White,
                    circleColor = Color.White,
                    selectedColor = Color(0xff1B8DDE),
                    unselectedColor = Color.Gray,
                    currentRoute = currentRoute
                )
            }
        }

    }, containerColor = Color(0xff1B8DDE)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NavgationGraph(
                    navController,
                    lichHocViewModel,
                    giangVienViewModel,
                    mayTinhViewModel
                )
            }

            // Hiển thị overlay loading khi isLoading = true
//             if (isLoading) {
//                 DotLoadingOverlay()
//             }
        }
    }
}


