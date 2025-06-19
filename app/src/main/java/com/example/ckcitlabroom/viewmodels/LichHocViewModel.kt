package com.example.ckcitlabroom.viewmodels

import LichHoc
import LichHocRP
import LichHocTheoThuVaPhongItem
import MaGVRequest
import MaLopHocRequest
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ckcitlabroom.api.Constants.ITLabRoomRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LichHocViewModel : ViewModel() {
    var danhSachLichHoc by mutableStateOf<List<LichHocRP>>(emptyList())
    var danhSachLichHoctheomagv by mutableStateOf<List<LichHocRP>>(emptyList())
    var danhSachLichHoctheomalop by mutableStateOf<List<LichHocRP>>(emptyList())

    var danhSachLichHocTheoThuVaPhong by mutableStateOf<List<LichHocTheoThuVaPhongItem>>(emptyList())
        private set


    var lichhoc by mutableStateOf<LichHoc?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var lichhocCreateResult by mutableStateOf("")
    var lichhocUpdateResult by mutableStateOf("")
    var lichhocDeleteResult by mutableStateOf("")

    private var pollingJob: Job? = null
    private var pollingAllJob: Job? = null
    private var pollingSVJob: Job? = null
    private var pollingALLGVJob: Job? = null

    fun getAllLichHoc() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = ITLabRoomRetrofitClient.lichhocAPIService.getAllLichHoc()
                danhSachLichHoc = response.lichhoc
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("LichHocViewModel", "Lỗi khi lấy tất cả lịch học", e)
            } finally {
                isLoading = false
            }
        }
    }


    fun startPollingAllLichHoc() {
        if (pollingAllJob?.isActive == true) return

        pollingAllJob = viewModelScope.launch {
            while (isActive) {
                try {
                    val response = withContext(Dispatchers.IO) {
                        ITLabRoomRetrofitClient.lichhocAPIService.getAllLichHoc()
                    }
                    danhSachLichHoc = response.lichhoc
                } catch (e: Exception) {
                    errorMessage = "Lỗi polling getAll: ${e.message}"
                    Log.e("LichHocViewModel", "Polling lỗi khi lấy tất cả lịch học", e)
                }
                delay(500)
            }
        }
    }

    fun stopPollingAllLichHoc() {
        pollingAllJob?.cancel()
        pollingAllJob = null
    }

    fun fetchLichHocByMaGVWithLoading(maGV: String) {
        viewModelScope.launch {
            isLoading = true
            getLichHocByMaGV(maGV)
            isLoading = false
        }
    }

    suspend fun getLichHocByMaGV(maGV: String) {
        try {
            val response = withContext(Dispatchers.IO) {
                ITLabRoomRetrofitClient.lichhocAPIService.getLichHocByMaGV(MaGVRequest(maGV))
            }
            danhSachLichHoctheomagv = response.lichhoc ?: emptyList()
        } catch (e: Exception) {
            errorMessage = "Lỗi khi lấy lịch học theo mã GV: ${e.message}"
            Log.e("LichHocViewModel", "Lỗi khi lấy lịch học theo mã GV", e)
        }
    }

    fun getLichHocByMaLich(malichhoc: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                lichhoc = ITLabRoomRetrofitClient.lichhocAPIService.getLichHocByID(malichhoc)
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("LichHocViewModel", "Lỗi khi lấy lịch học", e)

            } finally {
                isLoading = false
            }
        }
    }

    suspend fun getLichHocByMaLopHoc(maLopHoc: String) {
        try {
            val response = withContext(Dispatchers.IO) {
                ITLabRoomRetrofitClient.lichhocAPIService.getLichHocByMaLopHoc(MaLopHocRequest(maLopHoc))
            }
            danhSachLichHoctheomalop = response.lichhoc ?: emptyList()
        } catch (e: Exception) {
            errorMessage = "Lỗi khi lấy lịch học theo mã lop: ${e.message}"
            Log.e("LichHocViewModel", "Lỗi khi lấy lịch học theo mã lop", e)
        }
    }



    fun createLichHoc(lichhoc: LichHoc) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.lichhocAPIService.createLichHoc(lichhoc)
                }
                lichhocCreateResult = response.message
            } catch (e: Exception) {
                lichhocCreateResult = "Lỗi khi thêm lịch học: ${e.message}"
                Log.e("LichHocViewModel", "Lỗi khi thêm lịch học", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun createListLichHoc(list: List<LichHoc>) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.lichhocAPIService.createListLichHoc(list)
                }
                lichhocCreateResult = response.message
            } catch (e: Exception) {
                lichhocCreateResult = "Lỗi khi thêm danh sách lịch học: ${e.message}"
                Log.e("LichHocViewModel", "Lỗi khi thêm danh sách lịch học", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun updateLichHoc(lichhoc: LichHoc) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.lichhocAPIService.updateLichHoc(lichhoc)
                }
                lichhocUpdateResult = response.message
            } catch (e: Exception) {
                lichhocUpdateResult = "LLỗi khi cập nhật lịch học: ${e.message}"
                Log.e("LichHocViewModel", "Lỗi khi cập nhật lịch học: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun updateListLichHoc(list: List<LichHoc>) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.lichhocAPIService.updateListLichHoc(list)
                }
                lichhocUpdateResult = response.message
            } catch (e: Exception) {
                lichhocUpdateResult = "Lỗi khi cập nhật danh sách lịch học: ${e.message}"
                Log.e("LichHocViewModel", "Lỗi khi cập nhật danh sách lịch học", e)
            } finally {
                isLoading = false
            }
        }
    }

    // ==== Polling ====

    fun startPollingLichHocByMaGV(maGV: String) {
        if (pollingJob?.isActive == true) return // tránh chạy trùng

        pollingJob = viewModelScope.launch {
            while (isActive) {
                getLichHocByMaGV(maGV)
                delay(500) // đợi 500ms mỗi lần lặp
            }
        }
    }

    fun startPollingLichHocByMaLopHoc(maLopHoc: String) {
        if (pollingJob?.isActive == true) return

        pollingJob = viewModelScope.launch {
            while (isActive) {
                getLichHocByMaLopHoc(maLopHoc)
                delay(500)
            }
        }
    }

    fun stopPolling() {
        pollingJob?.cancel()
        pollingJob = null
    }

    fun stopPollingSV() {
        pollingSVJob?.cancel()
        pollingSVJob = null
    }

    fun getLichHocTheoThuVaPhong(maTuan: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.lichhocAPIService.getLichHocTheoThuVaPhong(maTuan)
                }
                danhSachLichHocTheoThuVaPhong = response
            } catch (e: Exception) {
                errorMessage = "Lỗi khi lấy lịch học theo thứ và phòng: ${e.message}"
                Log.e("LichHocViewModel", "Lỗi API getLichHocTheoThuVaPhong", e)
            } finally {
                isLoading = false
            }
        }
    }

    fun startPollingLichHocTheoThuVaPhong(maTuan: String) {
        // Dừng polling cũ nếu đang chạy
        pollingALLGVJob?.cancel()

        pollingALLGVJob = viewModelScope.launch {
            while (isActive) {
                getLichHocTheoThuVaPhong(maTuan)
                delay(1000)
            }
        }
    }


    fun stopPollingaALLGV() {
        pollingALLGVJob?.cancel()
        pollingALLGVJob = null
    }
}





