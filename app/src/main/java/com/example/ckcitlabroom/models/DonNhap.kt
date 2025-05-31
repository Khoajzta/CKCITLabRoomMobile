import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DonNhap(
    var MaDonNhap: String,
    var NgayNhap: String,
    var SoLuong: Int,
    var NhaCungCap: String,

) : Parcelable
