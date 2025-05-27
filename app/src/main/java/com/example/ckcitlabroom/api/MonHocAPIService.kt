import retrofit2.http.GET

data class monHocResponse(
    val mohoc: List<MoHoc>
)



interface MonHocAPIService {
    @GET("LichHoc/read.php")
    suspend fun getAllLichHoc(): LichHocResponse
}