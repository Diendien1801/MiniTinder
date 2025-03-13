package com.hd.minitinder.common.fragments.logo
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hd.minitinder.R

@Composable
fun LogoTinder(
    modifier: Modifier = Modifier,
    logoSize: Dp = 40.dp,
    textSize: TextUnit = 50.sp,
    color: Color = Color.White,
    colorLogo: Color = Color.White
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_tinder),
            contentDescription = "Logo",
            modifier = Modifier.size(logoSize),
            colorFilter = ColorFilter.tint(colorLogo)
        )
        Text(
            text = "tinder",
            fontSize = textSize,
            fontWeight = FontWeight.Bold,
            color = color,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

