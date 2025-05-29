package com.example.lapstore.viewmodels

import MayTinh
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

class MayTinhViewModel : ViewModel() {

    var danhSachAllMayTinh by mutableStateOf<List<MayTinh>>(emptyList())
        private set

    var danhSachAllMayTinhtheophong by mutableStateOf<List<MayTinh>>(emptyList())
        private set

    private var pollingJob: Job? = null

    var maytinhCreateResult by mutableStateOf("")
    var maytinhUpdateResult by mutableStateOf("")
    var maytinhDeleteResult by mutableStateOf("")

    var maytinh: MayTinh? by mutableStateOf(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun getAllMayTinh() {
        if (pollingJob != null) return

        pollingJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.maytinhAPIService.getAllMayTinh()
                    if (response.maytinh != null) {
                        danhSachAllMayTinh = response.maytinh!!
                    } else {
                        danhSachAllMayTinh = emptyList()
                    }

                } catch (e: Exception) {
                    Log.e("PhongMayViewModel", "Polling lỗi", e)
                }
                delay(500)
            }
        }
    }

    fun stopPollingMayTinh() {
        pollingJob?.cancel()
        pollingJob = null
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
        if (pollingJob != null) return

        pollingJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.maytinhAPIService.getMayTinhByMaPhong(maphong)
                    if (response.maytinh != null) {
                        danhSachAllMayTinhtheophong = response.maytinh!!
                    } else {
                        danhSachAllMayTinhtheophong = emptyList()
                    }

                } catch (e: Exception) {
                    Log.e("PhongMayViewModel", "Polling lỗi", e)
                }
                delay(500)
            }
        }
    }

    fun createMayTinh(maytinh: MayTinh) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.maytinhAPIService.createMayTinh(maytinh)
                }
                maytinhCreateResult = response.message
            } catch (e: Exception) {
                maytinhCreateResult = "Lỗi khi thêm máy tính: ${e.message}"
                Log.e("MayTinhViewModel", "Lỗi khi thêm máy tính: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun updateMayTinh(maytinh: MayTinh) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.maytinhAPIService.updateMayTinh(maytinh)
                }
                maytinhUpdateResult = response.message
            } catch (e: Exception) {
                maytinhUpdateResult = "Lỗi khi cập nhật máy tính: ${e.message}"
                Log.e("MayTinhViewModel", "Lỗi khi cập nhật máy tính: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteMayTinh(mamay: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val body = mapOf("MaMay" to mamay)
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.maytinhAPIService.deleteMayTinh(body)
                }
                maytinhDeleteResult = response.message

                if (response.message == "MayTinh deleted") {
                    // Cập nhật lại danh sách máy tính sau khi xóa thành công
                    val allResponse = withContext(Dispatchers.IO) {
                        ITLabRoomRetrofitClient.maytinhAPIService.getAllMayTinh()
                    }
                    danhSachAllMayTinh = allResponse.maytinh ?: emptyList()
                }
            } catch (e: Exception) {
                maytinhDeleteResult = "Lỗi khi xóa máy tính: ${e.localizedMessage ?: e.message}"
                Log.e("MayTinhViewModel", "Lỗi khi xóa máy tính", e)
            } finally {
                isLoading = false
            }
        }
    }

}






