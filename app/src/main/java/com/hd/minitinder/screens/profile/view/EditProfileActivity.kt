package com.hd.minitinder.screens.profile.view

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hd.minitinder.navigation.NavigationItem
import com.hd.minitinder.screens.profile.viewmodel.ProfileViewModel

import com.hd.minitinder.ui.theme.*

data class InfoButton(
    val label: String,
    val rightLabel: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
)

@Composable
fun InfoSection(
    title: String,
    infoButtons: List<InfoButton>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            // Dấu chấm
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(5.dp)
                    .background(SecondaryColor, shape = CircleShape)
            )

            // Tiêu đề
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp),
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Phần chọn thêm thông tin
        infoButtons.forEach { infoButton ->
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 0.4.dp,
                color = Color.Gray
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(Color.White)
                    .clickable { infoButton.onClick() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.padding(start = 12.dp))
                    Icon(
                        imageVector = infoButton.icon,
                        contentDescription = "${infoButton.label} icon"
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 4.dp),
                        text = infoButton.label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                }

                // Dấu mũi tên và thông tin
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (infoButton.rightLabel.isEmpty() or infoButton.rightLabel.isBlank()) "Add" else infoButton.rightLabel,
                        color = Color.DarkGray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Arrow Icon",
                        modifier = Modifier.padding(end = 8.dp),
                        tint = Color.Gray
                    )
                }
            }
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 0.4.dp,
            color = Color.Gray
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController
) {
    // Lấy state của user từ ViewModel
    val viewModel: ProfileViewModel = viewModel()
    val userState by viewModel.userState.collectAsStateWithLifecycle()

    var biographyText by remember(userState.bio) { mutableStateOf(userState.bio ?: "") }

    // Quản lý hiển thị Bottom Sheet
    var showSheet by remember { mutableStateOf(false) }
    var selectedField by remember { mutableStateOf("") }

    if (showSheet) {
        when (selectedField) {
            "Name", "Hometown", "Job", "Phone number" -> NameBottomSheet(
                fieldName = selectedField,
                onDismiss = {showSheet = false}
            )
            "Gender" -> GenderBottomSheet(
                onDismiss = {showSheet = false}
            )
            "Dob" -> DobBottomSheet (
                onDismiss = {showSheet = false}
            )
            "Height", "Weight" -> HWBottomSheet (
                fieldName = selectedField,
                onDismiss = {showSheet = false}
            )
            "Interest" -> InterestBottomSheet (
                onDismiss = {showSheet = false}
            )
        }
    }


    // Layout tổng
    Surface(modifier = Modifier.fillMaxSize()) {
        Column {
            Spacer(modifier = Modifier.height(24.dp))

            // Tiêu đê edit profile và nút done
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Edit profile",
                    color = Color.Black,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.Center)
                )

                TextButton(
                    onClick = {
                        viewModel.onBioChange(biographyText)
                        viewModel.saveUserToFirestore()
                        navController.navigate(NavigationItem.Profile.route) },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = "Done",
                        color = Color.Blue,
                    )
                }
            }

            // Nút edit và preview
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(onClick = {}) {
                        Text(
                            text = "Edit",
                            color = PrimaryColor,
                            fontSize = 18.sp
                        )
                    }
                }

//                HorizontalDivider(
//                    modifier = Modifier
//                        .height(16.dp)
//                        .width(2.dp),
//                    color = Color.Black,
//                )

                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(onClick = {}) {
                        Text(
                            text = "Preview",
                            color = Color.Gray,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            // Nội dung chính
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(LightGray)
            ) {
                // Tiểu sử
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        // Dấu chấm
                        Box(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(5.dp)
                                .background(SecondaryColor, shape = CircleShape)
                        )

                        // Tiêu đề
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 4.dp),
                            text = "Bio",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    TextField(
                        value = biographyText,
                        onValueChange = { biographyText = it },
                        placeholder = { Text(text = "Something about you...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .heightIn(min = 150.dp),
//                        colors = TextFieldDefaults.textFieldColors(
//                            backgroundColor = Color.White,
//                            focusedIndicatorColor = Color.Transparent,   // Ẩn đường viền khi focus (nếu cần)
//                            unfocusedIndicatorColor = Color.Transparent    // Ẩn đường viền khi không focus (nếu cần)
//                        ),
                        singleLine = false,
                        maxLines = 5,
                    )
                }

                // Thông tin cơ bản
                InfoSection(
                    title = "Basic Information", infoButtons = listOf(
                        InfoButton(label = "Name", icon = Icons.Filled.Person, rightLabel = userState.name ?: "") {
                            selectedField = "Name"
                            showSheet = true },
                        InfoButton(label = "Gender", icon = Icons.Filled.CheckCircle, rightLabel = userState.gender ?: "") {
                            selectedField = "Gender"
                            showSheet = true },
                        InfoButton(label = "Dob", icon = Icons.Filled.DateRange, rightLabel = userState.dob ?: "") {
                            selectedField = "Dob"
                            showSheet = true },
                    )
                )

                // Thông tin thêm
                InfoSection(
                    title = "More information", infoButtons = listOf(
                        InfoButton(label = "Hometown", icon = Icons.Filled.Home, rightLabel = userState.hometown ?: "") {
                            selectedField = "Hometown"
                            showSheet = true },
                        InfoButton(label = "Job", icon = Icons.Filled.Build, rightLabel = userState.job ?: "") {
                            selectedField = "Job"
                            showSheet = true },
                        InfoButton(label = "Height", icon = Icons.Filled.Info, rightLabel = if (userState.height?.toInt() == 0 || userState.height.toString().isBlank()) "" else userState.height.toString()) {
                            selectedField = "Height"
                            showSheet = true
                        },
                        InfoButton(label = "Weight", icon = Icons.Filled.Info, rightLabel = if (userState.weight?.toInt() == 0 || userState.weight.toString().isBlank()) "" else userState.weight.toString()) {
                            selectedField = "Weight"
                            showSheet = true
                        },
                    )
                )

                // Thông tin liên lạc
                InfoSection(
                    title = "Contact information", infoButtons = listOf(
                        InfoButton(label = "Phone Number", icon = Icons.Filled.Phone, rightLabel = userState.phoneNumber ?: "") {
                            selectedField = "Phone number"
                            showSheet = true },
                    )
                )

                // SỞ thích
                InfoSection(
                    title = "Interests",
                    infoButtons = listOf(
                        InfoButton(label = "Interests", icon = Icons.Filled.Favorite, rightLabel = userState.interests.joinToString(", ")) {
//                            navController.navigate(NavigationItem.EditInterest.route)
                            selectedField = "Interest"
                            showSheet = true },
                    ),
                )


            }
        }
    }
}

