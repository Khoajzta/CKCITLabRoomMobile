package com.example.ckcitlabroom

import AnimatedNavigationBar
import ButtonData
import GiangVienViewModel
import LichSuSuaMayViewModel
import MonHocViewModel
import NamHocViewModel
import NavRoute
import NavgationGraph
import NotificationViewModel
import PhieuMuonMayViewModel
import PhieuSuaChuaViewModel
import PhongMayViewModel
import RequestPermissionsOnFirstLaunch
import SinhVienViewModel
import TuanViewModel
import UpdateLichHocWorker
import android.content.Context
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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.composables.icons.lucide.Bell
import com.composables.icons.lucide.House
import com.composables.icons.lucide.LayoutGrid
import com.example.ckcitlabroom.ui.theme.CKCITLabRoomTheme
import com.example.ckcitlabroom.viewmodels.CaHocViewModel

import com.example.ckcitlabroom.viewmodels.LopHocViewModel
import com.example.lapstore.viewmodels.ChiTietDonNhapyViewModel
import com.example.lapstore.viewmodels.ChiTietPhieuMuonViewModel


import com.example.lapstore.viewmodels.ChiTietSuDungMayViewModel
import com.example.lapstore.viewmodels.DonNhapViewModel

import com.example.lapstore.viewmodels.LichHocViewModel
import com.example.lapstore.viewmodels.LichSuChuyenMayViewModel
import com.example.lapstore.viewmodels.MayTinhViewModel
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.ScanLine
import com.composables.icons.lucide.User
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CKCITLabRoomTheme {
                MainScreen()
                scheduleUpdateLichHocWorker(applicationContext)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    RequestPermissionsOnFirstLaunch()

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
    val phieuSuaChuaViewModel: PhieuSuaChuaViewModel = viewModel()
    val chiTietSuDungMayViewModel: ChiTietSuDungMayViewModel = viewModel()
    val lichSuSuaMayViewModel: LichSuSuaMayViewModel = viewModel()
    val phieuMuonMayViewModel: PhieuMuonMayViewModel = viewModel()
    val chiTietPhieuMuonViewModel: ChiTietPhieuMuonViewModel = viewModel()
    val caHocViewModel: CaHocViewModel = viewModel()
    val monHocViewModel: MonHocViewModel = viewModel()
    val notificationViewModel: NotificationViewModel = viewModel()

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val giangVien = giangVienViewModel.giangvienSet
    val sinhVien = sinhVienViewModel.sinhvienSet

    val buttons = listOf(
        ButtonData("Home", Lucide.House) {
            navController.navigate(NavRoute.HOME.route) {
                popUpTo(0) { inclusive = true }
            }
        },
        ButtonData("Quản Lý", Lucide.LayoutGrid) {
            navController.navigate(NavRoute.QUANLY.route) {
                popUpTo(0) { inclusive = true }
            }
        },
        ButtonData("Quét Mã", Lucide.ScanLine) {
            navController.navigate(NavRoute.QUETQRCODE.route) {
                popUpTo(0) { inclusive = true }
            }
        },
        ButtonData("Thông Tin", Lucide.User) {
            navController.navigate(NavRoute.ACCOUNT.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    )

    // AppBar logic dùng rõ ràng theo route thay vì index
    @Composable
    fun TopBar(
        navController: NavController,
        mayTinhViewModel: MayTinhViewModel,
        currentRoute: String?
    ) {
        when (currentRoute) {
            NavRoute.LOGINSINHVIEN.route,
            NavRoute.LOGINGIANGVIEN.route -> {}

            NavRoute.CHUYENMAYPHIEUMUON.route -> {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    title = {
                        Text("Trở lại", fontWeight = FontWeight.Bold, color = Color(0xFF1B8DDE))
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                mayTinhViewModel.clearDanhSachMayTinhDuocChon()
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Back",
                                tint = Color(0xFF1B8DDE)
                            )
                        }
                    }
                )
            }

            NavRoute.HOME.route,
            NavRoute.QUANLY.route,
            NavRoute.ACCOUNT.route -> {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "Logo",
                                modifier = Modifier.size(40.dp).clip(CircleShape)
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
            }

            else -> {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    title = {
                        Text("Trở lại", fontWeight = FontWeight.Bold, color = Color(0xFF1B8DDE))
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Back",
                                tint = Color(0xFF1B8DDE)
                            )
                        }
                    }
                )
            }
        }
    }



    Scaffold(
        topBar = { TopBar(navController,mayTinhViewModel,currentRoute) },
        bottomBar = {
            when (currentRoute) {
                NavRoute.LOGINSINHVIEN.route,
                NavRoute.LOGINGIANGVIEN.route -> {}
                else -> {
                    AnimatedNavigationBar(
                        buttons = buttons,
                        barColor = Color.White,
                        circleColor = Color.White,
                        selectedColor = Color.Black,
                        unselectedColor = Color.Black,
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
                        colors = listOf(Color.White, Color(0xFF1B8DDE)),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(12.dp),
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
                    tuanViewModel,
                    phieuSuaChuaViewModel,
                    chiTietSuDungMayViewModel,
                    lichSuSuaMayViewModel,
                    phieuMuonMayViewModel,
                    chiTietPhieuMuonViewModel,
                    caHocViewModel,
                    monHocViewModel,
                    notificationViewModel
                )
            }
        }
    }
}


fun scheduleUpdateLichHocWorker(context: Context) {
    // Chạy ngay một lần
    val immediateWork = OneTimeWorkRequestBuilder<UpdateLichHocWorker>().build()
    WorkManager.getInstance(context).enqueue(immediateWork)

    // Sau đó lặp lại mỗi 15 phút
    val periodicWork = PeriodicWorkRequestBuilder<UpdateLichHocWorker>(
        15, TimeUnit.MINUTES
    ).setConstraints(
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
    ).build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "updateLichHocWorker",
        ExistingPeriodicWorkPolicy.KEEP,
        periodicWork
    )
}






