
import Tuan
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// Data class phản hồi từ API
data class TuanResponse(
    val message: String? = null,
    val tuan: List<Tuan>? = null
)

interface TuanAPIService {

    @POST("Tuan/create.php")
    suspend fun createTuan(
        @Body tuan: Tuan
    ): CreateResponse

    @GET("Tuan/getTuanByMaNam.php")
    suspend fun getTuanByMaNam(
        @Query("MaNam") maNam: String
    ): TuanResponse

    @GET("Tuan/read.php")
    suspend fun getAllTuan(): TuanResponse
}
