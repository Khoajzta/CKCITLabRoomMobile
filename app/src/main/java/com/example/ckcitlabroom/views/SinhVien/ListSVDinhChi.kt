import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.models.LopHoc
import com.example.ckcitlabroom.viewmodels.LopHocViewModel

@Composable
fun ListSinhVienDinhChi(
    navController: NavHostController,
    sinhVienViewModel: SinhVienViewModel,
    lopHocViewModel: LopHocViewModel
) {
    // Gọi API lấy danh sách lớp học
    LaunchedEffect(Unit) {
        lopHocViewModel.getAllLopHoc()
    }

    val danhSachLop = lopHocViewModel.danhSachAllLopHoc
    val danhSachSinhVien = sinhVienViewModel.danhSachAllSinhVien
    val danhSachSinhVienDinhChi = danhSachSinhVien.filter { it.TrangThai == 0 }

    var selectedLop by remember { mutableStateOf<LopHoc?>(null) }

    // Gọi API lấy sinh viên khi chọn lớp
    LaunchedEffect(selectedLop) {
        selectedLop?.let {
            Log.d("SinhVienDinhChiScreen", "Đã chọn lớp: ${it.TenLopHoc}")
            sinhVienViewModel.getSinhVienByMaLop(it.MaLopHoc)
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Tiêu đề
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Danh Sách Sinh Viên Bị Đình Chỉ",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = Color(0xFF1B8DDE)
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth(),
            thickness = 2.dp,
            color = Color(0xFF1B8DDE),
        )

        // Dropdown chọn lớp học
        CustomDropdownSelectorBasic(
            label = "Chọn lớp",
            items = danhSachLop,
            selectedItem = selectedLop,
            itemLabel = { it.TenLopHoc },
            onItemSelected = { selectedLop = it }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Danh sách sinh viên
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            when {
                selectedLop == null -> {
                    item {
                        CenteredMessage("Vui lòng chọn lớp học.")
                    }
                }
                danhSachSinhVienDinhChi.isEmpty() -> {
                    item {
                        CenteredMessage("Không có sinh viên bị đình chỉ trong lớp này.")
                    }
                }
                else -> {
                    items(danhSachSinhVienDinhChi) { sv ->
                        CardSinhVien(
                            sinhVien = sv,
                            navController = navController,
                            sinhVienViewModel = sinhVienViewModel,
                            maLop = selectedLop?.MaLopHoc.orEmpty()
                        )
                    }
                }
            }
        }
    }

    // Debug log
    LaunchedEffect(danhSachLop) {
        Log.d("SinhVienDinhChiScreen", "Đã load ${danhSachLop.size} lớp học")
    }
}

