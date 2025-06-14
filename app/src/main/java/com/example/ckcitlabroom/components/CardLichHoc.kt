import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.ViewWeek
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.composables.icons.lucide.CircleAlert
import com.composables.icons.lucide.CircleCheck
import com.composables.icons.lucide.Clock
import com.composables.icons.lucide.Cpu
import com.composables.icons.lucide.HardDrive
import com.composables.icons.lucide.Keyboard
import com.composables.icons.lucide.Lucide
import com.composables.icons.lucide.MemoryStick
import com.composables.icons.lucide.Monitor
import com.composables.icons.lucide.MousePointer2

@Composable
fun CardLichHoc(
    lichHoc: LichHocRP,
    giangVien: GiangVien? = null,
    sinhvien: SinhVien? = null,
    navController: NavHostController
) {
    val isGiangVien = giangVien != null
    val tieuDe = if (isGiangVien) "Thông tin lịch dạy" else "Thông tin lịch học"
    val nhanCa = if (isGiangVien) "Ca dạy" else "Ca học"

    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {

            // Tiêu đề + icon mở rộng
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = tieuDe,
                    color = Color(0xFF1B8DDE),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Thu gọn" else "Mở rộng"
                )
            }

            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
                thickness = 2.dp,
                color = Color(0xFFDDDDDD),
            )

            InfoRow(icon = Icons.Filled.MenuBook, label = "Môn học", value = lichHoc.TenMonHoc.toString())
            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(icon = Icons.Filled.MeetingRoom, label = "Phòng", value = lichHoc.TenPhong.toString())
            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(icon = Icons.Filled.Schedule, label = nhanCa, value = lichHoc.TenCa.toString())
            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(icon = Icons.Filled.Today, label = "Thứ", value = lichHoc.Thu)
            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(
                icon = Icons.Filled.NoteAlt,
                label = "Thông báo",
                value = if (!lichHoc.GhiChu.isNullOrBlank()) lichHoc.GhiChu else "Không có ghi chú"
            )

            val (color, statusText, statusIcon) = when (lichHoc.TrangThai) {
                0 -> Triple(Color(0xFF1B8DDE), "Đã Kết Thúc", Lucide.CircleCheck)
                1 -> Triple(Color(0xFF4CAF50), "Đang Diễn Ra", Lucide.Clock)
                else -> Triple(Color.Gray, "Không xác định", Lucide.CircleAlert)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(
                    statusIcon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text("Trạng thái: ", fontWeight = FontWeight.ExtraBold)
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(Modifier.width(4.dp))
                Text(text = statusText, color = color, fontWeight = FontWeight.Bold)
            }

            // Nội dung mở rộng
            AnimatedVisibility(
                modifier = Modifier.fillMaxWidth(),
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    InfoRow(icon = Icons.Filled.Person, label = "Giảng viên", value = lichHoc.TenGiangVien)
                    InfoRow(icon = Icons.Filled.Event, label = "Ngày học", value = formatNgay(lichHoc.NgayDay))
                    InfoRow(icon = Icons.Filled.AccessTime, label = "Giờ bắt đầu", value = formatGio(lichHoc.GioBatDau))
                    InfoRow(icon = Icons.Filled.AccessTime, label = "Giờ kết thúc", value = formatGio(lichHoc.GioKetThuc))
                    InfoRow(icon = Icons.Filled.Groups, label = "Lớp", value = lichHoc.TenLopHoc.toString())
                    InfoRow(icon = Icons.Filled.DateRange, label = "Tuần", value = lichHoc.TenTuan.toString())
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        giangVien?.let {
                            val isChinhSua = it.MaLoaiTaiKhoan == 1
                            val buttonText = if (isChinhSua) "Chỉnh Sửa" else "Thông Báo"
                            val onClickAction = {
                                navController.navigate(NavRoute.EDITLICHHOC.route + "?malichhoc=${lichHoc.MaLichHoc}")
                            }

                            Box(modifier = Modifier.fillMaxWidth()) {
                                Button(
                                    onClick = onClickAction,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xff1B8DDE)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(buttonText, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }

                    }
                }
            }

        }
    }
}








