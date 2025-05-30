import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class LichSuChuyenMayResponse(
    val message: String? = null,
    val lichsuchuyenmay: List<LichSuChuyenMay>? = null
)

interface LichSuChuyenMayAPIService {
    @GET("LichSuChuyenMay/read.php")
    suspend fun getAllMayTinh(): MayTinhResponse

    @GET("LichSuChuyenMay/getLichSuByMaMay.php")
    suspend fun getLichSuByMaMay(
        @Query("MaMay") mamay: String
    ): LichSuChuyenMayResponse

    @GET("LichSuChuyenMay/getMayTinhTheoPhong.php")
    suspend fun getMayTinhByMaPhong(
        @Query("MaPhong") maphong: String
    ): MayTinhResponse

    @POST("LichSuChuyenMay/create.php")
    suspend fun createLichSuChuyenMay(
        @Body lichsuchuyenmay: LichSuChuyenMay
    ): CreateResponse

    @PUT("LichSuChuyenMay/update.php")
    suspend fun updateMayTinh(
        @Body maytiinh: MayTinh
    ): UpdateResponse

    @HTTP(method = "DELETE", path = "MayTinh/delete.php", hasBody = true)
    suspend fun deleteMayTinh(
        @Body body: Map<String, String>
    ): DeleteResponse
}
