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

data class UpdateTrangThaiRequest(
    val MaPhieuMuon: String,
    val TrangThai: Int
)


interface PhieuMuonMayAPIService {
    @GET("PhieuMuonMay/read.php")
    suspend fun getAllPhieuMuonMay(): PhieuMuonMayResponse

    @GET("PhieuMuonMay/show.php")
    suspend fun getPhieuMuonMayByMaPhieuMuon(
        @Query("MaPhieuMuon") maphieumuon: String
    ): PhieuMuonMay

    @POST("PhieuMuonMay/create.php")
    suspend fun createPhieuMuonMay(
        @Body phieuMuonMay: PhieuMuonMay
    ): CreateResponse

    @PUT("PhieuMuonMay/update.php")
    suspend fun updatePhieuMuonMay(
        @Body phieuMuonMay: PhieuMuonMay
    ): UpdateResponse

    @PUT("PhieuMuonMay/updateTrangThai.php")
    suspend fun updateTrangThaiPhieuMuon(
        @Body trangThaiRequest: UpdateTrangThaiRequest
    ): UpdateResponse
}
