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
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThongBaoViewModel : ViewModel() {
    var danhSachAllThongBao by mutableStateOf<List<ThongBao>>(emptyList())
        private set

    var thongBao: ThongBao? by mutableStateOf(null)
        private set

    private var pollingAllThongBaoJob: Job? = null

    var thongBaoCreateResult by mutableStateOf("")
    var thongBaoUpdateResult by mutableStateOf("")
    var thongBaoDeleteResult by mutableStateOf("")

    var isLoading by mutableStateOf(false)
        private set

    fun getAllThongBao() {
        if (pollingAllThongBaoJob != null) return
        pollingAllThongBaoJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.thongbaoAPIService.getAllThongBao()
                    danhSachAllThongBao = response.thongbao ?: emptyList()
                } catch (e: Exception) {
                    Log.e("ThongBaoViewModel", "Polling all thongbao lỗi", e)
                }
                delay(1000)
            }
        }
    }

    fun stopPollingAllThongBao() {
        pollingAllThongBaoJob?.cancel()
        pollingAllThongBaoJob = null
    }

    fun getThongBaoById(maTB: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                thongBao = ITLabRoomRetrofitClient.thongbaoAPIService.getThongBaoByID(maTB)
            } catch (e: Exception) {
                Log.e("ThongBaoViewModel", "Lỗi khi lấy thông báo", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun createThongBao(thongBao: ThongBao) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.thongbaoAPIService.createThongBao(thongBao)
                }
                thongBaoCreateResult = response.message
            } catch (e: Exception) {
                thongBaoCreateResult = "Lỗi khi thêm thông báo: ${e.message}"
                Log.e("ThongBaoViewModel", "Lỗi khi thêm thông báo: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun updateThongBao(thongBao: ThongBao) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.thongbaoAPIService.updateThongBao(thongBao)
                }
                thongBaoUpdateResult = response.message
            } catch (e: Exception) {
                thongBaoUpdateResult = "Lỗi khi cập nhật thông báo: ${e.message}"
                Log.e("ThongBaoViewModel", "Lỗi khi cập nhật thông báo: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteThongBao(maTB: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.thongbaoAPIService.deleteThongBao(mapOf("MaTB" to maTB))
                }
                thongBaoDeleteResult = response.message
            } catch (e: Exception) {
                thongBaoDeleteResult = "Lỗi khi xóa thông báo: ${e.message}"
                Log.e("ThongBaoViewModel", "Lỗi khi xóa thông báo: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
} 