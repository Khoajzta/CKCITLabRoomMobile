import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class ChiTietSuDungMayResponse(
    val message: String? = null,
    val chitietsudung: List<ChiTietSuDungMayRP>? = null
)


interface ChiTietSuDungMayAPIService {
    @POST("ChiTietSuDungMay/create.php")
    suspend fun createChiTietSuDungMay(
        @Body chiTietDonNhap: ChiTietSuDungMay
    ): CreateResponse

    @GET("ChiTietSuDungMay/read.php")
    suspend fun getAllChItietSuDungMay(): ChiTietSuDungMayResponse
}