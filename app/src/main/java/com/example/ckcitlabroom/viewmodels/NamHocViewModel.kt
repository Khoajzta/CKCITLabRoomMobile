import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ckcitlabroom.api.Constants.ITLabRoomRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NamHocViewModel : ViewModel() {

    var danhSachAllNamHoc by mutableStateOf<List<NamHoc>>(emptyList())
        private set

    private var pollingAllNamHocJob: Job? = null

    var namhocCreateResult by mutableStateOf("")
    var namhocUpdateResult by mutableStateOf("")
    var namhocDeleteResult by mutableStateOf("")

    var isLoading by mutableStateOf(false)
        private set

    fun stopPollingAllNamHoc() {
        pollingAllNamHocJob?.cancel()
        pollingAllNamHocJob = null
    }

    fun getAllNamHoc() {
        if (pollingAllNamHocJob != null) return

        pollingAllNamHocJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.namhocAPIService.getAllNamHoc()
                    if (response.namhoc != null) {
                        danhSachAllNamHoc = response.namhoc!!
                    } else {
                        danhSachAllNamHoc = emptyList()
                    }

                } catch (e: Exception) {
                    Log.e("NamHocViewModel", "Polling lỗi", e)
                }
                delay(200)
            }
        }
    }


    fun createNamHoc(namhoc: NamHoc) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.namhocAPIService.createNamHoc(namhoc)
                }
                namhocCreateResult = response.message
            } catch (e: Exception) {
                namhocCreateResult = "Lỗi khi thêm năm học: ${e.message}"
                Log.e("NamHocViewModel", "Lỗi khi thêm năm học: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun updateNamHoc(namhoc: NamHoc) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.namhocAPIService.updateNamHoc(namhoc)
                }
                namhocUpdateResult = response.message
            } catch (e: Exception) {
                namhocUpdateResult = "Lỗi khi cập nhật môn học: ${e.message}"
                Log.e("NamHocViewModel", "Lỗi khi cập nhật môn học: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
}