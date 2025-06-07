package com.example.lapstore.viewmodels

import ChiTietDonNhap
import ChiTietDonNhapAPIService
import ChiTietSuDungMay
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

class ChiTietSuDungMayViewModel : ViewModel() {


    var danhSachChiTietDonNhaptheoMaDonNhap by mutableStateOf<List<ChiTietDonNhap>>(emptyList())
        private set

    var danhSachAllChiTietDonNhap by mutableStateOf(listOf<ChiTietDonNhap>())

    private var pollingAllChiTietDonNhapJob: Job? = null

    var chitietsudungmayCreateResult by mutableStateOf("")
    var chitietdonnhapUpdateResult by mutableStateOf("")
    var chitietdonnhapDeleteResult by mutableStateOf("")

    var isLoading by mutableStateOf(false)
        private set

    private var pollingChiTietTheoMaDonNhapJob: Job? = null


    fun getAllChiTietDonNhap() {
        if (pollingAllChiTietDonNhapJob != null) return

        pollingAllChiTietDonNhapJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.chitietdonnhapAPIService.getAllChiTietDonNhap()
                    danhSachAllChiTietDonNhap = response.chitietdonnhap ?: emptyList()
                } catch (e: Exception) {
                    Log.e("ChiTietDonNhapViewModel", "Polling all chi tiết lỗi", e)
                }
                delay(500)
            }
        }
    }

    fun stopPollingAllChiTietDonNhap() {
        pollingAllChiTietDonNhapJob?.cancel()
        pollingAllChiTietDonNhapJob = null
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


    fun getChiTietDonNhapTheoMaDonNhap(madon: String) {
        if (pollingChiTietTheoMaDonNhapJob != null) return

        pollingChiTietTheoMaDonNhapJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.chitietdonnhapAPIService.getChiTietDonNhapTheoMaDon(madon)
                    danhSachChiTietDonNhaptheoMaDonNhap = response.chitietdonnhap ?: emptyList()
                } catch (e: Exception) {
                    Log.e("PhongMayViewModel", "Polling theo phòng lỗi", e)
                }
                delay(500)
            }
        }
    }

    fun stopPollingChiTietTheoMaDonNhap() {
        pollingChiTietTheoMaDonNhapJob?.cancel()
        pollingChiTietTheoMaDonNhapJob = null
    }

    suspend fun getChiTietDonNhapListOnce(maDonNhap: String): List<ChiTietDonNhap> {
        return try {
            val response = withContext(Dispatchers.IO) {
                ITLabRoomRetrofitClient.chitietdonnhapAPIService.getChiTietDonNhapTheoMaDon(maDonNhap)
            }
            response.chitietdonnhap ?: emptyList()
        } catch (e: Exception) {
            Log.e("ChiTietViewModel", "Lỗi: ${e.message}")
            emptyList()
        }
    }

}






