import android.app.DatePickerDialog
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.rememberScaffoldState
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lapstore.viewmodels.MayTinhViewModel
import java.util.Calendar
import kotlin.math.log

@Composable
fun EditMayTinhScreen(maMay: String){

    var mayTinhViewModel: MayTinhViewModel = viewModel()
    var maytinh = mayTinhViewModel.maytinh

    val maMayState = remember { mutableStateOf("") }
    val viTriState = remember { mutableStateOf("") }
    val mainState = remember { mutableStateOf("") }
    val cpuState = remember { mutableStateOf("") }
    val ramState = remember { mutableStateOf("") }
    val vgaState = remember { mutableStateOf("") }
    val manHinhState = remember { mutableStateOf("") }
    val banPhimState = remember { mutableStateOf("") }
    val chuotState = remember { mutableStateOf("") }
    val hddState = remember { mutableStateOf("") }
    val ssdState = remember { mutableStateOf("") }
    val maPhongState = remember { mutableStateOf("") }
    val trangThaiState = remember { mutableStateOf("") }

    LaunchedEffect(maMay) {
        mayTinhViewModel.getMayTinhByMaMay(maMay)
    }

    LaunchedEffect(maytinh) {
        maytinh?.let {
            maMayState.value = it.MaMay
            viTriState.value = if (maytinh.ViTri.isNullOrBlank()) "Kho" else maytinh.ViTri
            mainState.value = it.Main
            cpuState.value = it.CPU
            ramState.value = it.RAM
            vgaState.value = it.VGA
            manHinhState.value = it.ManHinh
            banPhimState.value = it.BanPhim
            chuotState.value = it.Chuot
            hddState.value = it.HDD
            ssdState.value = it.SSD
            maPhongState.value = it.MaPhong
            trangThaiState.value = it.TrangThai.toString()
        }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(640.dp),
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
                Text("Chỉnh Sửa Cấu hình", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if(maytinh!=null){
                    item {
                        Text(
                            text = "Mã Máy",
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
                                    maytinh.MaMay,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Vị Trí",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            value = viTriState.value,
                            onValueChange = { viTriState.value = it },
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
//
                    item {
                        Text(
                            text = "Main",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            value = mainState.value,
                            onValueChange = { mainState.value = it },
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

                    item {
                        Text(
                            text = "CPU",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            value = cpuState.value,
                            onValueChange = { cpuState.value = it },
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

                    item {
                        Text(
                            text = "RAM",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            value = ramState.value,
                            onValueChange = { ramState.value = it },
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
//
                    item {
                        Text(
                            text = "VGA",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            value = vgaState.value,
                            onValueChange = { vgaState.value = it },
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
//
                    item {
                        Text(
                            text = "Màn Hình",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            value = manHinhState.value,
                            onValueChange = { manHinhState.value = it },
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
//
                    item {
                        Text(
                            text = "Bàn Phím",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            value = banPhimState.value,
                            onValueChange = { banPhimState.value = it },
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
//
                    item {
                        Text(
                            text = "Chuột",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            value = chuotState.value,
                            onValueChange = { chuotState.value = it },
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
//
                    item {
                        Text(
                            text = "HDD",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            value = hddState.value,
                            onValueChange = { hddState.value = it },
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
//
                    item {
                        Text(
                            text = "SSD",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            value = ssdState.value,
                            onValueChange = { ssdState.value = it },
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
//
                    item {
                        Text(
                            text = "Phòng",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            value = maPhongState.value,
                            onValueChange = { maPhongState.value = it },
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

                }else{
                    item {
                        Text("Lỗi khi lấy API")
                    }

                }
            }
            val context = LocalContext.current

            Button(
                onClick = {
                    val mayTinhMoi = MayTinh(
                        MaMay = maMayState.value,
                        ViTri = viTriState.value,
                        Main = mainState.value,
                        CPU = cpuState.value,
                        RAM = ramState.value,
                        VGA = vgaState.value,
                        ManHinh = manHinhState.value,
                        BanPhim = banPhimState.value,
                        Chuot = chuotState.value,
                        HDD = hddState.value,
                        SSD = ssdState.value,
                        MaPhong = maPhongState.value,
                        TrangThai = trangThaiState.value.toIntOrNull() ?: 0
                    )
                    mayTinhViewModel.updateMayTinh(mayTinhMoi)

                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0XFF1B8DDE))
            ) {
                Text("Lưu Cấu Hình")
            }
        }
    }
}