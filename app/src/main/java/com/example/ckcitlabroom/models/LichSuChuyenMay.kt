import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LichSuChuyenMay(
    var MaLichSu: Int,
    var MaPhongCu: String,
    var MaPhongMoi: String,
    var NgayChuyen: String,
    var MaMay: String,
) : Parcelable
