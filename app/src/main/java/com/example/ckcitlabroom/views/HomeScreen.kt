import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(modifier = Modifier.fillMaxWidth()) {
            Text("Lịch Dạy", color = Color.White, fontWeight = FontWeight.ExtraBold)
        }
        //danh sách lịch dạy
        CardLichHoc(gv = "Lê Viết Hoàng Nguyên", ca = "1", phong = "F7.1", thu = "4", lop = "CĐ TH22 D", tuan = "34", mon = "CSDL", ngay = "25/05/2025")
    }
}