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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.viewmodels.LichSuChuyenMayViewModel
import com.example.ckcitlabroom.viewmodels.MayTinhViewModel
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
    navController: NavHostController
) {
    val scope = rememberCoroutineScope()

    /* ---------- DỮ LIỆU ---------- */
    val danhSachMayTinhtheophong = remember(mayTinhViewModel.danhSachAllMayTinhtheophong) {
        mayTinhViewModel.danhSachAllMayTinhtheophong.sortedBy { may ->
            Regex("""\d+""").find(may.TenMay)?.value?.toIntOrNull() ?: 0
        }
    }
    val danhSachPhongMay =
        phongMayViewModel.danhSachAllPhongMay.filter { it.LoaiPhong == 1 || it.LoaiPhong == 2 }
    val selectedMayTinhs = mayTinhViewModel.danhSachMayTinhDuocChon

    /* ---------- STATE ---------- */
    var selectedPhongMoi by remember { mutableStateOf<PhongMay?>(null) }
    var selectedMaPhongMoi by remember { mutableStateOf<String?>(null) }

    var showConfirm by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf("") }

    /* ---------- LOAD DỮ LIỆU ---------- */
    LaunchedEffect(maphong) {
        mayTinhViewModel.getMayTinhByPhong(maphong)
        phongMayViewModel.getAllPhongMay()
    }
    DisposableEffect(Unit) {
        onDispose { mayTinhViewModel.stopPollingMayTinhTheoPhong() }
    }

    /* ---------- UI ---------- */
    Column(Modifier.fillMaxSize()) {
        LazyColumn(Modifier.weight(1f)) {
            if (danhSachMayTinhtheophong.isEmpty()) {
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        contentAlignment = Alignment.Center
                    ) { Text("Chưa có máy tính nào", color = Color.Gray) }
                }
            } else {
                items(danhSachMayTinhtheophong) { may ->
                    CardMayTinhChuyenMuon2(
                        maytinh = may,
                        phongMayViewModel = phongMayViewModel,
                        selectedMayTinhs = selectedMayTinhs,
                        onLongPress = {
                            if (!selectedMayTinhs.contains(may)) selectedMayTinhs.add(may)
                            else selectedMayTinhs.remove(may)
                        }
                    )
                }
            }
        }

        Card(
            Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(Modifier.padding(16.dp)) {

                /* Số lượng máy đã chọn */
                Row(
                    Modifier.fillMaxWidth(),
                    Arrangement.SpaceBetween,
                    Alignment.CenterVertically
                ) {
                    Text("Danh sách máy", fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
                    Text(
                        "${selectedMayTinhs.size}",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                }

                Spacer(Modifier.height(8.dp))
                HorizontalDivider(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    thickness = 2.dp,
                    color = Color(0xFFDDDDDD)
                )

                /* Dropdown chọn phòng mới */
                CustomDropdownSelector(
                    label = "Phòng muốn chuyển đến",
                    items = danhSachPhongMay.filter { it.MaPhong != maphong },
                    selectedItem = selectedPhongMoi,
                    itemLabel = { it.TenPhong },
                    onItemSelected = { phong ->
                        selectedPhongMoi = phong
                        selectedMaPhongMoi = phong.MaPhong       // GÁN GIÁ TRỊ Ở ĐÂY
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(12.dp))

                /* Danh sách máy vừa chọn (trong bottom card) */
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .heightIn(max = 150.dp)
                ) {
                    items(selectedMayTinhs) { may ->
                        Column {
                            Text("Mã máy: ${may.MaMay}", fontWeight = FontWeight.SemiBold)
                            Text("Tên máy: ${may.TenMay}")
                            Divider(Modifier.padding(vertical = 6.dp))
                        }
                    }
                }

                /* Nút chuyển máy */
                Button(
                    onClick = {
                        when {
                            selectedMayTinhs.isEmpty() -> {
                                errorMsg = "Vui lòng chọn ít nhất một máy tính để chuyển."
                                showError = true
                            }

                            selectedPhongMoi == null -> {
                                errorMsg = "Vui lòng chọn phòng đích để chuyển máy."
                                showError = true
                            }

                            else -> showConfirm = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B8DDE))
                ) {
                    Text("Chuyển máy", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }

    /* ---------- DIALOG XÁC NHẬN ---------- */
    if (showConfirm) {
        AlertDialog(
            onDismissRequest = { showConfirm = false },
            containerColor = Color.White,
            title = {
                Text("Xác nhận chuyển máy", fontWeight = FontWeight.Bold, color = Color.Black)
            },
            text = {
                Text(
                    "Bạn có chắc muốn chuyển ${selectedMayTinhs.size} máy đến phòng ${selectedPhongMoi?.TenPhong}?",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            val phongMoi = selectedPhongMoi ?: return@launch
                            val maPhongMoi = phongMoi.MaPhong
                            val ngayChuyen =
                                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                            selectedMayTinhs.forEach { may ->
                                val mayCapNhat = may.copy(
                                    MaPhong = maPhongMoi,
                                    TenMay = if (phongMoi.LoaiPhong == 2) {
                                        may.MaMay
                                    } else {
                                        "MAY$maPhongMoi"
                                    }
                                )
                                mayTinhViewModel.updateMayTinh(mayCapNhat)

                                val lichSu = LichSuChuyenMay(
                                    MaLichSu = 0,
                                    MaPhongCu = may.MaPhong,
                                    MaPhongMoi = maPhongMoi,
                                    NgayChuyen = ngayChuyen,
                                    MaMay = may.MaMay
                                )
                                lichSuChuyenMayViewModel.createLichSuChuyenMay(lichSu)
                            }

                            mayTinhViewModel.clearDanhSachMayTinhDuocChon()
                            mayTinhViewModel.getMayTinhByPhong(maphong) // refresh
                            navController.popBackStack()
                            showConfirm = false
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B8DDE))
                ) { Text("Chuyển", color = Color.White, fontWeight = FontWeight.Bold) }
            }
        )
    }

    /* ---------- DIALOG LỖI ---------- */
    if (showError) {
        AlertDialog(
            onDismissRequest = { showError = false },
            containerColor = Color.White,
            title = { Text("Thông báo", fontWeight = FontWeight.Bold, color = Color.Black) },
            text = { Text(errorMsg, color = Color.Black) },
            confirmButton = {
                Button(
                    onClick = { showError = false },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF1B8DDE))
                ) { Text("OK", color = Color.White) }
            }
        )
    }
}


