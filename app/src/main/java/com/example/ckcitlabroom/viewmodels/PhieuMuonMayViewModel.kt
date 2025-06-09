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

    var phieuMuonMay = PhieuMuonMay(0, "", "", "", "", "", 0)

    var danhSachAllPhieuMuonMay by mutableStateOf<List<PhieuMuonMay>>(emptyList())
        private set

    var danhSachAllPhieuSuaChuaTheoMa by mutableStateOf<List<PhieuSuaChuaRp>>(emptyList())
        private set

    var phieuMuonMap by mutableStateOf<Map<String, PhieuMuonMay>>(emptyMap())
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

    fun getPhieuMuonByMaPhieu(maphieumuon: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                val result = ITLabRoomRetrofitClient.phieuMuonMayAPIService.getPhieuMuonMayByMaPhieuMuon(maphieumuon)
                phieuMuonMap = phieuMuonMap.toMutableMap().apply {
                    put(maphieumuon, result)
                }
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("PhieuMuonMayViewModel", "Lỗi khi lấy phiếu mượn", e)
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
                phieumuonmayCreateResult = "Lỗi khi thêm phieu muon may: ${e.message}"
                Log.e("PhieuMuonMayViewModel", "Lỗi khi thêm phieu muonmay: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun updatePhieuMuonMay(phieumuonmay: PhieuMuonMay) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.phieuMuonMayAPIService.updatePhieuMuonMay(phieumuonmay)
                }
                phieuSuaChuaUpdateResult = response.message
            } catch (e: Exception) {
                phieuSuaChuaUpdateResult = "Lỗi khi cập nhật phieu muon may: ${e.message}"
                Log.e("PhieuMuonMayViewModel", "Lỗi khi cập nhật phieu muon may: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun updateTrangThaiPhieuMuon(maPhieuMuon: String, trangThai: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.phieuMuonMayAPIService.updateTrangThaiPhieuMuon(
                        UpdateTrangThaiRequest(maPhieuMuon, trangThai)
                    )
                }
                phieuSuaChuaUpdateResult = response.message
            } catch (e: Exception) {
                phieuSuaChuaUpdateResult = "Lỗi khi cập nhật trạng thái: ${e.message}"
                Log.e("PhieuMuonMayViewModel", "Lỗi khi cập nhật trạng thái", e)
            } finally {
                isLoading = false
            }
        }
    }


}