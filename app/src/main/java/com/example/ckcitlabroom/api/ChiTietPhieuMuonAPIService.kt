import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class ChiTietPhieuMuonResponse(
    @SerializedName("chitietphieumuon")
    val chitietphieumuon: List<ChiTietPhieuMuon>? = null
)



interface ChiTietPhieuMuonAPIService {
    @POST("ChiTietPhieuMuon/createChiTietPhieuMuonList.php")
    suspend fun createListChiTietPhieuMuon(
        @Body chiTietPhieuMuonList: List<ChiTietPhieuMuon>
    ): CreateResponse

    @GET("ChiTietPhieuMuon/getChiTietPhieuByMaPhieu.php")
    suspend fun getChiTietPhieuMuonByMaPhieu(
        @Query("MaPhieuMuon") maPhieuMuon: String
    ): ChiTietPhieuMuonResponse

    @POST("ChiTietPhieuMuon/updateListChiTiet.php")
    suspend fun updateChiTietPhieuMuonList(
        @Body chiTietPhieuMuonList: List<ChiTietPhieuMuon>
    ): CreateResponse
}