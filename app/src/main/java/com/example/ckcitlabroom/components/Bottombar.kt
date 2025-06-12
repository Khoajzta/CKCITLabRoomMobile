import android.graphics.PointF
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntOffset
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

data class ButtonData(
    val text: String,
    val icon: ImageVector,
    val click:  () -> Unit = {}
)


@Composable
fun AnimatedNavigationBar(
    buttons: List<ButtonData>,
    barColor: Color,
    circleColor: Color,
    selectedColor: Color,
    unselectedColor: Color,
    currentRoute: String?
) {
    val circleRadius = 26.dp
    var selectedItem by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(currentRoute) {
        currentRoute?.let { route ->
            val index = when (route) {
                NavRoute.HOME.route -> 0
                NavRoute.QUANLY.route -> 1
                NavRoute.QUETQRCODE.route -> 2
                NavRoute.ACCOUNT.route -> 3
                else -> selectedItem
            }
            selectedItem = index
        }
    }

    val density = LocalDensity.current
    val circleRadiusPx = with(density) { circleRadius.toPx().toInt() }
    val itemCenters = remember { mutableStateListOf<Float>() }

    // Dùng Animatable để điều khiển animation mượt hơn
    val animatableOffset = remember { Animatable(0f) }

    LaunchedEffect(selectedItem, itemCenters) {
        val target = itemCenters.getOrNull(selectedItem) ?: return@LaunchedEffect
        animatableOffset.animateTo(
            target,
            animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow)
        )
    }

    // Tính toán vị trí điểm khuyết
    val circleOffset = run {
        val shiftRight = with(density) { 12.dp.toPx() }
        val circleSize = with(density) { (circleRadius * 2).toPx() }
        IntOffset((animatableOffset.value - circleSize / 2 + shiftRight).toInt(), -circleRadiusPx)
    }

    val barShape = remember(animatableOffset.value) {
        BarShape(
            offset = animatableOffset.value,
            circleRadius = circleRadius,
            cornerRadius = 35.dp,
        )
    }

    Box {
        // Điểm khuyết (circle)
        Circle(
            modifier = Modifier
                .offset { circleOffset }
                .zIndex(1f),
            color = circleColor,
            radius = circleRadius,
            button = buttons[selectedItem],
            iconColor = selectedColor,
        )

        // Navigation bar
        Row(
            modifier = Modifier
                .padding(bottom = 18.dp, start = 12.dp, end = 12.dp)
                .height(95.dp)
                .graphicsLayer {
                    shape = barShape
                    clip = true
                }
                .fillMaxWidth()
                .background(barColor),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            buttons.forEachIndexed { index, button ->
                val isSelected = index == selectedItem

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .onGloballyPositioned { coordinates ->
                            val centerX = coordinates.positionInParent().x + coordinates.size.width / 2
                            if (itemCenters.size <= index) {
                                itemCenters.add(centerX)
                            } else if (itemCenters[index] != centerX) {
                                itemCenters[index] = centerX
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    this@Row.NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            button.click()
                            selectedItem = index
                        },
                        icon = {
                            val iconAlpha by animateFloatAsState(
                                targetValue = if (isSelected) 0f else 1f,
                                label = "Navbar item icon"
                            )
                            Icon(
                                imageVector = button.icon,
                                contentDescription = button.text,
                                modifier = Modifier.alpha(iconAlpha).size(35.dp)
                            )
                        },
                        label = {
                            Text(button.text, fontWeight = FontWeight.Bold)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = selectedColor,
                            selectedTextColor = selectedColor,
                            unselectedIconColor = unselectedColor,
                            unselectedTextColor = unselectedColor,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }
}




private class BarShape(
    private val offset: Float,
    private val circleRadius: Dp,
    private val cornerRadius: Dp,
    private val circleGap: Dp = 5.dp,
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(getPath(size, density))
    }

    private fun getPath(size: Size, density: Density): Path {
        val cutoutCenterX = offset
        val cutoutRadius = density.run { (circleRadius + circleGap).toPx() }
        val cornerRadiusPx = density.run { cornerRadius.toPx() }

        return Path().apply {
            // Bo 4 góc
            addRoundRect(
                RoundRect(
                    rect = Rect(0f, 0f, size.width, size.height),
                    cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx)
                )
            )

            // Cutout vòng tròn
            val cutoutPath = Path().apply {
                moveTo(cutoutCenterX - cutoutRadius * 1.5f, 0f)
                cubicTo(
                    cutoutCenterX - cutoutRadius, 0f,
                    cutoutCenterX - cutoutRadius, cutoutRadius,
                    cutoutCenterX, cutoutRadius
                )
                cubicTo(
                    cutoutCenterX + cutoutRadius, cutoutRadius,
                    cutoutCenterX + cutoutRadius, 0f,
                    cutoutCenterX + cutoutRadius * 1.5f, 0f
                )
                close()
            }

            // Cắt cutout khỏi main path
            op(this, cutoutPath, PathOperation.Difference)
        }
    }
}


@Composable
private fun Circle(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    radius: Dp,
    button: ButtonData,
    iconColor: Color,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(radius * 2)
            .clip(CircleShape)
            .background(color),
    ) {
        AnimatedContent(
            targetState = button.icon, label = "Bottom bar circle icon",
        ) { targetIcon ->
            Icon(targetIcon, button.text, tint = iconColor, modifier = Modifier.size(35.dp))
        }
    }
}


