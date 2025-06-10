import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.ckcitlabroom.models.LopHoc
import com.example.ckcitlabroom.viewmodels.LopHocViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditLopHocScreen(
    lopHocViewModel: LopHocViewModel,
    malop: String
) {
    var lopHoc = lopHocViewModel.lophoc

    val maLopState = remember { mutableStateOf("") }
    val tenLopState = remember { mutableStateOf("") }


    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarData = remember { mutableStateOf<CustomSnackbarData?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(malop) {
        lopHocViewModel.getLopHocById(malop)
    }

    LaunchedEffect(lopHoc) {
        lopHoc?.let {
            maLopState.value = it.MaLopHoc
            tenLopState.value = it.TenLopHoc
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            lopHocViewModel.stopPollingLopHoc()
        }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Chỉnh Sửa Thông Tin Lớp Học", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (lopHoc != null) {
                    item {
                        Text(
                            text = "Mã Lớp Học",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(Color.White)
                                .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(12.dp)),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 10.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    lopHoc.MaLopHoc,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Lớp Học",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            value = tenLopState.value,
                            onValueChange = { tenLopState.value = it },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                focusedBorderColor = Color.Black,
                                unfocusedBorderColor = Color.Black,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            ),
                            placeholder = { Text("Nhập lớp") },
                            shape = RoundedCornerShape(12.dp),
                        )
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
                    if (tenLopState.value.isBlank()) {
                        coroutineScope.launch {
                            snackbarData.value = CustomSnackbarData(
                                message = "Vui lòng nhập đầy đủ thông tin!",
                                type = SnackbarType.ERROR
                            )
                            snackbarHostState.showSnackbar("Thông báo")
                        }
                    } else {
                        val lopHocMoi = LopHoc(
                            MaLopHoc = maLopState.value,
                            TenLopHoc = tenLopState.value,
                            TrangThai = lopHoc?.TrangThai ?: 1
                        )
                        lopHocViewModel.updateLopHoc(lopHocMoi)
                        coroutineScope.launch {
                            snackbarData.value = CustomSnackbarData(
                                message = "Cập nhật lớp học thành công!",
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
                Text("Lưu Thông Tin")
            }
        }
    }
}