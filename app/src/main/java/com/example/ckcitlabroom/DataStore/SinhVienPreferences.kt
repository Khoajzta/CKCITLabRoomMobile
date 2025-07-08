import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

val Context.sinhVienDataStore by preferencesDataStore(name = "sinhvien_prefs")

data class LoginSinhVienState(
    val isLoggedIn: Boolean = false,
    val maSinhVien: String? = null,
    val tenSinhVien: String? = null,
    val ngaySinh: String? = null,
    val gioiTinh: String? = null,
    val email: String? = null,
    val maLop: String? = null,
    val matKhau: String? = null,
    val maLoaiTaiKhoan: Int = 0,
    val token: String? = null,
    val trangThai: Int = 0
)


class SinhVienPreferences(private val context: Context) {
    private val dataStore = context.sinhVienDataStore

    companion object {
        private val LOGIN_KEY = booleanPreferencesKey("sinhvien_login")
        private val MASV_KEY = stringPreferencesKey("sinhvien_ma")
        private val TENSV_KEY = stringPreferencesKey("sinhvien_ten")
        private val NGAYSINH_KEY = stringPreferencesKey("sinhvien_ngaysinh")
        private val GIOITINH_KEY = stringPreferencesKey("sinhvien_gioitinh")
        private val EMAIL_KEY = stringPreferencesKey("sinhvien_email")
        private val MALOP_KEY = stringPreferencesKey("sinhvien_malop")
        private val MATKHAU_KEY = stringPreferencesKey("sinhvien_matkhau")
        private val LOAITK_KEY = intPreferencesKey("sinhvien_loaitk")
        private val TOKEN_KEY = stringPreferencesKey("sinhvien_token")
        private val TRANGTHAI_KEY = intPreferencesKey("sinhvien_trangthai")
    }

    val loginStateFlow: Flow<LoginSinhVienState> = dataStore.data.map { prefs ->
        LoginSinhVienState(
            isLoggedIn = prefs[LOGIN_KEY] ?: false,
            maSinhVien = prefs[MASV_KEY],
            tenSinhVien = prefs[TENSV_KEY],
            ngaySinh = prefs[NGAYSINH_KEY],
            gioiTinh = prefs[GIOITINH_KEY],
            email = prefs[EMAIL_KEY],
            maLop = prefs[MALOP_KEY],
            matKhau = prefs[MATKHAU_KEY],
            maLoaiTaiKhoan = prefs[LOAITK_KEY] ?: 0,
            token = prefs[TOKEN_KEY],
            trangThai = prefs[TRANGTHAI_KEY] ?: 0
        )
    }

    suspend fun saveLoginForSinhVien(sv: SinhVien) {
        dataStore.edit { prefs ->
            prefs[LOGIN_KEY] = true
            prefs[MASV_KEY] = sv.MaSinhVien
            prefs[TENSV_KEY] = sv.TenSinhVien
            prefs[NGAYSINH_KEY] = sv.NgaySinh
            prefs[GIOITINH_KEY] = sv.GioiTinh
            prefs[EMAIL_KEY] = sv.Email
            prefs[MALOP_KEY] = sv.MaLop
            prefs[MATKHAU_KEY] = sv.MatKhau
            prefs[LOAITK_KEY] = sv.MaLoaiTaiKhoan
            prefs[TOKEN_KEY] = sv.Token ?: ""
            prefs[TRANGTHAI_KEY] = sv.TrangThai
        }
        UserTypePreferences(context).saveUserType("sinhvien")
    }

    suspend fun getSinhVienFromDataStore(): SinhVien? {
        val prefs = dataStore.data.firstOrNull() ?: return null

        val maSV = prefs[MASV_KEY] ?: return null
        val tenSV = prefs[TENSV_KEY] ?: ""
        val ngaySinh = prefs[NGAYSINH_KEY] ?: ""
        val gioiTinh = prefs[GIOITINH_KEY] ?: ""
        val email = prefs[EMAIL_KEY] ?: ""
        val maLop = prefs[MALOP_KEY] ?: ""
        val matKhau = prefs[MATKHAU_KEY] ?: ""
        val maLoaiTK = prefs[LOAITK_KEY] ?: 0
        val token = prefs[TOKEN_KEY] ?: ""
        val trangThai = prefs[TRANGTHAI_KEY] ?: 0

        return SinhVien(
            MaSinhVien = maSV,
            TenSinhVien = tenSV,
            NgaySinh = ngaySinh,
            GioiTinh = gioiTinh,
            Email = email,
            MaLop = maLop,
            MatKhau = matKhau,
            MaLoaiTaiKhoan = maLoaiTK,
            Token = token,
            TrangThai = trangThai
        )
    }


    suspend fun logout() {
        dataStore.edit { it.clear() }
        UserTypePreferences(context).clearUserType()
    }

    suspend fun clearLogin() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
        UserTypePreferences(context).clearUserType()
    }
}

