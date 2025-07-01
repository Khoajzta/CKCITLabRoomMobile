import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FloatingActionButtonCustom(
    onClick: () -> Unit,
){
    FloatingActionButton(
        onClick = { onClick() },
        containerColor = Color(0xFF1B8DDE),
        contentColor   = Color.White,
        shape          = CircleShape,
        elevation      = FloatingActionButtonDefaults.elevation(
            defaultElevation = 6.dp,
            pressedElevation = 10.dp
        ),
        modifier = Modifier.size(60.dp)
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF1B8DDE), Color(0xFF6DC8F3))
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}