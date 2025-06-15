import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.viewmodels.CaHocViewModel
import java.time.format.DateTimeFormatter

@Composable
fun CardThoiKhoaBieuTuan(
    navController: NavHostController,
    lichHocList: List<LichHocRP>,
    modifier: Modifier = Modifier,
) {
    val caHocViewModel: CaHocViewModel = viewModel()

    LaunchedEffect(Unit) {
        caHocViewModel.getAllCaHoc()
    }

    val caHocList = caHocViewModel.danhSachAllCaHoc
    val thuList = listOf("Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "CN")

    val lichHocTheoCaThu = remember(lichHocList, caHocList) {
        caHocList.associate { ca ->
            val thuMap = thuList.associateWith { thu ->
                lichHocList.filter { it.MaCaHoc == ca.MaCaHoc && it.Thu == thu }
            }
            ca.MaCaHoc to thuMap
        }
    }

    val scrollState = rememberScrollState()

    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = {
            navController.navigate(
                NavRoute.CHITIETLICHHOCTHEOTUAN.route + "?matuan=${lichHocList.getOrNull(0)?.MaTuan}"
            )
        },
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color(0xFFF9FAFB)) // nền nhẹ
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Column(modifier = Modifier.horizontalScroll(scrollState)) {

                // Header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Ca / Thứ",
                        modifier = Modifier
                            .width(80.dp)
                            .padding(6.dp),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 15.sp,
                        color = Color(0xFF1B8DDE)
                    )
                    thuList.forEach { thu ->
                        Text(
                            thu,
                            modifier = Modifier
                                .width(100.dp)
                                .padding(6.dp),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            color = Color(0xFF1B8DDE)
                        )
                    }
                }

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    thickness = 1.dp,
                    color = Color(0XFF1B8DDE)
                )

                // Nội dung từng ca học
                Column {
                    caHocList.forEachIndexed { index, ca ->
                        val bgColor = if (index % 2 == 0) Color(0xFFEAF4FF) else Color(0xFFFFFFFF)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp)) // Bo góc trước
                                .background(bgColor)             // Rồi mới tô màu nền
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = ca.TenCa,
                                modifier = Modifier
                                    .width(80.dp)
                                    .padding(horizontal = 8.dp),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp,
                                color = Color(0xFF374151)
                            )

                            thuList.forEach { thu ->
                                val lichList = lichHocTheoCaThu[ca.MaCaHoc]?.get(thu) ?: emptyList()
                                Box(
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(70.dp)
                                        .padding(horizontal = 8.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (lichList.isNotEmpty()) Color(0xFFD1E9FF) else Color(0xFFF0F4F8)
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = Color(0xFFB3DAF3),
                                            shape = RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (lichList.isNotEmpty()) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center,
                                            modifier = Modifier.padding(4.dp)
                                        ) {
                                            lichList.forEach { lich ->
                                                Text(
                                                    text = "${lich.MaMonHoc} - ${lich.TenPhong}",
                                                    fontSize = 10.sp,
                                                    textAlign = TextAlign.Center,
                                                    color = Color(0xFF1E3A8A),
                                                    maxLines = 2,
                                                    overflow = TextOverflow.Ellipsis,
                                                    fontWeight = FontWeight.SemiBold
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
    }
}









