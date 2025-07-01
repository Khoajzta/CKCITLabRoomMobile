import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.icons.lucide.CalendarClock
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.Clock
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.Play
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

@Composable
fun CardLichHocListSV(
    lichHoc: LichHocRP,
    click: () -> Unit
) {

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")      // đổi nếu cần
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")        // "09:00:00"

    val lessonDate = try {
        LocalDate.parse(lichHoc.NgayDay, dateFormatter)
    } catch (e: DateTimeParseException) {
        null
    }        // chống crash

    val timeStart = LocalTime.parse(lichHoc.GioBatDau, timeFormatter)
    val timeEnd = LocalTime.parse(lichHoc.GioKetThuc, timeFormatter)
    val today = LocalDate.now()
    val now = LocalTime.now()

    val (color, statusText, statusIcon) = if (lessonDate == null) {
        // Ngày sai định dạng
        Triple(Color.Gray, "Ngày không hợp lệ", Lucide.CircleAlert)
    } else when {
        lessonDate.isAfter(today) -> Triple(
            Color(0xFFFFA000),            // cam nhạt
            "Sắp diễn ra",
            Lucide.CalendarClock          // biểu tượng lịch-đồng hồ
        )

        lessonDate.isBefore(today) -> Triple(
            Color(0xFF9E9E9E),            // xám
            "Đã kết thúc",
            Lucide.CircleCheck
        )

        /* ----- Cùng ngày -> so sánh giờ ----- */
        now.isBefore(timeStart) -> Triple(
            Color(0xFFFFA000),            // cam nhạt
            "Sắp diễn ra",
            Lucide.Clock
        )

        now.isAfter(timeEnd) -> Triple(
            Color(0xFF9E9E9E),            // xám
            "Đã kết thúc",
            Lucide.CircleCheck
        )

        else -> Triple(
            Color(0xFF4CAF50),            // xanh lá
            "Đang diễn ra",
            Lucide.Play              // biểu tượng play
        )
    }

    Card(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .width(360.dp),
        onClick = { click() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                /* ---------- Tên môn ---------- */
                Text(
                    text = lichHoc.TenMonHoc,
                    color = Color(0xFF1B8DDE),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                )

                /* ---------- Chip trạng thái ---------- */
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = color.lighten(0.85f),            // màu nền = màu chữ pha nhạt
                                shape = RoundedCornerShape(12.dp)        // bo 4 góc
                            )
                            .clip(RoundedCornerShape(12.dp))             // đảm bảo góc được bo
                            .padding(horizontal = 8.dp, vertical = 2.dp) // padding trong chip
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = statusIcon,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = color                                 // icon cùng màu chữ
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = statusText,
                                color = color,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                thickness = 2.dp,
                color = Color(0xFFDDDDDD),
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoRow(icon = Icons.Filled.MeetingRoom, label = "Phòng", value = lichHoc.TenPhong)
                Text(
                    text = "${formatGio(lichHoc.GioBatDau)} - ${formatGio(lichHoc.GioKetThuc)}",
                    color = Color(0xFF9E9E9E),
                )
            }
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = null,
                    tint = Color(0xFF1B8DDE),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = lichHoc.TenTuan,
                    fontWeight = FontWeight.ExtraBold,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,

                    ) {

                    Text(
                        text = ", ${lichHoc.Thu}",
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = ", ${lichHoc.TenCa}",
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            InfoRow(
                icon = Icons.Filled.Event,
                label = "Ngày dạy",
                value = formatNgay(lichHoc.NgayDay)
            )
            InfoRow(icon = Icons.Filled.Groups, label = "Lớp", value = lichHoc.TenLopHoc)
            Spacer(modifier = Modifier.height(4.dp))
            InfoRow(
                icon = Icons.Filled.NoteAlt,
                label = "Thông báo",
                value = if (!lichHoc.GhiChu.isNullOrBlank()) lichHoc.GhiChu else "Không có thông báo"
            )
        }
    }
}








