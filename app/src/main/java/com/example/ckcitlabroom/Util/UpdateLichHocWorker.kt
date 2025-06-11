import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.lapstore.api.Constants.ITLabRoomRetrofitClient
import java.time.LocalDate
import java.time.LocalTime

class UpdateLichHocWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            // B1: Lấy toàn bộ lịch học từ API
            val response = ITLabRoomRetrofitClient.lichhocAPIService.getAllLichHoc()
            val allLichHoc = response.lichhoc

            val currentDate = LocalDate.now()
            val currentTime = LocalTime.now()

            // B2: Lọc các lịch học đã kết thúc (dựa theo ngày và giờ)
            val maLichHocCanUpdate = allLichHoc.filter { lich ->
                try {
                    val ngayDay = LocalDate.parse(lich.NgayDay)
                    val gioKetThuc = LocalTime.parse(lich.GioKetThuc)

                    // Nếu đã qua ngày, hoặc hôm nay nhưng giờ kết thúc đã qua
                    ngayDay.isBefore(currentDate) ||
                            (ngayDay.isEqual(currentDate) && gioKetThuc.isBefore(currentTime))
                } catch (e: Exception) {
                    Log.e("UpdateLichHocWorker", "Lỗi khi parse thời gian: ${e.message}")
                    false
                }
            }.map { it.MaLichHoc }

            Log.d("UpdateLichHocWorker", "Lịch học cần cập nhật: $maLichHocCanUpdate")

            // B3: Gọi API nếu có lịch học cần cập nhật
            if (maLichHocCanUpdate.isNotEmpty()) {
                val requestBody = UpdateLichHocRequest(MaLichHocList = maLichHocCanUpdate)

                val updateResponse = ITLabRoomRetrofitClient
                    .lichhocAPIService
                    .updateTrangThaiLichHoc(requestBody)

                Log.d(
                    "UpdateLichHocWorker",
                    "Đã cập nhật ${maLichHocCanUpdate.size} lịch học: ${updateResponse.message}"
                )
            } else {
                Log.d("UpdateLichHocWorker", "Không có lịch học nào cần cập nhật.")
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("UpdateLichHocWorker", "Lỗi khi cập nhật lịch học", e)
            Result.failure()
        }
    }
}



