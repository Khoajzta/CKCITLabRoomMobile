import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lapstore.viewmodels.MayTinhViewModel

@Composable
fun ListPhieuSuaBySinhVienScreen(
    phieuSuaChuaViewModel: PhieuSuaChuaViewModel,
    mayTinhViewModel: MayTinhViewModel,
    lichSuSuaMayViewModel: LichSuSuaMayViewModel,
    sinhVienViewModel: SinhVienViewModel,
    giangVienViewModel: GiangVienViewModel
){
    var sinhVien = sinhVienViewModel.sinhvienSet

    val danhsachAllPhieuSuaChua = phieuSuaChuaViewModel.danhSachAllPhieuSuaChua

    LaunchedEffect(Unit) {
        phieuSuaChuaViewModel.getAllPhieuSuaChua()
    }

    DisposableEffect(Unit) {
        onDispose {
            phieuSuaChuaViewModel.stopPollingPhieuSuaChua()
        }
    }

    val danhSachPhieuBySinhVien by remember(danhsachAllPhieuSuaChua, sinhVien) {
        derivedStateOf {
            danhsachAllPhieuSuaChua.filter {
                it.MaNguoiBaoHong == sinhVien?.MaSinhVien
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Danh Sách Phiếu Đã Tạo",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = Color(0xFF1B8DDE)
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(bottom = 8.dp).fillMaxWidth(),
            thickness = 2.dp,
            color = Color(0xFF1B8DDE),
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (danhSachPhieuBySinhVien.isEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Không có phiếu nào",
                            color = Color.Black,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                }
            } else {
                items(danhSachPhieuBySinhVien) { phieusuachua ->
                    CardPhieuSuaChua(
                        phieusuachua,
                        phieuSuaChuaViewModel,
                        lichSuSuaMayViewModel,
                        mayTinhViewModel,
                        giangVienViewModel
                    )
                }
            }
        }
    }
}