import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapstore.api.ITLabRoomRetrofitClient
import kotlinx.coroutines.*

class TuanViewModel : ViewModel() {

    var danhSachAllTuan by mutableStateOf(listOf<Tuan>())
    var danhSachTuanTheoNamMap by mutableStateOf<Map<String, List<Tuan>>>(emptyMap())
        private set

    private var pollingAllTuanJob: Job? = null
    private var pollingTuanTheoNamJob: Job? = null

    var tuanCreateResult by mutableStateOf("")
    var isLoading by mutableStateOf(false)
        private set

    fun getAllTuan() {
        if (pollingAllTuanJob != null) return

        pollingAllTuanJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.tuanAPIService.getAllTuan()
                    danhSachAllTuan = response.tuan ?: emptyList()
                } catch (e: Exception) {
                    Log.e("TuanViewModel", "Polling all tuần lỗi", e)
                }
                delay(500)
            }
        }
    }

    fun stopPollingAllTuan() {
        pollingAllTuanJob?.cancel()
        pollingAllTuanJob = null
    }

    fun getTuanByMaNam(manam: String) {
        viewModelScope.launch {
            try {
                val response = ITLabRoomRetrofitClient.tuanAPIService.getTuanByMaNam(manam)
                val listTuan = response.tuan ?: emptyList()
                danhSachTuanTheoNamMap = danhSachTuanTheoNamMap.toMutableMap().apply {
                    put(manam, listTuan)
                }
            } catch (e: Exception) {
                Log.e("TuanViewModel", "Lỗi lấy tuần theo năm học: ${e.message}")
            }
        }
    }

    fun stopPollingTuanTheoNam() {
        pollingTuanTheoNamJob?.cancel()
        pollingTuanTheoNamJob = null
    }

    fun createTuan(tuan: Tuan) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.tuanAPIService.createTuan(tuan)
                }
                tuanCreateResult = response.message
            } catch (e: Exception) {
                tuanCreateResult = "Lỗi khi thêm tuần: ${e.message}"
                Log.e("TuanViewModel", "Lỗi khi thêm tuần: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
}
