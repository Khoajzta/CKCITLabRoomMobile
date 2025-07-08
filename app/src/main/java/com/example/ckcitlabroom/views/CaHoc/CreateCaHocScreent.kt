import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@Composable
fun CreateCaHocScreen(
    navController: NavHostController,
    caHocViewModel: CaHocViewModel
) {
    var context = LocalContext.current
    val danhSachCaHoc = caHocViewModel.danhSachAllCaHoc

    LaunchedEffect(Unit) {
        caHocViewModel.getAllCaHoc()
    }

    val tenCaHocState = remember { mutableStateOf("") }
    val thoiGianBatDauState = remember { mutableStateOf("") }
    val thoiGianKetThucState = remember { mutableStateOf("") }

    TimePickerField(label = "Thời gian bắt đầu", timeState = thoiGianBatDauState)
    TimePickerField(label = "Thời gian kết thúc", timeState = thoiGianKetThucState)

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
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

            Button(
                onClick = {
                    val tenCa = tenCaHocState.value.trim()
                    val batDau = thoiGianBatDauState.value.trim()
                    val ketThuc = thoiGianKetThucState.value.trim()
                    val daTonTai = danhSachCaHoc.any { it.TenCa.equals(tenCa, ignoreCase = true) }

                    when {
                        tenCa.isEmpty() -> {
                            Toast.makeText(
                                context,
                                "Tên ca học không được để trống",
                                Toast.LENGTH_SHORT
                            )
                        }

                        batDau.isEmpty() -> {
                            Toast.makeText(
                                context,
                                "Thời gian bắt đầu không được để trống",
                                Toast.LENGTH_SHORT
                            )
                        }

                        ketThuc.isEmpty() -> {
                            Toast.makeText(
                                context,
                                "Thời gian kết thúc không được để trống",
                                Toast.LENGTH_SHORT
                            )
                        }

                        daTonTai -> {
                            Toast.makeText(context, "Tên ca học đã tồn tại", Toast.LENGTH_SHORT)
                        }

                        else -> {
                            val caHocMoi = CaHoc(
                                MaCaHoc = 0,
                                TenCa = tenCa,
                                GioBatDau = batDau,
                                GioKetThuc = ketThuc,
                                TrangThai = 1
                            )

                            caHocViewModel.createCaHoc(caHocMoi)

                            Toast.makeText(context, "Thêm ca học thành công", Toast.LENGTH_SHORT)
                                .show()
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

