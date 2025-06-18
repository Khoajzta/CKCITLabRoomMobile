import retrofit2.http.GET

data class CauHinhResponse(
    val lichhoc: List<LichHoc>
)



interface CauHinhAPIService {
    @GET("LichHoc/read.php")
    suspend fun getAllLichHoc(): LichHocResponse
}
