import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.clickable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CardLichHoc(
    gv: String,
    mon: String,
    phong: String,
    ca: String,
    thu: String,
    lop: String,
    tuan: String,
    ngay: String
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .width(300.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                expanded = !expanded
            }
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 20,
                    easing = FastOutSlowInEasing
                )
            ),
        colors = CardDefaults.cardColors(containerColor = Color.White),


    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(text = "GV: $gv", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = "Môn: $mon", fontSize = 16.sp)
            Text(text = "Phòng: $phong", fontSize = 16.sp)
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Text(text = "Ca: $ca", fontSize = 16.sp)
                    Text(text = "Thứ: $thu", fontSize = 16.sp)
                    Text(text = "Lớp: $lop", fontSize = 16.sp)
                    Text(text = "Tuần: $tuan", fontSize = 16.sp)
                    Text(text = "Ngày: $ngay", fontSize = 16.sp)
                }
            }
        }
    }
}




