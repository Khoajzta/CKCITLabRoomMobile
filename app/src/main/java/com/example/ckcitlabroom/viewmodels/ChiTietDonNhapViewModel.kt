package com.example.lapstore.viewmodels

import ChiTietDonNhap
import ChiTietDonNhapAPIService
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

class ChiTietDonNhapyViewModel : ViewModel() {


    var danhSachChiTietDonNhaptheoMaDonNhap by mutableStateOf<List<ChiTietDonNhap>>(emptyList())
        private set

    var danhSachAllChiTietDonNhap by mutableStateOf(listOf<ChiTietDonNhap>())

    private var pollingAllChiTietDonNhapJob: Job? = null

    var chitietdonnhapCreateResult by mutableStateOf("")
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

    suspend fun createChiTietDonNhap(chiTietDonNhap: ChiTietDonNhap) {
        isLoading = true
        try {
            val response = withContext(Dispatchers.IO) {
                ITLabRoomRetrofitClient.chitietdonnhapAPIService.createChiTietDonNhap(chiTietDonNhap)
            }
            chitietdonnhapCreateResult = response.message
        } catch (e: Exception) {
            chitietdonnhapCreateResult = "Lỗi khi thêm chi tiết đơn nhập: ${e.message}"
            Log.e("ChiTietDonNhapViewModel", "Lỗi khi thêm chi tiết đơn nhập: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    suspend fun createNhieuChiTietDonNhap(danhSach: List<ChiTietDonNhap>): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = ITLabRoomRetrofitClient.chitietdonnhapAPIService.createListChiTietDonNhap(danhSach)
                Result.success(response.message ?: "Thêm chi tiết đơn nhập thành công")
            } catch (e: Exception) {
                Log.e("ChiTietDonNhapViewModel", "Lỗi thêm nhiều chi tiết đơn nhập: ${e.message}")
                Result.failure(e)
            }
        }
    }


    suspend fun createNhieuChiTietDonNhapAsync(ds: List<ChiTietDonNhap>): Boolean {
        return try {
            val response = ITLabRoomRetrofitClient.chitietdonnhapAPIService.createListChiTietDonNhap(ds)
            response.message.contains("thành công", ignoreCase = true)
        } catch (e: Exception) {
            Log.e("ChiTietDonNhapVM", "Lỗi khi tạo chi tiết đơn nhập: ${e.message}")
            false
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






