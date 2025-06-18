import retrofit2.http.GET

data class LoaiTaiKhoanResponse(
    val lichhoc: List<LichHoc>
)



interface LoaiTaiKhoanAPIService {
    @GET("LichHoc/read.php")
    suspend fun getAllLichHoc(): LichHocResponse
}
