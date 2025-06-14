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
import androidx.compose.ui.text.style.TextAlign
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

    LaunchedEffect(maphieumuon) {
        chiTietPhieuMuonViewModel.getChiTietPhieuMuonTheoMaPhieuOnce(maphieumuon)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            "Danh Sách Máy Cho Mượn",
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color(0xFF1B8DDE)
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (danhSachChiTiet.isEmpty()) {
                item {
                    Text(
                        "Không có máy tính nào",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                items(danhSachChiTiet) { chitiet ->
                    CardMayTinhMuon(
                        chiTietPhieuMuon = chitiet,
                        mayTinhViewModel = mayTinhViewModel,
                        phongMayViewModel = phongMayViewModel,
                        click = {}
                    )
                }
            }
        }
    }
}



