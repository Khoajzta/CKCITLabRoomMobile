import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class PhieSuaChuaResponse(
    val lichhoc: List<LichHoc>
)



interface PhieuSuaChuaAPIService {
    @POST("PhieuSuaChua/create.php")
    suspend fun createPhieuSuaChua(
        @Body phieusuachua: PhieuSuaChua
    ): CreateResponse
}
