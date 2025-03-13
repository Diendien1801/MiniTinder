package com.hd.minitinder.screens.tinderGold.view

import TinderGoldViewModel
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.auth.User
import com.hd.minitinder.common.fragments.logo.LogoTinder



import android.util.Log
import androidx.compose.ui.draw.blur
import com.hd.minitinder.common.fragments.button.ButtonGradient

@Composable
fun TinderGoldActivity(navController: NavController? = null) {
    val viewModel: TinderGoldViewModel = viewModel()
    val likedUsers by viewModel.likedUsers.collectAsState()
    val isPremium by viewModel.isPremium.collectAsState()

    // Debugging: In ra danh sách người dùng đã thích
    Log.d("TinderGoldActivity", "Liked Users Size: ${likedUsers.size}")

    Box {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    //.background(MaterialTheme.colorScheme.background)
                    .background(Color.Black)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Tinder logo
                LogoTinder(
                    logoSize = 24.dp,
                    textSize = 30.sp,
                    colorLogo = MaterialTheme.colorScheme.primary,
                    color = MaterialTheme.colorScheme.primary
                )

                // Title
                Text(
                    text = "Upgrade to Gold to see people \n who already liked you",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    // MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )

                // Sort Options
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(option.size) { index ->
                        sortOption(option[index])
                    }
                }

                // Hiển thị danh sách người đã thích
                if (likedUsers.isNotEmpty()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(likedUsers.size) { index ->
                            Log.d("TinderGoldActivity", "Rendering user: ${likedUsers[index].name}")
                            personLikeYouItem(
                                imageUrl = likedUsers[index].imageUrls.firstOrNull() ?: "",
                                idUser = likedUsers[index].name,
                                isPremium = isPremium ?: false,
                            )
                        }
                    }
                } else {
                    Text(
                        text = "No one has liked you yet!",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        if(isPremium == false){
            Box (
                modifier = Modifier
                    .padding(horizontal = 16.dp,36.dp)
                    .align(Alignment.BottomCenter)
            ){
                ButtonGradient(
                    buttonText = "See who Likes you",
                    onClick = {
                        navController?.navigate(com.hd.minitinder.navigation.NavigationItem.PaymentOption.route)
                    }
                )
            }
        }


    }
}




@Composable
fun personLikeYouItem(imageUrl: String, idUser: String, isPremium: Boolean) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(150.dp)
            .height(220.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Ảnh đại diện
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Nếu không phải Premium, thêm hiệu ứng kính mờ
            if (!isPremium) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)) // Overlay tối
                        .blur(12.dp) // Làm mờ
                )
            }
        }
    }
}




// Composable hiển thị option sắp xếp
@Composable
fun sortOption(text: String) {
    Surface(
        modifier = Modifier.padding(8.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
    ) {

            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(8.dp)
            )

    }
}

// 🔥 Thêm Preview để xem trước giao diện
@Preview(showBackground = true)
@Composable
fun PreviewTinderGoldActivity() {
    TinderGoldActivity()
}

val option = listOf("Nearby", "Top Rated", "Newest", "Oldest","Most Liked")
