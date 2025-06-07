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

    var danhSachAllPhieuSuaChua by mutableStateOf<List<PhieuSuaChuaRp>>(emptyList())
        private set

    var danhSachAllMayTinhtheophong by mutableStateOf<List<MayTinh>>(emptyList())
        private set

    private var pollingAllPhieuSuaChuaJob: Job? = null
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

    fun updatePhieuSuaChua(phieuSuaChua: PhieuSuaChua) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.phieusuachuaAPIService.updatePhieuSuaChua(phieuSuaChua)
                }
                maytinhUpdateResult = response.message
            } catch (e: Exception) {
                maytinhUpdateResult = "Lỗi khi cập nhật phieu sua chua: ${e.message}"
                Log.e("PhieuSuaChuaViewModel", "Lỗi khi cập nhật phieu sua chua: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

}