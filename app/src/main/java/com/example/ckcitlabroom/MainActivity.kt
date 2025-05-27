package com.example.ckcitlabroom

import AnimatedNavigationBar
import ButtonData
import CardLichHoc
import DotLoadingIndicator
import DotLoadingOverlay
import HomeScreen
import NavRoute
import NavgationGraph
import TopAbar
import android.os.Bundle
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ckcitlabroom.ui.theme.CKCITLabRoomTheme
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
    // Khởi tạo NavController để điều hướng giữa các màn hình
    val navController = rememberNavController()

    // Biến quản lý trạng thái loading
    var isLoading by remember { mutableStateOf(false) }

    // Lấy route hiện tại để xác định màn hình nào đang hiển thị
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // Danh sách các button cho Bottom Navigation Bar
    val buttons = listOf(
        ButtonData("Home", Icons.Default.Home, click = {
            isLoading = true
            CoroutineScope(Dispatchers.Main).launch {
                delay(500)
                isLoading = false
                navController.navigate(NavRoute.HOME.route){
                    popUpTo(0) { inclusive = true }
                }
            }
        }),
        ButtonData("Quản Lý", Icons.Default.Apps, click = {
            isLoading = true
            CoroutineScope(Dispatchers.Main).launch {
                delay(500)
                isLoading = false
                navController.navigate(NavRoute.QUANLY.route){
                    popUpTo(0) { inclusive = true }
                }
            }
        }),
        ButtonData("Quét Mã", Icons.Default.QrCodeScanner, {}),
        ButtonData("Thông Báo", Icons.Default.Notifications, {}),
        ButtonData("Thông Tin", Icons.Default.AccountCircle, {
            navController.navigate(NavRoute.ACCOUNT.route){
                popUpTo(0) { inclusive = true }
            }

        }),
    )

    val topAppBars = listOf<@Composable () -> Unit>({
        TopAppBar(colors = TopAppBarDefaults.topAppBarColors(Color(0XFF1B8DDE)),
            title = { Text("Trở lại", fontWeight = FontWeight.Bold, color = Color.White) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "", tint = Color.White
                    )
                }
            })
    }, {
        // TopAppBar với logo và tiêu đề (cho màn hình Home)
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
    })

    // Scaffold chứa TopAppBar, BottomBar và nội dung màn hình
    Scaffold(topBar = {
        // Chọn TopAppBar tuỳ theo route hiện tại
        when (currentRoute) {
            NavRoute.HOME.route, NavRoute.QUANLY.route, NavRoute.ACCOUNT.route -> topAppBars[1]()  // Nếu là Home thì dùng TopAppBar số 2
            else -> topAppBars[0]()                 // Ngược lại dùng TopAppBar số 1
        }
    }, bottomBar = {
        // Custom Bottom Navigation Bar luôn cố định tại MainScreen
        AnimatedNavigationBar(
            buttons = buttons,
            barColor = Color.White,
            circleColor = Color.White,
            selectedColor = Color(0xff1B8DDE),
            unselectedColor = Color.Gray,
            currentRoute = currentRoute  // truyền vào đây
        )
    }, containerColor = Color(0xff1B8DDE)
    ) { paddingValues ->

        // Nội dung chính của màn hình
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
                // Điều hướng màn hình bằng NavHost
                NavgationGraph(navController)
            }

            // Hiển thị overlay loading khi isLoading = true
            // if (isLoading) {
            //     DotLoadingOverlay()
            // }
        }
    }
}


