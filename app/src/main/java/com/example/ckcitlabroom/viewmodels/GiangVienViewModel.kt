
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

class GiangVienViewModel : ViewModel() {

    var giangvien: GiangVien? by mutableStateOf(null)
        private set

    var danhSachAllGiangVien by mutableStateOf<List<GiangVien>>(emptyList())
        private set
    var giangvienCreateResult by mutableStateOf("")
    var giangvienUpdateResult by mutableStateOf("")
    var giangvienDeleteResult by mutableStateOf("")

    private var pollingJob: Job? = null
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun stopPollingGiangVien() {
        pollingJob?.cancel()
        pollingJob = null
    }

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

    fun getAllGiangVien() {
        if (pollingJob != null) return

        pollingJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.giangVienAPIService.getAllGiangVien()
                    if (response.giangvien != null) {
                        danhSachAllGiangVien = response.giangvien!!
                    } else {
                        danhSachAllGiangVien = emptyList()
                    }

                } catch (e: Exception) {
                    Log.e("GiangVienViewModel", "Polling lỗi", e)
                }
                delay(500)
            }
        }
    }

    fun getGiangVienById(magv: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                val result = ITLabRoomRetrofitClient.giangVienAPIService.getGiangVienByByID(magv)
                giangvien = result
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("GiangVienViewModel", "Lỗi khi lấy thông tin giảng viên", e)
            } finally {
                isLoading = false
            }
        }
    }



    fun createGiangVien(giangVien: GiangVien) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.giangVienAPIService.createGiangVien(giangVien)
                }
                giangvienCreateResult = response.message
            } catch (e: Exception) {
                giangvienCreateResult = "Lỗi khi thêm giảng viên: ${e.message}"
                Log.e("GiangVienViewModel", "Lỗi khi thêm giảng viên: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun updateGiangVien(giangVien: GiangVien) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.giangVienAPIService.updateGiangVien(giangVien)
                }
                giangvienUpdateResult = response.message
            } catch (e: Exception) {
                giangvienUpdateResult = "Lỗi khi cập nhật máy tính: ${e.message}"
                Log.e("GiangVienViewModel", "Lỗi khi cập nhật máy tính: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteGiangVien(magv: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val body = mapOf("MaGV" to magv)
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.giangVienAPIService.deleteGiangVien(body)
                }
                giangvienDeleteResult = response.message

                if (response.message == "GiangVien deleted") {
                    // Cập nhật lại danh sách sau khi xóa thành công
                    val allResponse = withContext(Dispatchers.IO) {
                        ITLabRoomRetrofitClient.giangVienAPIService.getAllGiangVien()
                    }
                    danhSachAllGiangVien = allResponse.giangvien ?: emptyList()
                }
            } catch (e: Exception) {
                giangvienDeleteResult = "Lỗi khi xóa Giảng viên: ${e.localizedMessage ?: e.message}"
                Log.e("GiangVienViewModel", "Lỗi khi xóa giảng viên", e)
            } finally {
                isLoading = false
            }
        }
    }

}

