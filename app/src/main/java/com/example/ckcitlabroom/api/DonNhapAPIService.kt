import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class DonNhapResponse(
    val message: String? = null,
    val donnhap: List<DonNhap>? = null
)

interface DonNhapAPIService {
    @GET("DonNhap/read.php")
    suspend fun getAllDonNhap(): DonNhapResponse

    @POST("DonNhap/create.php")
    suspend fun createDonNhap(
        @Body donnhap: DonNhap
    ): CreateResponse
}