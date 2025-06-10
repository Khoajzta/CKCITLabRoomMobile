import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ckcitlabroom.models.CaHoc
import com.example.ckcitlabroom.viewmodels.CaHocViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCaHocScreen(
    caHocViewModel: CaHocViewModel,
    maca: String
) {
    val caHoc = caHocViewModel.cahoc

    val tenCaState = remember { mutableStateOf("") }
    val thoiGianBatDauState = remember { mutableStateOf("") }
    val thoiGianKetThucState = remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarData = remember { mutableStateOf<CustomSnackbarData?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(maca) {
        caHocViewModel.getCaHocById(maca)
    }

    LaunchedEffect(caHoc) {
        caHoc?.let {
            tenCaState.value = it.TenCa
            thoiGianBatDauState.value = it.GioBatDau ?: ""
            thoiGianKetThucState.value = it.GioKetThuc ?: ""
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            caHocViewModel.stopPollingCaHoc()
        }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(480.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Chỉnh Sửa Thông Tin Ca Học", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (caHoc != null) {
                    item {
                        Text("Tên Ca Học", color = Color.Black, fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            value = tenCaState.value,
                            onValueChange = { tenCaState.value = it },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                focusedBorderColor = Color.Black,
                                unfocusedBorderColor = Color.Black,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            placeholder = { Text("Nhập ca học") },
                            shape = RoundedCornerShape(12.dp),
                        )

                        // Giờ bắt đầu và kết thúc
                        TimePickerField(label = "Thời gian bắt đầu", timeState = thoiGianBatDauState)
                        TimePickerField(label = "Thời gian kết thúc", timeState = thoiGianKetThucState)
                    }
                } else {
                    item {
                        Text("Lỗi khi lấy API")
                    }
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(16.dp)
            ) { data ->
                snackbarData.value?.let { customData ->
                    Snackbar(
                        containerColor = Color(0xFF1B8DDE),
                        contentColor = Color.White,
                        shape = RoundedCornerShape(12.dp),
                        action = {
                            TextButton(onClick = { snackbarData.value = null }) {
                                Text("Đóng", color = Color.White)
                            }
                        }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (customData.type == SnackbarType.SUCCESS) Icons.Default.Info else Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (customData.type == SnackbarType.SUCCESS) Color.Cyan else Color.Yellow,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = customData.message)
                        }
                    }
                }
            }

            Button(
                onClick = {
                    if (tenCaState.value.isBlank() ||
                        thoiGianBatDauState.value.isBlank() ||
                        thoiGianKetThucState.value.isBlank()
                    ) {
                        coroutineScope.launch {
                            snackbarData.value = CustomSnackbarData(
                                message = "Vui lòng nhập đầy đủ thông tin!",
                                type = SnackbarType.ERROR
                            )
                            snackbarHostState.showSnackbar("Thông báo")
                        }
                    } else {
                        val caHocMoi = CaHoc(
                            MaCaHoc = caHoc?.MaCaHoc ?: 0,
                            TenCa = tenCaState.value,
                            GioBatDau = thoiGianBatDauState.value,
                            GioKetThuc = thoiGianKetThucState.value,
                            TrangThai = caHoc?.TrangThai ?: 1
                        )

                        caHocViewModel.updateCaHoc(caHocMoi)

                        coroutineScope.launch {
                            snackbarData.value = CustomSnackbarData(
                                message = "Cập nhật ca học thành công!",
                                type = SnackbarType.SUCCESS
                            )
                            snackbarHostState.showSnackbar("Thông báo")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF1B8DDE))
            ) {
                Text("Lưu Thông Tin", color = Color.White)
            }
        }
    }
}

