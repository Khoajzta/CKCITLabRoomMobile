package com.example.lapstore.viewmodels

import MayTinh
import MayTinhTrangThaiUpdateRequest
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lapstore.api.Constants.ITLabRoomRetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MayTinhViewModel : ViewModel() {

    var mt = MayTinh("","",""",""","","","","","","","","","","","",1)

    var danhSachAllMayTinh by mutableStateOf(listOf<MayTinh>())

    var danhSachAllMayTinhtheophong by mutableStateOf<List<MayTinh>>(emptyList())
        private set

    private val _danhSachMayTinhDuocChon = mutableStateListOf<MayTinh>()
    val danhSachMayTinhDuocChon: SnapshotStateList<MayTinh> get() = _danhSachMayTinhDuocChon // ✅



    fun addMayTinhDuocChon(mayTinh: MayTinh) {
        // Chỉ thêm nếu chưa có trong danh sách (dựa vào MaMay)
        if (_danhSachMayTinhDuocChon.none { it.MaMay == mayTinh.MaMay }) {
            _danhSachMayTinhDuocChon.add(mayTinh)
        }
    }

    fun clearDanhSachMayTinhDuocChon() {
        _danhSachMayTinhDuocChon.clear()
        Log.d("Clear", "clear rồi")
    }

    fun removeMayTinhDuocChon(mayTinh: MayTinh) {
        _danhSachMayTinhDuocChon.removeAll { it.MaMay == mayTinh.MaMay }
    }


    private var pollingAllMayTinhJob: Job? = null
    private var pollingMayTinhTheoPhongJob: Job? = null

    var maytinhCreateResult by mutableStateOf("")
    var maytinhUpdateResult by mutableStateOf("")
    var maytinhDeleteResult by mutableStateOf("")

    var maytinh: MayTinh by mutableStateOf(mt)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun getAllMayTinh() {
        if (pollingAllMayTinhJob != null) return

        pollingAllMayTinhJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.maytinhAPIService.getAllMayTinh()
                    danhSachAllMayTinh = response.maytinh ?: emptyList()
                } catch (e: Exception) {
                    Log.e("PhongMayViewModel", "Polling all máy lỗi", e)
                }
                delay(500)
            }
        }
    }

    fun stopPollingAllMayTinh() {
        pollingAllMayTinhJob?.cancel()
        pollingAllMayTinhJob = null
    }

    // Version A: Gán kết quả vào biến `maytinh`
    fun getMayTinhByMaMay(maMay: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                maytinh = ITLabRoomRetrofitClient.maytinhAPIService.getMayTinhByMaMay(maMay)
            } catch (e: Exception) {
                errorMessage = e.message
                Log.e("MayTinhViewModel", "Lỗi khi lấy thông tin máy tính", e)
            } finally {
                isLoading = false
            }
        }
    }

    // Version B: Trả kết quả qua callback
    fun getMayTinhByMaMay(maMay: String, onResult: (MayTinh?) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            try {
                val result = ITLabRoomRetrofitClient.maytinhAPIService.getMayTinhByMaMay(maMay)
                withContext(Dispatchers.Main) {
                    onResult(result)
                }
            } catch (e: Exception) {
                Log.e("MayTinhViewModel", "Lỗi khi lấy máy theo mã: ${e.message}")
                withContext(Dispatchers.Main) {
                    onResult(null)
                }
            } finally {
                isLoading = false
            }
        }
    }


    fun getMayTinhByPhong(maphong: String) {
        if (pollingMayTinhTheoPhongJob != null) return

        pollingMayTinhTheoPhongJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                try {
                    val response = ITLabRoomRetrofitClient.maytinhAPIService.getMayTinhByMaPhong(maphong)
                    danhSachAllMayTinhtheophong = response.maytinh ?: emptyList()
                } catch (e: Exception) {
                    Log.e("PhongMayViewModel", "Polling theo phòng lỗi", e)
                }
                delay(500)
            }
        }
    }

    fun stopPollingMayTinhTheoPhong() {
        pollingMayTinhTheoPhongJob?.cancel()
        pollingMayTinhTheoPhongJob = null
    }

    suspend fun getAllMayTinhOnce(): List<MayTinh> {
        return try {
            val response = withContext(Dispatchers.IO) {
                ITLabRoomRetrofitClient.maytinhAPIService.getAllMayTinh()
            }
            response.maytinh ?: emptyList()
        } catch (e: Exception) {
            Log.e("MayTinhViewModel", "Lỗi: ${e.message}")
            emptyList()
        }
    }



    fun createMayTinh(maytinh: MayTinh) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.maytinhAPIService.createMayTinh(maytinh)
                }
                maytinhCreateResult = response.message
            } catch (e: Exception) {
                maytinhCreateResult = "Lỗi khi thêm máy tính: ${e.message}"
                Log.e("MayTinhViewModel", "Lỗi khi thêm máy tính: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    suspend fun createNhieuMayTinh(danhSach: List<MayTinh>): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = ITLabRoomRetrofitClient.maytinhAPIService.createListMayTinh(danhSach)
                Result.success(response.message ?: "Thêm máy thành công")
            } catch (e: Exception) {
                Log.e("MayTinhViewModel", "Lỗi thêm nhiều máy: ${e.message}")
                Result.failure(e)
            }
        }
    }



    suspend fun createMayTinhBlocking(maytinh: MayTinh): Boolean {
        return try {
            val response = withContext(Dispatchers.IO) {
                ITLabRoomRetrofitClient.maytinhAPIService.createMayTinh(maytinh)
            }
            response.message.contains("thành công", ignoreCase = true)
        } catch (e: Exception) {
            Log.e("MayTinhViewModel", "Lỗi khi thêm máy tính: ${e.message}")
            false
        }
    }


    fun updateMayTinh(maytinh: MayTinh) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.maytinhAPIService.updateMayTinh(maytinh)
                }
                maytinhUpdateResult = response.message
            } catch (e: Exception) {
                maytinhUpdateResult = "Lỗi khi cập nhật máy tính: ${e.message}"
                Log.e("MayTinhViewModel", "Lỗi khi cập nhật máy tính: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun updateTrangThaiMayTinh(request: MayTinhTrangThaiUpdateRequest) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.maytinhAPIService.updateTrangThaiMay(request)
                }
                maytinhUpdateResult = response.message
            } catch (e: Exception) {
                maytinhUpdateResult = "Lỗi khi cập nhật máy tính: ${e.message}"
                Log.e("MayTinhViewModel", "Lỗi khi cập nhật máy tính: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }




    fun deleteMayTinh(mamay: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val body = mapOf("MaMay" to mamay)
                val response = withContext(Dispatchers.IO) {
                    ITLabRoomRetrofitClient.maytinhAPIService.deleteMayTinh(body)
                }
                maytinhDeleteResult = response.message

                if (response.message == "MayTinh deleted") {
                    // Cập nhật lại danh sách máy tính sau khi xóa thành công
                    val allResponse = withContext(Dispatchers.IO) {
                        ITLabRoomRetrofitClient.maytinhAPIService.getAllMayTinh()
                    }
                    danhSachAllMayTinh = allResponse.maytinh ?: emptyList()
                }
            } catch (e: Exception) {
                maytinhDeleteResult = "Lỗi khi xóa máy tính: ${e.localizedMessage ?: e.message}"
                Log.e("MayTinhViewModel", "Lỗi khi xóa máy tính", e)
            } finally {
                isLoading = false
            }
        }
    }
}






