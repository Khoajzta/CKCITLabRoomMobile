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

        if(giangvien!=null)
            CardGiangVienInfo(giangvien)
    }
}

