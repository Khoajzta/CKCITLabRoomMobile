package com.example.ckcitlabroom

import AnimatedNavigationBar
import ButtonData
import GiangVienViewModel
import LoginSVScreen
import LoginSinhVienState
import NamHocViewModel
import NavRoute
import NavgationGraph
import PhongMayViewModel
import SinhVienViewModel
import SinhVienPreferences
import TuanViewModel
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ckcitlabroom.ui.theme.CKCITLabRoomTheme

import com.example.ckcitlabroom.viewmodels.LopHocViewModel

import com.example.lapstore.viewmodels.ChiTietDonNhapyViewModel
import com.example.lapstore.viewmodels.DonNhapViewModel

import com.example.lapstore.viewmodels.LichHocViewModel
import com.example.lapstore.viewmodels.LichSuChuyenMayViewModel
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
    val phongMayViewModel: PhongMayViewModel = viewModel()
    val lichSuChuyenMayViewModel: LichSuChuyenMayViewModel = viewModel()
    val donNhapyViewModel: DonNhapViewModel = viewModel()
    val chitietdonNhapyViewModel: ChiTietDonNhapyViewModel = viewModel()
    val sinhVienViewModel: SinhVienViewModel = viewModel()
    val lopHocViewModel: LopHocViewModel = viewModel()
    val namHocViewModel: NamHocViewModel = viewModel()
    val tuanViewModel: TuanViewModel = viewModel()

    var isLoading by remember { mutableStateOf(false) }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val giangVien = giangVienViewModel.giangvienSet
    val sinhVien = sinhVienViewModel.sinhvienSet

    val buttons = listOf(
        ButtonData("Home", Icons.Default.Home) {
            isLoading = true
            CoroutineScope(Dispatchers.Main).launch {
                delay(500)
                isLoading = false
                navController.navigate(NavRoute.HOME.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        },
        ButtonData("Quản Lý", Icons.Default.Apps) {
            isLoading = true
            CoroutineScope(Dispatchers.Main).launch {
                delay(500)
                isLoading = false
                navController.navigate(NavRoute.QUANLY.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        },
        ButtonData("Quét Mã", Icons.Default.QrCodeScanner) {
            navController.navigate(NavRoute.QUETQRCODE.route)
        },
        ButtonData("Thông Báo", Icons.Default.Notifications) {},
        ButtonData("Thông Tin", Icons.Default.AccountCircle) {
            if (giangVien != null || sinhVien != null) {
                navController.navigate(NavRoute.ACCOUNT.route){
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    )

    val topAppBars = listOf<@Composable () -> Unit>(
        {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                title = {
                    Text("Trở lại", fontWeight = FontWeight.Bold, color = Color(0xFF1B8DDE))
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = Color(0xFF1B8DDE)
                        )
                    }
                }
            )
        },
        {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                title = {
                    Row(
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
                            text = "IT LabRoom",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 25.sp,
                            color = Color(0xFF1B8DDE)
                        )
                    }
                }
            )
        },
        {}
    )

    Scaffold(
        topBar = {
            when (currentRoute) {
                NavRoute.LOGINSINHVIEN.route,
                NavRoute.LOGINGIANGVIEN.route -> {}

                NavRoute.HOME.route,
                NavRoute.QUANLY.route,
                NavRoute.ACCOUNT.route -> topAppBars[1]()

                else -> topAppBars[0]()
            }
        },
        bottomBar = {
            when (currentRoute) {
                NavRoute.LOGINSINHVIEN.route,
                NavRoute.LOGINGIANGVIEN.route -> {}
                else -> {
                    AnimatedNavigationBar(
                        buttons = buttons,
                        barColor = Color.White,
                        circleColor = Color.White,
                        selectedColor = Color(0xFF1B8DDE),
                        unselectedColor = Color.Gray,
                        currentRoute = currentRoute
                    )
                }
            }
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White,
                            Color(0xFF1B8DDE)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
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
                    mayTinhViewModel,
                    phongMayViewModel,
                    lopHocViewModel,
                    lichSuChuyenMayViewModel,
                    donNhapyViewModel,
                    chitietdonNhapyViewModel,
                    sinhVienViewModel,
                    namHocViewModel,
                    tuanViewModel
                )
            }
        }
    }
}




