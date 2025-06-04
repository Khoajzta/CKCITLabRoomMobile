package com.example.ckcitlabroom.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ckcitlabroom.models.LopHoc
import com.example.lapstore.api.ITLabRoomRetrofitClient
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
//
//    fun getGiangVienById(magv: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            isLoading = true
//            try {
//                giangvien = ITLabRoomRetrofitClient.giangVienAPIService.getGiangVienByByID(magv)
//            } catch (e: Exception) {
//                errorMessage = e.message
//                Log.e("MayTinhViewModel", "Lỗi khi lấy thông tin máy tính", e)
//            } finally {
//                isLoading = false
//            }
//        }
//    }



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

//    fun updateGiangVien(giangVien: GiangVien) {
//        viewModelScope.launch {
//            isLoading = true
//            try {
//                val response = withContext(Dispatchers.IO) {
//                    ITLabRoomRetrofitClient.giangVienAPIService.updateGiangVien(giangVien)
//                }
//                giangvienUpdateResult = response.message
//            } catch (e: Exception) {
//                giangvienUpdateResult = "Lỗi khi cập nhật máy tính: ${e.message}"
//                Log.e("GiangVienViewModel", "Lỗi khi cập nhật máy tính: ${e.message}")
//            } finally {
//                isLoading = false
//            }
//        }
//    }
//
//    fun deleteGiangVien(magv: String) {
//        viewModelScope.launch {
//            isLoading = true
//            try {
//                val body = mapOf("MaGV" to magv)
//                val response = withContext(Dispatchers.IO) {
//                    ITLabRoomRetrofitClient.giangVienAPIService.deleteGiangVien(body)
//                }
//                giangvienDeleteResult = response.message
//
//                if (response.message == "GiangVien deleted") {
//                    // Cập nhật lại danh sách sau khi xóa thành công
//                    val allResponse = withContext(Dispatchers.IO) {
//                        ITLabRoomRetrofitClient.giangVienAPIService.getAllGiangVien()
//                    }
//                    danhSachAllGiangVien = allResponse.giangvien ?: emptyList()
//                }
//            } catch (e: Exception) {
//                giangvienDeleteResult = "Lỗi khi xóa Giảng viên: ${e.localizedMessage ?: e.message}"
//                Log.e("GiangVienViewModel", "Lỗi khi xóa giảng viên", e)
//            } finally {
//                isLoading = false
//            }
//        }
//    }
}
