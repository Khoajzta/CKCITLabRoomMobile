import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class LichSuSuaMayResponse(
    val message: String? = null,
    val lichsusuamay: List<LichSuSuaMay>? = null
)

interface LichSuSuaMayAPIService {

    @GET("LichSuSuaMay/read.php")
    suspend fun getAllLichSuSuaMay(): LichSuSuaMayResponse

    @POST("LichSuSuaMay/create.php")
    suspend fun createLichSuSuaMay(
        @Body lichSuSuaMay: LichSuSuaMay
    ): CreateResponse

    @PUT("LichSuSuaMay/update.php")
    suspend fun updateLichSuSuaMay(
        @Body lichSuSuaMay: LichSuSuaMay
    ): UpdateResponse

    // Nếu bạn có API lấy lịch sử sửa máy theo MaMay, ví dụ:
    @GET("LichSuSuaMay/getLichSuSuaMayTheoMaMay.php")
    suspend fun getLichSuTheoMaMay(
        @Query("MaMay") maMay: String
    ): LichSuSuaMayResponse

    @GET("LichSuSuaMay/getLichSuSuaMayTheoMaPhieuSuaChua.php")
    suspend fun getLichSuTheoMaPhieu(
        @Query("MaPhieuSuaChua") maPhieuSuaChua: String
    ): LichSuSuaMay
}

