import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
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
data class GiangVienResponse(
    val message: String? = null,
    val giangvien: List<GiangVien>? = null
)

interface GiangVienAPIService {
    @GET("GiangVien/checkLogin.php")
    suspend fun checkLogin(
        @Query("email") email: String,
        @Query("matkhau") matkhau: String
    ): GiangVien

    @GET("GiangVien/read.php")
    suspend fun getAllGiangVien(): GiangVienResponse

    @GET("GiangVien/show.php")
    suspend fun getGiangVienByByID(
        @Query("MaGV") magv: String
    ): GiangVien

    @POST("GiangVien/create.php")
    suspend fun createGiangVien(
        @Body giangVien: GiangVien
    ): CreateResponse

    @PUT("GiangVien/update.php")
    suspend fun updateGiangVien(
        @Body giangVien: GiangVien
    ): UpdateResponse

    @HTTP(method = "DELETE", path = "GiangVien/delete.php", hasBody = true)
    suspend fun deleteGiangVien(
        @Body body: Map<String, String>
    ): DeleteResponse
}

