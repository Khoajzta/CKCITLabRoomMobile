import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lapstore.viewmodels.LichSuChuyenMayViewModel
import com.example.lapstore.viewmodels.MayTinhViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhongMayChuyenScreen(
    maphong: String,
    navController: NavHostController,
    phongMayViewModel: PhongMayViewModel,
    mayTinhViewModel: MayTinhViewModel,
    lichSuChuyenMayViewModel: LichSuChuyenMayViewModel
) {
    val danhSachMayTinhtheophong = mayTinhViewModel.danhSachAllMayTinhtheophong
    val phongmay = phongMayViewModel.phongmay

    Log.d("maphong", danhSachMayTinhtheophong.toString())

    LaunchedEffect(maphong) {
        mayTinhViewModel.getMayTinhByPhong(maphong)
        phongMayViewModel.getPhongMayByMaPhong(maphong)
    }

    DisposableEffect(Unit) {
        onDispose {
            mayTinhViewModel.stopPollingMayTinhTheoPhong()
        }
    }

    val danhSachPhongMay = phongMayViewModel.danhSachAllPhongMay


    val selectedMayTinhs = remember { mutableStateListOf<MayTinh>() }

    var showDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }

    var selectedMaPhong by remember { mutableStateOf("") }

    val selectedTenPhong = danhSachPhongMay.find { it.MaPhong == selectedMaPhong }?.TenPhong ?: ""

    LaunchedEffect(danhSachPhongMay) {
        if (danhSachPhongMay.isNotEmpty() && selectedMaPhong.isEmpty()) {
            selectedMaPhong = danhSachPhongMay[0].MaPhong
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
                "Danh Sách Máy Tính Phòng ${phongmay.TenPhong}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = Color(0xFF1B8DDE)
            )

        }

        LazyColumn(
            modifier = Modifier.height(550.dp)
        ) {
            if (danhSachMayTinhtheophong == null || danhSachMayTinhtheophong.isEmpty()) {
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
                items(danhSachMayTinhtheophong) { maytinh ->
                    CardMayTinhChuyen(
                        maytinh,
                        mayTinhViewModel,
                        phongMayViewModel,
                        selectedMayTinhs,
                        lichSuChuyenMayViewModel
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = selectedMayTinhs.isNotEmpty(),
            enter = fadeIn(animationSpec = tween(durationMillis = 300)) + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut(animationSpec = tween(durationMillis = 200)) + slideOutVertically(targetOffsetY = { it })
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                onClick = {
                    if (selectedMayTinhs.isEmpty()) {
                        showErrorDialog = true
                    } else {
                        showDialog = true
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color.White),
                elevation = ButtonDefaults.elevatedButtonElevation(7.dp)
            ) {
                Text("Chuyển máy", color = Color.Black, fontWeight = FontWeight.ExtraBold)
            }
        }


        // Dialog xác nhận chuyển máy
        if (showDialog) {
            AlertDialog(
                containerColor = Color.White,
                onDismissRequest = { showDialog = false },
                title = {
                    Text(
                        text = "Chuyển máy",
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "Chuyển đến phòng",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )

                        ExposedDropdownMenuBox(
                            expanded = isExpanded,
                            onExpandedChange = { isExpanded = !isExpanded }
                        ) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                                    .padding(bottom = 12.dp),
                                value = selectedTenPhong,
                                onValueChange = {},
                                readOnly = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedContainerColor = Color.White,
                                    focusedContainerColor = Color.White,
                                    focusedBorderColor = Color.Black,
                                    unfocusedBorderColor = Color.Black,
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black
                                ),
                                shape = RoundedCornerShape(12.dp),
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                                }
                            )

                            ExposedDropdownMenu(
                                modifier = Modifier
                                    .background(Color.White)
                                    .height(300.dp)
                                    .padding(bottom = 8.dp),
                                expanded = isExpanded,
                                onDismissRequest = { isExpanded = false },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                danhSachPhongMay.forEach { phongMay ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                phongMay.TenPhong,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.Black
                                            )
                                        },
                                        onClick = {
                                            selectedMaPhong = phongMay.MaPhong
                                            isExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp)
                            .shadow(4.dp, shape = RoundedCornerShape(12.dp)),
                        onClick = {
                            val ngayChuyen =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                    Date()
                                )

                            selectedMayTinhs.forEach { mayTinh ->
                                // Cập nhật máy tính với phòng mới
                                val mayTinhCapNhat = mayTinh.copy(MaPhong = selectedMaPhong, TenMay = "MAY${selectedMaPhong}")
                                mayTinhViewModel.updateMayTinh(mayTinhCapNhat)

                                // Tạo đối tượng lịch sử chuyển máy
                                val lichSu = LichSuChuyenMay(
                                    MaLichSu = 0,  // nếu API tự sinh thì truyền 0 hoặc bỏ qua nếu không cần
                                    MaPhongCu = mayTinh.MaPhong,
                                    MaPhongMoi = selectedMaPhong,
                                    NgayChuyen = ngayChuyen,
                                    MaMay = mayTinh.MaMay
                                )

                                // Gọi API thêm lịch sử
                                lichSuChuyenMayViewModel.createLichSuChuyenMay(lichSu)
                            }

                            selectedMayTinhs.clear()
                            showDialog = false
                        },

                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(Color(0XFF1B8DDE))
                    ) {
                        Text("Chuyển máy", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // Dialog báo lỗi khi chưa chọn máy
        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = {
                    Text("Thông báo", fontWeight = FontWeight.Bold, color = Color.Black)
                },
                containerColor = Color.White,
                text = {
                    Text("Vui lòng chọn ít nhất một máy tính để chuyển.", color = Color.Black)
                },
                confirmButton = {
                    Button(
                        onClick = { showErrorDialog = false },
                        colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("OK", color = Color.White)
                    }
                }
            )
        }
    }
}