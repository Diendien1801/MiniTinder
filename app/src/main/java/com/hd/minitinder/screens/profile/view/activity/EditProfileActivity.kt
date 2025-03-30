package com.hd.minitinder.screens.profile.view.activity

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hd.minitinder.navigation.NavigationItem
import com.hd.minitinder.ui.theme.*
import com.hd.minitinder.screens.profile.view.ui.theme.LightGray
import com.hd.minitinder.screens.profile.view.ui.theme.DarkCharcoal
import com.hd.minitinder.screens.profile.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
) {
    val viewModel: ProfileViewModel = viewModel()

    var selectedItem by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            Column {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .background(DarkCharcoal)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkCharcoal)
                ) {
                    Text(
                        text = "Edit profile",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    TextButton(
                        onClick = {
                            viewModel.saveUserToFirestore()
                            navController.navigate(NavigationItem.Profile.route)
                        },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 8.dp)
                    ) {
                        Text(
                            text = "Done",
                            color = Color.Blue
                        )
                    }
                }

                // Nút Edit và Preview
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkCharcoal),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        TextButton(
                            onClick = {
                                selectedItem = 0
                            },
                            enabled = (selectedItem != 0)
                        ) {
                            Text(
                                text = "Edit",
                                color = if ((selectedItem == 0)) PrimaryColor else LightGray,
                                fontSize = 18.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        TextButton(
                            onClick = {
                                selectedItem = 1
                            },
                            enabled = (selectedItem != 1)
                        ) {
                            Text(
                                text = "Preview",
                                color = if (selectedItem == 1) PrimaryColor else LightGray,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedItem) {
                0 -> { EditFieldScreen(viewModel) }
                1 -> { PreviewActivity(viewModel) }
                else -> { EditFieldScreen(viewModel) }
            }
        }
    }
}