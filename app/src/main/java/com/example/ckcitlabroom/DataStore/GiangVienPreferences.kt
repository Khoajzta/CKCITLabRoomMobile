
import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.giangVienDataStore by preferencesDataStore(name = "giangvien_prefs")

data class LoginGiangVienState(
    val isLoggedIn: Boolean = false,
    val maGiangVien: String? = null,
    val tenGiangVien: String? = null,
    val ngaySinh: String? = null,
    val gioiTinh: String? = null,
    val email: String? = null,
    val matKhau: String? = null,
    val maLoaiTaiKhoan: Int? = null,
    val trangThai: Int? = null
)


class GiangVienPreferences(private val context: Context) {
    private val dataStore = context.giangVienDataStore

    companion object {
        private val LOGIN_KEY = booleanPreferencesKey("giangvien_login")
        private val MAGV_KEY = stringPreferencesKey("giangvien_ma")
        private val TENGV_KEY = stringPreferencesKey("giangvien_ten")
        private val NGAYSINH_KEY = stringPreferencesKey("giangvien_ngaysinh")
        private val GIOITINH_KEY = stringPreferencesKey("giangvien_gioitinh")
        private val EMAIL_KEY = stringPreferencesKey("giangvien_email")
        private val MATKHAU_KEY = stringPreferencesKey("giangvien_matkhau")
        private val MALOAITK_KEY = intPreferencesKey("giangvien_maloaitaikhoan")
        private val TRANGTHAI_KEY = intPreferencesKey("giangvien_trangthai")
    }

    val loginStateFlow: Flow<LoginGiangVienState> = dataStore.data.map { prefs ->
        LoginGiangVienState(
            isLoggedIn = prefs[LOGIN_KEY] ?: false,
            maGiangVien = prefs[MAGV_KEY],
            tenGiangVien = prefs[TENGV_KEY],
            ngaySinh = prefs[NGAYSINH_KEY],
            gioiTinh = prefs[GIOITINH_KEY],
            email = prefs[EMAIL_KEY],
            matKhau = prefs[MATKHAU_KEY],
            maLoaiTaiKhoan = prefs[MALOAITK_KEY],
            trangThai = prefs[TRANGTHAI_KEY]
        )
    }

    suspend fun saveLoginForGiangVien(giangVien: GiangVien) {
        dataStore.edit { prefs ->
            prefs[LOGIN_KEY] = true
            prefs[MAGV_KEY] = giangVien.MaGV
            prefs[TENGV_KEY] = giangVien.TenGiangVien
            prefs[NGAYSINH_KEY] = giangVien.NgaySinh
            prefs[GIOITINH_KEY] = giangVien.GioiTinh
            prefs[EMAIL_KEY] = giangVien.Email
            prefs[MATKHAU_KEY] = giangVien.MatKhau
            prefs[MALOAITK_KEY] = giangVien.MaLoaiTaiKhoan
            prefs[TRANGTHAI_KEY] = giangVien.TrangThai
        }
        UserTypePreferences(context).saveUserType("giangvien")
    }

    suspend fun logout() {
        dataStore.edit { it.clear() }
        UserTypePreferences(context).clearUserType()
    }
}
