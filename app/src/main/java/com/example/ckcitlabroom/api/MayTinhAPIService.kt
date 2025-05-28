import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PUT
import retrofit2.http.Query

data class MayTinhResponse(
    val maytinh: List<MayTinh>
)

data class UpdateResponse(
    val message: String
)

interface MayTinhAPIService {
    @GET("MayTinh/read.php")
    suspend fun getAllMayTinh(): MayTinhResponse

    @GET("MayTinh/show.php")
    suspend fun getMayTinhByMaMay(
        @Query("MaMay") mamay: String
    ): MayTinh

    @PUT("MayTinh/update.php")
    suspend fun updateMayTinh(
        @Body maytiinh: MayTinh
    ): UpdateResponse

    @HTTP(method = "DELETE", path = "MayTinh/delete.php", hasBody = true)
    suspend fun deleteMayTinh(
        @Body body: Map<String, String>
    ): UpdateResponse

}
