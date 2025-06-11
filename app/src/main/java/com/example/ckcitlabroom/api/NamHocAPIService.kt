import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class NamHocResponse(
    val message: String? = null,
    val namhoc: List<NamHoc>? = null
)


interface NamHocAPIService {
    @POST("NamHoc/create.php")
    suspend fun createNamHoc(
        @Body namhoc: NamHoc
    ): CreateResponse


    @PUT("NamHoc/update.php")
    suspend fun updateNamHoc(
        @Body namhoc: NamHoc
    ): UpdateResponse

    @GET("NamHoc/read.php")
    suspend fun getAllNamHoc(): NamHocResponse
}