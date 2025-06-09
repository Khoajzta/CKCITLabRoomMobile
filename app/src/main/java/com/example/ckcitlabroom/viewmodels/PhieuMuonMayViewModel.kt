import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapstore.api.Constants.ITLabRoomRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PhieuMuonMayViewModel : ViewModel() {

    var danhSachAllPhieuMuonMay by mutableStateOf<List<PhieuMuonMay>>(emptyList())
        private set

    var danhSachAllPhieuSuaChuaTheoMa by mutableStateOf<List<PhieuSuaChuaRp>>(emptyList())
        private set

    private var pollingAllPhieuMuonMayJob: Job? = null

    var phieumuonmayCreateResult by mutableStateOf("")
    var phieuSuaChuaUpdateResult by mutableStateOf("")


    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun getAllPhieuMuonMay() {
        if (pollingAllPhieuMuonMayJob != null) return

        pollingAllPhieuMuonMayJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.phieuMuonMayAPIService.getAllPhieuMuonMay()
                    danhSachAllPhieuMuonMay = response.phieumuonmay ?: emptyList()
                } catch (e: Exception) {
                    Log.e("PhieuMuonMayViewModel", "Polling all phiếu lỗi", e)
                }
                delay(500)
            }
        }
    }

    fun stopPollingPhieuMuonMay() {
        pollingAllPhieuMuonMayJob?.cancel()
        pollingAllPhieuMuonMayJob = null
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



    fun createPhieuMuonMay(phieuMuonMay: PhieuMuonMay) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.phieuMuonMayAPIService.createPhieuMuonMay(phieuMuonMay)
                }
                phieumuonmayCreateResult = response.message
            } catch (e: Exception) {
                phieumuonmayCreateResult = "Lỗi khi thêm phieu sua chua: ${e.message}"
                Log.e("PhieuMuonMayViewModel", "Lỗi khi thêm phieu muonmay: ${e.message}")
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