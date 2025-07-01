import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChiTietLichSuSuaMayScreen(
    mamay: String,
    phieuSuaChuaViewModel: PhieuSuaChuaViewModel,
    lichSuSuaMayViewModel: LichSuSuaMayViewModel,
) {
    var danhsachphieusuachua = phieuSuaChuaViewModel.danhSachAllPhieuSuaChuaTheoMa

    var solan = danhsachphieusuachua.count()

    LaunchedEffect(mamay) {
        phieuSuaChuaViewModel.getPhieuSuaChuaTheoMaMay(mamay)
        lichSuSuaMayViewModel.getLichSuTheoMaMay(mamay)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Số lần sửa: $solan",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp,
                color = Color(0xFF1B8DDE)
            )

        }

        HorizontalDivider(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            thickness = 2.dp,
            color = Color(0xFF1B8DDE),
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (danhsachphieusuachua == null || danhsachphieusuachua.isEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Chưa có lịch sử sửa máy",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                }
            } else {
                items(danhsachphieusuachua) { phieusuachua ->
                    CardChiTietLichSuSuaMay(phieusuachua, lichSuSuaMayViewModel)
                }
            }
        }
    }
}