import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SwapHorizontalCircle
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Card
import com.example.lapstore.viewmodels.ChiTietSuDungMayViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DiemDanhScreen(
    mamay: String,
    namHocViewModel: NamHocViewModel,
    tuanViewModel: TuanViewModel,
    chiTietSuDungMayViewModel: ChiTietSuDungMayViewModel
) {
    var isExpanded by remember { mutableStateOf(false) }
    var selectedTuan by remember { mutableStateOf<Int?>(null) }

    val danhSachNamHoc = namHocViewModel.danhSachAllNamHoc
    val maNamHocDangDienRa = danhSachNamHoc.find { it.TrangThai == 0 }?.MaNam

    val danhSachTuan = tuanViewModel.danhSachTuanTheoNamMap[maNamHocDangDienRa] ?: emptyList()

    // Gọi API lấy tuần khi biết năm học
    LaunchedEffect(maNamHocDangDienRa) {
        if (maNamHocDangDienRa != null) {
            tuanViewModel.getTuanByMaNam(maNamHocDangDienRa.toString())
        }
    }

    // Gọi API lấy năm học khi màn hình mở
    LaunchedEffect(Unit) {
        namHocViewModel.getAllNamHoc()
    }

    // Ngừng polling khi thoát màn hình
    DisposableEffect(Unit) {
        onDispose {
            namHocViewModel.stopPollingAllNamHoc()
        }
    }

    // Mặc định chọn tuần đầu tiên khi danh sách tuần load xong
    LaunchedEffect(danhSachTuan) {
        if (danhSachTuan.isNotEmpty() && selectedTuan == null) {
            selectedTuan = danhSachTuan.first().MaTuan.toInt()
        }
    }

    // Lấy tên tuần theo selectedTuan
    val tenTuanDangChon =
        danhSachTuan.find { it.MaTuan.toInt() == selectedTuan }?.TenTuan ?: "Chọn tuần"

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text("Chọn Tuần", fontWeight = FontWeight.Bold)
            }

            // Dropdown chọn tuần
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = { isExpanded = !isExpanded }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    value = tenTuanDangChon,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                ExposedDropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp) // nếu muốn giới hạn chiều cao tối đa, hoặc bỏ luôn nếu không cần
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                ) {
                    danhSachTuan.forEach { tuan ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    tuan.TenTuan,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black
                                )
                            },
                            onClick = {
                                selectedTuan = tuan.MaTuan.toInt()
                                isExpanded = false
                            }
                        )
                    }
                }

            }
        }
    }
}


