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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MayTinhViewModel : ViewModel() {

    var danhSachAllMayTinh by mutableStateOf<List<MayTinh>>(emptyList())
    var maytinhUpdateResult by mutableStateOf("")
    var maytinhDeleteResult by mutableStateOf("")  // Thêm biến lưu kết quả xóa

    var maytinh: MayTinh? by mutableStateOf(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun getAllMayTinh() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                val response = ITLabRoomRetrofitClient.maytinhAPIService.getAllMayTinh()
                danhSachAllMayTinh = response.maytinh
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("MayTinhViewModel", "Lỗi khi lấy danh sách máy tính", e)
            } finally {
                isLoading = false
            }
        }
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

    // Hàm xóa máy tính
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
                    getAllMayTinh()
                }
            } catch (e: Exception) {
                maytinhDeleteResult = "Lỗi khi xóa máy tính: ${e.message}"
                Log.e("MayTinhViewModel", "Lỗi khi xóa máy tính: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

}






