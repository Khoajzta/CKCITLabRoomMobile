import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DisplaySettings
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Mouse
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.lapstore.viewmodels.MayTinhViewModel


@Composable
fun CardMayTinh(maytinh: MayTinh, navController: NavHostController) {

    Log.d("MaMay",maytinh.MaMay)

    val maytinhViewModel: MayTinhViewModel = viewModel()

    var expanded by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) } // trạng thái hiển thị dialog

    Card(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth()
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
        Column(modifier = Modifier.padding(12.dp)) {

            // 4 thông tin đầu
            Text("Mã Máy: ${maytinh.MaMay}", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Vị Trí: ${maytinh.ViTri}", fontSize = 16.sp)
            Text("Phòng: ${maytinh.MaPhong}")
            val (color, statusText) = when (maytinh.TrangThai) {
                1 -> Color(0xFF4CAF50) to "Hoạt động"
                0 -> Color(0xFFF44336) to "Không hoạt động"
                else -> Color.Gray to "Không xác định"
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Trạng thái: ")
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(color)
                )
                Spacer(Modifier.width(4.dp))
                Text(statusText, color = color)
            }

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Text("Main: ${maytinh.Main}", fontSize = 16.sp)
                    Text("CPU: ${maytinh.CPU}", fontSize = 16.sp)
                    Text("RAM: ${maytinh.RAM}")
                    Text("VGA: ${maytinh.VGA}")
                    Text("Màn Hình: ${maytinh.ManHinh}")
                    Text("Bàn Phím: ${maytinh.BanPhim}")
                    Text("Chuột: ${maytinh.Chuot}")
                    Text("HDD: ${maytinh.HDD}")
                    Text("SSD: ${maytinh.SSD}")

                    Spacer(Modifier.height(8.dp))

                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            navController.navigate(NavRoute.EDITMAYTINH.route + "?mamay=${maytinh.MaMay}")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xff1B8DDE))
                    ) {
                        Text("Chỉnh Sửa", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            showConfirmDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xffAC0808))
                    ) {
                        Text("Xóa", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }

    // Dialog xác nhận xóa
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Xác nhận") },
            text = { Text("Bạn có chắc chắn muốn xóa máy tính này không?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        maytinhViewModel.deleteMayTinh(maytinh.MaMay)
                        showConfirmDialog = false
                    }
                ) {
                    Text("Xóa", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmDialog = false }
                ) {
                    Text("Hủy")
                }
            }
        )
    }
}


