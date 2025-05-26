import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class LichHoc(
    val gv: String,       // Giáo viên
    val ca: String,       // Ca học
    val phong: String,    // Phòng học
    val thu: String,      // Thứ (1,2,3,... hoặc thứ 2, thứ 3)
    val lop: String,      // Lớp học
    val tuan: String,     // Tuần học
    val mon: String,      // Môn học
    val ngay: String      // Ngày học (vd: 25/05/2025)
)


@Composable
fun HomeScreen() {
    val danhSachLichHoc = listOf(
        LichHoc("Lê Viết Hoàng Nguyên", "1", "F7.1", "4", "CĐ TH22 D", "34", "CSDL", "25/05/2025"),
        LichHoc("Nguyễn Văn A", "2", "F7.2", "5", "CĐ TH22 E", "35", "Toán", "26/05/2025"),
        // Thêm các lịch học khác...
    )
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Lịch Dạy",
                color = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(danhSachLichHoc) { lichHoc ->
                // Giả sử bạn muốn show nhiều trường, ở đây tạm dùng dữ liệu mẫu:
                CardLichHoc(
                    gv = lichHoc.gv,
                    ca = lichHoc.ca,
                    phong = lichHoc.phong,
                    thu = lichHoc.thu,
                    lop = lichHoc.lop,
                    tuan = lichHoc.tuan,
                    mon = lichHoc.mon,
                    ngay = lichHoc.ngay
                )
            }
        }
    }
}
