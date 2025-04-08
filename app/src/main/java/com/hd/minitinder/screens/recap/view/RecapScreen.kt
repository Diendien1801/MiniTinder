package com.hd.minitinder.screens.recap.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hd.minitinder.screens.recap.model.RecapData
import com.hd.minitinder.screens.recap.model.RecapPeriod
import com.hd.minitinder.screens.recap.model.TopConversation
import com.hd.minitinder.screens.recap.model.UserInsight
import com.hd.minitinder.screens.recap.viewmodel.RecapViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Arrangement
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel


// Direct color definitions instead of using theme colors
private val PrimaryColor = Color(0xFFFF4458) // Tinder Red
private val SecondaryColor = Color(0xFFFF8C94) // Light Tinder Red
private val LightRed = Color(0xFFFF6B6B)
private val PurpleColor = Color(0xFF9C27B0)
private val BlueColor = Color(0xFF2196F3)
private val GreenColor = Color(0xFF8BC34A)
private val OrangeColor = Color(0xFFFF9800)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecapScreen(
    onBackPressed: () -> Unit,
    viewModel: RecapViewModel = viewModel()
) {
    val context = LocalContext.current

    val recapData by viewModel.recapData.collectAsState()
    val selectedPeriod by viewModel.selectedPeriod.collectAsState()
    val userInsights by viewModel.userInsights.collectAsState()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var showExitFeedback by remember { mutableStateOf(false) }
    var exitFeedbackMessage by remember { mutableStateOf("") }

    var visibleCards by remember { mutableStateOf(0) }
    val totalCards = 8 // Increased for new cards
    LaunchedEffect(Unit) {
        viewModel.updateUsageMinutes(context)
    }
    LaunchedEffect(key1 = true) {
        // Animate cards appearing one by one
        repeat(totalCards) { index ->
            delay(300)
            visibleCards = index + 1
        }
    }

    val onExitRecap: () -> Unit = {
        // Generate exit message based on user stats
        exitFeedbackMessage = when {
            recapData.matches > 20 -> "Impressive! You're a match magnet. Keep the momentum going!"
            recapData.messagesSent > 100 -> "Great conversations lead to meaningful connections. Keep chatting!"
            recapData.rightSwipes > recapData.leftSwipes -> "Your positivity shows! Keep being open to new connections."
            else -> "Every swipe is a step towards finding someone special. Keep going!"
        }

        showExitFeedback = true
        // Hide feedback after a delay and then navigate back
        coroutineScope.launch {
            delay(2500)
            showExitFeedback = false
            delay(300)
            onBackPressed()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Your Recap") },
                    navigationIcon = {
                        IconButton(onClick = onExitRecap) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Share functionality */ }) {
                            Icon(Icons.Default.Share, contentDescription = "Share")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = PrimaryColor,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Your ${selectedPeriod.name} Recap",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryColor,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Period selector
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    RecapPeriod.values().forEach { period ->
                        PeriodTab(
                            period = period,
                            isSelected = period == selectedPeriod,
                            onClick = {
                                viewModel.selectPeriod(context, period)
                                coroutineScope.launch {
                                    visibleCards = 0
                                    delay(200)
                                    visibleCards = totalCards
                                }
                            }
                        )
                    }
                }

                // User Insight Card - New!
                AnimatedVisibility(
                    visible = visibleCards >= 1,
                    enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { it / 2 }
                ) {
                    UserInsightCard(userInsights)
                }

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(
                    visible = visibleCards >= 2,
                    enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { it / 2 }
                ) {
                    StatisticCard(
                        title = "Swipes",
                        value = "${recapData.totalSwipes}",
                        description = "${recapData.rightSwipes} right · ${recapData.leftSwipes} left",
                        color = LightRed
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(
                    visible = visibleCards >= 3,
                    enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { it / 2 }
                ) {
                    StatisticCard(
                        title = "Matches",
                        value = "${recapData.matches}",
                        description = "That's ${recapData.matchRate}% of your right swipes!",
                        color = PrimaryColor
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(
                    visible = visibleCards >= 4,
                    enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { it / 2 }
                ) {
                    StatisticCard(
                        title = "Messages",
                        value = "${recapData.messagesSent}",
                        description = "Average ${recapData.messagesPerMatch} per match",
                        color = SecondaryColor
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(
                    visible = visibleCards >= 5,
                    enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { it / 2 }
                ) {
                    EngagementScoreCard(recapData)
                }

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(
                    visible = visibleCards >= 6,
                    enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { it / 2 }
                ) {
                    TopConversationsCard(recapData.topConversations)
                }

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(
                    visible = visibleCards >= 7,
                    enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { it / 2 }
                ) {
                    StatisticCard(
                        title = "Daily Activity",
                        value = "${recapData.averageActivityMinutes} min",
                        description = "per day",
                        color = GreenColor
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(
                    visible = visibleCards >= 8,
                    enter = fadeIn(tween(500)) + slideInVertically(tween(500)) { it / 2 }
                ) {
                    CommonInterestsCard(recapData.commonInterests)
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        // Exit Feedback Overlay
        AnimatedVisibility(
            visible = showExitFeedback,
            enter = fadeIn(tween(300)),
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.7f)),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .padding(32.dp)
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Thanks for reviewing your stats!",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryColor,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = exitFeedbackMessage,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserInsightCard(insights: List<UserInsight>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your Dating Style",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = OrangeColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            insights.forEach { insight ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = insight.icon,
                        contentDescription = insight.title,
                        tint = OrangeColor,
                        modifier = Modifier.padding(end = 12.dp)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = insight.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = insight.description,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                if (insight != insights.last()) {
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}

@Composable
fun EngagementScoreCard(recapData: RecapData) {
    val engagementScore = calculateEngagementScore(recapData)
    val engagementLevel = when {
        engagementScore >= 85 -> "Excellent"
        engagementScore >= 70 -> "Very Good"
        engagementScore >= 50 -> "Good"
        engagementScore >= 30 -> "Average"
        else -> "Getting Started"
    }

    val tips = when {
        engagementScore >= 95 -> "You're a top-tier engaged! Your connections are thriving."
        engagementScore >= 85 -> "You're doing everything right! Keep up the great connections."
        engagementScore >= 75 -> "You're very engaged, but try varying your conversation starters."
        engagementScore >= 65 -> "You’re doing well! Consider following up more to build stronger bonds."
        engagementScore >= 55 -> "Try sending more follow-up messages to turn matches into dates."
        engagementScore >= 45 -> "Your engagement is decent, but replying faster could help!"
        engagementScore >= 35 -> "Be more selective with right swipes to increase your match rate."
        engagementScore >= 25 -> "Send more personalized opening messages to get better responses."
        engagementScore >= 15 -> "Improve your profile bio and initiate more conversations."
        else -> "Add more photos and details to your profile to get more matches."
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Engagement Score",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = BlueColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "$engagementScore",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = BlueColor
            )

            Text(
                text = engagementLevel,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = BlueColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            Divider(color = Color.LightGray)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "💡 Tip",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = OrangeColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = tips,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun PeriodTab(
    period: RecapPeriod,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) PrimaryColor else Color.LightGray.copy(alpha = 0.3f))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = period.name,
            color = if (isSelected) Color.White else Color.Black,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun StatisticCard(
    title: String,
    value: String,
    description: String,
    color: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = color
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun TopConversationsCard(topConversations: List<TopConversation>) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Top Conversations",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = PurpleColor,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            topConversations.forEachIndexed { index, conversation ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${index + 1}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PurpleColor,
                        modifier = Modifier.padding(end = 16.dp)
                    )

                    Column {
                        Text(
                            text = conversation.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "${conversation.messageCount} messages",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                if (index < topConversations.size - 1) {
                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = Color.LightGray
                    )
                }
            }
        }
    }
}

@Composable
fun CommonInterestsCard(commonInterests: Map<String, Int>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Common Interests",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = BlueColor,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Use FlowRow with correct parameters
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                commonInterests.entries
                    .sortedByDescending { it.value }
                    .forEach { (interest, count) ->
                        InterestChip(interest = interest, count = count)
                    }
            }
        }
    }
}

@Composable
fun InterestChip(interest: String, count: Int) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(BlueColor.copy(alpha = 0.1f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = "$interest ($count)",
            fontSize = 14.sp,
            color = BlueColor
        )
    }
}

// Helper function to calculate engagement score
private fun calculateEngagementScore(recapData: RecapData): Int {
    // Create a weighted score (0-100) based on various metrics
    val matchRate = recapData.matchRate
    val messageRate = (recapData.messagesSent.toFloat() / recapData.matches.toFloat()) * 10
    val activityScore = recapData.averageActivityMinutes.toFloat() * 2

    // Calculate base score
    var score = (matchRate * 0.4) + (messageRate * 0.4) + (activityScore * 0.2)

    // Cap at 100
    return minOf(score.toInt(), 100)
}