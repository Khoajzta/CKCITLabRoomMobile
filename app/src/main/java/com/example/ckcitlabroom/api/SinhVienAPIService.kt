import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP

import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

//data class SinhVienResponse(
//    val lichhoc: List<LichHoc>
//)
data class SinhVienResponse(
    val message: String? = null,
    val sinhvien: List<SinhVien>? = null
)


interface SinhVienAPIService {
    @GET("LichHoc/read.php")
    suspend fun getAllLichHoc(): LichHocResponse


    @POST("SinhVien/checkLogin.php")
    suspend fun CheckLogin(@Body request: LoginRequest): LoginResponse

    @GET("SinhVien/read.php")
    suspend fun getAllSinhVien(): SinhVienResponse

    @GET("SinhVien/show.php")
    suspend fun getSinhVienByByID(
        @Query("MaSinhVien") masv: String
    ): SinhVien

    @POST("SinhVien/create.php")
    suspend fun createSinhVien(
        @Body sinhVien: SinhVien
    ): CreateResponse

    @PUT("SinhVien/update.php")
    suspend fun updateSinhVien(
        @Body sinhVien: SinhVien
    ): UpdateResponse

    @PUT("SinhVien/updateTrangThaiSinhVien.php")
    suspend fun updateTrangThaiSinhVien(
        @Body sinhVien: SinhVien
    ): UpdateResponse

    @HTTP(method = "DELETE", path = "SinhVien/delete.php", hasBody = true)
    suspend fun deleteSinhVien(
        @Body body: Map<String, String>
    ): DeleteResponse

    @GET("SinhVien/getSinhVienTheoMaOrEmail.php")
    suspend fun getSinhVienByEmailOrMaSV(@Query("key") key: String): SinhVien

}
