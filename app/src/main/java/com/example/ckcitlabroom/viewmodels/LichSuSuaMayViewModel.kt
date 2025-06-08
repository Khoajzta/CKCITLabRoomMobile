import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
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

class LichSuSuaMayViewModel : ViewModel() {

    var danhsachlichsusuamaytheomamay by mutableStateOf<List<LichSuSuaMay>>(emptyList())
        private set

    private val _lichSuSuaMayMap = mutableStateMapOf<String, LichSuSuaMay?>()
    val lichSuSuaMayMap: Map<String, LichSuSuaMay?> get() = _lichSuSuaMayMap

    var lichSuSuaMayCreateResult by mutableStateOf("")

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun getLichSuTheoMaMay(maMay: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.lichSuSuaMayAPIService.getLichSuTheoMaMay(maMay)
                }
                if (response.lichsusuamay != null) {
                    danhsachlichsusuamaytheomamay = response.lichsusuamay
                } else {
                    danhsachlichsusuamaytheomamay = emptyList()
                    errorMessage = response.message ?: "Không có lịch sử sửa máy cho mã máy này"
                }
            } catch (e: Exception) {
                errorMessage = "Lỗi khi lấy lịch sử sửa máy theo mã máy: ${e.message}"
                Log.e("LichSuSuaMayViewModel", errorMessage ?: "")
                danhsachlichsusuamaytheomamay = emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    fun getLichSuTheoMaPhieu(maPhieuSuaChua: String) {
        viewModelScope.launch {
            try {
                val lichSu = ITLabRoomRetrofitClient.lichSuSuaMayAPIService.getLichSuTheoMaPhieu(maPhieuSuaChua)
                _lichSuSuaMayMap[maPhieuSuaChua] = lichSu
            } catch (e: Exception) {
                _lichSuSuaMayMap[maPhieuSuaChua] = null
                Log.e("LichSuSuaMayViewModel", "Lỗi lấy lịch sử: ${e.message}")
            }
        }
    }


    fun createLichSuSuaMay(lichSuSuaMay: LichSuSuaMay) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.lichSuSuaMayAPIService.createLichSuSuaMay(lichSuSuaMay)
                }
                lichSuSuaMayCreateResult = response.message
            } catch (e: Exception) {
                lichSuSuaMayCreateResult = "Lỗi khi thêm phieu sua chua: ${e.message}"
                Log.e("LichSuSuaMayViewModel", "Lỗi khi thêm phieu sua chua: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
}