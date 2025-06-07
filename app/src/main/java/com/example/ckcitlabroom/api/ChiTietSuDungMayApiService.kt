import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class ChiTietSuDungMayResponse(
    val message: String? = null,
    val chitietdonnhap: List<ChiTietDonNhap>? = null
)


interface ChiTietSuDungMayAPIService {
    @POST("ChiTietSuDungMay/create.php")
    suspend fun createChiTietSuDungMay(
        @Body chiTietDonNhap: ChiTietSuDungMay
    ): CreateResponse


    @GET("ChiTietDonNhap/getChiTietDonNhapTheoMaDon.php")
    suspend fun getChiTietDonNhapTheoMaDon(
        @Query("MaDonNhap") MaDonNhap: String
    ): ChiTietDonNhapResponse

    @GET("ChiTietdonnhap/read.php")
    suspend fun getAllChiTietDonNhap(): ChiTietDonNhapResponse
}