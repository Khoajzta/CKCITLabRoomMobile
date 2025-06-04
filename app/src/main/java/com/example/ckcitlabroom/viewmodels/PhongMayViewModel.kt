import android.util.Log
import androidx.compose.runtime.getValue
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

class PhongMayViewModel : ViewModel() {

    var pm = PhongMay(MaPhong = "", TenPhong = "", TrangThai = 0)

    var phongmay: PhongMay by mutableStateOf(pm)
        private set

    var phongmaymoi: PhongMay by mutableStateOf(pm)
        private set

    var phongmaycu: PhongMay by mutableStateOf(pm)
        private set


    var danhSachAllPhongMay by mutableStateOf(listOf<PhongMay>())
        private set


    private var pollingJob: Job? = null

    var phongmayCreateResult by mutableStateOf("")
    var phogmayUpdateTrangThaiResult by mutableStateOf("")
    var phogmayUpdateResult by mutableStateOf("")
    var phongmayDeleteResult by mutableStateOf("")


    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun getAllPhongMay() {
        if (pollingJob != null) return

        pollingJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.phongmayAPIService.getAllPhongMay()
                    if (response.phongmay != null) {
                        danhSachAllPhongMay = response.phongmay!!
                    } else {
                        danhSachAllPhongMay = emptyList()
                    }

                } catch (e: Exception) {
                    Log.e("PhongMayViewModel", "Polling lỗi", e)
                }
                delay(500)
            }
        }
    }

    fun stopPollingPhongMay() {
        pollingJob?.cancel()
        pollingJob = null
    }

    fun getPhongMayByMaPhong(maphong: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                phongmay = ITLabRoomRetrofitClient.phongmayAPIService.getPhongMayByMaPhong(maphong)
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("PhongMayViewModel", "Lỗi khi lấy thông tin phòng máy", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun getPhongMayMoi(maphong: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                phongmaymoi = ITLabRoomRetrofitClient.phongmayAPIService.getPhongMayByMaPhong(maphong)
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("PhongMayViewModel", "Lỗi khi lấy thông tin phòng máy", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun getPhongMayCu(maphong: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                phongmaycu = ITLabRoomRetrofitClient.phongmayAPIService.getPhongMayByMaPhong(maphong)
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("PhongMayViewModel", "Lỗi khi lấy thông tin phòng máy", e)
            } finally {
                isLoading = false
            }
        }
    }

    suspend fun fetchPhongMayByMaPhong(maphong: String): PhongMay {
        return try {
            ITLabRoomRetrofitClient.phongmayAPIService.getPhongMayByMaPhong(maphong)
        } catch (e: Exception) {
            Log.e("PhongMayViewModel", "Lỗi khi lấy thông tin phòng máy (suspend)", e)
            pm // trả về phòng mặc định hoặc null tùy bạn
        }
    }

    //
    fun createPhongMay(phongMay: PhongMay) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.phongmayAPIService.createPhongMay(phongMay)
                }
                phongmayCreateResult = response.message
            } catch (e: Exception) {
                phongmayCreateResult = "Lỗi khi thêm phòng máy: ${e.message}"
                Log.e("PhongMayViewModel", "Lỗi khi thêm phòng máy: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    //
    fun updateTrangThaiPhongMay(phogmay: PhongMay) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.phongmayAPIService.updateTrangThaiPhongMay(phogmay)
                }
                phogmayUpdateTrangThaiResult = response.message
            } catch (e: Exception) {
                phogmayUpdateTrangThaiResult = "Lỗi khi cập nhật trạng thái phòng máy: ${e.message}"
                Log.e("PhongMayViewModel", "Lỗi khi cập nhật trạng thái phòng máy: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun updatePhongMay(phogmay: PhongMay) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.phongmayAPIService.updatePhongMay(phogmay)
                }
                phogmayUpdateResult = response.message
            } catch (e: Exception) {
                phogmayUpdateResult = "Lỗi khi cập nhật phòng máy: ${e.message}"
                Log.e("PhongMayViewModel", "Lỗi khi cập nhật phòng máy: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    //
//    // Hàm xóa máy tính
    fun deletePhongMay(maphong: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val body = mapOf("MaPhong" to maphong)
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.phongmayAPIService.deletePhongMay(body)
                }
                phongmayDeleteResult = response.message

                if (response.message == "phongmay deleted") {
                    // Cập nhật lại danh sách máy tính sau khi xóa thành công
                    val allResponse = withContext(Dispatchers.IO) {
                        ITLabRoomRetrofitClient.phongmayAPIService.getAllPhongMay()
                    }
                    danhSachAllPhongMay = allResponse.phongmay ?: emptyList()
                }
            } catch (e: Exception) {
                phongmayDeleteResult = "Lỗi khi xóa máy tính: ${e.localizedMessage ?: e.message}"
                Log.e("MayTinhViewModel", "Lỗi khi xóa máy tính", e)
            } finally {
                isLoading = false
            }
        }
    }
}