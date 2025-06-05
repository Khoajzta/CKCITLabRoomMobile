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
import androidx.compose.material3.DropdownMenuItem
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

    Card(
        modifier = Modifier
            .padding(bottom = 12.dp)
            .fillMaxWidth()
            .shadow(7.dp, shape = RoundedCornerShape(16.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        if (selectedMayTinhs.any { it.MaMay == maytinh.MaMay }) {
                            selectedMayTinhs.removeAll { it.MaMay == maytinh.MaMay }
                        } else {
                            selectedMayTinhs.add(maytinh)
                        }
                        Log.d("SelectedMachines", "Máy đã chọn: ${selectedMayTinhs.map { it.MaMay }}")
                    },
                    onTap = { expanded = !expanded }
                )
            },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFBBDEFB) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            InfoRow(icon = Lucide.Monitor, label = "Mã Máy", value = maytinh.MaMay)
            InfoRow(icon = Lucide.Monitor, label = "Tên Máy", value = maytinh.TenMay)
            InfoRow(icon = Lucide.Building2, label = "Phòng hiện tại", value = phongMayCard?.TenPhong ?: "Đang tải...")

            val (color, statusText, statusIcon) = when (maytinh.TrangThai) {
                1 -> Triple(Color(0xFF4CAF50), "Hoạt động", Lucide.CircleCheck)
                0 -> Triple(Color(0xFFF44336), "Không hoạt động", Lucide.CircleX)
                else -> Triple(Color.Gray, "Không xác định", Lucide.CircleAlert)
            }

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                Icon(statusIcon, contentDescription = "Trạng thái", tint = color, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text("Trạng thái: ", fontWeight = FontWeight.Medium)
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(Modifier.width(4.dp))
                Text(statusText, color = color, fontWeight = FontWeight.Bold)
            }

            // Chi tiết khi mở rộng
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.SwapHorizontalCircle,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("Chuyển đến phòng", fontWeight = FontWeight.Bold)
                    }

                    // Dropdown chọn phòng
                    ExposedDropdownMenuBox(
                        expanded = isExpanded,
                        onExpandedChange = { isExpanded = !isExpanded }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .padding(vertical = 8.dp),
                            value = selectedTenPhong,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color.White,
                                focusedContainerColor = Color.White,
                                focusedBorderColor = Color.Black,
                                unfocusedBorderColor = Color.Black,
                                focusedTextColor = Color.Black,
                                unfocusedTextColor = Color.Black
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = isExpanded,
                            onDismissRequest = { isExpanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            danhSachPhongMay.forEach { phongMay ->
                                DropdownMenuItem(
                                    text = {
                                        Text(phongMay.TenPhong, fontWeight = FontWeight.SemiBold,color= Color.Black)
                                    },
                                    onClick = {
                                        selectdMaPhong = phongMay.MaPhong
                                        isExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Nút chuyển máy
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        onClick = {
                            if (maPhongState.value == maytinh.MaPhong) {
                                showDialog = true
                                return@Button
                            }

                            val ngayChuyen = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            val mayTinhMoi = maytinh.copy(
                                TenMay = "MAY${maPhongState.value}",
                                MaPhong = maPhongState.value,
                                TrangThai = 1,
                                ViTri = if (maPhongState.value == "KHO") "" else maytinh.ViTri
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
                            expanded = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xff1B8DDE)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Chuyển máy", fontWeight = FontWeight.ExtraBold, color = Color.White)
                    }

                    // Dialog cảnh báo
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            confirmButton = {
                                TextButton(onClick = { showDialog = false }) {
                                    Text("OK", color = Color(0xff1B8DDE), fontWeight = FontWeight.Bold)
                                }
                            },
                            title = { Text("Thông báo", fontWeight = FontWeight.Bold, color = Color(0xff1B8DDE)) },
                            text = { Text("Phòng mới phải khác phòng hiện tại!", fontWeight = FontWeight.SemiBold) },
                            containerColor = Color.White
                        )
                    }
                }
            }
        }
    }

}


