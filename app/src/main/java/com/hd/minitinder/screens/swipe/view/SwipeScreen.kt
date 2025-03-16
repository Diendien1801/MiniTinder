package com.hd.minitinder.screens.swipe.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.draw.clip
import kotlinx.coroutines.delay
import kotlin.math.roundToInt


data class UserProfile(
    val name: String,
    val age: Int,
    val imageUrls: List<String>, // Changed to list of image URLs
    val tags: List<String>,
    val address: String,
    val occupation: String,
    val bio: String
)

@OptIn(ExperimentalLayoutApi::class)
@SuppressLint("NewApi")
@Composable
fun SwipeScreen(navController: NavController) {
    val density = LocalDensity.current
    val users = remember {
        mutableStateListOf(
            UserProfile(
                name = "Alice",
                age = 24,
                imageUrls = listOf(
                    "https://randomuser.me/api/portraits/women/1.jpg",
                    "https://randomuser.me/api/portraits/women/11.jpg",
                    "https://randomuser.me/api/portraits/women/21.jpg"
                ),
                tags = listOf("badminton", "coffee", "travel", "photography", "cooking", "hiking"),
                address = "1234 Elm Street, Springfield",
                occupation = "Software Developer",
                bio = "Passionate about coding and tech innovations. Alice has been working in the tech industry for over 5 years and enjoys experimenting with new technologies like AI and machine learning. In her free time, she loves to contribute to open-source projects and stay up-to-date with the latest developments in the world of programming."
            ),
            UserProfile(
                name = "Bob",
                age = 27,
                imageUrls = listOf(
                    "https://randomuser.me/api/portraits/men/2.jpg",
                    "https://randomuser.me/api/portraits/men/12.jpg",
                    "https://randomuser.me/api/portraits/men/22.jpg"
                ),
                tags = listOf("badminton", "coffee", "travel", "photography", "cooking", "hiking"),
                address = "5678 Oak Avenue, Seattle",
                occupation = "Graphic Designer",
                bio = "Loves creating beautiful visual designs. Bob is passionate about transforming concepts into compelling visuals that resonate with audiences. With over 6 years of experience in graphic design, he has worked on various branding projects, advertising campaigns, and digital art. His creative journey is driven by his desire to merge aesthetics with functionality in every project."
            ),
            UserProfile(
                name = "Charlie",
                age = 22,
                imageUrls = listOf(
                    "https://randomuser.me/api/portraits/women/3.jpg",
                    "https://randomuser.me/api/portraits/women/13.jpg",
                    "https://randomuser.me/api/portraits/women/23.jpg"
                ),
                tags = listOf("badminton", "coffee", "travel", "photography", "cooking", "hiking"),
                address = "9102 Pine Road, Miami",
                occupation = "Marketing Specialist",
                bio = "Creative thinker, always striving to bring fresh ideas. Charlie has been working in marketing for the past 3 years, specializing in digital marketing strategies. She thrives on finding innovative ways to engage with target audiences and loves analyzing data to measure the success of campaigns. When she's not brainstorming new marketing ideas, she enjoys exploring the latest trends in social media and content creation."
            ),
            UserProfile(
                name = "David",
                age = 29,
                imageUrls = listOf(
                    "https://randomuser.me/api/portraits/men/4.jpg",
                    "https://randomuser.me/api/portraits/men/14.jpg",
                    "https://randomuser.me/api/portraits/men/24.jpg"
                ),
                tags = listOf("badminton", "coffee", "travel", "photography", "cooking", "hiking"),
                address = "4321 Maple Lane, San Francisco",
                occupation = "Project Manager",
                bio = "Focused on delivering projects on time with excellent team coordination. David has managed diverse projects across multiple industries, ensuring they are completed successfully while maintaining high standards. He believes in effective communication, teamwork, and problem-solving to tackle challenges. With a background in both engineering and management, he enjoys bridging the gap between technical and non-technical teams to drive results."
            )
        )
    }

    var swipeCount by remember { mutableIntStateOf(0) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var isAnimating by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val cw = 300f
    val thresholdDp = cw / 2
    val previousUser = remember { mutableStateListOf<UserProfile?>() }

    // Current image index for each user
    var currentImageIndex by remember { mutableIntStateOf(0) }

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

    Box(modifier = Modifier.fillMaxSize()) {
        if (users.isNotEmpty()) {
            val user = users.first()
            Card(
                modifier = Modifier
                    .width(LocalConfiguration.current.screenWidthDp.dp * 0.98f)
                    .height(LocalConfiguration.current.screenHeightDp.dp * 0.8f)
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
                                        previousUser.add(users.removeFirst())
                                        swipeCount++
                                        offsetX = 0f
                                        currentImageIndex = 0 // Reset image index for new user
                                        isAnimating = false
                                    }
                                } else if (offsetX > thresholdDp) {
                                    isAnimating = true
                                    coroutineScope.launch {
                                        animateOffsetX(1000f)
                                        previousUser.add(users.removeFirst())
                                        swipeCount++
                                        offsetX = 0f
                                        currentImageIndex = 0 // Reset image index for new user
                                        isAnimating = false
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
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp))
                    .align(Alignment.Center),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box {
                    // Display current image based on currentImageIndex
                    Image(
                        painter = rememberAsyncImagePainter(user.imageUrls[currentImageIndex]),
                        contentDescription = "${user.name} - Photo ${currentImageIndex + 1}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Row(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 16.dp, start = 8.dp, end = 8.dp)
                            .fillMaxWidth(0.9f) // Đặt chiều rộng khoảng 80% màn hình
                            .height(6.dp) // Giảm chiều cao của các thanh
                            .clip(RoundedCornerShape(3.dp))
                        ,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(3) { index ->
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f)
                                    .background(
                                        if (index == currentImageIndex) Color.White else Color.Gray
                                    )
                                    .clip(RoundedCornerShape(3.dp)) // Bo góc các thanh
                            )
                        }
                    }



                    // Image navigation controls
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
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
                            modifier = Modifier.size(40.dp),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.White.copy(alpha = 0.7f)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowLeft,
                                contentDescription = "Previous Image",
                                tint = Color.Black
                            )
                        }


                        // Next image button
                        IconButton(
                            onClick = {
                                if (currentImageIndex < user.imageUrls.size - 1) {
                                    currentImageIndex++
                                }
                            },
                            modifier = Modifier.size(40.dp),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = Color.White.copy(alpha = 0.7f)
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowRight,
                                contentDescription = "Next Image",
                                tint = Color.Black
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "${user.name}, ${user.age}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Tags horizontal scrollable row
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
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        isAnimating = true
                        coroutineScope.launch {
                            animateOffsetX(1000f)
                            previousUser.add(users.removeFirst())
                            swipeCount++
                            offsetX = 0f
                            currentImageIndex = 0 // Reset image index for new user
                            isAnimating = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                ) {
                    Text("Like")
                }

                Button(
                    onClick = {
                        isAnimating = true
                        coroutineScope.launch {
                            animateOffsetX(-1000f)
                            previousUser.add(users.removeFirst())
                            swipeCount++
                            offsetX = 0f
                            currentImageIndex = 0 // Reset image index for new user
                            isAnimating = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("No Like")
                }

                Button(
                    onClick = {
                        if (previousUser.isNotEmpty()) {
                            previousUser.removeLast()?.let { users.add(0, it) }
                            currentImageIndex = 0 // Reset image index for restored user
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Undo")
                }
            }
        } else {
            Text(
                text = "No more profiles left!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center),
                color = Color.Black
            )
        }
        Text(
            text = "MiniTinder",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
        )
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
            fontSize = 14.sp
        )
    }
}