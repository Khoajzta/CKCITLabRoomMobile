package com.example.ckcitlabroom.viewmodels

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

class LopHocViewModel: ViewModel() {
    var lophoc: LopHoc? by mutableStateOf(null)
        private set

    var danhSachAllLopHoc by mutableStateOf<List<LopHoc>>(emptyList())
        private set
    var lophocCreateResult by mutableStateOf("")
    var lophocUpdateTrangThaiResult by mutableStateOf("")
    var lophocUpdateResult by mutableStateOf("")
    var lophocDeleteResult by mutableStateOf("")

    private var pollingJob: Job? = null
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun stopPollingLopHoc() {
        pollingJob?.cancel()
        pollingJob = null
    }

    fun getAllLopHoc() {
        if (pollingJob != null) return

        pollingJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.lophocAPIService.getAllLopHoc()
                    if (response.lophoc != null) {
                        danhSachAllLopHoc = response.lophoc!!
                    } else {
                        danhSachAllLopHoc = emptyList()
                    }

                } catch (e: Exception) {
                    Log.e("LopHocViewModel", "Polling lỗi", e)
                }
                delay(500)
            }
        }
    }

    fun getLopHocById(malop: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                lophoc = ITLabRoomRetrofitClient.lophocAPIService.getLopHocByByID(malop)
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("LopHocViewModel", "Lỗi khi lấy thông tin lớp học", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun createLopHoc(lopHoc: LopHoc) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.lophocAPIService.createLopHoc(lopHoc)
                }
                lophocCreateResult = response.message
            } catch (e: Exception) {
                lophocCreateResult = "Lỗi khi thêm lớp học: ${e.message}"
                Log.e("LopHocViewModel", "Lỗi khi thêm lớp học: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun updateTrangThaiLopHoc(lopHoc: LopHoc) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.lophocAPIService.updateTrangThaiLopHoc(lopHoc)
                }
                lophocUpdateTrangThaiResult = response.message
            } catch (e: Exception) {
                lophocUpdateTrangThaiResult = "Lỗi khi cập nhật trạng thái lớp: ${e.message}"
                Log.e("LopHocViewModel", "Lỗi khi cập nhật trạng thái lớp: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun updateLopHoc(lopHoc: LopHoc) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.lophocAPIService.updateLopHoc(lopHoc)
                }
                lophocUpdateResult = response.message
            } catch (e: Exception) {
                lophocUpdateResult = "Lỗi khi cập nhật lớp học: ${e.message}"
                Log.e("LopHocViewModel", "Lỗi khi cập nhật lớp học: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }


    fun deleteLopHoc(malop: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val body = mapOf("MaLopHoc" to malop)
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.lophocAPIService.deleteLopHoc(body)
                }
                lophocDeleteResult = response.message

                if (response.message == "LopHoc deleted") {
                    // Cập nhật lại danh sách sau khi xóa thành công
                    val allResponse = withContext(Dispatchers.IO) {
                        ITLabRoomRetrofitClient.lophocAPIService.getAllLopHoc()
                    }
                    danhSachAllLopHoc = allResponse.lophoc ?: emptyList()
                }
            } catch (e: Exception) {
                lophocDeleteResult = "Lỗi khi xóa Lớp Học: ${e.localizedMessage ?: e.message}"
                Log.e("LopHocViewModel", "Lỗi khi xóa Lớp Học", e)
            } finally {
                isLoading = false
            }
        }
    }

}
