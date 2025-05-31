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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SinhVienViewModel : ViewModel() {

    var sinhvien: SinhVien? by mutableStateOf(null)
        private set

    var danhSachAllSinhVien by mutableStateOf<List<SinhVien>>(emptyList())
        private set
    var sinhvienCreateResult by mutableStateOf("")
    var sinhvienUpdateResult by mutableStateOf("")
    var sinhvienDeleteResult by mutableStateOf("")

    private var pollingJob: Job? = null
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun stopPollingSinhVien() {
        pollingJob?.cancel()
        pollingJob = null
    }

//    fun checkLogin(email: String, matkhau: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                Log.d("TaiKhoanViewModel", "Gửi yêu cầu đến API với tên tài khoản: $email")
//                sinhvien = ITLabRoomRetrofitClient.sinhvienAPIService.checkLogin(email,matkhau)
////                Log.d("TaiKhoanViewModel", "Dữ liệu trả về: $taikhoan")
//            } catch (e: Exception) {
//                Log.e("TaiKhoanViewModel", "Lỗi khi lấy dữ liệu từ API", e)
//            }
//        }
//    }

    fun getAllSinhVien() {
        if (pollingJob != null) return

        pollingJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.sinhvienAPIService.getAllSinhVien()
                    if (response.sinhvien != null) {
                        danhSachAllSinhVien = response.sinhvien!!
                    } else {
                        danhSachAllSinhVien = emptyList()
                    }

                } catch (e: Exception) {
                    Log.e("SinhVienViewModel", "Polling lỗi", e)
                }
                delay(500)
            }
        }
    }


    fun createSinhVien(sinhVien: SinhVien) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.sinhvienAPIService.createSinhVien(sinhVien)
                }
                sinhvienCreateResult = response.message
            } catch (e: Exception) {
                sinhvienCreateResult = "Lỗi khi thêm giảng viên: ${e.message}"
                Log.e("GiangVienViewModel", "Lỗi khi thêm giảng viên: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

//    fun updateGiangVien(giangVien: GiangVien) {
//        viewModelScope.launch {
//            isLoading = true
//            try {
//                val response = withContext(Dispatchers.IO) {
//                    ITLabRoomRetrofitClient.giangVienAPIService.updateGiangVien(giangVien)
//                }
//                giangvienUpdateResult = response.message
//            } catch (e: Exception) {
//                giangvienUpdateResult = "Lỗi khi cập nhật máy tính: ${e.message}"
//                Log.e("MayTinhViewModel", "Lỗi khi cập nhật máy tính: ${e.message}")
//            } finally {
//                isLoading = false
//            }
//        }
//    }
//
    fun deleteSinhVien(masinhvien: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val body = mapOf("MaSinhVien" to masinhvien)
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.sinhvienAPIService.deleteSinhVien(body)
                }
                sinhvienDeleteResult = response.message

                if (response.message == "MaSinhVien deleted") {
                    // Cập nhật lại danh sách máy tính sau khi xóa thành công
                    val allResponse = withContext(Dispatchers.IO) {
                        ITLabRoomRetrofitClient.sinhvienAPIService.getAllSinhVien()
                    }
                    danhSachAllSinhVien = allResponse.sinhvien ?: emptyList()
                }
            } catch (e: Exception) {
                sinhvienDeleteResult = "Lỗi khi xóa sinh viên: ${e.localizedMessage ?: e.message}"
                Log.e("SinhVienViewModel", "Lỗi khi xóa sinh viên", e)
            } finally {
                isLoading = false
            }
        }
    }

}

