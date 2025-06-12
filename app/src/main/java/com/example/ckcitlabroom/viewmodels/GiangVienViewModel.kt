import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ckcitlabroom.models.LopHoc
import com.example.lapstore.api.Constants.ITLabRoomRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GiangVienViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private val giangvienPreferences = GiangVienPreferences(context)

    var giangvien by mutableStateOf<GiangVien?>(null)
        private set

    var giangvienSet: GiangVien? by mutableStateOf(null)
        private set

    var danhSachAllGiangVien by mutableStateOf<List<GiangVien>>(emptyList())
        private set

    var giangvienCreateResult by mutableStateOf("")
    var giangvienUpdateResult by mutableStateOf("")
    var giangvienDeleteResult by mutableStateOf("")
    var giangVienUpdateTrangThaiResult by mutableStateOf("")

    private var pollingJob: Job? = null
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun stopPollingGiangVien() {
        pollingJob?.cancel()
        pollingJob = null
    }

    fun setGV(gv: GiangVien?) {
        giangvienSet = gv
    }

    private val _loginResult = MutableStateFlow<LoginResponse?>(null)
    val loginResult: StateFlow<LoginResponse?> = _loginResult

    fun checkLogin(email: String, matKhau: String) {
        viewModelScope.launch {
            try {
                val request = LoginRequest(email, matKhau)
                val response = ITLabRoomRetrofitClient.giangVienAPIService.CheckLogin(request)
                _loginResult.value = response
            } catch (e: Exception) {
                _loginResult.value = LoginResponse(
                    result = false, message = "Lỗi kết nối: ${e.message}"
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            giangvienPreferences.logout()
            giangvien = null
            _loginResult.value = null
        }
    }

    fun resetLoginResult() {
        _loginResult.value = null
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
                delay(200)
            }
        }
    }

    fun getGiangVienByMaGV(magv: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                giangvien = ITLabRoomRetrofitClient.giangVienAPIService.getGiangVienByByID(magv)
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("GiangVienViewModel", "Lỗi khi lấy thông tin giảng vieen", e)

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
                    ITLabRoomRetrofitClient.giangVienAPIService.createGiangVien(
                        giangVien
                    )
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

    fun getGiangVienByMaGOrEmail(key: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                giangvien =
                    ITLabRoomRetrofitClient.giangVienAPIService.getGiangVienByEmailOrMaGV(
                        key
                    )
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("GiangVienViewModel", "Lỗi khi lấy thông tin máy tính", e)
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
                    ITLabRoomRetrofitClient.giangVienAPIService.updateGiangVien(
                        giangVien
                    )
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

    fun updateTrangThaiGiangVien(giangVien: GiangVien) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.giangVienAPIService.updateTrangThaiGiangVien(giangVien)
                }
                giangVienUpdateTrangThaiResult = response.message
            } catch (e: Exception) {
                giangVienUpdateTrangThaiResult = "Lỗi khi cập nhật trạng thái giảng viên: ${e.message}"
                Log.e("GiangVienViewModel", "Lỗi khi cập nhật trạng thái giảng viên: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun updateLoaiTKGiangVien(giangVien: GiangVien) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.giangVienAPIService.updateLoaiTaiKhoanGiangVien(giangVien)
                }
                giangvienUpdateResult = response.message
            } catch (e: Exception) {
                giangvienUpdateResult = "Lỗi khi cập nhật loại tài khoản giảng viên: ${e.message}"
                Log.e("GiangVienViewModel", "Lỗi khi cập nhật loại tài khoản giảng viên: ${e.message}")
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
                giangvienDeleteResult =
                    "Lỗi khi xóa Giảng viên: ${e.localizedMessage ?: e.message}"
                Log.e("GiangVienViewModel", "Lỗi khi xóa giảng viên", e)
            } finally {
                isLoading = false
            }
        }
    }


    fun setGVFromPrefs(state: LoginGiangVienState) {
        giangvien = GiangVien(
            MaGV = state.maGiangVien.toString(),
            TenGiangVien = state.tenGiangVien.toString(),
            NgaySinh = "",
            GioiTinh = "",
            Email = "",
            MatKhau = "",
            MaLoaiTaiKhoan = 3,
            TrangThai = 0
        )
    }

    suspend fun getGiangVienByMaGOrEmailSync(key: String): GiangVien? {
        return try {
            ITLabRoomRetrofitClient.giangVienAPIService.getGiangVienByEmailOrMaGV(key)
        } catch (e: Exception) {
            Log.e("GiangVienViewModel", "Lỗi khi lấy giảng viên (sync): ${e.localizedMessage}", e)
            null
        }
    }

}