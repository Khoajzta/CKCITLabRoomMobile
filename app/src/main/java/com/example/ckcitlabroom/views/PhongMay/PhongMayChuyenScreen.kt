import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhongMayChuyenScreen(
    maphong: String,
    phongMayViewModel: PhongMayViewModel,
    mayTinhViewModel: MayTinhViewModel,
    lichSuChuyenMayViewModel: LichSuChuyenMayViewModel,
) {
    val coroutineScope = rememberCoroutineScope()
    val danhSachMayTinhtheophong = mayTinhViewModel.danhSachAllMayTinhtheophong
    val danhSachPhongMay = phongMayViewModel.danhSachAllPhongMay
    val selectedMayTinhs = mayTinhViewModel.danhSachMayTinhDuocChon

    var selectedMaPhongMoi by remember { mutableStateOf<String?>(null) }
    var isExpanded by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        mayTinhViewModel.getMayTinhByPhong(maphong)
        phongMayViewModel.getAllPhongMay()
    }


    DisposableEffect(Unit) {
        onDispose {
            mayTinhViewModel.stopPollingMayTinhTheoPhong()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Tiêu đề
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Danh Sách Máy Tính Phòng",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = Color(0xFF1B8DDE)
            )
        }

        // Danh sách máy
        LazyColumn(modifier = Modifier.weight(1f)) {
            if (danhSachMayTinhtheophong.isNullOrEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Chưa có máy tính nào", color = Color.Gray)
                    }
                }
            } else {
                items(danhSachMayTinhtheophong) { maytinh ->
                    CardMayTinhChuyenMuon2(
                        maytinh = maytinh,
                        phongMayViewModel = phongMayViewModel,
                        selectedMayTinhs = selectedMayTinhs,
                        onLongPress = {
                            if (!selectedMayTinhs.contains(maytinh)) {
                                selectedMayTinhs.add(maytinh)
                            } else {
                                selectedMayTinhs.remove(maytinh)
                            }
                        }
                    )
                }
            }
        }

        // Khung thông tin và nút chuyển
        Card(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
                .shadow(8.dp, shape = RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Danh sách máy",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )

                    Text(
                        "${selectedMayTinhs.size}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )

                }

                Spacer(Modifier.height(8.dp))

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
                    thickness = 2.dp,
                    color = Color(0xFFDDDDDD),
                )
                CustomDropdownSelector(
                    label = "Phòng muốn chuyển đến",
                    items = danhSachPhongMay.filter { it.MaPhong != maphong },
                    selectedItem = danhSachPhongMay.find { it.MaPhong == selectedMaPhongMoi },
                    itemLabel = { it.TenPhong },
                    onItemSelected = { selectedMaPhongMoi = it.MaPhong },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 150.dp)
                ) {
                    items(selectedMayTinhs) { mayTinh ->
                        Column {
                            Text("Mã máy: ${mayTinh.MaMay}", fontWeight = FontWeight.SemiBold)
                            Text("Tên máy: ${mayTinh.TenMay}", color = Color.Black)
                            Divider(modifier = Modifier.padding(vertical = 6.dp))
                        }
                    }
                }


                Button(
                    onClick = {
                        when {
                            selectedMayTinhs.isEmpty() -> {
                                errorMessage = "Vui lòng chọn ít nhất một máy tính để chuyển."
                                showErrorDialog = true
                            }

                            selectedMaPhongMoi == null -> {
                                errorMessage = "Vui lòng chọn phòng đích để chuyển máy."
                                showErrorDialog = true
                            }

                            else -> {
                                showDialog = true
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xFF1B8DDE)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Chuyển máy", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Dialog xác nhận
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Xác nhận chuyển máy", fontWeight = FontWeight.Bold, color = Color.Black) },
            containerColor = Color.White,
            text = {
                Text(
                    "Bạn có chắc muốn chuyển ${selectedMayTinhs.size} máy đến phòng ${danhSachPhongMay.find { it.MaPhong == selectedMaPhongMoi }?.TenPhong}?",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val ngayChuyen = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            selectedMayTinhs.forEach { mayTinh ->
                                val mayTinhCapNhat = mayTinh.copy(
                                    MaPhong = selectedMaPhongMoi!!,
                                    TenMay = "MAY${selectedMaPhongMoi}"
                                )
                                mayTinhViewModel.updateMayTinh(mayTinhCapNhat)

                                val lichSu = LichSuChuyenMay(
                                    MaLichSu = 0,
                                    MaPhongCu = mayTinh.MaPhong,
                                    MaPhongMoi = selectedMaPhongMoi!!,
                                    NgayChuyen = ngayChuyen,
                                    MaMay = mayTinh.MaMay
                                )
                                lichSuChuyenMayViewModel.createLichSuChuyenMay(lichSu)
                            }
                            mayTinhViewModel.clearDanhSachMayTinhDuocChon()
                            showDialog = false
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF1B8DDE))
                ) {
                    Text("Chuyển", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Dialog báo lỗi
    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Lỗi", fontWeight = FontWeight.Bold, color = Color.Black) },
            containerColor = Color.White,
            text = { Text(errorMessage, color = Color.Black) },
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

