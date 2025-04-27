package com.hd.minitinder.screens.tinderGold.view

import TinderGoldViewModel
import android.graphics.Bitmap
import android.graphics.Canvas
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
import android.widget.ImageView
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.hd.minitinder.R
import com.hd.minitinder.common.fragments.button.ButtonGradient
import com.hd.minitinder.navigation.NavigationItem
import com.hd.minitinder.ui.theme.PrimaryColor

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
                    .background(MaterialTheme.colorScheme.background)

                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Tinder logo
                LogoTinder(
                    logoSize = 24.dp,
                    textSize = 30.sp,
                    colorLogo = PrimaryColor,
                    color = PrimaryColor
                )

                // Title
                Text(
                    text = stringResource(R.string.upgrade_to_gold_to_see_people_who_already_liked_you),
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
                        SortOption(option[index])
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
                                navController = navController,
                                imageUrl = likedUsers[index].imageUrls.firstOrNull() ?: "",
                                idUser = likedUsers[index].id,
                                isPremium = isPremium ?: false,
                            )
                        }
                    }
                } else {
                    Text(
                        text = stringResource(R.string.no_one_has_liked_you_yet),
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
                    .padding(horizontal = 16.dp, 36.dp)
                    .align(Alignment.BottomCenter)
            ){
                ButtonGradient(
                    gradientColors = listOf(Color(0xFFE6AF16), Color(0xFFFFE8A5)),
                    buttonText = stringResource(R.string.see_who_likes_you),
                    onClick = {
                        navController?.navigate(com.hd.minitinder.navigation.NavigationItem.PaymentOption.route)
                    },
                    textColor = Color.Black

                )
            }
        }


    }
}




@Composable
fun personLikeYouItem(navController: NavController? = null, imageUrl: String, idUser: String, isPremium: Boolean) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .width(150.dp)
            .height(220.dp)
            .clickable(onClick = {
                if (isPremium){
                    navController?.navigate(NavigationItem.ViewProfile.createRoute(idUser))
                }
                else {
                    navController?.navigate(NavigationItem.PaymentOption.route)
                }

            })
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
                        // Màu kính mờ
                )
                Image(
                    painter = painterResource(id = R.drawable.mystery), // Ảnh PNG có hiệu ứng kính mờ
                    contentDescription = "Blur Overlay",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.8f)
                )
            }
        }
    }
}




@Composable
fun SortOption(text: String, icon: Int? = R.drawable.adjust) {
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .widthIn(min = 60.dp),
        shape = RoundedCornerShape(20.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, PrimaryColor)
    ) {
        if (text.isEmpty()) { // Kiểm tra đúng chuẩn
            icon?.let { // Đảm bảo icon không null
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Sort Icon",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(8.dp),
                    colorFilter = ColorFilter.tint(PrimaryColor)
                )
            }
        } else {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryColor,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}


// 🔥 Thêm Preview để xem trước giao diện
@Preview(showBackground = true)
@Composable
fun PreviewTinderGoldActivity() {
    TinderGoldActivity()
}

val option = listOf("Nearby", "Top Rated", "Newest", "Oldest","Most Liked")
