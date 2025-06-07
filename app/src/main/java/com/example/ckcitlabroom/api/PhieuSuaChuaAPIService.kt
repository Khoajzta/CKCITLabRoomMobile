import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class PhieuSuaChuaResponse(
    val message: String? = null,
    val phieusuachua: List<PhieuSuaChuaRp>? = null
)



interface PhieuSuaChuaAPIService {
    @GET("PhieuSuaChua/read.php")
    suspend fun getAllPhieuSuaChua(): PhieuSuaChuaResponse

    @POST("PhieuSuaChua/create.php")
    suspend fun createPhieuSuaChua(
        @Body phieusuachua: PhieuSuaChua
    ): CreateResponse

    @PUT("PhieuSuaChua/update.php")
    suspend fun updatePhieuSuaChua(
        @Body phieusuachua: PhieuSuaChua
    ): UpdateResponse
}
