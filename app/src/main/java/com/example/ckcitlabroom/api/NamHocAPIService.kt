import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class NamHocResponse(
    val message: String? = null,
    val namhoc: List<NamHoc>? = null
)


interface NamHocAPIService {
    @POST("NamHoc/create.php")
    suspend fun createNamHoc(
        @Body namhoc: NamHoc
    ): CreateResponse


    @GET("NamHoc/show.php")
    suspend fun getChiTietDonNhapTheoMaDon(
        @Query("MaDonNhap") MaDonNhap: String
    ): ChiTietDonNhapResponse

    @GET("NamHoc/read.php")
    suspend fun getAllNamHoc(): NamHocResponse
}