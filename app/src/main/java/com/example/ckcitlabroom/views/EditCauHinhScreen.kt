import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.unit.dp
import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import java.util.Calendar


//, onSave: (CauHinh) -> Unit


@Composable
fun EditCauHinhScreen(cauHinh: CauHinh) {
    val context = LocalContext.current

    val textFieldStates = remember {
        listOf(
            mutableStateOf(cauHinh.MaCauHinh),
            mutableStateOf(cauHinh.Main),
            mutableStateOf(cauHinh.CPU),
            mutableStateOf(cauHinh.RAM),
            mutableStateOf(cauHinh.VGA),
            mutableStateOf(cauHinh.ManHinh),
            mutableStateOf(cauHinh.BanPhim),
            mutableStateOf(cauHinh.Chuot),
            mutableStateOf(cauHinh.HDD)
        )
    }

    val ngayNhapState = remember { mutableStateOf(cauHinh.NgayNhap) }

    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            ngayNhapState.value = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val fieldLabels = listOf(
        "Mã Cấu hình", "Main", "CPU", "RAM", "VGA",
        "Màn Hình", "Bàn phím", "Chuột", "HDD"
    )

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .height(600.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(fieldLabels.indices.toList()) { index ->
                    Text(
                        text = fieldLabels[index],
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        value = textFieldStates[index].value,
                        onValueChange = { textFieldStates[index].value = it },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White,
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color.Black,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp),
                    )
                }

                // Ô Ngày nhập
                item {
                    Text(
                        text = "Ngày nhập",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        value = ngayNhapState.value,
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
                            IconButton(onClick = {
                                datePickerDialog.show()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Chọn ngày"
                                )
                            }
                        },
                        singleLine = true
                    )
                }
            }

            Button(
                onClick = {
//                    val updatedCauHinh = CauHinh(
//                        MaCauHinh = textFieldStates[0].value,
//                        Main = textFieldStates[1].value,
//                        CPU = textFieldStates[2].value,
//                        RAM = textFieldStates[3].value,
//                        VGA = textFieldStates[4].value,
//                        ManHinh = textFieldStates[5].value,
//                        BanPhim = textFieldStates[6].value,
//                        Chuot = textFieldStates[7].value,
//                        HDD = textFieldStates[8].value,
//                        TrangThai = cauHinh.TrangThai
//                    )
//                    onSave(updatedCauHinh)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color.Cyan)
            ) {
                Text("Lưu cấu hình")
            }
        }
    }
}






