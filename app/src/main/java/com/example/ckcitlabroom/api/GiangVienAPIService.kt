import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class LoginRequest(
    val Email: String,
    val MatKhau: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: GiangVien?
)


interface GiangVienAPIService {
    @GET("GiangVien/checkLogin.php")
    suspend fun checkLogin(
        @Query("email") email: String,
        @Query("matkhau") matkhau: String
    ): GiangVien


    @POST("GiangVien/create.php")
    suspend fun createGiangVien(
        @Body giangVien: GiangVien
    ): CreateResponse
}

