package com.example.lapstore.viewmodels

import ChiTietDonNhap
import DonNhap
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

class ChiTietDonNhapyViewModel : ViewModel() {


    var danhSachChiTietDonNhapTheoMaDon by mutableStateOf<List<ChiTietDonNhap>>(emptyList())
        private set

    var chitietdonnhapCreateResult by mutableStateOf("")
    var chitietdonnhapUpdateResult by mutableStateOf("")
    var chitietdonnhapDeleteResult by mutableStateOf("")

    var isLoading by mutableStateOf(false)
        private set

    private var pollingChiTietTheoMaDonNhapJob: Job? = null

    fun createChiTietDonNhap(chiTietDonNhap: ChiTietDonNhap) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.chitietdonnhapAPIService.createChiTietDonNhap(chiTietDonNhap)
                }
                chitietdonnhapCreateResult = response.message
            } catch (e: Exception) {
                chitietdonnhapCreateResult = "Lỗi khi thêm máy tính: ${e.message}"
                Log.e("ChiTietDonNhapViewModel", "Lỗi khi thêm máy tính: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }


    fun getChiTietDonNhapTheoMaDonNhap(madonnhap: String) {
        if (pollingChiTietTheoMaDonNhapJob != null) return

        pollingChiTietTheoMaDonNhapJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.chitietdonnhapAPIService.getChiTietDonNhapTheoMaDon(madonnhap)
                    danhSachChiTietDonNhapTheoMaDon = response.chitietdonnhap ?: emptyList()
                } catch (e: Exception) {
                    Log.e("ChiTietDonNhapViewModel", "Polling theo mã lỗi", e)
                }
                delay(500)
            }
        }
    }

    fun stopPollingChiTietTheoMaDonNhap() {
        pollingChiTietTheoMaDonNhapJob?.cancel()
        pollingChiTietTheoMaDonNhapJob = null
    }
}






