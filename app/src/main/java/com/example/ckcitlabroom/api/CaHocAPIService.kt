import com.example.ckcitlabroom.models.CaHoc
import com.example.ckcitlabroom.models.LopHoc
import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class CaHocResponse(
    val lichhoc: List<LichHoc>
)
data class CaResponse(
    val message: String? = null,
    val cahoc: List<CaHoc>? = null
)


interface CaHocAPIService {
    @GET("LichHoc/read.php")
    suspend fun getAllLichHoc(): LichHocResponse

    @GET("CaHoc/read.php")
    suspend fun getAllCaHoc(): CaResponse

    @GET("CaHoc/show.php")
    suspend fun getCaHocByByID(
        @Query("MaCaHoc") maca: String
    ): CaHoc

    @POST("CaHoc/create.php")
    suspend fun createCaHoc(
        @Body caHoc: CaHoc
    ): CreateResponse

    @PUT("CaHoc/update.php")
    suspend fun updateCaHoc(
        @Body caHoc: CaHoc
    ): UpdateResponse

    @PUT("CaHoc/updateTrangThaiCaHoc.php")
    suspend fun updateTrangThaiCaHoc(
        @Body caHoc: CaHoc
    ): UpdateResponse

    @HTTP(method = "DELETE", path = "CaHoc/delete.php", hasBody = true)
    suspend fun deleteCaHoc(
        @Body body: Map<String, String>
    ): DeleteResponse

}
