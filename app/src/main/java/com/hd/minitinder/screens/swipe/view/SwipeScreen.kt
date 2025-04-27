package com.hd.minitinder.screens.swipe.view

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.IntOffset
import coil.compose.rememberAsyncImagePainter
import kotlin.math.abs
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.graphics.graphicsLayer
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hd.minitinder.R
import com.hd.minitinder.common.fragments.logo.LogoTinder
import com.hd.minitinder.screens.swipe.viewmodel.SwipeViewModel
import com.hd.minitinder.ui.theme.PrimaryColor
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import com.hd.minitinder.screens.swipe.viewmodel.UserProfile

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("NewApi")
@Composable
fun SwipeScreen(navController: NavController, SwipeViewModel: SwipeViewModel = viewModel()) {
    val density = LocalDensity.current
    val users = SwipeViewModel.availableUsers;
    val currentUser by SwipeViewModel.userState.collectAsState()
    var swipeCount by remember { mutableIntStateOf(0) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var isAnimating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val cw = 300f
    val thresholdDp = cw / 2
    val previousUser = SwipeViewModel.previousUsers;

    // Current image index for each user
    var currentImageIndex by remember { mutableIntStateOf(0) }

    // Animation states for feedback effects
    var showLikeEffect by remember { mutableStateOf(false) }
    var showDislikeEffect by remember { mutableStateOf(false) }
    var showUndoEffect by remember { mutableStateOf(false) }

    // Animation values
    val likeIconScale by animateFloatAsState(
        targetValue = if (showLikeEffect) 1.5f else 0f,
        animationSpec = keyframes {
            durationMillis = 500
            0f at 0
            1.5f at 150
            1.2f at 300
            1.5f at 500
        },
        label = "likeIconScale"
    )

    val likeIconAlpha by animateFloatAsState(
        targetValue = if (showLikeEffect) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "likeIconAlpha"
    )

    val dislikeIconScale by animateFloatAsState(
        targetValue = if (showDislikeEffect) 1.5f else 0f,
        animationSpec = keyframes {
            durationMillis = 500
            0f at 0
            1.5f at 150
            1.2f at 300
            1.5f at 500
        },
        label = "dislikeIconScale"
    )

    val dislikeIconAlpha by animateFloatAsState(
        targetValue = if (showDislikeEffect) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "dislikeIconAlpha"
    )

    val undoIconScale by animateFloatAsState(
        targetValue = if (showUndoEffect) 1.5f else 0f,
        animationSpec = keyframes {
            durationMillis = 500
            0f at 0
            1.5f at 150
            1.2f at 300
            1.5f at 500
        },
        label = "undoIconScale"
    )

    val undoIconAlpha by animateFloatAsState(
        targetValue = if (showUndoEffect) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "undoIconAlpha"
    )

    // Function to trigger feedback animation
    fun showFeedbackEffect(effect: String) {
        when (effect) {
            "like" -> {
                showLikeEffect = true
                coroutineScope.launch {
                    delay(800)
                    showLikeEffect = false
                }
            }
            "dislike" -> {
                showDislikeEffect = true
                coroutineScope.launch {
                    delay(800)
                    showDislikeEffect = false
                }
            }
            "undo" -> {
                showUndoEffect = true
                coroutineScope.launch {
                    delay(800)
                    showUndoEffect = false
                }
            }
        }
    }

    suspend fun animateOffsetX(target: Float, duration: Long = 300) {
        val startTime = System.currentTimeMillis()
        val startValue = offsetX
        while (true) {
            val currentTime = System.currentTimeMillis()
            val elapsedTime = currentTime - startTime
            val progress = elapsedTime.toFloat() / duration
            if (progress >= 1f) {
                offsetX = target
                break
            } else {
                val currentValue = startValue + (target - startValue) * progress
                offsetX = currentValue
            }
            delay(16)
        }
    }

    fun calculateRotation(offsetX: Float): Float {
        val maxAngle = 15f
        return (offsetX / thresholdDp) * maxAngle
    }

    fun calculateScale(offsetX: Float): Float {
        val scaleReduction = 0.1f
        val reductionFactor = (abs(offsetX) / thresholdDp) * scaleReduction
        return 1f - reductionFactor
    }

    fun calculateVerticalOffset(offsetX: Float): Float {
        return abs(offsetX) / 10f
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        if (users.isNotEmpty()) {
            val user = users.first()
            Card(
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp * 0.94f)
                    .height(LocalConfiguration.current.screenHeightDp.dp * 0.70f)
                    .offset {
                        val offsetXPx = with(density) { offsetX.dp.toPx() }
                        IntOffset(offsetXPx.roundToInt(), 0)
                    }
                    .graphicsLayer {
                        val angle = calculateRotation(offsetX)
                        val scale = calculateScale(offsetX)
                        val verticalOffsetDp = calculateVerticalOffset(offsetX)

                        val verticalOffsetPx = with(density) { verticalOffsetDp.dp.toPx() }
                        translationY = -verticalOffsetPx
                        rotationZ = angle
                        scaleX = scale
                        scaleY = scale
                    }
                    .pointerInput(Unit) {
                        if (isAnimating) return@pointerInput

                        detectHorizontalDragGestures(
                            onDragEnd = {
                                if (offsetX < -thresholdDp) {
                                    isAnimating = true
                                    coroutineScope.launch {
                                        animateOffsetX(-1000f)
                                        SwipeViewModel.missUser(users[0].id)
                                        previousUser.add(users.removeFirst())
                                        swipeCount++
                                        offsetX = 0f
                                        currentImageIndex = 0 // Reset image index for new user
                                        isAnimating = false
                                        showFeedbackEffect("dislike")
                                    }
                                } else if (offsetX > thresholdDp) {
                                    isAnimating = true
                                    coroutineScope.launch {
                                        animateOffsetX(1000f)
                                        SwipeViewModel.likeUser(users[0].id)
                                        previousUser.add(users.removeFirst())
                                        swipeCount++
                                        offsetX = 0f
                                        currentImageIndex = 0 // Reset image index for new user
                                        isAnimating = false
                                        showFeedbackEffect("like")
                                    }
                                } else {
                                    isAnimating = true
                                    coroutineScope.launch {
                                        animateOffsetX(0f)
                                        isAnimating = false
                                    }
                                }
                            },
                            onHorizontalDrag = { _, dragAmountPx ->
                                val dragAmountDp = with(density) { dragAmountPx.toDp() }
                                offsetX += dragAmountDp.value
                            }
                        )
                    }
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(20.dp),
                        spotColor = Color.Black
                    )
                    .align(Alignment.Center),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black)
            ) {
                Box {
                    // Display current image based on currentImageIndex
                    Image(
                        painter = rememberAsyncImagePainter(user.imageUrls[currentImageIndex]),
                        contentDescription = "${user.name} - Photo ${currentImageIndex + 1}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Add subtle gradient overlay at the top for better visibility
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .align(Alignment.TopCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.4f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    // Image indicators at the top
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 12.dp, start = 12.dp, end = 12.dp)
                            .fillMaxWidth(0.95f)
                            .height(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        for (i in user.imageUrls.indices) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        if (i == currentImageIndex)
                                            Color.White
                                        else
                                            Color.White.copy(alpha = 0.4f)
                                    )
                            )
                        }
                    }

                    // Image navigation controls
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp)
                            .align(Alignment.Center),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Previous image button
                        IconButton(
                            onClick = {
                                if (currentImageIndex > 0) {
                                    currentImageIndex--
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.3f)),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.Transparent
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowLeft,
                                contentDescription = "Previous Image",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        // Next image button
                        IconButton(
                            onClick = {
                                if (currentImageIndex < user.imageUrls.size - 1) {
                                    currentImageIndex++
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.3f)),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.Transparent
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = "Next Image",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    // Gradient overlay at the bottom for better text visibility
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.6f),
                                        Color.Black.copy(alpha = 0.8f)
                                    )
                                )
                            )
                    )

                    // User info at the bottom
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 16.dp, end = 16.dp, bottom = 20.dp, top = 8.dp)
                            .fillMaxWidth()
                    ) {
                        // Name and age with shadow
                        Text(
                            text = "${user.name}, ${user.age}",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        // Location and occupation
                        Text(
                            text = "${user.occupation} • ${user.address}",
                            fontSize = 16.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        // Tags in a flow layout
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            maxItemsInEachRow = Int.MAX_VALUE
                        ) {
                            user.tags.forEach { tag ->
                                TagChip(tag = tag)
                            }
                        }
                    }

                    // Like and dislike indicators that appear when swiping
                    if (offsetX > 60) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 36.dp, end = 36.dp)
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF4CAF50))
                                .border(2.dp, Color.White, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "LIKE",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    } else if (offsetX < -60) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(top = 36.dp, start = 36.dp)
                                .size(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFF5252))
                                .border(2.dp, Color.White, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "NOPE",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }

            // Action buttons at the bottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
                    .height(80.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Undo button
                    if (currentUser.isPremium) {
                        IconButton(
                            onClick = {
                                if (previousUser.isNotEmpty()) {
                                    previousUser.removeLast()?.let { users.add(0, it) }
                                    currentImageIndex = 0 // Reset image index for restored user
                                    showFeedbackEffect("undo")
                                }
                            },
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color.Transparent)
                                .border(2.dp, Color(0xFFC8720B), CircleShape)
                                .padding(4.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(R.drawable.reload),
                                contentDescription = "Undo",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }


                    // Dislike button
                    IconButton(
                        onClick = {
                            isAnimating = true
                            coroutineScope.launch {
                                animateOffsetX(-1000f)
                                SwipeViewModel.missUser(users[0].id)
                                previousUser.add(users.removeFirst())
                                swipeCount++
                                offsetX = 0f
                                currentImageIndex = 0 // Reset image index for new user
                                isAnimating = false
                                showFeedbackEffect("dislike")
                            }
                        },
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.Transparent)
                            .border(2.dp, Color(0xFFF3485B), CircleShape)
                            .padding(8.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(R.drawable.dislike),
                            contentDescription = "Dislike",
                            //tint = Color(0xFFF3485B),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Like button
                    IconButton(
                        onClick = {
                            isAnimating = true
                            coroutineScope.launch {
                                animateOffsetX(1000f)
                                SwipeViewModel.likeUser(users[0].id)
                                previousUser.add(users.removeFirst())
                                swipeCount++
                                offsetX = 0f
                                currentImageIndex = 0 // Reset image index for new user
                                isAnimating = false
                                showFeedbackEffect("like")
                            }
                        },
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.Transparent)
                            .border(2.dp, Color(0xFF199A6A), CircleShape)
                            .padding(8.dp)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(R.drawable.green_heart),
                            contentDescription = "Like",
                            //tint = Color(0xFF199A6A),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // Feedback Effects Overlay
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                // Like feedback effect
                Icon(
                    imageVector = Icons.Rounded.Favorite,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier
                        .size(200.dp)
                        .scale(likeIconScale)
                        .alpha(likeIconAlpha)
                        .graphicsLayer {
                            transformOrigin = TransformOrigin(0.5f, 0.5f)
                        }
                )

                // Dislike feedback effect
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null,
                    tint = Color(0xFFFF5252),
                    modifier = Modifier
                        .size(200.dp)
                        .scale(dislikeIconScale)
                        .alpha(dislikeIconAlpha)
                        .graphicsLayer {
                            transformOrigin = TransformOrigin(0.5f, 0.5f)
                        }
                )

                // Undo feedback effect
                Icon(
                    imageVector = Icons.Rounded.Refresh,
                    contentDescription = null,
                    tint = Color(0xFFFFEB3B),
                    modifier = Modifier
                        .size(160.dp)
                        .scale(undoIconScale)
                        .alpha(undoIconAlpha)
                        .graphicsLayer {
                            transformOrigin = TransformOrigin(0.5f, 0.5f)
                        }
                )
            }
        } else {
            // No more profiles
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No more profiles left!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Check back later for new matches",
                    fontSize = 16.sp,
                    color = PrimaryColor,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Keep the logo at the top
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, end = 32.dp)
                .height(60.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                LogoTinder(
                    logoSize = 24.dp,
                    textSize = 30.sp,
                    colorLogo = PrimaryColor,
                    color = PrimaryColor
                )
            }

            Box(
                modifier = Modifier.clickable {
                    navController.navigate(com.hd.minitinder.navigation.NavigationItem.History.route)
                }
            ) {
                Image(
                    painter = rememberAsyncImagePainter(R.drawable.bell),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(bottom = 16.dp),
                    colorFilter = ColorFilter.tint(Color.Gray) // Áp dụng màu xám
                )
            }
        }

    }
}

@Composable
fun TagChip(tag: String) {
    Surface(
        shape = RoundedCornerShape(50),
        color = Color.White.copy(alpha = 0.2f),
        contentColor = Color.White
    ) {
        Text(
            text = tag,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 14.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}