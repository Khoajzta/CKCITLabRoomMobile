import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class ChiTietSuDungMayResponse(
    val message: String? = null,
    val chitietsudung: List<ChiTietSuDungMayRP>? = null
)


data class DeleteChiTietSuDungRequest(
    val MaChiTietSuDung: Int
)


interface ChiTietSuDungMayAPIService {
    @POST("ChiTietSuDungMay/create.php")
    suspend fun createChiTietSuDungMay(
        @Body chiTietDonNhap: ChiTietSuDungMay
    ): CreateResponse

    @GET("ChiTietSuDungMay/read.php")
    suspend fun getAllChItietSuDungMay(): ChiTietSuDungMayResponse

    @POST("ChiTietSuDungMay/delete.php")
    suspend fun deleteChiTietSuDungMay(
        @Body request: DeleteChiTietSuDungRequest
    ): DeleteResponse

}