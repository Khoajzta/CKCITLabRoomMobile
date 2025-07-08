import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CardThoiKhoaBieuTheoTuan(
    danhSach: List<LichHocTheoThuVaPhongItem>,
    click: () -> Unit
) {
    val scrollState = rememberScrollState()

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { click() },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFF1F5F9))
                .padding(6.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .horizontalScroll(scrollState)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFDBEAFE))
                    .padding(vertical = 6.dp)
            ) {
                Text(
                    text = "Phòng",
                    modifier = Modifier.width(80.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Color(0xFF1E40AF),
                    textAlign = TextAlign.Center
                )

                listOf("Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ Nhật").forEach {
                    Text(
                        text = it,
                        modifier = Modifier.width(240.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1E40AF),
                        textAlign = TextAlign.Center
                    )
                }
            }


            danhSach.forEachIndexed { index, item ->
                val bgColor = if (index % 2 == 0) Color.White else Color(0xFFF0F4FF)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState)
                        .background(bgColor)
                        .clip(RoundedCornerShape(10.dp))
                        .padding(vertical = 4.dp)
                ) {
                    // Tên phòng
                    Text(
                        text = item.TenPhong,
                        modifier = Modifier
                            .width(80.dp)
                            .padding(horizontal = 4.dp),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = Color(0xFF0F172A),
                        textAlign = TextAlign.Center
                    )

                    listOf(
                        item.Thu2, item.Thu3, item.Thu4,
                        item.Thu5, item.Thu6, item.Thu7, item.ChuNhat
                    ).forEach { monCaList ->
                        Card(
                            modifier = Modifier
                                .width(240.dp)
                                .padding(horizontal = 2.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (monCaList.isNotEmpty()) Color(0xFFDBEAFE) else Color(
                                    0xFFE2E8F0
                                )
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (monCaList.isNotEmpty()) Color(0xFF93C5FD) else Color(0xFFCBD5E1)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(6.dp)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (monCaList.isEmpty()) {
                                    Text(
                                        text = "—",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                } else {
                                    monCaList.forEach {
                                        Text(
                                            text = "${it.MaMonHoc} - Ca ${it.MaCaHoc} - ${it.TenGiangVien}",
                                            fontSize = 11.sp,
                                            color = Color(0xFF1E3A8A),
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}







