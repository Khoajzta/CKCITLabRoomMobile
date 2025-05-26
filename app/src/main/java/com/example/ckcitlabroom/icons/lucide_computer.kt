import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val iconComputer: ImageVector
	get() {
		if (_undefined != null) {
			return _undefined!!
		}
		_undefined = ImageVector.Builder(
            name = "Computer",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
			path(
    			fill = null,
    			fillAlpha = 1.0f,
    			stroke = SolidColor(Color(0xFF000000)),
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 2f,
    			strokeLineCap = StrokeCap.Round,
    			strokeLineJoin = StrokeJoin.Round,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(7f, 2f)
				horizontalLineTo(17f)
				arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 19f, 4f)
				verticalLineTo(8f)
				arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 17f, 10f)
				horizontalLineTo(7f)
				arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 5f, 8f)
				verticalLineTo(4f)
				arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 7f, 2f)
				close()
			}
			path(
    			fill = null,
    			fillAlpha = 1.0f,
    			stroke = SolidColor(Color(0xFF000000)),
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 2f,
    			strokeLineCap = StrokeCap.Round,
    			strokeLineJoin = StrokeJoin.Round,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(4f, 14f)
				horizontalLineTo(20f)
				arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 22f, 16f)
				verticalLineTo(20f)
				arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 20f, 22f)
				horizontalLineTo(4f)
				arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 2f, 20f)
				verticalLineTo(16f)
				arcTo(2f, 2f, 0f, isMoreThanHalf = false, isPositiveArc = true, 4f, 14f)
				close()
			}
			path(
    			fill = null,
    			fillAlpha = 1.0f,
    			stroke = SolidColor(Color(0xFF000000)),
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 2f,
    			strokeLineCap = StrokeCap.Round,
    			strokeLineJoin = StrokeJoin.Round,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(6f, 18f)
				horizontalLineToRelative(2f)
			}
			path(
    			fill = null,
    			fillAlpha = 1.0f,
    			stroke = SolidColor(Color(0xFF000000)),
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 2f,
    			strokeLineCap = StrokeCap.Round,
    			strokeLineJoin = StrokeJoin.Round,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(12f, 18f)
				horizontalLineToRelative(6f)
			}
		}.build()
		return _undefined!!
	}

private var _undefined: ImageVector? = null
