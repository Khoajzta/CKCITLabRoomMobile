import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ChucNang(
    val name: String,
    val icon: ImageVector,
    val Click: () -> Unit
)

@Composable
fun CardChucNang(chucnang: ChucNang) {
    Card(
        onClick = chucnang.Click,
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color(0x331B8DDE),
                spotColor = Color(0x551B8DDE)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp), // ↓ Giảm khoảng cách giữa các phần tử
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon với hiệu ứng nổi và gradient nền
            Box(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .size(50.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF1B8DDE), Color(0xFF6DC8F3))
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = chucnang.icon,
                    contentDescription = chucnang.name,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            // Text gần hơn
            Text(
                text = chucnang.name,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = Color(0xFF1B1B1B),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

        }
    }
}



