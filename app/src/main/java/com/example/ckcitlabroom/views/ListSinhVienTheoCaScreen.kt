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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.Hash
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MapPin
import com.example.lapstore.viewmodels.ChiTietSuDungMayViewModel

@Composable
fun ListSinhVienTheoCa(
    maCa: String,
    maTuan: String,
    maphong: String,
    ngaySuDung: String,
    chiTietSuDungMayViewModel: ChiTietSuDungMayViewModel
){
    Log.d("maca",maCa)
    Log.d("tuan",maTuan)
    Log.d("phong",maphong)
    Log.d("ngay",ngaySuDung)

    LaunchedEffect(Unit) {
        chiTietSuDungMayViewModel.getAllChiTietSuDungMay()
    }

    DisposableEffect(Unit) {
        onDispose {
            chiTietSuDungMayViewModel.stopPollingAllChiTiet()
        }
    }

    var listAllchitiet = chiTietSuDungMayViewModel.danhSachAllChiTiet

    var listchitiettheolich = listAllchitiet.filter {
        it.MaCa == maCa.toInt() && it.MaPhong == maphong && it.NgaySuDung == ngaySuDung && it.MaTuan == maTuan.toInt()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // Tiêu đề
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Danh Sách Sinh Viên",
                fontWeight = FontWeight.ExtraBold,
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


        // Danh sách lịch
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (listchitiettheolich.isEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Không có sinh viên nào sử dụng.",
                            color = Color.Black,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            } else {
                items(listchitiettheolich) { chitiet ->
                    Card(
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            androidx.compose.material3.Text(
                                text = "Thông tin sinh viên sử dụng",
                                color = Color(0xFF1B8DDE),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )

                            HorizontalDivider(
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth(),
                                thickness = 2.dp,
                                color = Color(0xFFDDDDDD),
                            )

                            InfoRow(icon = Lucide.Hash, label = "MSSV", value = chitiet.MaSV)
                            Spacer(modifier = Modifier.height(8.dp))

                            InfoRow(icon = Lucide.Hash, label = "Mã Máy", value = chitiet.MaMay)
                            Spacer(modifier = Modifier.height(8.dp))

                            InfoRow(icon = Lucide.MapPin, label = "Vị Trí Máy", value = chitiet.ViTri.toString())
                        }
                    }
                }
            }
        }
    }
}

