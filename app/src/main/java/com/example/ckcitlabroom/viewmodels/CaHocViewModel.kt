package com.example.ckcitlabroom.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ckcitlabroom.models.CaHoc
import com.example.lapstore.api.ITLabRoomRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CaHocViewModel: ViewModel() {
    var cahoc: CaHoc? by mutableStateOf(null)
        private set

    var danhSachAllCaHoc by mutableStateOf<List<CaHoc>>(emptyList())
        private set
    var cahocCreateResult by mutableStateOf("")
    var cahocUpdateTrangThaiResult by mutableStateOf("")
    var cahocUpdateResult by mutableStateOf("")
    var cahocDeleteResult by mutableStateOf("")

    private var pollingJob: Job? = null
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun stopPollingCaHoc() {
        pollingJob?.cancel()
        pollingJob = null
    }

    fun getAllCaHoc() {
        if (pollingJob != null) return

        pollingJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.cahocAPIService.getAllCaHoc()
                    if (response.cahoc != null) {
                        danhSachAllCaHoc = response.cahoc!!
                    } else {
                        danhSachAllCaHoc = emptyList()
                    }

                } catch (e: Exception) {
                    Log.e("CaHocViewModel", "Polling lỗi", e)
                }
                delay(500)
            }
        }
    }

    fun getCaHocById(maca: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                cahoc = ITLabRoomRetrofitClient.cahocAPIService.getCaHocByByID(maca)
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("CaHocViewModel", "Lỗi khi lấy thông tin ca học", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun createCaHoc(caHoc: CaHoc) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.cahocAPIService.createCaHoc(caHoc)
                }
                cahocCreateResult = response.message
            } catch (e: Exception) {
                cahocCreateResult = "Lỗi khi thêm ca học: ${e.message}"
                Log.e("CaHocViewModel", "Lỗi khi thêm ca học: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun updateTrangThaiCaHoc(caHoc: CaHoc) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.cahocAPIService.updateTrangThaiCaHoc(caHoc)
                }
                cahocUpdateTrangThaiResult = response.message
            } catch (e: Exception) {
                cahocUpdateTrangThaiResult = "Lỗi khi cập nhật trạng thái ca: ${e.message}"
                Log.e("CaHocViewModel", "Lỗi khi cập nhật trạng thái ca: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun updateCaHoc(caHoc: CaHoc) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.cahocAPIService.updateCaHoc(caHoc)
                }
                cahocUpdateResult = response.message
            } catch (e: Exception) {
                cahocUpdateResult = "Lỗi khi cập nhật ca học: ${e.message}"
                Log.e("CaHocViewModel", "Lỗi khi cập nhật ca học: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteCaHoc(maca: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val body = mapOf("MaCaHoc" to maca)
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.cahocAPIService.deleteCaHoc(body)
                }
                cahocDeleteResult = response.message

                if (response.message == "CaHoc deleted") {
                    // Cập nhật lại danh sách sau khi xóa thành công
                    val allResponse = withContext(Dispatchers.IO) {
                        ITLabRoomRetrofitClient.cahocAPIService.getAllCaHoc()
                    }
                    danhSachAllCaHoc = allResponse.cahoc ?: emptyList()
                }
            } catch (e: Exception) {
                cahocDeleteResult = "Lỗi khi xóa Ca Học: ${e.localizedMessage ?: e.message}"
                Log.e("CaHocViewModel", "Lỗi khi xóa Ca Học", e)
            } finally {
                isLoading = false
            }
        }
    }
}
