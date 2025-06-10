import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class PhieuMuonMayResponse(
    val message: String? = null,
    val phieumuonmay: List<PhieuMuonMay>? = null
)



interface PhieuMuonMayAPIService {
    @GET("PhieuMuonMay/read.php")
    suspend fun getAllPhieuMuonMay(): PhieuMuonMayResponse

    @GET("PhieuMuonMay/getPhieuSuaChuaTheoMa.php")
    suspend fun getPhieuSuaChuaByMaMay(
        @Query("MaMay") maMay: String
    ): PhieuSuaChuaResponse

    @POST("PhieuMuonMay/create.php")
    suspend fun createPhieuMuonMay(
        @Body phieuMuonMay: PhieuMuonMay
    ): CreateResponse

    @PUT("PhieuMuonMay/update.php")
    suspend fun updatePhieuSuaChua(
        @Body phieusuachua: PhieuSuaChua
    ): UpdateResponse
}
