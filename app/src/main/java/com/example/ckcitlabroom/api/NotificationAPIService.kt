import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.POST

data class NotificationRequest(
    val tokens: List<String>,
    val title: String,
    val body: String
)


data class NotificationResponse(
    val success: Boolean,
    val sent: Int,
    val results: List<TokenResult>
)

data class TokenResult(
    val token: String,
    val response: Map<String, Any>?,
    val status: Any
)


interface NotificationApiService {
    @POST("sendNotification.php")
    suspend fun sendNotification(
        @Body request: NotificationRequest
    ): Response<NotificationResponse>
}