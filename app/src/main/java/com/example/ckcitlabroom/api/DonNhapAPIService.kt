import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class DonNhapResponse(
    val message: String? = null,
    val donnhap: List<DonNhap>? = null
)

data class DonNhapRequest(
    val NgayNhap: String,
    val SoLuong: Int,
    val NhaCungCap: String,
    val MaPhong: String,
    val LinhKien: LinhKien
)

data class LinhKien(
    val main: String,
    val cpu: String,
    val ram: String,
    val vga: String,
    val manHinh: String,
    val banPhim: String,
    val chuot: String,
    val hdd: String,
    val ssd: String
)



interface DonNhapAPIService {
    @GET("DonNhap/read.php")
    suspend fun getAllDonNhap(): DonNhapResponse

    @POST("DonNhap/create.php")
    suspend fun createDonNhap(
        @Body donNhapRequest: DonNhapRequest
    ): CreateResponse
}