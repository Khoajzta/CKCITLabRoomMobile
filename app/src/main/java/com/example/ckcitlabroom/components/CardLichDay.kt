import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CardLichHoc(
    gv:String,
    ca:String,
    phong:String,
    thu:String,
    lop:String,
    tuan:String,
    mon:String,
    ngay:String
){
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.width(400.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 15.dp),
        shape = RoundedCornerShape(30.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.width(230.dp)
                ) {
                    Text("GV : ${gv}", fontWeight = FontWeight.ExtraBold)
                }

                Row(modifier = Modifier.fillMaxWidth().padding(start = 20.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text("Ca: ${ca}", fontWeight = FontWeight.ExtraBold)
                }

            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.width(230.dp)
                ) {
                    Text("Phòng: ${phong}", fontWeight = FontWeight.ExtraBold)
                }

                Row(modifier = Modifier.fillMaxWidth().padding(start = 20.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text("Thứ: ${thu}", fontWeight = FontWeight.ExtraBold)
                }

            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.width(230.dp)
                ) {
                    Text("Lớp: ${lop}", fontWeight = FontWeight.ExtraBold)
                }

                Row(modifier = Modifier.fillMaxWidth().padding(start = 20.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text("Tuần: ${tuan}", fontWeight = FontWeight.ExtraBold)
                }

            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.width(230.dp)
                ) {
                    Text("Môn: ${mon}", fontWeight = FontWeight.ExtraBold)
                }

                Row(modifier = Modifier.fillMaxWidth().padding(start = 20.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                )
                {
                    Text("Ngày: ${ngay}", fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}