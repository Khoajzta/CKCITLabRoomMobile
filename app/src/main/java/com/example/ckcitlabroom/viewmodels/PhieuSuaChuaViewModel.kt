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

    var danhSachAllPhieuSuaChua by mutableStateOf<List<PhieuSuaChuaRp>>(emptyList())
        private set

    var danhSachAllPhieuSuaChuaTheoMa by mutableStateOf<List<PhieuSuaChuaRp>>(emptyList())
        private set

    private var pollingAllPhieuSuaChuaJob: Job? = null

    var phieusuachuaCreateResult by mutableStateOf("")
    var phieuSuaChuaUpdateResult by mutableStateOf("")


    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun getAllPhieuSuaChua() {
        if (pollingAllPhieuSuaChuaJob != null) return

        pollingAllPhieuSuaChuaJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.phieusuachuaAPIService.getAllPhieuSuaChua()
                    danhSachAllPhieuSuaChua = response.phieusuachua ?: emptyList()
                } catch (e: Exception) {
                    Log.e("PhieuSuaChuaViewModel", "Polling all máy lỗi", e)
                }
                delay(500)
            }
        }
    }

    fun stopPollingPhieuSuaChua() {
        pollingAllPhieuSuaChuaJob?.cancel()
        pollingAllPhieuSuaChuaJob = null
    }

    fun getPhieuSuaChuaTheoMaMay(maMay: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.phieusuachuaAPIService.getPhieuSuaChuaByMaMay(maMay)
                }
                danhSachAllPhieuSuaChuaTheoMa = response.phieusuachua ?: emptyList()
                errorMessage = null
            } catch (e: Exception) {
                danhSachAllPhieuSuaChuaTheoMa = emptyList()
                errorMessage = "Lỗi khi lấy phiếu sửa chữa theo mã máy: ${e.message}"
                Log.e("PhieuSuaChuaViewModel", "Lỗi khi lấy phiếu sửa chữa theo mã máy", e)
            } finally {
                isLoading = false
            }
        }
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

    fun updatePhieuSuaChua(phieuSuaChua: PhieuSuaChua) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.phieusuachuaAPIService.updatePhieuSuaChua(phieuSuaChua)
                }
                phieuSuaChuaUpdateResult = response.message
            } catch (e: Exception) {
                phieuSuaChuaUpdateResult = "Lỗi khi cập nhật phieu sua chua: ${e.message}"
                Log.e("PhieuSuaChuaViewModel", "Lỗi khi cập nhật phieu sua chua: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

}