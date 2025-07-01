import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ckcitlabroom.viewmodels.ChiTietPhieuMuonViewModel
import com.example.ckcitlabroom.viewmodels.MayTinhViewModel

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
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (danhSachChiTiet.isEmpty()) {
                item {
                    Text(
                        "Không có máy tính nào đã chuyển đi",
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



