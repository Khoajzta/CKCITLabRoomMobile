import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import android.util.Log
import com.example.lapstore.api.Constants.ITLabRoomRetrofitClient

class NotificationViewModel : ViewModel() {

    fun sendNotificationToTokens(tokens: List<String>, title: String, body: String) {
        viewModelScope.launch {
            try {
                val request = NotificationRequest(
                    tokens = tokens,
                    title = title,
                    body = body
                )

                val response = ITLabRoomRetrofitClient.notificationAPIService.sendNotification(request)

                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("FCM", "Notification sent to ${result?.sent} devices")
                    result?.results?.forEach {
                        Log.d("FCM", "Token: ${it.token}, Status: ${it.status}, Response: ${it.response}")
                    }
                } else {
                    Log.e("FCM", "Failed to send notification: ${response.code()}")
                }

            } catch (e: Exception) {
                Log.e("FCM", "Exception: ${e.message}")
            }
        }
    }
}