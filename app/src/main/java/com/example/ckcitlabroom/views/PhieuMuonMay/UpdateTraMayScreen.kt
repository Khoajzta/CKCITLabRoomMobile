import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.composables.icons.lucide.ClipboardList
import com.composables.icons.lucide.Lucide
import com.example.lapstore.viewmodels.ChiTietPhieuMuonViewModel
import com.example.lapstore.viewmodels.LichSuChuyenMayViewModel
import com.example.lapstore.viewmodels.MayTinhViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun UpdateTraMayScreen(
    maphieumuon: String,
    mayTinhViewModel: MayTinhViewModel,
    phieuMuonMayViewModel: PhieuMuonMayViewModel,
    chitetPhieuMuonViewModel: ChiTietPhieuMuonViewModel,
    lichSuChuyenMayViewModel: LichSuChuyenMayViewModel
) {

    LaunchedEffect(maphieumuon) {
        chitetPhieuMuonViewModel.getChiTietPhieuMuonTheoMaPhieuOnce(maphieumuon)
        mayTinhViewModel.getAllMayTinh()
        phieuMuonMayViewModel.getPhieuMuonByMaPhieu(maphieumuon)
    }

    var phieumuon = phieuMuonMayViewModel.phieuMuonMay
    var danhsachallMayTinh = mayTinhViewModel.danhSachAllMayTinh

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarData = remember { mutableStateOf<CustomSnackbarData?>(null) }

    val chiTietList by remember { derivedStateOf { chitetPhieuMuonViewModel.danhSachChiTietPhieuMuonTheoMaPhieu } }

    val mayTinhList by remember {
        derivedStateOf {
            chiTietList.mapNotNull { chitiet ->
                danhsachallMayTinh.find { it.MaMay == chitiet.MaMay }
            }
        }
    }


    val tinhTrangMap = remember { mutableStateMapOf<String, String>() }

    val coroutineScope = rememberCoroutineScope()


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyColumn(
            modifier = Modifier.height(550.dp)
        ) {
            items(chiTietList) { chitiet ->
                val maMay = chitiet.MaMay
                val mayTinh = mayTinhViewModel.danhSachAllMayTinh.find { it.MaMay == maMay }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        InfoRow(icon = Lucide.ClipboardList, label = "Mã máy", value = maMay)
                        Spacer(modifier = Modifier.height(8.dp))
                        InfoRow(
                            icon = Lucide.ClipboardList,
                            label = "Tên máy",
                            value = mayTinh?.TenMay ?: "Không tìm thấy"
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = tinhTrangMap[maMay] ?: "",
                            onValueChange = { tinhTrangMap[maMay] = it },
                            label = { Text("Tình trạng sau khi trả") },
                            modifier = Modifier.fillMaxWidth(),
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
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState, modifier = Modifier.padding(16.dp)
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
                            androidx.compose.material3.Text("Đóng", color = Color.White)
                        }
                    }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (customData.type == SnackbarType.SUCCESS) Icons.Default.Info else Icons.Default.Warning,
                            contentDescription = null,
                            tint = if (customData.type == SnackbarType.SUCCESS) Color.Cyan else Color.Yellow,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        androidx.compose.material3.Text(text = customData.message)
                    }
                }
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val ngayHienTai = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                var maphongcu = ""
                if (phieumuon != null) {
                    maphongcu = phieumuon.MaPhong!!.toString()
                }


                val listChiTietUpdate = chiTietList.map { chitiet ->
                    chitiet.copy(TinhTrangTra = tinhTrangMap[chitiet.MaMay] ?: "")
                }

                coroutineScope.launch {
                    chitetPhieuMuonViewModel.updateNhieuChiTietPhieuMuon(listChiTietUpdate)
                    val message = chitetPhieuMuonViewModel.chiTietPhieuMuonUpdateResult
                    Log.d("Update", "Kết quả cập nhật: $message")

                    if(phieumuon!= null){
                        val phieuMuonCapNhat = phieumuon.copy(NgayTra = ngayHienTai, TrangThai = 2)

                        phieuMuonMayViewModel.updatePhieuMuonMay(phieuMuonCapNhat)
                    }

                    mayTinhList.forEach { mayTinh ->
                        val mayTinhCapNhat =
                            mayTinh.copy(MaPhong = "KHOLUUTRU", TenMay = "MAYKHOLUUTRU")
                        mayTinhViewModel.updateMayTinh(mayTinhCapNhat)

                        val lichSu = LichSuChuyenMay(
                            MaLichSu = 0,
                            MaMay = mayTinh.MaMay,
                            MaPhongCu = maphongcu,
                            MaPhongMoi = "KHOLUUTRU",
                            NgayChuyen = ngayHienTai
                        )
                        lichSuChuyenMayViewModel.createLichSuChuyenMay(lichSu)
                    }
                }

                coroutineScope.launch {
                    snackbarData.value = CustomSnackbarData(
                        message = "Cập nhật trả máy thành công!",
                        type = SnackbarType.SUCCESS
                    )
                    snackbarHostState.showSnackbar("Thành công")
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xff0b9adc))
        ) {
            androidx.compose.material3.Text(
                "Cập nhật trả máy",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
