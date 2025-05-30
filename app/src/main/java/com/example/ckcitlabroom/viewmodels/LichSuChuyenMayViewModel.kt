package com.example.lapstore.viewmodels

import LichSuChuyenMay
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

class LichSuChuyenMayViewModel : ViewModel() {


    var danhSachLichSuTheoMay by mutableStateOf<List<LichSuChuyenMay>>(emptyList())
        private set

    private var pollingLichSuTheoMayJob: Job? = null

    var lichsuchuyenmayCreateResult by mutableStateOf("")
    var lichsuchuyenmaUpdateResult by mutableStateOf("")
    var lichsuchuyenmaDeleteResult by mutableStateOf("")

    var isLoading by mutableStateOf(false)
        private set


    fun getLichSuTheoMay(mamay: String) {
        if (pollingLichSuTheoMayJob != null) return

        pollingLichSuTheoMayJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.lichSuChuyenMayAPIService.getLichSuByMaMay(mamay)
                    danhSachLichSuTheoMay = response.lichsuchuyenmay ?: emptyList()
                } catch (e: Exception) {
                    Log.e("PhongMayViewModel", "Polling theo phòng lỗi", e)
                }
                delay(500)
            }
        }
    }

    fun stopPollingLichSuTheoMay() {
        pollingLichSuTheoMayJob?.cancel()
        pollingLichSuTheoMayJob = null
    }

    fun createLichSuChuyenMay(lichSuChuyenMay: LichSuChuyenMay) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.lichSuChuyenMayAPIService.createLichSuChuyenMay(lichSuChuyenMay)
                }
                lichsuchuyenmayCreateResult = response.message
            } catch (e: Exception) {
                lichsuchuyenmayCreateResult = "Lỗi khi thêm lich su: ${e.message}"
                Log.e("LichSuChuyenMayViewModel", "Lỗi khi thêm lich su: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
}






