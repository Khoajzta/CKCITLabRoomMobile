import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AccountScreen(
    magiangvien:String,
    masinhvien:String,
    giangVienViewModel: GiangVienViewModel
){
    var giangvien = giangVienViewModel.giangvienSet


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        // Thẻ thông tin
//        var sinhvien = SinhVien(
//            "0306221353",
//            "Nguyễn Văn Minh Mẫn",
//            "12/01/2004",
//            "Nam",
//            "CĐTH22DDD",
//            "LOP01",
//            "12345678",
//            2
//        )
//        CardSinhVien(sinhvien)


        if(giangvien!=null)
            CardGiangVienInfo(giangvien)
    }
}

