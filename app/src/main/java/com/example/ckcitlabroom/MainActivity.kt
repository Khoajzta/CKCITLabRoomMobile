package com.example.ckcitlabroom

import AnimatedNavigationBar
import ButtonData
import GiangVien
import GiangVienPreferences
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
import SinhVien
import SinhVienPreferences
import SinhVienViewModel
import TuanViewModel
import UpdateLichHocWorker
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
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
import com.composables.icons.lucide.BellDot
import com.composables.icons.lucide.House
import com.composables.icons.lucide.LayoutGrid
import com.example.ckcitlabroom.ui.theme.CKCITLabRoomTheme
import com.example.ckcitlabroom.viewmodels.CaHocViewModel
import com.example.ckcitlabroom.viewmodels.LopHocViewModel
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.ScanLine
import com.composables.icons.lucide.User
import com.example.ckcitlabroom.viewmodels.ChiTietDonNhapyViewModel
import com.example.ckcitlabroom.viewmodels.ChiTietPhieuMuonViewModel
import com.example.ckcitlabroom.viewmodels.ChiTietSuDungMayViewModel
import com.example.ckcitlabroom.viewmodels.DonNhapViewModel
import com.example.ckcitlabroom.viewmodels.LichHocViewModel
import com.example.ckcitlabroom.viewmodels.LichSuChuyenMayViewModel
import com.example.ckcitlabroom.viewmodels.MayTinhViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit
import androidx.compose.ui.unit.LayoutDirection


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

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = true

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons
        )

        systemUiController.setNavigationBarColor(
            color = Color.Transparent,
            darkIcons = false
        )
    }

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
    val context = LocalContext.current

    var gv = giangVienViewModel.giangvienSet
    var sv = sinhVienViewModel.sinhvienSet

    var SinhVienPreferences = remember { SinhVienPreferences(context) }
    var GiangVienPreferences = remember { GiangVienPreferences(context) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(15_000)

            try {
                val localSV = SinhVienPreferences.getSinhVienFromDataStore()
                val localGV = GiangVienPreferences.getGiangVienFromDataStore()

                val currentToken = FirebaseMessaging.getInstance().token.await()

                if (localSV != null) {
                    // Gọi từ API, không dùng state cũ trong ViewModel
                    val svServer = sinhVienViewModel.getSinhVienByMaGOrEmailNow(localSV.MaSinhVien)

                    if (svServer != null && svServer.Token != currentToken) {
                        Toast.makeText(context, "Tài khoản đã đăng nhập trên thiết bị khác", Toast.LENGTH_SHORT).show()
                        sinhVienViewModel.logout()
                        SinhVienPreferences.logout()
                        navController.navigate(NavRoute.LOGINSINHVIEN.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }

                if (localGV != null) {
                    val gvServer = giangVienViewModel.getGiangVienByMaGOrEmailNow(localGV.MaGV)

                    if (gvServer != null && gvServer.Token != currentToken) {
                        Toast.makeText(context, "Tài khoản đã đăng nhập trên thiết bị khác", Toast.LENGTH_SHORT).show()
                        giangVienViewModel.logout()
                        GiangVienPreferences.logout()
                        navController.navigate(NavRoute.LOGINSINHVIEN.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e("AutoLogout", "Lỗi kiểm tra thiết bị khác: ${e.message}")
            }
        }
    }

    val unreadCount = notificationViewModel.danhSachAllThongBao.count { tb ->
        !tb.DaDoc && (
            (sv != null && tb.MaNguoiDung == sv.MaSinhVien) ||
            (gv != null && tb.MaNguoiDung == gv.MaGV)
        )
    }


    val buttons = listOf(
        ButtonData("Home", Lucide.House) {
            navController.navigate(NavRoute.HOME.route) {
                popUpTo(0) { inclusive = true }
            }
        },
        ButtonData("Chức Năng", Lucide.LayoutGrid) {
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

    LaunchedEffect(Unit) {
        notificationViewModel.getAllThongBao()
    }

    @Composable
    fun TopBar(
        navController: NavController,
        mayTinhViewModel: MayTinhViewModel,
        currentRoute: String?
    ) {
        var textTopbar = "Trở lại"

        when (currentRoute){
            NavRoute.QUANLYDONNHAP.route -> {
                textTopbar = "Quản Lý Đơn Nhập"
            }
            NavRoute.QUANLYPHONGMAY.route -> {
                textTopbar = "Quản Lý Phòng Máy"
            }
            NavRoute.QUANLYCHUYENMAY.route -> {
                textTopbar = "Quản Lý Chuyển Máy"
            }
            NavRoute.QUANLYPHIEUSUACHUA.route -> {
                textTopbar = "Quản Lý Phiếu Sửa Chữa"
            }
            NavRoute.QUANLYPHIEUMUONMAY.route -> {
                textTopbar = "Quản Lý Phiếu Mượn Máy"
            }
            NavRoute.QUANLYGIANGVIEN.route -> {
                textTopbar = "Quản Lý Giảng Viên"
            }
            NavRoute.QUANLYSINHVIEN.route -> {
                textTopbar = "Quản Lý Sinh Viên"
            }
            NavRoute.LISTLICHHOCSUDUNGMAY.route -> {
                textTopbar = "Quản Lý Điểm Danh"
            }
            NavRoute.QUANLYLICHHOC.route -> {
                if(gv!=null){
                    textTopbar = "Quản Lý Lịch Dạy"
                }else{
                    textTopbar = "Quản Lý Lịch Học"
                }
            }
            NavRoute.QUANLYLOPHOC.route -> {
                textTopbar = "Quản Lý Lớp Học"
            }
            NavRoute.QUANLYNAMHOC.route -> {
                textTopbar = "Quản Lý Năm Học"
            }
            NavRoute.QUANLYCAHOC.route -> {
                textTopbar = "Quản Lý Ca Học"
            }
            NavRoute.QUANLYMONHOC.route -> {
                textTopbar = "Quản Lý Môn Học"
            }
            NavRoute.ListThongBaoSinhVien.route -> {
                textTopbar = "Thông Báo"
            }
        }

        when (currentRoute) {
            NavRoute.STARTSCREEN.route,
            NavRoute.LOGINSINHVIEN.route,
            NavRoute.LOGINGIANGVIEN.route -> {}

            NavRoute.CHUYENMAYPHIEUMUON.route -> {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    title = {
                        Text("Trở lại",
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1B8DDE),
                            modifier = Modifier.padding(start = 10.dp),
                            fontSize = 21.sp,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.25f),
                                    offset = Offset(2f, 2f),
                                    blurRadius = 4f
                                )
                            )
                        )
                    },
                    navigationIcon = {
                        Box(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .size(40.dp)
                                .shadow(4.dp, CircleShape, clip = false)
                                .background(Color.White, CircleShape)
                                .border(1.dp, Color.White, CircleShape)
                                .clickable {
                                    mayTinhViewModel.clearDanhSachMayTinhDuocChon()
                                    navController.popBackStack()
                                 },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Back",
                                tint = Color(0xFF1B8DDE),
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    }
                )
            }

            NavRoute.HOME.route,
            NavRoute.QUETQRCODE.route,
            NavRoute.QUANLY.route,
            NavRoute.ACCOUNT.route -> {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .size(50.dp)
                                    .shadow(8.dp, shape = CircleShape)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.6f))
                                    .border(1.dp, Color.White.copy(alpha = 0.4f), shape = CircleShape)
                            )
                            Spacer(modifier = Modifier.width(7.dp))
                            Text(
                                text = "IT LabRoom",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 25.sp,
                                color = Color(0xFF1B8DDE),
                                style = TextStyle(
                                    shadow = Shadow(
                                        color = Color.Black.copy(alpha = 0.25f),
                                        offset = Offset(2f, 2f),
                                        blurRadius = 4f
                                    )
                                )
                            )

                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { navController.navigate(NavRoute.ListThongBaoSinhVien.route) },
                            modifier = Modifier
                                .padding(end = 12.dp)
                                .size(48.dp)
                        ) {
                            Box(modifier = Modifier.size(40.dp)) {
                                Icon(
                                    imageVector = Lucide.Bell,
                                    contentDescription = "Thông báo",
                                    tint = Color(0xFF1B8DDE),
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .offset(x = (-8).dp)
                                        .size(30.dp)
                                )

                                if (unreadCount > 0) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .offset(x = 0.dp, y = (-3).dp)
                                            .background(Color(0xFF1B8DDE), shape = RoundedCornerShape(50.dp))
                                            .border(1.dp, Color.White, RoundedCornerShape(50.dp))
                                            .padding(horizontal = 5.dp, vertical = 1.dp)
                                            .defaultMinSize(minWidth = 16.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (unreadCount > 99) "99+" else unreadCount.toString(),
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Medium,
                                            textAlign = TextAlign.Center,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                )
            }

            else -> {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    title = {
                        Text(
                            textTopbar,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1B8DDE),
                            modifier = Modifier.padding(start = 10.dp),
                            fontSize = 21.sp,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.25f),
                                    offset = Offset(2f, 2f),
                                    blurRadius = 4f
                                )
                            )
                        )
                    },
                    navigationIcon = {
                        Box(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .size(40.dp)
                                .shadow(4.dp, CircleShape, clip = false)
                                .background(Color.White, CircleShape)
                                .border(1.dp, Color.White, CircleShape)
                                .clickable { navController.popBackStack() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Back",
                                tint = Color(0xFF1B8DDE),
                                modifier = Modifier.size(25.dp)
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
                NavRoute.STARTSCREEN.route,
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
                .padding(
                    top = 90.dp,
                    bottom = 110.dp,
                    start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                    end = paddingValues.calculateEndPadding(LayoutDirection.Ltr)
                )
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






