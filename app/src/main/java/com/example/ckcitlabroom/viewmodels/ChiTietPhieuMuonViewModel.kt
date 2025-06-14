package com.example.lapstore.viewmodels

import ChiTietDonNhap
import ChiTietPhieuMuon
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChiTietPhieuMuonViewModel : ViewModel() {

    var danhSachChiTietPhieuMuonTheoMaPhieu by mutableStateOf<List<ChiTietPhieuMuon>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var chiTietPhieuMuonUpdateResult by mutableStateOf("")

    private var pollingChiTietPhieuMuonTheoMaPhieuJob: Job? = null

    suspend fun createNhieuChiTietPhieuMuon(danhSach: List<ChiTietPhieuMuon>): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = ITLabRoomRetrofitClient.chiteitieuMuonMayAPIService.createListChiTietPhieuMuon(danhSach)
                Result.success(response.message ?: "Thêm chi tiết đơn nhập thành công")
            } catch (e: Exception) {
                Log.e("ChiTietDonNhapViewModel", "Lỗi thêm nhiều chi tiết đơn nhập: ${e.message}")
                Result.failure(e)
            }
        }
    }

    fun getChiTietPhieuMuonTheoMaPhieuOnce(maphieu: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ITLabRoomRetrofitClient.chiteitieuMuonMayAPIService.getChiTietPhieuMuonByMaPhieu(maphieu)
                danhSachChiTietPhieuMuonTheoMaPhieu = response.chitietphieumuon ?: emptyList()
            } catch (e: Exception) {
                Log.e("ChiTietPhieuMuonVM", "Lỗi khi lấy chi tiết phiếu: ${e.message}")
            }
        }
    }

    fun updateNhieuChiTietPhieuMuon(list: List<ChiTietPhieuMuon>) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.chiteitieuMuonMayAPIService.updateChiTietPhieuMuonList(list)
                }
                chiTietPhieuMuonUpdateResult = response.message
            } catch (e: Exception) {
                chiTietPhieuMuonUpdateResult = "Lỗi khi cập nhật danh sách chi tiết phiếu mượn: ${e.message}"
                Log.e("ChiTietPhieuMuonVM", "Lỗi khi cập nhật danh sách chi tiết phiếu mượn", e)
            } finally {
                isLoading = false
            }
        }
    }

}






