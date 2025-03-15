package com.hd.minitinder.screens.profile.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.hd.minitinder.navigation.NavigationItem

//import com.google.accompanist.flowlayout.FlowRow


@Composable
fun EditInterestsScreen(
    navController: NavController,
    onCancel: () -> Unit = {},
    onDone: (List<String>) -> Unit = {}
) {
    // Danh sách toàn bộ sở thích
    val allInterests = listOf(
        "Chèo thuyền", "Lặn", "Mô tô nước", "Tour dã bộ", "Nhảy dù",
        "Sân nhà vườn", "Đánh golf", "Bắn cung", "Trượt tuyết",
        "PlayStation", "Nintendo", "Anime", "Truyện tranh",
        "Hoạt động ngoài trời", "Cắm trại", "Nhảy hiện đại",
        "Quyền của cộng đồng LGBTQA+", "Xem phim", "Marvel", "DC",
        "Harry Potter", "XBox", "Dungeons & Dragons", "Bóng rổ", "NBA"
        // ... Thêm sở thích khác tuỳ ý
    )

    // Danh sách sở thích đã chọn (tối đa 5)
    val selectedInterests = remember { mutableStateListOf<String>() }

    // Hàm xử lý khi chọn/bỏ chọn
    fun toggleInterest(interest: String) {
        if (selectedInterests.contains(interest)) {
            // Nếu đã chọn thì bỏ chọn
            selectedInterests.remove(interest)
        } else {
            // Nếu chưa chọn, kiểm tra số lượng < 5 thì thêm
            if (selectedInterests.size < 5) {
                selectedInterests.add(interest)
            } else {
                // Có thể hiển thị Toast hoặc Snackbar báo đã đủ 5
            }
        }
    }

    // Tạo danh sách sắp xếp lại: Mục nào được chọn thì đưa lên trước
    val sortedInterests = allInterests.sortedBy { interest ->
        !selectedInterests.contains(interest) // Selected = true => xếp lên trên
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Thanh trên cùng: Nút "Hủy" bên trái, tiêu đề, nút "Xong" bên phải
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onCancel) {
                Text("Hủy")
            }

            // Hiển thị số lượng đã chọn
            Text(
                text = "${selectedInterests.size} trong tổng số 5",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )

            TextButton(onClick = { onDone(selectedInterests) }) {
                Text("Xong")
            }
        }

        Spacer(Modifier.height(8.dp))

        // Ví dụ có thêm các chip gợi ý trên cùng (Tùy biến)
        Row {
            // Giả sử có vài bộ lọc: "Ẩm thực", "Tập gym", "Kịch", ...
            // Tùy bạn muốn hiển thị ra sao
            // ...
        }

        Spacer(Modifier.height(8.dp))

        // Ô tìm kiếm (giả lập)
        TextField(
            value = "",
            onValueChange = { /* Xử lý tìm kiếm */ },
            placeholder = { Text("Tìm kiếm") },
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        // Khu vực hiển thị danh sách sở thích bằng FlowRow
        // - Dùng Accompanist FlowLayout: com.google.accompanist.flowlayout.FlowRow
        // - Hoặc FlowRow của Compose 1.5+ (ExperimentalLayoutApi)
        FlowRow(
//            mainAxisSpacing = 8.dp,
//            crossAxisSpacing = 8.dp
        ) {
            sortedInterests.forEach { interest ->
                val isSelected = selectedInterests.contains(interest)

                // Tạo "chip" đơn giản
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (isSelected) Color(0xFFB3E5FC) // Xanh nhạt khi chọn
                            else Color.White
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) Color(0xFF0288D1) else Color.Gray,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { toggleInterest(interest) }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = interest,
                        color = if (isSelected) Color(0xFF0288D1) else Color.Black,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
