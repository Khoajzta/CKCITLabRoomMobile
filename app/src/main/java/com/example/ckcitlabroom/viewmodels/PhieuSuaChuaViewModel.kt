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

class PhieuSuaChuaViewModel : ViewModel() {

    var mt = MayTinh("","",""",""","","","","","","","","","","","",1)

    var danhSachAllMayTinh by mutableStateOf<List<MayTinh>>(emptyList())
        private set

    var danhSachAllMayTinhtheophong by mutableStateOf<List<MayTinh>>(emptyList())
        private set

    private var pollingAllMayTinhJob: Job? = null
    private var pollingMayTinhTheoPhongJob: Job? = null

    var phieusuachuaCreateResult by mutableStateOf("")
    var maytinhUpdateResult by mutableStateOf("")
    var maytinhDeleteResult by mutableStateOf("")

    var maytinh: MayTinh by mutableStateOf(mt)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun getAllMayTinh() {
        if (pollingAllMayTinhJob != null) return

        pollingAllMayTinhJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.maytinhAPIService.getAllMayTinh()
                    danhSachAllMayTinh = response.maytinh ?: emptyList()
                } catch (e: Exception) {
                    Log.e("PhongMayViewModel", "Polling all máy lỗi", e)
                }
                delay(500)
            }
        }
    }

    fun stopPollingAllMayTinh() {
        pollingAllMayTinhJob?.cancel()
        pollingAllMayTinhJob = null
    }

    fun getMayTinhByMaMay(mamay: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                maytinh = ITLabRoomRetrofitClient.maytinhAPIService.getMayTinhByMaMay(mamay)
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("MayTinhViewModel", "Lỗi khi lấy thông tin máy tính", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun getMayTinhByPhong(maphong: String) {
        if (pollingMayTinhTheoPhongJob != null) return

        pollingMayTinhTheoPhongJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.maytinhAPIService.getMayTinhByMaPhong(maphong)
                    danhSachAllMayTinhtheophong = response.maytinh ?: emptyList()
                } catch (e: Exception) {
                    Log.e("PhongMayViewModel", "Polling theo phòng lỗi", e)
                }
                delay(500)
            }
        }
    }

    fun stopPollingMayTinhTheoPhong() {
        pollingMayTinhTheoPhongJob?.cancel()
        pollingMayTinhTheoPhongJob = null
    }

    fun createPhieuSuaChua(phieuSuaChua: PhieuSuaChua) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.phieusuachuaAPIService.createPhieuSuaChua(phieuSuaChua)
                }
                phieusuachuaCreateResult = response.message
            } catch (e: Exception) {
                phieusuachuaCreateResult = "Lỗi khi thêm phieu sua chua: ${e.message}"
                Log.e("PhieuSuaChuaViewModel", "Lỗi khi thêm phieu sua chua: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun updateMayTinh(maytinh: MayTinh) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.maytinhAPIService.updateMayTinh(maytinh)
                }
                maytinhUpdateResult = response.message
            } catch (e: Exception) {
                maytinhUpdateResult = "Lỗi khi cập nhật máy tính: ${e.message}"
                Log.e("MayTinhViewModel", "Lỗi khi cập nhật máy tính: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteMayTinh(mamay: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val body = mapOf("MaMay" to mamay)
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.maytinhAPIService.deleteMayTinh(body)
                }
                maytinhDeleteResult = response.message

                if (response.message == "MayTinh deleted") {
                    // Cập nhật lại danh sách máy tính sau khi xóa thành công
                    val allResponse = withContext(Dispatchers.IO) {
                        ITLabRoomRetrofitClient.maytinhAPIService.getAllMayTinh()
                    }
                    danhSachAllMayTinh = allResponse.maytinh ?: emptyList()
                }
            } catch (e: Exception) {
                maytinhDeleteResult = "Lỗi khi xóa máy tính: ${e.localizedMessage ?: e.message}"
                Log.e("MayTinhViewModel", "Lỗi khi xóa máy tính", e)
            } finally {
                isLoading = false
            }
        }
    }
}