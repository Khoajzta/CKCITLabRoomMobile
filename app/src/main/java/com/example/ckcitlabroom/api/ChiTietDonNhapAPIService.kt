import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class ChiTietDonNhapResponse(
    val message: String? = null,
    val chitietdonnhap: List<ChiTietDonNhap>? = null
)


interface ChiTietDonNhapAPIService {
    @POST("ChiTietDonNhap/create.php")
    suspend fun createChiTietDonNhap(
        @Body chiTietDonNhap: ChiTietDonNhap
    ): CreateResponse

    @POST("ChiTietDonNhap/createListChiTietDonNhap.php")
    suspend fun createListChiTietDonNhap(
        @Body chiTietDonNhapList: List<ChiTietDonNhap>
    ): CreateResponse

    @GET("ChiTietDonNhap/getChiTietDonNhapTheoMaDon.php")
    suspend fun getChiTietDonNhapTheoMaDon(
        @Query("MaDonNhap") MaDonNhap: String
    ): ChiTietDonNhapResponse

    @GET("ChiTietDonNhap/read.php")
    suspend fun getAllChiTietDonNhap(): ChiTietDonNhapResponse
}