package com.hd.minitinder.screens.profile.view.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hd.minitinder.R
import com.hd.minitinder.screens.profile.view.bottomSheet.DobBottomSheet
import com.hd.minitinder.screens.profile.view.bottomSheet.GenderBottomSheet
import com.hd.minitinder.screens.profile.view.bottomSheet.HWBottomSheet
import com.hd.minitinder.screens.profile.view.bottomSheet.InterestBottomSheet
import com.hd.minitinder.screens.profile.view.bottomSheet.NameBottomSheet
import com.hd.minitinder.screens.profile.view.ui.theme.DarkCharcoal
import com.hd.minitinder.screens.profile.viewmodel.ProfileViewModel

import com.hd.minitinder.ui.theme.*
import com.hd.minitinder.screens.profile.view.ui.theme.LightGray

data class InfoButton(
    val label: String,
    val rightLabel: String,
    val icon: ImageVector,
    val onClick:  () -> Unit,
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
                text = title.uppercase(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
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
                    .background(DarkCharcoal)
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
                        contentDescription = "${infoButton.label} icon",
                        tint = LightGray,
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 4.dp),
                        text = infoButton.label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }

                // Dấu mũi tên và thông tin
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (infoButton.rightLabel.isEmpty() or infoButton.rightLabel.isBlank()) "Add" else infoButton.rightLabel,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Arrow Icon",
                        modifier = Modifier.padding(end = 8.dp),
                        tint = LightGray
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

@Composable
fun EditFieldScreen(
    viewModel: ProfileViewModel
) {
    val userState by viewModel.userState.collectAsStateWithLifecycle()

    var biographyText by remember { mutableStateOf("") }
    LaunchedEffect(userState.bio) {
        biographyText = userState.bio
    }

    var showSheet by remember { mutableStateOf(false) }
    var selectedField by remember { mutableStateOf("") }

    if (showSheet) {
        when (selectedField) {
            stringResource(R.string.name), stringResource(R.string.hometown), stringResource(R.string.job), stringResource(
                R.string.phone_number
            ) -> NameBottomSheet(
                fieldName = selectedField,
                onDismiss = { showSheet = false }
            )
            stringResource(R.string.gender) -> GenderBottomSheet(onDismiss = { showSheet = false })
            stringResource(R.string.dob) -> DobBottomSheet(onDismiss = { showSheet = false })
            stringResource(R.string.height), stringResource(R.string.weight) -> HWBottomSheet(
                fieldName = selectedField,
                onDismiss = { showSheet = false }
            )
            stringResource(R.string.interest) -> InterestBottomSheet(onDismiss = { showSheet = false })
        }
    }
    // Nội dung chính với nền tối
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.Black)
    ) {
        // Phần Tiểu sử
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Dấu chấm
                Box(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(5.dp)
                        .background(SecondaryColor, shape = CircleShape)
                )

                // Tiêu đề Bio
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp),
                    text = "BIO",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            TextField(
                value = biographyText,
                onValueChange = {newText ->
                    biographyText = newText
                    viewModel.onBioChange(newText) },
                placeholder = {
                    Text(text = stringResource(R.string.something_about_you), color = Color.LightGray)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1E1E1E))
                    .heightIn(min = 150.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedPlaceholderColor = Color.Gray,
                    focusedPlaceholderColor = Color.Gray,
                    unfocusedContainerColor = DarkCharcoal,
                    focusedContainerColor = DarkCharcoal,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Blue,
                ),
                singleLine = false,
                maxLines = 5,
            )
        }

        // Các phần thông tin khác (Basic Information, More information, Contact information, Interests)
        InfoSection(
            title = stringResource(R.string.basic_information),
            infoButtons = listOf(
                InfoButton(label = stringResource(R.string.name), icon = Icons.Default.Person, rightLabel = userState.name) {
                    selectedField = "Name"
                    showSheet = true
                },
                InfoButton(label = stringResource(R.string.gender), icon = Icons.Default.Wc, rightLabel = userState.gender) {
                    selectedField = "Gender"
                    showSheet = true
                },
                InfoButton(label = stringResource(R.string.dob), icon = Icons.Default.Cake, rightLabel = userState.dob) {
                    selectedField = "Dob"
                    showSheet = true
                },
            )
        )

        InfoSection(
            title = "More information",
            infoButtons = listOf(
                InfoButton(label = stringResource(R.string.hometown), icon = Icons.Default.Place, rightLabel = userState.hometown) {
                    selectedField = "Hometown"
                    showSheet = true
                },
                InfoButton(label = stringResource(R.string.job), icon = Icons.Default.Work, rightLabel = userState.job) {
                    selectedField = "Job"
                    showSheet = true
                },
                InfoButton(
                    label = stringResource(R.string.height),
                    icon = Icons.Default.Height,
                    rightLabel = if (userState.height.toInt() == 0 || userState.height.toString().isBlank()) "" else userState.height.toString()
                ) {
                    selectedField = "Height"
                    showSheet = true
                },
                InfoButton(
                    label = stringResource(R.string.weight),
                    icon = Icons.Default.MonitorWeight,
                    rightLabel = if (userState.weight.toInt() == 0 || userState.weight.toString().isBlank()) "" else userState.weight.toString()
                ) {
                    selectedField = "Weight"
                    showSheet = true
                },
            )
        )

        InfoSection(
            title = stringResource(R.string.contact_information),
            infoButtons = listOf(
                InfoButton(label = stringResource(R.string.phone_number), icon = Icons.Default.Phone, rightLabel = userState.phoneNumber) {
                    selectedField = "Phone number"
                    showSheet = true
                },
            )
        )

        InfoSection(
            title = stringResource(R.string.interests),
            infoButtons = listOf(
                InfoButton(label = stringResource(R.string.interests), icon = Icons.Default.Favorite, rightLabel = userState.interests.joinToString(", ")) {
                    selectedField = "Interest"
                    showSheet = true
                },
            )
        )
    }
}
