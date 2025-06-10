import android.app.TimePickerDialog
import android.icu.util.Calendar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.models.CaHoc
import com.example.ckcitlabroom.viewmodels.CaHocViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CreateCaHocScreen(
    navController: NavHostController,
    caHocViewModel: CaHocViewModel
) {
    val danhSachCaHoc = caHocViewModel.danhSachAllCaHoc

    LaunchedEffect(Unit) {
        caHocViewModel.getAllCaHoc()
    }

    val tenCaHocState = remember { mutableStateOf("") }
    val thoiGianBatDauState = remember { mutableStateOf("") }
    val thoiGianKetThucState = remember { mutableStateOf("") }

    TimePickerField(label = "Thời gian bắt đầu", timeState = thoiGianBatDauState)
    TimePickerField(label = "Thời gian kết thúc", timeState = thoiGianKetThucState)


    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarData = remember { mutableStateOf<CustomSnackbarData?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Thêm Ca Học",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Tên Ca Học", color = Color.Black, fontWeight = FontWeight.Bold)

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                value = tenCaHocState.value,
                onValueChange = { tenCaHocState.value = it },
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
            TimePickerField(label = "Thời gian bắt đầu", timeState = thoiGianBatDauState)
            TimePickerField(label = "Thời gian kết thúc", timeState = thoiGianKetThucState)

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
                            TextButton(onClick = {
                                snackbarData.value = null
                            }) {
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
                    val maCaMoi = tenCaHocState.value
                    val daTonTai = danhSachCaHoc.any { it.MaCaHoc.toString() == maCaMoi }

                    if (tenCaHocState.value == "") {
                        coroutineScope.launch {
                            snackbarData.value = CustomSnackbarData(
                                message = "Tên ca học không được để trống!",
                                type = SnackbarType.ERROR
                            )
                            snackbarHostState.showSnackbar("Thông báo")
                        }
                    } else if (daTonTai) {
                        coroutineScope.launch {
                            snackbarData.value = CustomSnackbarData(
                                message = "Tên ca học đã tồn tại!",
                                type = SnackbarType.ERROR
                            )
                            snackbarHostState.showSnackbar("Thông báo")
                        }
                    } else {
                        val caHocMoi = CaHoc(
                            0,
                            tenCaHocState.value,
                            thoiGianBatDauState.value,
                            thoiGianKetThucState.value,
                            1
                        )

                        caHocViewModel.createCaHoc(caHocMoi)

                        coroutineScope.launch {
                            snackbarData.value = CustomSnackbarData(
                                message = "Thêm ca học thành công",
                                type = SnackbarType.SUCCESS
                            )
                            snackbarHostState.showSnackbar("Thông báo")
                            delay(1000)
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0XFF1B8DDE))
            ) {
                Text("Thêm Ca Học", color = Color.White)
            }
        }
    }
}

@Composable
fun TimePickerField(
    label: String,
    timeState: MutableState<String>
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    var showTimePicker by remember { mutableStateOf(false) }

    if (showTimePicker) {
        TimePickerDialog(
            context,
            { _, hour: Int, minute: Int ->
                val formattedTime = String.format("%02d:%02d:00", hour, minute)
                timeState.value = formattedTime
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
        showTimePicker = false
    }

    Column {
        Text(text = label, color = Color.Black, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            value = timeState.value,
            onValueChange = { },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showTimePicker = true }) {
                    Icon(Icons.Default.AccessTime, contentDescription = "Chọn giờ")
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            placeholder = { Text("Chọn giờ") },
            shape = RoundedCornerShape(12.dp),
        )
    }
}

