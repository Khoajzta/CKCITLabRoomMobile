import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lapstore.viewmodels.MayTinhViewModel

@Composable
fun PhongMayDetailScreen(
    maphong: String,
    navController: NavHostController,
    phongMayViewModel: PhongMayViewModel,
    mayTinhViewModel: MayTinhViewModel
) {
    val danhSachMayTinh = mayTinhViewModel.danhSachAllMayTinhtheophong
    val phongmay = phongMayViewModel.phongmay

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

    val tenPhongState = remember { mutableStateOf(phongmay.TenPhong) }

    LaunchedEffect(phongmay) {
        tenPhongState.value = phongmay.TenPhong
    }

    Column(
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Danh Sách Máy Tính",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                color = Color(0xFF1B8DDE)
            )
        }

        LazyColumn(
            modifier = Modifier.height(550.dp)
        ) {

            if (danhSachMayTinh == null || danhSachMayTinh.isEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Chưa có máy tính nào",
                            color = Color.White,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                items(danhSachMayTinh) { maytinh ->
                    CardMayTinh(maytinh, navController, mayTinhViewModel,phongMayViewModel)
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = Modifier.width(180.dp),
                onClick = { showDialog = true },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Chỉnh sửa", color = Color.White, fontWeight = FontWeight.ExtraBold)
            }

            Button(
                modifier = Modifier.width(180.dp),
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
                Text("Xóa", color = Color.White, fontWeight = FontWeight.ExtraBold)
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
                        text = "Bạn có chắc muốn xóa phòng ${phongmay.MaPhong} không?",
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


        if (showDialog) {
            AlertDialog(
                containerColor = Color.White,
                onDismissRequest = { showDialog = false },
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Cập nhật",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                text = {
                    Column(

                    ) {
                        Text(
                            text = "Mã Phòng", color = Color.Black, fontWeight = FontWeight.ExtraBold
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(55.dp)
                                .background(Color.White)
                                .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(12.dp)),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 15.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    phongmay.MaPhong,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    fontSize = 15.sp
                                )
                            }
                        }

                        Text(
                            text = "Tên Phòng", color = Color.Black, fontWeight = FontWeight.ExtraBold
                        )

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = tenPhongState.value,
                            onValueChange = { tenPhongState.value = it },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                focusedBorderColor = Color.Black,
                                unfocusedBorderColor = Color.Black,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            placeholder = { Text("Nhập thông tin") },
                            shape = RoundedCornerShape(12.dp),
                        )
                    }
                },

                confirmButton = {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .shadow(4.dp, shape = RoundedCornerShape(12.dp)),
                        onClick = {
                            val phongmaynew = PhongMay(phongmay.MaPhong, tenPhongState.value, phongmay.TrangThai)
                            phongMayViewModel.updatePhongMay(phongmaynew)
                            phongMayViewModel.getPhongMayByMaPhong(maphong)
                            showDialog = false
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(Color(0XFF1B8DDE))
                    ) {
                        Text(
                            text = "Lưu phòng máy", color = Color.White, fontWeight = FontWeight.Bold
                        )
                    }
                },
            )
        }
    }
}