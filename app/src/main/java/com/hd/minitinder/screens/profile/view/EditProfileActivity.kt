package com.hd.minitinder.screens.profile.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.icons.filled.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.hd.minitinder.data.model.UserModel
import com.hd.minitinder.navigation.NavigationItem
import com.hd.minitinder.screens.profile.viewmodel.ProfileViewModel

import com.hd.minitinder.ui.theme.*

data class InfoButton(
    val label: String,
    val rightLabel: String,
    val onClick: () -> Unit
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
                    .clickable{ infoButton.onClick() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.padding(start = 12.dp))
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = "Icon"
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 4.dp),
                        text = "${infoButton.label}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                }

                // Dấu mũi tên và thông tin
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (infoButton.rightLabel.isEmpty()) "Add" else infoButton.rightLabel,
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
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    // Lấy state của user từ ViewModel
    val userState by viewModel.userState.collectAsStateWithLifecycle()

    // Biến tạm để hiển thị dialog
    var showEditDialog by remember { mutableStateOf(false) }
    var dialogField by remember { mutableStateOf("") }      // Tên trường đang edit
    var dialogValue by remember { mutableStateOf("") }      // Giá trị tạm cho dialog

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
                    fontSize = 22.sp,
                    modifier = Modifier.align(Alignment.Center)
                )

                TextButton(
                    onClick = { navController.navigate(NavigationItem.Profile.route) },
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
                    .background(LightGray)
            ) {
                // Tiểu sử
                var biographyText by remember { mutableStateOf("") }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Tiêu đề
                    Text(
                        text = "Bio",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Trường nhập liệu cho tiểu sử
                    OutlinedTextField(
                        value = biographyText,
                        onValueChange = { biographyText = it },
                        placeholder = { Text(text = "Nhập tiểu sử của bạn ở đây...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 150.dp),
                        singleLine = false,
                        maxLines = 5
                    )
                }

                // Thông tin cơ bản
                InfoSection(
                    title = "Basic Informations", infoButtons = listOf(
                        InfoButton(label = "Name", rightLabel = "") { /* Xử lý khi click nút Tên */ },
                        InfoButton(label = "Gender", rightLabel = "") { /* Xử lý khi click nút  */ },
                        InfoButton(label = "Dob", rightLabel = "") { /* Xử lý khi click nút Ngày sinh */ },
                ))

                // Thông tin thêm
                InfoSection(
                    title = "More informations", infoButtons = listOf(
                        InfoButton(label = "Hometown", rightLabel = "") { /* Xử lý khi click nút Tên */ },
                        InfoButton(label = "Job", rightLabel = "") { /* Xử lý khi click nút  */ },
                        InfoButton(label = "Height", rightLabel = "") { /* Xử lý khi click nút Ngày sinh */ },
                        InfoButton(label = "Weight", rightLabel = "") { /* Xử lý khi click nút Ngày sinh */ },
                    )
                )

                // Thông tin liên lạc
                InfoSection(
                    title = "Contact informations", infoButtons = listOf(
                        InfoButton(label = "Phone Number", rightLabel = "") { /* Xử lý khi click nút Tên */ },
                    )
                )


                // Ngôn ngữ
                InfoSection(
                    title = "Languages I know",
                    infoButtons = listOf(
                        InfoButton(label = "Languages", rightLabel = "") {},
                    ),
                )

                // SỞ thích
                InfoSection(
                    title = "Interests",
                    infoButtons = listOf(
                        InfoButton(label = "Interests", rightLabel = "") { navController.navigate(NavigationItem.EditInterest.route) },
                    ),
                )



            }
        }
    }
}


        // Dialog chỉnh sửa trường
//        if (showEditDialog) {
//            AlertDialog(
//                onDismissRequest = { showEditDialog = false },
//                title = { Text(text = "Edit $dialogField") },
//                text = {
//                    OutlinedTextField(
//                        value = dialogValue,
//                        onValueChange = { dialogValue = it },
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                },
//                confirmButton = {
//                    Button(
//                        onClick = {
//                            // Lưu giá trị vào ViewModel
//                            when (dialogField) {
//                                "Name" -> viewModel.onNameChange(dialogValue)
//                                "Gender" -> viewModel.onGenderChange(dialogValue)
//                                "Date of Birth" -> viewModel.onDobChange(dialogValue)
//                                "Hometown" -> viewModel.onHometownChange(dialogValue)
//                                "Job" -> viewModel.onJobChange(dialogValue)
//                                "Height" -> dialogValue.toDoubleOrNull()?.let {
//                                    viewModel.onHeightChange(it)
//                                }
//                                "Weight" -> dialogValue.toDoubleOrNull()?.let {
//                                    viewModel.onWeightChange(it)
//                                }
//                                "Phone Number" -> viewModel.onPhoneNumberChange(dialogValue)
//                                "Bio" -> viewModel.onBioChange(dialogValue)
//                            }
//                            showEditDialog = false
//                        }
//                    ) {
//                        Text(text = "OK")
//                    }
//                },
//                dismissButton = {
//                    Button(onClick = { showEditDialog = false }) {
//                        Text(text = "Cancel")
//                    }
//                }
//            )
//        }

///**
// * Data class để lưu thông tin mỗi dòng hiển thị
// */
//data class ProfileItemData(
//    val label: String,
//    val value: String,
//    val onClick: () -> Unit
//)
//
///**
// * Composable hiển thị một dòng (label - value - nút Add/Edit)
// */
//@Composable
//fun ProfileListItem(
//    label: String,
//    value: String,
//    onClick: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 4.dp)
//            .clickable { onClick() },
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(text = label, fontWeight = FontWeight.SemiBold)
//            Text(text = value, color = Color.Gray)
//        }
//    }
//}
