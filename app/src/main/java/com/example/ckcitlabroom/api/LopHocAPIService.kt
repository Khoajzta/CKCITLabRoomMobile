import com.example.ckcitlabroom.models.LopHoc
import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class LoiHocResponse(
    val lichhoc: List<LichHoc>
)
data class LopResponse(
    val message: String? = null,
    val lophoc: List<LopHoc>? = null
)


interface LopHocAPIService {
    @GET("LichHoc/read.php")
    suspend fun getAllLichHoc(): LichHocResponse

    @GET("LopHoc/read.php")
    suspend fun getAllLopHoc(): LopResponse

    @GET("LopHoc/show.php")
    suspend fun getLopHocByByID(
        @Query("MaLopHoc") malop: String
    ): LopHoc

    @POST("LopHoc/create.php")
    suspend fun createLopHoc(
        @Body lopHoc: LopHoc
    ): CreateResponse

    @PUT("LopHoc/update.php")
    suspend fun updateLopHoc(
        @Body lopHoc: LopHoc
    ): UpdateResponse

    @HTTP(method = "DELETE", path = "LopHoc/delete.php", hasBody = true)
    suspend fun deleteLopHoc(
        @Body body: Map<String, String>
    ): DeleteResponse
}

