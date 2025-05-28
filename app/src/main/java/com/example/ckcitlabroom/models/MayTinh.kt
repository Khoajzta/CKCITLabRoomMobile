import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MayTinh(
    var MaMay: String,
    var ViTri: String,
    var Main: String,
    var CPU: String,
    var RAM: String,
    var VGA: String,
    var ManHinh: String,
    var BanPhim: String,
    var Chuot: String,
    var HDD: String,
    var SSD: String,
    var MaPhong: String,
    var TrangThai: Int
) : Parcelable
