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

class NamHocViewModel : ViewModel() {

    var danhSachAllNamHoc by mutableStateOf(listOf<NamHoc>())

    private var pollingAllNamHocJob: Job? = null

    var namhocCreateResult by mutableStateOf("")
    var namhocUpdateResult by mutableStateOf("")
    var namhocDeleteResult by mutableStateOf("")

    var isLoading by mutableStateOf(false)
        private set


    fun getAllNamHoc() {
        if (pollingAllNamHocJob != null) return

        pollingAllNamHocJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.namhocAPIService.getAllNamHoc()
                    danhSachAllNamHoc = response.namhoc ?: emptyList()
                } catch (e: Exception) {
                    Log.e("NamHocViewModel", "Polling all năm học lỗi", e)
                }
                delay(500)
            }
        }
    }

    fun stopPollingAllNamHoc() {
        pollingAllNamHocJob?.cancel()
        pollingAllNamHocJob = null
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
}