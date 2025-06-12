import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapstore.api.Constants.ITLabRoomRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class SinhVienViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private val sinhvienPreferences = SinhVienPreferences(context)

    var sinhvien: SinhVien? by mutableStateOf(null)
        private set

    var sinhvienSet: SinhVien? by mutableStateOf(null)
        private set

    var danhSachAllSinhVien by mutableStateOf<List<SinhVien>>(emptyList())
        private set

    var sinhvienCreateResult by mutableStateOf("")
    var sinhvienUpdateResult by mutableStateOf("")
    var sinhvienDeleteResult by mutableStateOf("")
    var sinhVienUpdateTrangThaiResult by mutableStateOf("")

    private var pollingJob: Job? = null

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private val _loginResult = MutableStateFlow<LoginResponse?>(null)
    val loginResult: StateFlow<LoginResponse?> = _loginResult

    fun stopPollingSinhVien() {
        pollingJob?.cancel()
        pollingJob = null
    }

    fun setSV(sv: SinhVien?) {
        sinhvienSet = sv
    }

    fun checkLogin(email: String, matKhau: String) {
        viewModelScope.launch {
            try {
                val request = LoginRequest(email, matKhau)
                val response = ITLabRoomRetrofitClient.sinhvienAPIService.CheckLogin(request)
                _loginResult.value = response
            } catch (e: Exception) {
                Log.e("LoginError", "Exception khi gọi API", e)
                _loginResult.value = LoginResponse(
                    result = false, message = "Lỗi kết nối: ${e.message}"
                )
            }
        }
    }

    fun setSVFromPrefs(state: LoginSinhVienState) {
        if (state.isLoggedIn && state.maSinhVien != null && state.tenSinhVien != null) {
            sinhvien = SinhVien(
                MaSinhVien = state.maSinhVien,
                TenSinhVien = state.tenSinhVien,
                NgaySinh = "",
                GioiTinh = "",
                MaLop = "",
                Email = "",
                MatKhau = "",
                MaLoaiTaiKhoan = 3,
                TrangThai = 0
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            sinhvienPreferences.logout()
            sinhvien = null
            _loginResult.value = null
        }
    }

    fun getAllSinhVien() {
        if (pollingJob != null) return

        pollingJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.sinhvienAPIService.getAllSinhVien()
                    danhSachAllSinhVien = response.sinhvien ?: emptyList()
                } catch (e: Exception) {
                    Log.e("SinhVienViewModel", "Polling lỗi", e)
                }
                delay(500)
            }
        }
    }

    fun getSinhVienByMaSV(maSV: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                sinhvien = ITLabRoomRetrofitClient.sinhvienAPIService.getSinhVienByByID(maSV)
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("SinhVienViewModel", "Lỗi khi lấy thông tin sinh viên", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun getSinhVienByMaLop(maLop: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                val response = ITLabRoomRetrofitClient.sinhvienAPIService.getSinhVienByMaLop(maLop)
                danhSachAllSinhVien = response.sinhvien ?: emptyList()
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("SinhVienViewModel", "Lỗi khi lấy sinh viên theo lớp", e)
            } finally {
                isLoading = false
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
                sinhvienCreateResult = "Lỗi khi thêm sinh viên: ${e.message}"
                Log.e("SinhVienViewModel", "Lỗi khi thêm sinh viên: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

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

    fun resetLoginResult() {
        _loginResult.value = null
    }

    fun getSinhVienByMaGOrEmail(key: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                val result = ITLabRoomRetrofitClient.sinhvienAPIService.getSinhVienByEmailOrMaSV(key)
                sinhvien = result
            } catch (e: HttpException) {
                errorMessage = "Lỗi HTTP: ${e.code()} - ${e.message()}"
                Log.e("API", "Lỗi HTTP", e)
            } catch (e: IOException) {
                errorMessage = "Không thể kết nối tới server"
                Log.e("API", "Lỗi mạng", e)
            } catch (e: Exception) {
                errorMessage = "Lỗi không xác định: ${e.localizedMessage}"
                Log.e("API", "Lỗi khác", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun updateSinhVien(sinhVien: SinhVien) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.sinhvienAPIService.updateSinhVien(
                        sinhVien
                    )
                }
                sinhvienUpdateResult = response.message
            } catch (e: Exception) {
                sinhvienUpdateResult = "Lỗi khi cập nhật sinh viên: ${e.message}"
                Log.e("SinhVienViewModel", "Lỗi khi cập nhật sinh viên: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun updateTrangThaiSinhVien(sinhVien: SinhVien, maLop: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.sinhvienAPIService.updateTrangThaiSinhVien(sinhVien)
                }
                sinhVienUpdateTrangThaiResult = response.message

                // ✅ Gọi lại danh sách sinh viên trong lớp để cập nhật giao diện
                getSinhVienByMaLop(maLop)

            } catch (e: Exception) {
                sinhVienUpdateTrangThaiResult = "Lỗi khi cập nhật trạng thái sinh viên: ${e.message}"
                Log.e("SinhVienViewModel", "Lỗi khi cập nhật trạng thái sinh viên: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }


}



