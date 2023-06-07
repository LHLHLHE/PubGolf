package jg.com.pubgolf.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import jg.com.pubgolf.R


private val Gotham = FontFamily(
    Font(R.font.gotham_bold, FontWeight.Bold),
    Font(R.font.gothamxnarrow_medium, FontWeight.Medium)
)

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    h1 = TextStyle(
        fontFamily = Gotham,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp
    ),
    h4 = TextStyle(
        fontFamily = Gotham,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)