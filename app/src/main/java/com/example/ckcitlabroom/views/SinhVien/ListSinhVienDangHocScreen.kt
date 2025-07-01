import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.models.LopHoc
import com.example.ckcitlabroom.viewmodels.LopHocViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSinhVienDangHoc(
    navController: NavHostController,
    sinhVienViewModel: SinhVienViewModel,
    lopHocViewModel: LopHocViewModel
) {

    LaunchedEffect(Unit) {
        sinhVienViewModel.getAllSinhVien()
        lopHocViewModel.getAllLopHoc()
    }

    var selectedLop by remember { mutableStateOf<LopHoc?>(null) }

    val danhsachlophoc = lopHocViewModel.danhSachAllLopHoc.filter { it.TrangThai == 1 }

    LaunchedEffect(danhsachlophoc) {
        if (danhsachlophoc.isNotEmpty() && selectedLop == null) {
            selectedLop = danhsachlophoc.first()
        }
    }
    var danhsachsinhvien = sinhVienViewModel.danhSachAllSinhVien

    var sinhVienDangHoc =
        danhsachsinhvien.filter { it.MaLop == selectedLop?.MaLopHoc && it.TrangThai == 1 }

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
    ) {
        val primary = Color(0xFF1B8DDE)

        ExposedDropdownMenuBox(
            modifier = Modifier.padding(bottom = 8.dp),
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            Row(
                modifier = Modifier
                    .menuAnchor()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(horizontal = 8.dp, vertical = 6.dp)
                    .fillMaxWidth()
                    .wrapContentWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedLop?.TenLopHoc ?: "Chọn lớp",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = primary,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    if (expanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                containerColor = Color.White
            ) {
                danhsachlophoc.forEach { lop ->
                    DropdownMenuItem(
                        text = { Text(lop.TenLopHoc) },
                        onClick = {
                            selectedLop = lop
                            expanded = false
                        }
                    )
                }
            }
        }

        LazyColumn {
            if (sinhVienDangHoc.isEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Không có sinh viên nào đang học.",
                            color = Color.Black,
                            modifier = Modifier.padding(16.dp),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                items(sinhVienDangHoc) { sinhvien ->
                    CardSinhVien(sinhvien, navController, sinhVienViewModel, sinhvien.MaLop)
                }
            }
        }
    }
}


