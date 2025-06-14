import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    val thuList = listOf("Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật")

    // Gom dữ liệu thành map<MaCaHoc, map<Thu, List<LichHocRP>>>
    val lichHocTheoCaThu = remember(lichHocList, caHocList) {
        caHocList.associate { ca ->
            val thuMap = thuList.associateWith { thu ->
                lichHocList.filter { it.MaCaHoc == ca.MaCaHoc && it.Thu == thu }
            }
            ca.MaCaHoc to thuMap
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = {
            navController.navigate(
                NavRoute.CHITIETLICHHOCTHEOTUAN.route + "?matuan=${lichHocList.getOrNull(0)?.MaTuan}"
            )
        },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {

            // Header các thứ
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                Text(
                    "Ca \\ Thứ",
                    modifier = Modifier
                        .width(80.dp)
                        .padding(4.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )

                thuList.forEach { thu ->
                    Text(
                        thu,
                        modifier = Modifier
                            .width(100.dp)
                            .padding(4.dp),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                thickness = 1.dp,
                color = Color(0XFF1B8DDE)
            )

            // Nội dung TKBT theo từng ca
            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                Column {
                    caHocList.forEach { ca ->
                        Row(
                            modifier = Modifier.padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Hiển thị tên ca học
                            Text(
                                text = ca.TenCa,
                                modifier = Modifier
                                    .width(80.dp)
                                    .padding(4.dp),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )

                            // Dữ liệu từng thứ
                            thuList.forEach { thu ->
                                val lichList = lichHocTheoCaThu[ca.MaCaHoc]?.get(thu) ?: emptyList()
                                Box(
                                    modifier = Modifier
                                        .width(100.dp)
                                        .height(70.dp)
                                        .padding(2.dp)
                                        .border(1.dp, Color(0XFF1B8DDE), RoundedCornerShape(12.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (lichList.isNotEmpty()) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.padding(2.dp)
                                        ) {
                                            lichList.forEach { lich ->
                                                Text(
                                                    text = "${lich.MaMonHoc} - ${lich.TenPhong}",
                                                    fontSize = 10.sp,
                                                    textAlign = TextAlign.Center,
                                                    maxLines = 2
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



