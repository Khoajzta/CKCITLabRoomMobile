import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ckcitlabroom.models.LopHoc
import com.example.lapstore.api.Constants.ITLabRoomRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MonHocViewModel : ViewModel() {

    var danhSachAllMonHoc by mutableStateOf<List<MonHoc>>(emptyList())
        private set

    var monhoc: MonHoc? by mutableStateOf(null)
        private set

    private var pollingAllMonHocJob: Job? = null

    var monhocCreateResult by mutableStateOf("")
    var monhocUpdateResult by mutableStateOf("")
    var monhocDeleteResult by mutableStateOf("")

    var isLoading by mutableStateOf(false)
        private set


    fun getAllMonHoc() {
        if (pollingAllMonHocJob != null) return

        pollingAllMonHocJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.monHocAPIService.getAllMonHoc()
                    danhSachAllMonHoc = response.monhoc ?: emptyList()
                } catch (e: Exception) {
                    Log.e("MonHocViewModel", "Polling all phiếu lỗi", e)
                }
                delay(500)
            }
        }
    }

    fun stopPollingAllMonHoc() {
        pollingAllMonHocJob?.cancel()
        pollingAllMonHocJob = null
    }

    fun getMonHocById(mamonhoc: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                monhoc = ITLabRoomRetrofitClient.monHocAPIService.getMonHocByByID(mamonhoc)
            } catch (e: Exception) {
                Log.e("MonHocViewModel", "Lỗi khi lấy thông tin môn học", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun createMonHoc(monhoc: MonHoc) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.monHocAPIService.createMonHoc(monhoc)
                }
                monhocCreateResult = response.message
            } catch (e: Exception) {
                monhocCreateResult = "Lỗi khi thêm môn học: ${e.message}"
                Log.e("MonHocViewModel", "Lỗi khi thêm môn học: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun updateMonHoc(monHoc: MonHoc) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.monHocAPIService.updateMonHoc(monHoc)
                }
                monhocUpdateResult = response.message
            } catch (e: Exception) {
                monhocUpdateResult = "Lỗi khi cập nhật môn học: ${e.message}"
                Log.e("MonHocViewModel", "Lỗi khi cập nhật môn học: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
}