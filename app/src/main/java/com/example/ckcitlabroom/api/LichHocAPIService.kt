import com.google.gson.annotations.SerializedName
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

data class LichHocResponse(
    val lichhoc: List<LichHoc>
)

data class LichHocRPResponse(
    val lichhoc: List<LichHocRP>
)

data class MaGVRequest(
    val MaGV: String
)

data class MaLopHocRequest(
    val MaLopHoc: String
)

data class UpdateLichHocRequest(
    val MaLichHocList: List<Int>
)

data class LichHocTheoThuVaPhongItem(
    val MaPhong: String,
    val TenPhong: String,
    val Thu2: List<MonCa>,
    val Thu3: List<MonCa>,
    val Thu4: List<MonCa>,
    val Thu5: List<MonCa>,
    val Thu6: List<MonCa>,
    val Thu7: List<MonCa>,
    val ChuNhat: List<MonCa>
)

data class MonCa(
    val MaMonHoc: String,
    val MaCaHoc: String,
    val TenGiangVien: String
)

interface LichHocAPIService {
    @GET("LichHoc/read.php")
    suspend fun getAllLichHoc(): LichHocRPResponse

    @GET("LichHoc/show.php")
    suspend fun getLichHocByID(
        @Query("MaLichHoc") malichhoc: String
    ): LichHoc

    @PUT("LichHoc/update.php")
    suspend fun updateLichHoc(@Body lichHoc: LichHoc): UpdateResponse

    @POST("LichHoc/create.php")
    suspend fun createLichHoc(@Body lichHoc: LichHoc): CreateResponse

    @POST("LichHoc/createListLichHoc.php")
    suspend fun createListLichHoc(@Body listLichHoc: List<LichHoc>): CreateResponse

    @POST("LichHoc/getLichHocByMaGV.php")
    suspend fun getLichHocByMaGV(@Body request: MaGVRequest): LichHocRPResponse

    @POST("LichHoc/getLichHocByMaLopHoc.php")
    suspend fun getLichHocByMaLopHoc(@Body request: MaLopHocRequest): LichHocRPResponse

    @POST("LichHoc/updateListLichHoc.php")
    suspend fun updateListLichHoc(
        @Body listLichHoc: List<LichHoc>
    ): UpdateResponse

    @POST("LichHoc/updateTrangThai.php")
    suspend fun updateTrangThaiLichHoc(
        @Body request: UpdateLichHocRequest
    ): UpdateResponse

    @GET("LichHoc/getAllLichHocTheoTuan.php")
    suspend fun getLichHocTheoThuVaPhong(
        @Query("MaTuan") maTuan: String
    ): List<LichHocTheoThuVaPhongItem>

}
