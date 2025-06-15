import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
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
import com.example.lapstore.viewmodels.ChiTietDonNhapyViewModel
import com.example.lapstore.viewmodels.LichSuChuyenMayViewModel
import com.example.lapstore.viewmodels.MayTinhViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChiTietDonNhapChuyenScreen(
    maDonNhap: String,
    maPhongHienTai: String,
    chiTietDonNhapyViewModel: ChiTietDonNhapyViewModel,
    phongMayViewModel: PhongMayViewModel,
    mayTinhViewModel: MayTinhViewModel,
    lichSuChuyenMayViewModel: LichSuChuyenMayViewModel,
) {
    val coroutineScope = rememberCoroutineScope()

    val danhsachchitietdonnhap = chiTietDonNhapyViewModel.danhSachChiTietDonNhaptheoMaDonNhap
    val danhSachMayTinh = mayTinhViewModel.danhSachAllMayTinh

    val danhSachMayTheoDon = remember(danhsachchitietdonnhap, danhSachMayTinh) {
        val maMayTheoDon = danhsachchitietdonnhap.map { it.MaMay }
        danhSachMayTinh.filter { it.MaMay in maMayTheoDon && it.MaPhong == "KHOLUUTRU"}
    }

    var danhSachPhong = phongMayViewModel.danhSachAllPhongMay

    LaunchedEffect(Unit) {
        mayTinhViewModel.getAllMayTinh()
        chiTietDonNhapyViewModel.getChiTietDonNhapTheoMaDonNhap(maDonNhap)
        phongMayViewModel.getAllPhongMay()
    }

    DisposableEffect(Unit) {
        onDispose {
            mayTinhViewModel.stopPollingAllMayTinh()
            chiTietDonNhapyViewModel.stopPollingChiTietTheoMaDonNhap()
        }
    }

    val selectedMayTinhs = mayTinhViewModel.danhSachMayTinhDuocChon
    var selectedPhongMoi by remember { mutableStateOf<PhongMay?>(null) }

    var showDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }


    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            if(danhSachMayTheoDon.isNullOrEmpty()){
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Hết máy trong kho", color = Color.Gray)
                    }
                }
            }else{
                items(danhSachMayTheoDon) { mayTinh ->
                    CardMayTinhChuyenMuon2(
                        maytinh = mayTinh,
                        phongMayViewModel = phongMayViewModel,
                        selectedMayTinhs = selectedMayTinhs,
                        onLongPress = {
                            if (mayTinh in selectedMayTinhs) {
                                selectedMayTinhs.remove(mayTinh)
                            } else {
                                selectedMayTinhs.add(mayTinh)
                            }
                        }
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
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

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
                    thickness = 2.dp,
                    color = Color(0xFFDDDDDD),
                )

                Spacer(Modifier.height(8.dp))

                CustomDropdownSelector(
                    label = "Phòng muốn chuyển đến",
                    items = danhSachPhong.filter { it.MaPhong != maPhongHienTai },
                    selectedItem = selectedPhongMoi,
                    itemLabel = { it.TenPhong },
                    onItemSelected = { selectedPhongMoi = it }
                )

                LazyColumn(modifier = Modifier.heightIn(max = 160.dp)) {
                    items(selectedMayTinhs) {
                        Column {
                            Text("Mã máy: ${it.MaMay}", fontWeight = FontWeight.SemiBold)
                            Text("Tên máy: ${it.TenMay}")
                            Divider(modifier = Modifier.padding(vertical = 6.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B8DDE)),
                    shape = RoundedCornerShape(12.dp),
                    onClick = {
                        when {
                            selectedMayTinhs.isEmpty() -> {
                                errorMessage = "Vui lòng chọn máy tính cần chuyển."
                                showErrorDialog = true
                            }
                            selectedPhongMoi == null -> {
                                errorMessage = "Vui lòng chọn phòng muốn chuyển đến."
                                showErrorDialog = true
                            }
                            else -> showDialog = true
                        }
                    }
                ) {
                    Text("Xác nhận chuyển máy", color = Color.White)
                }
            }
        }
    }

    if (showDialog && selectedPhongMoi != null) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            containerColor = Color.White,
            title = { Text("Xác nhận chuyển máy", fontWeight = FontWeight.Bold, color = Color.Black) },
            text = {
                Text("Bạn có chắc chắn muốn chuyển ${selectedMayTinhs.size} máy sang phòng ${selectedPhongMoi!!.TenPhong}?", fontWeight = FontWeight.Bold, color = Color.Black)
            },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            val ngayChuyen = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                            selectedMayTinhs.forEach { may ->
                                val mayMoi = may.copy(MaPhong = selectedPhongMoi!!.MaPhong)
                                mayTinhViewModel.updateMayTinh(mayMoi)

                                lichSuChuyenMayViewModel.createLichSuChuyenMay(
                                    LichSuChuyenMay(
                                        MaLichSu = 0,
                                        MaPhongCu = may.MaPhong,
                                        MaPhongMoi = selectedPhongMoi!!.MaPhong,
                                        NgayChuyen = ngayChuyen,
                                        MaMay = may.MaMay
                                    )
                                )
                            }
                            mayTinhViewModel.clearDanhSachMayTinhDuocChon()
                            showDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFF1B8DDE))
                ) {
                    Text("Xác nhận", color = Color.White)
                }
            }
        )
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Thông báo", fontWeight = FontWeight.Bold) },
            text = { Text(errorMessage) },
            containerColor = Color.White,
            confirmButton = {
                Button(onClick = { showErrorDialog = false }) {
                    Text("OK", color = Color.White)
                }
            }
        )
    }
}


