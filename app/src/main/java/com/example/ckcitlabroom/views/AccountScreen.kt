import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AccountScreen(){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        // Thẻ thông tin
        var sinhvien = SinhVien(
            "0306221353",
            "Nguyễn Văn Minh Mẫn",
            "12/01/2004",
            "Nam",
            "CĐTH22DDD",
            "123456",
            1,
            1
        )
//        CardSinhVien(sinhvien)

        val giangVien = GiangVien(
            MaGV = "GV001",
            TenGiangVien = "Nguyễn Văn A",
            NgaySinh = "1985-07-15",
            GioiTinh = "Nam",
            MatKhau = "12345678",
            Email = "abc@gmail.com",
            MaLoaiTaiKhoan = 2,
            TrangThai = 1
        )

        CardGiangVienInfo(giangVien)
    }
}

