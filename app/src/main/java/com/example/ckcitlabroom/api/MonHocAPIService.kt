import com.example.ckcitlabroom.models.LopHoc
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class monHocResponse(
    val monhoc: List<MonHoc>
)

interface MonHocAPIService {

    @GET("MonHoc/read.php")
    suspend fun getAllMonHoc(): monHocResponse

    @GET("MonHoc/show.php")
    suspend fun getMonHocByByID(
        @Query("MaMonHoc") mamonhoc: String
    ): MonHoc

    @POST("MonHoc/create.php")
    suspend fun createMonHoc(@Body monHoc: MonHoc): CreateResponse

    @PUT("MonHoc/update.php")
    suspend fun updateMonHoc(
        @Body monhoc: MonHoc
    ): UpdateResponse



}