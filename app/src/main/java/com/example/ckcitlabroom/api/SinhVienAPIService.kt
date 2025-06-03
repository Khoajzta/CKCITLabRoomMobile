import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class SinhVienResponse(
    val lichhoc: List<LichHoc>
)



interface SinhVienAPIService {
    @GET("LichHoc/read.php")
    suspend fun getAllLichHoc(): LichHocResponse

    @POST("SinhVien/checklogin.php")
    suspend fun CheckLogin(@Body request: LoginRequest): LoginResponse
}
