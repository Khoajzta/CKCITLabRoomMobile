import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.userTypeDataStore by preferencesDataStore(name = "user_type_prefs")

class UserTypePreferences(private val context: Context) {
    private val dataStore = context.userTypeDataStore

    companion object {
        val USER_TYPE_KEY = stringPreferencesKey("user_type") // "sinhvien", "giangvien", hoặc null nếu chưa đăng nhập
    }

    val userTypeFlow: Flow<String?> = dataStore.data.map { prefs ->
        prefs[USER_TYPE_KEY]
    }

    suspend fun saveUserType(type: String) {
        dataStore.edit { prefs ->
            prefs[USER_TYPE_KEY] = type
        }
    }

    suspend fun clearUserType() {
        dataStore.edit { prefs ->
            prefs.remove(USER_TYPE_KEY)
        }
    }
}
