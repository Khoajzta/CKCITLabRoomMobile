import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

data class PhongMayResponse(
    val lichhoc: List<LichHoc>
)



interface PhongMayAPIService {
    @GET("LichHoc/read.php")
    suspend fun getAllLichHoc(): LichHocResponse
}
