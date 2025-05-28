import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

enum class SnackbarType {
    SUCCESS, ERROR
}

data class CustomSnackbarData(
    val message: String,
    val type: SnackbarType
)


