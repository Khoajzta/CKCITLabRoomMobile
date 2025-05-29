import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class PhongMayResponse(
    val phongmay: List<PhongMay>
)

interface PhongMayAPIService {
    @GET("PhongMay/read.php")
    suspend fun getAllPhongMay(): PhongMayResponse

    @GET("PhongMay/show.php")
    suspend fun getPhongMayByMaPhong(
        @Query("MaPhong") maphong: String
    ): PhongMay

    @POST("PhongMay/create.php")
    suspend fun createPhongMay(
        @Body phongmay: PhongMay
    ): CreateResponse

    @PUT("PhongMay/update.php")
    suspend fun updateTrangThaiPhongMay(
        @Body phongmay: PhongMay
    ): UpdateResponse

    @PUT("PhongMay/update.php")
    suspend fun updatePhongMay(
        @Body phongmay: PhongMay
    ): UpdateResponse

    @HTTP(method = "DELETE", path = "PhongMay/delete.php", hasBody = true)
    suspend fun deletePhongMay(
        @Body body: Map<String, String>
    ): DeleteResponse
}
