import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lapstore.viewmodels.ChiTietPhieuMuonViewModel
import com.example.lapstore.viewmodels.MayTinhViewModel

@Composable
fun ChiTietPhieuMuonMay(
    maphieumuon: String,
    chiTietPhieuMuonViewModel: ChiTietPhieuMuonViewModel,
    mayTinhViewModel: MayTinhViewModel,
    phongMayViewModel: PhongMayViewModel
) {
    val danhSachChiTiet by remember { derivedStateOf { chiTietPhieuMuonViewModel.danhSachChiTietPhieuMuonTheoMaPhieu } }
    val danhSachMayTinh by remember { derivedStateOf { mayTinhViewModel.danhSachAllMayTinh } }

    val mayTinhDaMuon = remember(danhSachChiTiet, danhSachMayTinh) {
        danhSachMayTinh.filter { may ->
            danhSachChiTiet.any { ct -> ct.MaMay == may.MaMay }
        }
    }

    LaunchedEffect(maphieumuon) {
        chiTietPhieuMuonViewModel.getChiTietPhieuMuonTheoMaPhieuOnce(maphieumuon)
        mayTinhViewModel.getAllMayTinh()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Danh Sách Máy Cho Mượn",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = Color(0xFF1B8DDE)
            )
        }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (mayTinhDaMuon.isEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Không có máy tính nào",
                            color = Color.Black,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                items(mayTinhDaMuon) { mayTinh ->
                    CardMayTinhLichSu(mayTinh, phongMayViewModel, click = {})
                }
            }
        }
    }
}


