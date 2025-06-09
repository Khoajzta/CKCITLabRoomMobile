package com.example.lapstore.viewmodels

import DonNhap
import LichSuChuyenMay
import MayTinh
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

class DonNhapViewModel : ViewModel() {


    var danhSachDonNhap by mutableStateOf<List<DonNhap>>(emptyList())
        private set

    private var pollingAllDonNhapJob: Job? = null

    var donnhapCreateResult by mutableStateOf("")
    var donnhapUpdateResult by mutableStateOf("")
    var donnhapDeleteResult by mutableStateOf("")

    var isLoading by mutableStateOf(false)
        private set


    fun getAllDonNhap() {
        if (pollingAllDonNhapJob != null) return

        pollingAllDonNhapJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.donnhapAPIService.getAllDonNhap()
                    danhSachDonNhap = response.donnhap ?: emptyList()
                } catch (e: Exception) {
                    Log.e("DonNhapViewModel", "Polling all don nhap lỗi", e)
                }
                delay(500)
            }
        }
    }

    fun stopPollingAllDonNhap() {
        pollingAllDonNhapJob?.cancel()
        pollingAllDonNhapJob = null
    }

    // 1. Hàm suspend thực hiện tạo đơn nhập và trả về kết quả
    suspend fun createDonNhapAsync(donNhap: DonNhap): Boolean {
        return try {
            val response = withContext(Dispatchers.IO) {
                ITLabRoomRetrofitClient.donnhapAPIService.createDonNhap(donNhap)
            }
            // Giả sử response có trường 'success' hoặc kiểm tra message
            response.message.contains("thành công", ignoreCase = true)
        } catch (e: Exception) {
            Log.e("DonNhapViewModel", "Lỗi khi thêm don nhap: ${e.message}")
            false
        }
    }

    // 2. Hàm gọi launch để cập nhật UI
    fun createDonNhap(donNhap: DonNhap) {
        viewModelScope.launch {
            isLoading = true
            val success = createDonNhapAsync(donNhap)
            donnhapCreateResult = if(success) "Thêm đơn nhập thành công" else "Thêm đơn nhập thất bại"
            isLoading = false
        }
    }

}






