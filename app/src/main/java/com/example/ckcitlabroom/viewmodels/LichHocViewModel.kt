package com.example.lapstore.viewmodels

import LichHoc
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapstore.api.ITLabRoomRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LichHocViewModel : ViewModel() {
    var danhSachLichHoc by mutableStateOf<List<LichHoc>>(emptyList())

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun getAllLichHoc() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ITLabRoomRetrofitClient.lichhocAPIService.getAllLichHoc()
                danhSachLichHoc = response.lichhoc
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("SanPhamViewModel", "Error fetching all products", e)
            } finally {
                isLoading = false
            }
        }
    }
}




