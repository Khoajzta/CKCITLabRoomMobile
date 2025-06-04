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


    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {

        if(giangvien!=null)
            CardGiangVienInfo(giangvien,navController,giangVienViewModel)
        if(sinhvien!=null)
            CardSinhVienInfo(sinhvien, navController, sinhVienViewModel)
    }
}

