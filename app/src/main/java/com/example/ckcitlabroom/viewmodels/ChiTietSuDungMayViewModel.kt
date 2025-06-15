package com.example.lapstore.viewmodels

import ChiTietDonNhap
import ChiTietDonNhapAPIService
import ChiTietSuDungMay
import ChiTietSuDungMayRP
import DonNhap
import LichSuChuyenMay
import MayTinh
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapstore.api.Constants.ITLabRoomRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChiTietSuDungMayViewModel : ViewModel() {

    var danhSachAllChiTiet by mutableStateOf<List<ChiTietSuDungMayRP>>(emptyList())
        private set

    var chitietsudungmayCreateResult by mutableStateOf("")

    private var pollingAllChiTietJob: Job? = null

    var isLoading by mutableStateOf(false)
        private set

    fun getAllChiTietSuDungMay() {
        if (pollingAllChiTietJob != null) return

        pollingAllChiTietJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.chiTietSuDungMayAPIService.getAllChItietSuDungMay()
                    danhSachAllChiTiet = response.chitietsudung ?: emptyList()
                } catch (e: Exception) {
                    Log.e("ChiTietSuDungMayViewModel", "Polling all chi tiet lỗi", e)
                }
                delay(500)
            }
        }
    }

    fun stopPollingAllChiTiet() {
        pollingAllChiTietJob?.cancel()
        pollingAllChiTietJob = null
    }


    fun createChiTietSuDungMay(chiTietSuDungMay: ChiTietSuDungMay) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.chiTietSuDungMayAPIService.createChiTietSuDungMay(chiTietSuDungMay)
                }
                chitietsudungmayCreateResult = response.message

            } catch (e: Exception) {
                chitietsudungmayCreateResult = "Lỗi khi thêm máy tính: ${e.message}"
                Log.e("ChiTietSuDungMayViewModel", "Lỗi khi thêm máy tính: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
}






