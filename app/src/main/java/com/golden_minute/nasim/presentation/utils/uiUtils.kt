package com.golden_minute.nasim.presentation.utils


/**
 * this modifier is used for configuring glass effect on the composable
 */

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.golden_minute.nasim.R
import com.golden_minute.nasim.presentation.main.ActivityViewModel
import kotlinx.coroutines.launch

enum class BorderSide {
    TOP, BOTTOM, LEFT, RIGHT
}

private const val RefreshTriggerDistance = 100f
private const val MaxPullDistance = RefreshTriggerDistance * 1.5f

fun Modifier.oneSideBorder(
    width: Dp = 1.dp,
    color: Color = Color.Black,
    side: BorderSide
) = this.then(
    Modifier.drawBehind {
        val strokeWidth = width.toPx()
        when (side) {
            BorderSide.TOP -> {
                drawLine(
                    color = color,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokeWidth
                )
            }

            BorderSide.BOTTOM -> {
                drawLine(
                    color = color,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
            }

            BorderSide.LEFT -> {
                drawLine(
                    color = color,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = strokeWidth
                )
            }

            BorderSide.RIGHT -> {
                drawLine(
                    color = color,
                    start = Offset(size.width, 0f),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
            }
        }
    }
)




/**
 * Applies a one-time shimmer effect to a Composable, typically used for text.
 * The shimmer animation sweeps a highlight color across the Composable's content.
 *
 * This Modifier is designed to run the shimmer animation once after a specified delay.
 * It's useful for drawing attention to an element or indicating a newly loaded state.
 *
 * The shimmer effect works by drawing the original content and then overlaying a gradient
 * that moves horizontally. The gradient is clipped to the shape of the content,
 * creating the illusion that the content itself is shimmering.
 *
 *
 * @param textColor The base color of the content (e.g., the text color).
 * @param highlightColor The color that will sweep across the content to create the shimmer.
 * @param durationMillis The duration of the shimmer animation in milliseconds.
 *                       How long it takes for the highlight to pass over the content.
 * @param delayMillis The delay in milliseconds before the shimmer animation starts.
 *
 * @return A [Modifier] that applies the one-time shimmer effect.
 **/
fun Modifier.oneTimeShimmer(
    textColor: Color,
    highlightColor: Color,
    startAnimation: Boolean,
    durationMillis: Int = 3000,
    delayMillis: Int = 2500
): Modifier = composed {

    // `size` stores the dimensions (width and height) of the Composable this Modifier is applied to.
    // It's initialized to IntSize.Zero (width=0, height=0) and updated when the Composable is laid out.
    var size by remember { mutableStateOf(IntSize.Zero) }

    // `progress` is an `Animatable` float value that will drive the shimmer animation.
    // An Animatable allows for smooth animation between values. It starts at 0f.
    val progress = remember { Animatable(0f) }

    // This derived state is the key to a flicker-free transition.
    // `isEffectActive` is true only when the animation is actually running (progress is between 0 and 1).
    // This prevents applying the graphics layers and drawing operations when they are not needed,
    // potentially improving performance and avoiding visual artifacts at the start or end of the animation.
    val isEffectActive = progress.value > 0f && progress.value < 1f

    // `LaunchedEffect` is a coroutine builder that runs a suspend function (lambda)
    // when the Composable enters the composition and cancels it when it leaves.
    // The `key1 = size` parameter means this effect will relaunch if the `size` of the Composable changes.
    // This is important because the shimmer calculation depends on the Composable's width.
    LaunchedEffect(size) {

        // Only start the animation if the Composable has a valid width and the animation has never started before.
        if (size.width > 0 && startAnimation) {
            // `snapTo(0f)` immediately sets the progress to 0f without animation.
            // This resets the animation if the size changes, ensuring it starts from the beginning.
            progress.snapTo(0f)
            // `animateTo` starts an animation that changes `progress.value` from its current value (0f)
            // to the `targetValue` (1f).
            progress.animateTo(
                targetValue = 1f,
                // `animationSpec` defines how the animation progresses over time.
                // `tween` creates a simple animation with a specified duration and delay.
                animationSpec = tween(
                    durationMillis = durationMillis, // How long the shimmer takes to complete.
                    delayMillis = delayMillis       // Wait this long before starting.
                )
            )
            ActivityViewModel.animatePullToRefresh = false
        }
    }

    this // `this` refers to the Modifier chain we are building upon.
        // `onSizeChanged` is a Modifier that calls the provided lambda whenever the
        // size of the Composable it's applied to changes. We use it to update our `size` state.
        .onSizeChanged { size = it }
        // `then` is used to chain another Modifier. The Modifier we apply next
        // will only be added if `isEffectActive` is true.
        .then(
            // Conditionally apply the entire effect chain ONLY when the animation is active.
            if (isEffectActive) {

                // Calculate the horizontal translation of the shimmer gradient based on the animation `progress`.
                // The gradient moves from left to right. `size.width * 1.5f` makes the shimmer
                // start from outside the left edge and end outside the right edge, creating a full sweep.
                val shimmerTranslate = progress.value * (size.width * 1.5f)
                // The width of the gradient itself. We make it half the width of the Composable
                // to create a noticeable highlight band.
                val gradientWidth = size.width / 2f

                // `Brush.linearGradient` creates a gradient that transitions between colors along a line.
                val brush = Brush.linearGradient(
                    colors = listOf(
                        textColor,      // Start with the base text color (outside the shimmer area)
                        highlightColor, // The bright shimmer color in the middle
                        textColor,      // End with the base text color (again, outside the shimmer area)
                    ),
                    // `start` and `end` define the line along which the gradient colors are distributed.
                    // We offset these by `shimmerTranslate` to move the gradient across the Composable.
                    start = androidx.compose.ui.geometry.Offset(x = shimmerTranslate - gradientWidth, y = 0f),
                    end = androidx.compose.ui.geometry.Offset(x = shimmerTranslate, y = 0f)
                )

                Modifier
                    // `graphicsLayer` allows applying transformations (like alpha) and rendering effects.
                    // Setting alpha to 0.99f (or any value slightly less than 1f) can sometimes help
                    // with how blend modes are applied, ensuring the `SrcIn` mode works correctly
                    // by forcing the content into an offscreen buffer.
                    .graphicsLayer(alpha = 0.99f)
                    // `drawWithContent` allows drawing custom graphics on top of or beneath the
                    // Composable's original content.
                    .drawWithContent {
                        // `drawContent()` is crucial. It draws the original content of the Composable
                        // (e.g., the text itself).
                        drawContent()
                        // `drawRect` draws a rectangle. Here, this rectangle will be filled with our `brush`.
                        drawRect(
                            brush = brush,
                            // `blendMode = BlendMode.SrcIn` is the magic that makes the shimmer effect work.
                            // `SrcIn` means "draw the source (our gradient rectangle) only where the
                            // destination (the original content drawn by `drawContent()`) is opaque."
                            // This effectively clips the gradient to the shape of the text or other content.
                            blendMode = BlendMode.SrcIn
                        )
                    }
            } else {
                // When `isEffectActive` is false (animation hasn't started, has finished, or width is 0),
                // we apply an empty Modifier. This means no shimmer effect is drawn.
                Modifier
            }
        )
}


fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2f * size.width.toFloat(),
        targetValue = 2f * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ),
        label = "shimmerEffect",
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFC5C5C5).copy(0.7f),
                Color(0xFF5B5B5B),
                Color(0xFFC5C5C5).copy(0.7f),
            ), start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())

        )
    ).onGloballyPositioned {
        size = it.size
    }
}


@Composable
fun WeatherDetailElement(
    weatherIcon: Int,
    weatherStatus: String,
    weatherLabel: String,
    iconAtTop: Boolean,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        if (iconAtTop) {
            Icon(
                painter = painterResource(weatherIcon),
                contentDescription = weatherLabel,
                modifier = modifier.size(40.dp)
            )
            Spacer(modifier.height(10.dp))
            Text(
                text = weatherStatus,
                style = MaterialTheme.typography.headlineSmall,
                letterSpacing = 1.sp
            )
            Spacer(modifier.height(10.dp))
            Text(
                text = weatherLabel,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(0.6f)
            )

        } else {
            Text(
                text = weatherLabel,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White.copy(0.8f)
            )
            Spacer(modifier.height(10.dp))
            Image(
                painter = painterResource(weatherIcon),
                contentDescription = weatherLabel,
                modifier = Modifier.size(35.dp)
            )
            Spacer(modifier.height(10.dp))
            Text(
                text = weatherStatus,
                style = MaterialTheme.typography.titleLarge,
                letterSpacing = 2.sp
            )
            Spacer(modifier.height(16.dp))

        }

    }
}

/**
 * A custom composable that implements the pull-to-refresh pattern.
 *
 * It displays a progress indicator that animates as the user pulls down. When the pull
 * gesture exceeds a defined threshold, the refresh action is triggered.
 *
 * @param isRefreshing A boolean state representing whether the content is currently being refreshed.
 * Setting this to `true` shows an indeterminate loading indicator.
 * @param onRefresh A suspend lambda function that is executed when a refresh is triggered.
 * This is where the data loading logic should reside.
 * @param canBePulled A lambda that returns `true` if the pull gesture should be enabled. This can be used
 * to disable pull-to-refresh when the scrollable content is not at the top.
 * @param content The main composable content to be displayed below the refresh indicator.
 */
@Composable
fun CustomPullToRefresh(
    isRefreshing: Boolean,
    onRefresh: suspend () -> Unit,
    canBePulled: () -> Boolean,
    pulledToDistance : (Boolean) -> Unit,
    content: @Composable () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    var pullProgress by remember { mutableFloatStateOf(0f) }

    // Animate the height of the indicator container.
    // It grows as the user pulls, snaps to a fixed height when refreshing,
    // and animates back to 0 otherwise.
    val indicatorHeight by animateFloatAsState(
        targetValue = when {
            isRefreshing -> RefreshTriggerDistance
            pullProgress > 0f -> pullProgress.coerceAtMost(RefreshTriggerDistance)
            else -> 0f
        }, label = "IndicatorHeight"
    )


    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // Prevent scrolling while a refresh is in progress.
                if (isRefreshing) return Offset(0f, available.y)

                val isPullingDown = available.y > 0
                val isPushingUp = available.y < 0

                // If the user pushes up while the indicator is visible, reduce the pull progress.
                if (isPushingUp && pullProgress > 0f) {
                   if (pullProgress < RefreshTriggerDistance) pulledToDistance(false)
                    val newProgress = pullProgress + (available.y * 0.23f) // available.y is negative
                    pullProgress = newProgress.coerceAtLeast(0f)
                    // Consume the scroll delta to shrink the indicator instead of scrolling the list.
                    return Offset(0f, available.y)
                }

                // If the user is pulling down and the gesture is allowed, update the pull progress.
                if (isPullingDown && canBePulled() && source == NestedScrollSource.UserInput) {
                    val newProgress = pullProgress + (available.y * 0.23f) // Use a multiplier to create resistance.
                    if (pullProgress >= RefreshTriggerDistance) pulledToDistance(true)
                    pullProgress = newProgress.coerceIn(0f, MaxPullDistance)
                    // Consume the scroll delta to show the indicator instead of scrolling the list.


                    return Offset(0f, available.y)
                }

                return Offset.Zero // Don't interfere with other scroll events.
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                // If the user flings past the trigger distance, trigger the refresh.
                if (pullProgress > RefreshTriggerDistance) {
                    coroutineScope.launch {
                        onRefresh()
                    }
                }
                pulledToDistance(false)
                // Always reset pull progress after a fling.
                pullProgress = 0f
                return super.onPostFling(consumed, available)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        // The container for the refresh indicator. Its height is animated.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(indicatorHeight.dp)
                // Add padding to account for the status bar.
                .padding(top = WindowInsets.statusBars
                    .asPaddingValues()
                    .calculateTopPadding() + 32.dp),
            contentAlignment = Alignment.Center
        ) {
            // Only show the indicator if it has a meaningful height.
            if (indicatorHeight.dp > 32.dp) {
                if (isRefreshing) {
                    // Show an indeterminate spinner when refreshing.
                    CircularProgressIndicator(strokeWidth = 3.dp)
                } else {
                    // Show a determinate spinner that fills up as the user pulls.
                    val progressFraction = (pullProgress / RefreshTriggerDistance).coerceIn(0f, 1f)
                    CircularProgressIndicator(
                        progress = { progressFraction },
                        strokeWidth = 3.dp,
                        // Scale the indicator for a more dynamic feel.
                        modifier = Modifier.size(65.dp).scale(progressFraction.coerceAtLeast(0.2f))
                    )
                }
            }
        }

        // The main content area.
        Box(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()) {
            content()
        }
    }
}

fun getWeatherAppearance(weatherCode: Int, isDay: Int, isOutline: Boolean = false): Int {
    return when (weatherCode) {
        1000 -> {
            if (isDay == 1) {
                if (isOutline) {
                    R.drawable.sun_outline
                } else {
                    R.drawable.sun_main
                }
            } else
                if (isOutline) {
                    R.drawable.moon_outline
                } else {
                    R.drawable.moon
                }
        } // Sunny-Clear
        1003 -> {
            if (isDay == 1) {
                if (isOutline)
                    R.drawable.cloudy_day_outline
                else
                    R.drawable.cloudy_day
            } else {
                if (isOutline)
                    R.drawable.cloudy_night_outline
                else
                    R.drawable.cloudy_night
            }
        } // Partly cloudy day-night
        1006 -> {
            if (isOutline)
                R.drawable.cloud_outline
            else
                R.drawable.cloud
        } // Cloudy
        1009 -> {
            if (isOutline)
                R.drawable.cloud_outline
            else
                R.drawable.cloud
        } // Overcast
        1030 -> {
            if (isOutline)
                R.drawable.haze_outline
            else
                R.drawable.haze
        } // Mist
        1063, 1150, 1153, 1180, 1183, 1186, 1189, 1192, 1195, 1240, 1243, 1246 -> {
            if (isOutline)
                R.drawable.rain_outline
            else
                R.drawable.rain_icon
        } // Rain variations
        1066, 1114, 1117, 1210, 1213, 1216, 1219, 1222, 1225, 1255, 1258 -> {
            if (isOutline)
                R.drawable.snow_outline
            else
                R.drawable.snow
        } // Snow variations
        1069, 1072, 1168, 1171, 1198, 1201, 1204, 1207, 1249, 1252, 1261, 1264 -> {
            if (isOutline)
                R.drawable.hail_outline
            else
                R.drawable.hail
        } // Sleet, freezing drizzle, ice pellets
        1087, 1273, 1276, 1279, 1282 -> {
            if (isOutline)
                R.drawable.lightning_and_rain_outline
            else
                R.drawable.lightning_and_rain
        } // Thunder with rain
        1135, 1147 -> {
            if (isOutline)
                R.drawable.haze_outline
            else
                R.drawable.haze
        } // Fog, freezing fog
        1100 -> {
            if (isOutline)
                R.drawable.windy_outline
            else
                R.drawable.windy
        }// Windy
        1089 -> {
            if (isOutline)
                R.drawable.lightning_and_cloud
            else
                R.drawable.lightning_and_cloud
        } // Lightning and cloud
        else -> R.drawable.cloud // Default to cloud icon for unknown codes
    }
}
