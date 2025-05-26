import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import kotlinx.coroutines.delay

@Composable
fun DotLoadingIndicator() {
    val dotCount = 3
    val delays = listOf(0L, 150L, 300L)
    val maxOffset = 10.dp

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(dotCount) { index ->
            var offsetY by remember { mutableStateOf(0.dp) }

            LaunchedEffect(Unit) {
                while (true) {
                    delay(delays[index])
                    animateDot(offsetY, maxOffset) { newValue ->
                        offsetY = newValue
                    }
                }
            }

            Box(
                modifier = Modifier
                    .size(14.dp)
                    .offset(y = -offsetY)
                    .background(Color.White, CircleShape)
            )
        }
    }
}

suspend fun animateDot(
    currentValue: Dp,
    targetValue: Dp,
    onValueChange: (Dp) -> Unit
) {
    val duration = 300
    val step = 16L

    // nhảy lên
    for (time in 0..duration step step.toInt()) {
        val fraction = time.toFloat() / duration
        val value = lerp(0.dp, targetValue, fraction)
        onValueChange(value)
        delay(step)
    }

    // nhảy xuống
    for (time in 0..duration step step.toInt()) {
        val fraction = time.toFloat() / duration
        val value = lerp(targetValue, 0.dp, fraction)
        onValueChange(value)
        delay(step)
    }
}

@Composable
fun DotLoadingOverlay() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x80000000)),
        contentAlignment = Alignment.Center
    ) {
        DotLoadingIndicator()
    }
}





