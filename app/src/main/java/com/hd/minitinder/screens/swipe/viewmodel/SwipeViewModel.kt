package com.hd.minitinder.screens.swipe.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.hd.minitinder.screens.chatList.repository.ChatListRepository
import com.hd.minitinder.screens.swipe.view.UserProfile

data class UserProfile(
    val name: String,
    val age: Int,
    val imageUrls: List<String>, // Changed to list of image URLs
    val tags: List<String>,
    val address: String,
    val occupation: String,
    val bio: String
)
class SwipeViewModel: ViewModel() {

    val users = mutableStateListOf(
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