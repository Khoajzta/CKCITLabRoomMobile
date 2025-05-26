import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ckcitlabroom.R

@Composable
fun CardCauHinh(cauHinh: CauHinh) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
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
                .fillMaxSize()
                .padding(12.dp)
        ) {
            // Thông tin chính - luôn hiển thị
            Text(text = "Mã cấu hình: ${cauHinh.MaCauHinh}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = "Mainboard: ${cauHinh.Main}", fontSize = 16.sp)
            Text(text = "CPU: ${cauHinh.CPU}", fontSize = 16.sp)

            // Phần mở rộng ẩn/hiện
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(text = "RAM: ${cauHinh.RAM}", fontSize = 16.sp)
                    Text(text = "VGA: ${cauHinh.VGA}", fontSize = 16.sp)
                    Text(text = "Màn hình: ${cauHinh.ManHinh}", fontSize = 16.sp)
                    Text(text = "Bàn phím: ${cauHinh.BanPhim}", fontSize = 16.sp)
                    Text(text = "Chuột: ${cauHinh.Chuot}", fontSize = 16.sp)
                    Text(text = "Ngày nhập: ${cauHinh.NgayNhap}", fontSize = 16.sp)
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Trạng thái: ",
                            fontWeight = FontWeight.SemiBold
                        )
                        val (color, statusText) = when (cauHinh.TrangThai) {
                            1 -> Color(0xFF4CAF50) to "Hoạt động"      // xanh lá
                            0 -> Color(0xFFF44336) to "Không hoạt động" // đỏ
                            else -> Color(0xFF9E9E9E) to "Không xác định" // xám
                        }

                        // Hiện dấu chấm màu
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(color)
                                .align(Alignment.CenterVertically)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Hiện chữ trạng thái
                        Text(
                            text = statusText,
                            fontWeight = FontWeight.Normal,
                            color = color
                        )
                    }


                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {}
                    ) {
                        Text(
                            "Chỉnh Sửa"
                        )
                    }
                }
            }
        }
    }
}

