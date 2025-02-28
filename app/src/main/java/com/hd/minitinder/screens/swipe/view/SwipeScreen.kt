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
import kotlin.math.roundToInt


data class UserProfile(
    val name: String,
    val age: Int,
    val imageUrl: String,
    val address: String,
    val occupation: String,
    val bio: String
)

@SuppressLint("NewApi")
@Composable
fun SwipeScreen() {
    val density = LocalDensity.current
    val users = remember {
        mutableStateListOf(
            UserProfile(
                name = "Alice",
                age = 24,
                imageUrl = "https://randomuser.me/api/portraits/women/1.jpg",
                address = "1234 Elm Street, Springfield",
                occupation = "Software Developer",
                bio = "Passionate about coding and tech innovations. Alice has been working in the tech industry for over 5 years and enjoys experimenting with new technologies like AI and machine learning. In her free time, she loves to contribute to open-source projects and stay up-to-date with the latest developments in the world of programming."
            ),
            UserProfile(
                name = "Bob",
                age = 27,
                imageUrl = "https://randomuser.me/api/portraits/men/2.jpg",
                address = "5678 Oak Avenue, Seattle",
                occupation = "Graphic Designer",
                bio = "Loves creating beautiful visual designs. Bob is passionate about transforming concepts into compelling visuals that resonate with audiences. With over 6 years of experience in graphic design, he has worked on various branding projects, advertising campaigns, and digital art. His creative journey is driven by his desire to merge aesthetics with functionality in every project."
            ),
            UserProfile(
                name = "Charlie",
                age = 22,
                imageUrl = "https://randomuser.me/api/portraits/women/3.jpg",
                address = "9102 Pine Road, Miami",
                occupation = "Marketing Specialist",
                bio = "Creative thinker, always striving to bring fresh ideas. Charlie has been working in marketing for the past 3 years, specializing in digital marketing strategies. She thrives on finding innovative ways to engage with target audiences and loves analyzing data to measure the success of campaigns. When she's not brainstorming new marketing ideas, she enjoys exploring the latest trends in social media and content creation."
            ),
            UserProfile(
                name = "David",
                age = 29,
                imageUrl = "https://randomuser.me/api/portraits/men/4.jpg",
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
            kotlinx.coroutines.delay(16)
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
                    .fillMaxWidth()
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
                                        isAnimating = false
                                    }
                                } else if (offsetX > thresholdDp) {
                                    isAnimating = true
                                    coroutineScope.launch {
                                        animateOffsetX(1000f)
                                        previousUser.add(users.removeFirst())
                                        swipeCount++
                                        offsetX = 0f
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
                    Image(
                        painter = rememberAsyncImagePainter(user.imageUrl),
                        contentDescription = user.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
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

                        Text(
                            text = "Address: ${user.address}",
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Text(
                            text = "Occupation: ${user.occupation}",
                            fontSize = 16.sp,
                            color = Color.White,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Text(
                            text = "Bio: ${user.bio}",
                            fontSize = 14.sp,
                            color = Color.White,
                            modifier = Modifier.padding(top = 4.dp)
                        )
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