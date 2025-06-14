import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun AccountScreen(
    giangVienViewModel: GiangVienViewModel,
    sinhVienViewModel: SinhVienViewModel,
    navController: NavHostController
){
    var giangvien = giangVienViewModel.giangvienSet
    var sinhvien = sinhVienViewModel.sinhvienSet

    var sinhviennew = sinhVienViewModel.sinhvien
    var giangviennew = giangVienViewModel.giangvien

    if(sinhvien!=null){
        LaunchedEffect(sinhvien) {
            sinhVienViewModel.getSinhVienByMaGOrEmail(sinhvien.MaSinhVien)
        }
    }

    if(giangvien!=null){
        LaunchedEffect(giangvien) {
            giangVienViewModel.getGiangVienByMaGOrEmail(giangvien.MaGV)
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        if(giangviennew!=null)
            CardGiangVienInfo(giangviennew,navController,giangVienViewModel)
        if(sinhviennew!=null)
            CardSinhVienInfo(sinhviennew, navController, sinhVienViewModel)
    }
}

