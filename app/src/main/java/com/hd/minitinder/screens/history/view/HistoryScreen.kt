package com.hd.minitinder.screens.history.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.hd.minitinder.common.fragments.logo.LogoTinder
import com.hd.minitinder.screens.history.viewmodel.HistoryViewModel
import com.hd.minitinder.ui.theme.PrimaryColor

@Composable
fun HistoryScreen(nav: NavController) {
    val viewModel: HistoryViewModel = HistoryViewModel()
    val users by viewModel.filteredUsers.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // Set toàn bộ màn hình về đen
    ) {
        Text(
            text = "Activity",
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 10.dp),
            textAlign = TextAlign.Center,
        )
        HistoryCategorySelector(viewModel)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black) // Box chứa danh sách cũng có nền đen
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .background(Color.Black) // LazyColumn có nền đen
            ) {
                items(users) { user ->
                    HistoryItem("https://scontent.fsgn8-4.fna.fbcdn.net/v/t39.30808-6/475977913_1362026488292271_813961950003817138_n.jpg?_nc_cat=103&ccb=1-7&_nc_sid=6ee11a&_nc_eui2=AeFWrdsEbXW_Cf8LVNL-Ut6QW7ZP8AZF96xbtk_wBkX3rLVLW2dS1JDll9tL1TNvrM1Y-g3Az1VW7Ha96KvDaiEt&_nc_ohc=sgY8gSdSIX8Q7kNvgGC9ePw&_nc_oc=AdjV4JxCDAmQbqA4prv_ucx5FtOFBUZdAQ0t4vizoE_A22u-FpnWQqgjOBK-TNA5Isb4jggjeDL72BGLAru66ClD&_nc_zt=23&_nc_ht=scontent.fsgn8-4.fna&_nc_gid=A0VWSyImDlHA7fqwydE9x00&oh=00_AYFEEPcVbWI3qSP-I8-4NmmD5MlQX4e-1LlGSpma1ndzUw&oe=67D76D79", user.name, "11/11/2025")
                }
            }
        }
    }
}
@Composable
fun HistoryCategorySelector(viewModel: HistoryViewModel) {
    val selectedCategory by viewModel.selectedCategory.collectAsState() // Lấy trạng thái đã chọn

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        val categories = viewModel.categories

        categories.forEach { category ->
            HistoryCategory(
                name = category,
                isSelected = category == selectedCategory,
                onClick = { viewModel.updateCategory(category) } // Gọi hàm cập nhật ViewModel
            )
        }
    }
}


@Composable
fun HistoryCategory(name: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .height(30.dp)
            .width(70.dp)
            .background(Color.Black)
            .clickable { onClick() }, // Bắt sự kiện nhấn

        shape = RoundedCornerShape(16.dp),
        shadowElevation = 4.dp,
        border = BorderStroke(1.dp, if (isSelected) PrimaryColor else Color.Gray.copy(alpha = 0.6f)), // Viền đổi màu khi chọn
        //color = if (isSelected) PrimaryColor else Color.Transparent // Đổi màu nền khi chọn
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)

        ) {
            Text(

                text = name,
                color = if (isSelected) PrimaryColor else Color.Gray.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,

            )
        }
    }
}




@Composable
fun HistoryItem(avtUrl: String , name: String, time: String) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(Color.Black), // Đảm bảo toàn bộ item có màu đen
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.background(Color.Black) // Row chứa thông tin có nền đen
        ) {
            Surface(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(50.dp), // Đảm bảo Surface hình tròn chứa avatar có kích thước rõ ràng
                shape = CircleShape,
                color = Color.Black // Đặt màu cho Surface này để tránh nền bị khác màu
            ) {
                Image(
                    painter = rememberAsyncImagePainter(avtUrl),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Transparent, CircleShape)

                )
            }

            Column(
                modifier = Modifier

                    .background(Color.Black) // Nền đen cho Column chứa nội dung
            ) {
                Text(
                    text = "You matched with $name. Excited? Good. Now, go say hi.",
                    color = Color.White,
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp
                )
                Box(
                    modifier = Modifier
                        .background(Color.Black)
                        .padding(bottom = 20.dp,top = 10.dp)
                        ,
                ) {
                    Surface(
                        modifier = Modifier.padding(8.dp).width(180.dp).height(40.dp),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, Color.White),
                        color = Color.Black // Đặt màu nền cho Surface này
                    ) {
                        Text(
                            text = "SEND A MESSAGE",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
                //divider
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.Gray.copy(alpha = 0.2f))


                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHistoryScreen() {
    HistoryScreen(nav = rememberNavController()) // Giả lập NavController
}
