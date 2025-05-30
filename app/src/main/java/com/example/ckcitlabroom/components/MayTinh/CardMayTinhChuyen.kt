import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SwapHorizontalCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.composables.icons.lucide.Building2
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.CircleX
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Monitor
import com.example.lapstore.viewmodels.LichSuChuyenMayViewModel
import com.example.lapstore.viewmodels.MayTinhViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardMayTinhChuyen(
    maytinh: MayTinh,
    maytinhViewModel: MayTinhViewModel,
    phongMayViewModel: PhongMayViewModel,
    selectedMayTinhs: SnapshotStateList<MayTinh>,
    lichSuChuyenMayViewModel: LichSuChuyenMayViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    var isExpanded by remember { mutableStateOf(false) }

    val danhSachPhongMay = phongMayViewModel.danhSachAllPhongMay
    var selectdMaPhong by remember { mutableStateOf("") }
    var phongMayCard by remember { mutableStateOf<PhongMay?>(null) }
    val maPhongState = remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        phongMayViewModel.getAllPhongMay()
    }

    LaunchedEffect(maytinh.MaPhong) {
        phongMayCard = phongMayViewModel.fetchPhongMayByMaPhong(maytinh.MaPhong)
    }

    LaunchedEffect(selectdMaPhong) {
        maPhongState.value = selectdMaPhong
    }

    LaunchedEffect(danhSachPhongMay) {
        if (danhSachPhongMay.isNotEmpty() && selectdMaPhong.isEmpty()) {
            selectdMaPhong = danhSachPhongMay[0].MaPhong
        }
    }

    val selectedTenPhong = danhSachPhongMay.find { it.MaPhong == selectdMaPhong }?.TenPhong ?: ""

    val isSelected = selectedMayTinhs.contains(maytinh)

    Card(modifier = Modifier
        .padding(bottom = 8.dp)
        .fillMaxWidth()
        .pointerInput(Unit) {
            detectTapGestures(onLongPress = {
                if (selectedMayTinhs.any { it.MaMay == maytinh.MaMay }) {
                    selectedMayTinhs.removeAll { it.MaMay == maytinh.MaMay }
                } else {
                    selectedMayTinhs.add(maytinh)
                }
                Log.d(
                    "SelectedMachines", "Máy đã chọn: ${selectedMayTinhs.map { it.MaMay }}"
                )
            }, onTap = {
                expanded = !expanded
            })
        }
        .shadow(7.dp, shape = RoundedCornerShape(12.dp)), colors = CardDefaults.cardColors(
        containerColor = if (isSelected) Color(0xFFBBDEFB) else Color.White
    ), elevation = CardDefaults.cardElevation(10.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Lucide.Monitor,
                    contentDescription = "Mã Máy",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text("Mã Máy: ${maytinh.MaMay}", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Lucide.Building2,
                    contentDescription = "Mã Máy",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text("Phòng hiện tại: ${phongMayCard?.TenPhong ?: "Đang tải..."}")
            }

            val (color, statusText, statusIcon) = when (maytinh.TrangThai) {
                1 -> Triple(Color(0xFF4CAF50), "Hoạt động", Lucide.CircleCheck)
                0 -> Triple(Color(0xFFF44336), "Không hoạt động", Lucide.CircleX)
                else -> Triple(Color.Gray, "Không xác định", Lucide.CircleAlert)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = statusIcon,
                    contentDescription = "Trạng thái",
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text("Trạng thái: ")
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(Modifier.width(4.dp))
                Text(statusText, color = color)
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.SwapHorizontalCircle,
                            contentDescription = "Mã Máy",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Chuyển đến phòng",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }


                    Column {
                        ExposedDropdownMenuBox(
                            expanded = isExpanded,
                            onExpandedChange = { isExpanded = !isExpanded },
                        ) {
                            OutlinedTextField(modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .padding(bottom = 12.dp),
                                value = selectedTenPhong,
                                onValueChange = { },
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
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) })

                            ExposedDropdownMenu(
                                modifier = Modifier
                                    .background(Color.White)
                                    .height(300.dp)
                                    .padding(bottom = 8.dp),
                                expanded = isExpanded,
                                onDismissRequest = { isExpanded = false },
                                shape = RoundedCornerShape(12.dp),
                            ) {
                                danhSachPhongMay.forEach { phongMay ->
                                    androidx.compose.material3.DropdownMenuItem(text = {
                                        Text(
                                            text = phongMay.TenPhong,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black
                                        )
                                    }, onClick = {
                                        selectdMaPhong = phongMay.MaPhong
                                        isExpanded = false
                                    })
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(55.dp),
                        onClick = {
                            if (maPhongState.value == maytinh.MaPhong) {
                                showDialog = true
                                return@Button
                            }

                            val ngayChuyen =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                            val mayTinhMoi = MayTinh(
                                MaMay = maytinh.MaMay,
                                ViTri = maytinh.ViTri,
                                Main = maytinh.Main,
                                CPU = maytinh.CPU,
                                RAM = maytinh.RAM,
                                VGA = maytinh.VGA,
                                ManHinh = maytinh.ManHinh,
                                BanPhim = maytinh.BanPhim,
                                Chuot = maytinh.Chuot,
                                HDD = maytinh.HDD,
                                SSD = maytinh.SSD,
                                MaPhong = maPhongState.value,
                                TrangThai = 1
                            )
                            maytinhViewModel.updateMayTinh(mayTinhMoi)

                            val lichSu = LichSuChuyenMay(
                                MaLichSu = 0,
                                MaPhongCu = maytinh.MaPhong,
                                MaPhongMoi = maPhongState.value,
                                NgayChuyen = ngayChuyen,
                                MaMay = maytinh.MaMay
                            )

                            lichSuChuyenMayViewModel.createLichSuChuyenMay(lichSu)
                            expanded = !expanded
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xff1B8DDE)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Chuyển máy", fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }

                    if (showDialog) {
                        AlertDialog(
                            containerColor = Color.White,
                            onDismissRequest = { showDialog = false },
                            confirmButton = {
                                TextButton(onClick = { showDialog = false }) {
                                    Text("OK", color = Color(0xff1B8DDE), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                }
                            },
                            title = { Text("Thông báo",fontWeight = FontWeight.ExtraBold, color = Color(0xff1B8DDE)) },
                            text = { Text("Phòng mới phải khác phòng hiện tại!", fontSize = 16.sp, fontWeight = FontWeight.Bold ,color = Color.Black) }
                        )
                    }
                }
            }
        }
    }
}


