import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuanLyCauHinh() {
    val danhSachCauHinh = listOf(
        CauHinh(
            MaCauHinh = "CH001",
            Main = "Asus B660",
            CPU = "Intel i5-12400F",
            RAM = "16GB DDR4",
            VGA = "GTX 1660",
            ManHinh = "27 inch",
            BanPhim = "Logitech K120",
            Chuot = "Logitech M90",
            NgayNhap = "2025-05-20",
            TrangThai = 1
        ),
        CauHinh(
            MaCauHinh = "CH002",
            Main = "MSI B550",
            CPU = "Ryzen 5 5600X",
            RAM = "16GB DDR4",
            VGA = "RTX 3060",
            ManHinh = "24 inch",
            BanPhim = "Razer Cynosa",
            Chuot = "Razer DeathAdder",
            NgayNhap = "2025-05-18",
            TrangThai = 0
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Quản Lý Cấu Hình",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                color = Color.White
            )
            IconButton(
                onClick = {
                    // Xử lý thêm mới cấu hình
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = "Thêm cấu hình",
                    tint = Color.White
                )
            }
        }

        // Danh sách cấu hình
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(danhSachCauHinh) { cauHinh ->
                CardCauHinh(cauHinh)
            }
        }
    }
}
