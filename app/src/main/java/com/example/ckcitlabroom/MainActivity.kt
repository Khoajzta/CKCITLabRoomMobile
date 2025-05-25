package com.example.ckcitlabroom

import AnimatedNavigationBar
import ButtonData
import CardLichHoc
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ckcitlabroom.ui.theme.CKCITLabRoomTheme

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

    val buttons = listOf(
        ButtonData("Home", Icons.Default.Home,{}),
        ButtonData("Quản Lý", Icons.Default.Apps,{}),
        ButtonData("Quét Mã", Icons.Default.QrCodeScanner,{}),
        ButtonData("Thông Báo", Icons.Default.Notifications,{}),
        ButtonData("Thông Tin", Icons.Default.AccountCircle,{}),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0XFF1B8DDE)),
                title = { Text("") },
                navigationIcon = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(Icons.Default.Menu,
                                contentDescription = "Menu",
                                modifier = Modifier.size(40.dp),
                                Color.White
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.logo),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(7.dp))
                        Text("IT LabRoom", fontWeight = FontWeight.ExtraBold, fontSize = 25.sp, color = Color.White)
                    }
                },
            )
        },
        bottomBar = {
            AnimatedNavigationBar(
                buttons = buttons,
                barColor = Color.White,
                circleColor = Color.White,
                selectedColor = Color(0xff1B8DDE),
                unselectedColor = Color.Gray,
            )
        },
        containerColor = Color(0xff1B8DDE)

    ) {
        Column (
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(modifier = Modifier.fillMaxWidth()) {
                Text("Lịch Dạy", color = Color.White, fontWeight = FontWeight.ExtraBold)
            }
            //danh sách lịch dạy
            CardLichHoc(gv = "Lê Viết Hoàng Nguyên", ca = "1", phong = "F7.1", thu = "4", lop = "CĐ TH22 D", tuan = "34", mon = "CSDL", ngay = "25/05/2025")


        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CKCITLabRoomTheme {
        MainScreen()
    }
}