import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lapstore.viewmodels.DonNhapyViewModel
import com.example.lapstore.viewmodels.MayTinhViewModel

@Composable
fun PhongMayDonNhapScreen(
    maphong:String,
    navController: NavHostController,
    phongMayViewModel: PhongMayViewModel,
    mayTinhViewModel: MayTinhViewModel,
    donNhapyViewModel: DonNhapyViewModel

){
    var danhsachdonnhap = donNhapyViewModel.danhSachDonNhap
    val danhSachMayTinh = mayTinhViewModel.danhSachAllMayTinhtheophong

    var showDialog by remember { mutableStateOf(false) }
    var showDeleteWarning by remember { mutableStateOf(false) }
    var showConfirmDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        mayTinhViewModel.getMayTinhByPhong(maphong)
        phongMayViewModel.getPhongMayByMaPhong(maphong)
    }

    DisposableEffect(Unit) {
        onDispose {
            mayTinhViewModel.stopPollingMayTinhTheoPhong()
        }
    }

    LaunchedEffect(Unit) {
        donNhapyViewModel.getAllDonNhap()
    }

    DisposableEffect(Unit) {
        onDispose {
            donNhapyViewModel.stopPollingAllDonNhap()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Quản Lý Đơn Nhập",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                color = Color.White
            )
            IconButton(
                onClick = {
                    navController.navigate(NavRoute.ADDDONNHAP.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Thêm cấu hình",
                    tint = Color.White
                )
            }
        }

        // Danh sách đơn nhập
        LazyColumn(
            modifier = Modifier.height(520.dp)
        ) {

            if(danhsachdonnhap.isNullOrEmpty()){
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Chưa có đơn nhập nào",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }
                }
            }else{
                items(danhsachdonnhap) { donnhap ->
                    CardDonNhap(donnhap,navController)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (danhSachMayTinh.isNullOrEmpty()) {
                        showConfirmDeleteDialog = true
                    } else {
                        showDeleteWarning = true
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
            ) {
                Text("Xóa phòng máy", color = Color.White, fontWeight = FontWeight.ExtraBold)
            }
        }

        if (showConfirmDeleteDialog) {
            AlertDialog(
                containerColor = Color.White,
                onDismissRequest = { showConfirmDeleteDialog = false },
                title = {
                    Text(
                        text = "Xác nhận xóa",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "Bạn có chắc muốn xóa phòng ${maphong} không?",
                        color = Color.Black
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            phongMayViewModel.deletePhongMay(maphong)
                            navController.popBackStack()
                            showConfirmDeleteDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                    ) {
                        Text("Xóa", color = Color.White)
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showConfirmDeleteDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Text("Hủy", color = Color.White)
                    }
                }
            )
        }

        if (showDeleteWarning) {
            AlertDialog(
                containerColor = Color.White,
                onDismissRequest = { showDeleteWarning = false },
                title = {
                    Text(
                        text = "Không thể xóa phòng",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "Phòng này hiện vẫn còn máy tính, hãy chuyển hết máy tính trước khi xóa phòng.",
                        color = Color.Black
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { showDeleteWarning = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("Đã hiểu", color = Color.White)
                    }
                }
            )
        }
    }
}