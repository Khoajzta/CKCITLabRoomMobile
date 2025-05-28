
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapstore.api.ITLabRoomRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GiangVienViewModel : ViewModel() {

    var giangvien: GiangVien? by mutableStateOf(null)
        private set

    fun checkLogin(email: String, matkhau: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("TaiKhoanViewModel", "Gửi yêu cầu đến API với tên tài khoản: $email")
                giangvien = ITLabRoomRetrofitClient.giangVienAPIService.checkLogin(email,matkhau)
//                Log.d("TaiKhoanViewModel", "Dữ liệu trả về: $taikhoan")
            } catch (e: Exception) {
                Log.e("TaiKhoanViewModel", "Lỗi khi lấy dữ liệu từ API", e)
            }
        }
    }
}

