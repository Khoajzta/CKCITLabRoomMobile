import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun DotLoadingLight() {
    val dotCount = 3
    val delays = listOf(0, 200, 400)
    val offsets = List(dotCount) { remember { Animatable(0f) } }

    offsets.forEachIndexed { index, animatable ->
        LaunchedEffect(Unit) {
            delay(delays[index].toLong())
            animatable.animateTo(
                targetValue = -20f,
                animationSpec = infiniteRepeatable(
                    animation = tween(300, easing = LinearOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize() // giữ nguyên layout chiếm màn hình
            .background(Color.Transparent) // background trong suốt
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            offsets.forEach { animatable ->
                Text(
                    text = ".",
                    fontSize = 200.sp,
                    color = Color.White,
                    modifier = Modifier
                        .offset(y = animatable.value.dp)
                        .padding(8.dp)
                )
            }
        }
    }
}







