import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.github.kittinunf.fuel.toolbox.HttpClient
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

@Composable
fun CreateNamHocScreen(
    navController: NavHostController,
    namHocViewModel: NamHocViewModel
) {
    val maNam = remember { mutableStateOf("") }
    val tenNam = remember { mutableStateOf("") }
    val ngayBatDau = remember { mutableStateOf("") }
    val ngayKetThuc = remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarData = remember { mutableStateOf<CustomSnackbarData?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var showDatePickerStart by remember { mutableStateOf(false) }
    var showDatePickerEnd by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Mã Năm Học", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = maNam.value,
                onValueChange = { maNam.value = it },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                placeholder = { Text("Nhập thông tin") },
                shape = RoundedCornerShape(12.dp),
            )

            Text("Tên Năm Học", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = tenNam.value,
                onValueChange = { tenNam.value = it },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                placeholder = { Text("Nhập thông tin") },
                shape = RoundedCornerShape(12.dp),
            )

            Text("Ngày Bắt Đầu", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = formatNgay(ngayBatDau.value),
                onValueChange = {},
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePickerStart = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Chọn ngày bắt đầu", tint = Color.Black)
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                placeholder = { Text("Nhập thông tin") },
                shape = RoundedCornerShape(12.dp),
            )

            Text("Ngày Kết Thúc", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = formatNgay(ngayKetThuc.value),
                onValueChange = {},
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePickerEnd = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Chọn ngày kết thúc", tint = Color.Black)
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                placeholder = { Text("Nhập thông tin") },
                shape = RoundedCornerShape(12.dp),
            )

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(16.dp)
            ) { data ->
                snackbarData.value?.let { customData ->
                    Snackbar(
                        containerColor = if (customData.type == SnackbarType.SUCCESS) Color(0xFF1B8DDE) else Color(0xFFD32F2F),
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
                    if (maNam.value.isBlank() || tenNam.value.isBlank() || ngayBatDau.value.isBlank() || ngayKetThuc.value.isBlank()) {
                        snackbarData.value = CustomSnackbarData("Vui lòng nhập đầy đủ thông tin", SnackbarType.ERROR)
                        return@Button
                    }

                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                    val namHoc = NamHoc(
                        MaNam = maNam.value,
                        TenNam = tenNam.value,
                        NgayBatDau = ngayBatDau.value.format(formatter),
                        NgayKetThuc = ngayKetThuc.value.format(formatter),
                        TrangThai = 0
                    )

                    namHocViewModel.createNamHoc(namHoc)

                    coroutineScope.launch {
                        snackbarData.value = CustomSnackbarData(
                            message = "Tạo năm học thành công",
                            type = SnackbarType.SUCCESS
                        )
                        snackbarHostState.showSnackbar(snackbarData.value!!.message) // Hiển thị nội dung thực
                        snackbarData.value = null
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF1B8DDE))
            ) {
                Text("Tạo Năm Học", color = Color.White)
            }

        }

        // DatePicker cho Ngày Bắt Đầu
        if (showDatePickerStart) {
            val datePickerDialog = DatePickerDialog(
                context,
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    ngayBatDau.value = sdf.format(calendar.time)
                    showDatePickerStart = false
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.setOnDismissListener { showDatePickerStart = false }
            datePickerDialog.show()
        }

        // DatePicker cho Ngày Kết Thúc
        if (showDatePickerEnd) {
            val datePickerDialog = DatePickerDialog(
                context,
                { _, year, month, day ->
                    calendar.set(year, month, day)
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    ngayKetThuc.value = sdf.format(calendar.time)
                    showDatePickerEnd = false
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.setOnDismissListener { showDatePickerEnd = false }
            datePickerDialog.show()
        }
    }
}
