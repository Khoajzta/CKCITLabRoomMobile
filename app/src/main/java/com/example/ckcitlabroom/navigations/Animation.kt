
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.ui.graphics.TransformOrigin
import androidx.navigation.*

fun defaultEnterTransition(
    direction: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.Left
): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    scaleIn(
        initialScale = 0.8f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        transformOrigin = TransformOrigin.Center
    ) + fadeIn(
        initialAlpha = 0.3f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
    )
}



fun defaultExitTransition(
    direction: AnimatedContentTransitionScope.SlideDirection = AnimatedContentTransitionScope.SlideDirection.Right
): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    scaleOut(
        targetScale = 0.8f,
        animationSpec = tween(durationMillis = 300, easing = FastOutLinearInEasing),
        transformOrigin = TransformOrigin.Center
    ) + fadeOut(
        animationSpec = tween(durationMillis = 300)
    )
}


