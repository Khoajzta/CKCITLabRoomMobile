import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

// Response cho ThongBao
// message là optional để đồng bộ với các response khác
// thongbao là list các ThongBao trả về từ server

data class ThongBaoResponse(
    val message: String? = null,
    val thongbao: List<ThongBao>? = null
)

interface ThongBaoAPIService {
    @GET("ThongBao/read.php")
    suspend fun getAllThongBao(): ThongBaoResponse

    @GET("ThongBao/show.php")
    suspend fun getThongBaoByID(
        @Query("MaTB") maTB: Int
    ): ThongBao

    @POST("ThongBao/create.php")
    suspend fun createThongBao(
        @Body thongBao: ThongBao
    ): CreateResponse

    @PUT("ThongBao/update.php")
    suspend fun updateThongBao(
        @Body thongBao: ThongBao
    ): UpdateResponse

    @HTTP(method = "DELETE", path = "ThongBao/delete.php", hasBody = true)
    suspend fun deleteThongBao(
        @Body body: Map<String, Int>
    ): DeleteResponse
}
