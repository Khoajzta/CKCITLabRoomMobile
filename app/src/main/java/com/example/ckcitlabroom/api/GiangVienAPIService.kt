import com.example.ckcitlabroom.models.LopHoc
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class LoginRequest(
    val email: String,
    val matkhau: String
)

data class GiangVienRequest(
    val email_or_magv: String
)

data class GiangVienResponse(
    val message: String? = null,
    val giangvien: List<GiangVien>? = null
)

data class LoginResponse(
    val result: Boolean,
    val message: String? = null
)

interface GiangVienAPIService {

    @POST("GiangVien/checkLogin.php")
    suspend fun CheckLogin(@Body request: LoginRequest): LoginResponse


    @GET("GiangVien/read.php")
    suspend fun getAllGiangVien(): GiangVienResponse

    @GET("GiangVien/getGiangVienTheoMaOrEmail.php")
    suspend fun getGiangVienByEmailOrMaGV(@Query("key") key: String): GiangVien

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

    @PUT("GiangVien/updateTrangThaiGiangVien.php")
    suspend fun updateTrangThaiGiangVien(
        @Body giangVien: GiangVien
    ): UpdateResponse

    @PUT("giangvien/updateLoaiTKGiangVien.php")
    suspend fun updateLoaiTaiKhoanGiangVien(
        @Body giangVien: GiangVien
    ): UpdateResponse

    @HTTP(method = "DELETE", path = "GiangVien/delete.php", hasBody = true)
    suspend fun deleteGiangVien(
        @Body body: Map<String, String>
    ): DeleteResponse
}

