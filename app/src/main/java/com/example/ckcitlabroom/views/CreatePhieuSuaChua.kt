import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.lapstore.viewmodels.MayTinhViewModel

@Composable
fun CreatePhieuSuaChua(
    mamay:String,
    mayTinhViewModel: MayTinhViewModel,
){
    Column {
        Text(
            text = "Tạo phiếu sửa chữa"
        )
    }
}