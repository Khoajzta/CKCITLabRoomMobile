import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.models.LopHoc
import com.example.ckcitlabroom.viewmodels.LopHocViewModel

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

    var sinhVienDangHoc = danhsachsinhvien.filter { it.MaLop == selectedLop?.MaLopHoc && it.TrangThai == 1 }

    Column {

        Text("Lớp", fontWeight = FontWeight.Bold, color = Color.Black)
        CustomDropdownSelector(
            label = "Chọn lớp",
            items = danhsachlophoc,
            selectedItem = selectedLop,
            itemLabel = { it.TenLopHoc },
            onItemSelected = { selectedLop = it }
        )

        LazyColumn {

            if(sinhVienDangHoc.isEmpty()){
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Không có sinh viên nào đang học.",
                            color = Color.Black,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }else{
                items(sinhVienDangHoc) { sinhvien->
                    CardSinhVien(sinhvien, navController, sinhVienViewModel, sinhvien.MaLop)
                }
            }
        }
    }
}


