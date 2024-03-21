package com.ab.weatherforecast.ui.splash

import android.widget.AbsoluteLayout
import android.window.SplashScreen
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ab.weatherforecast.R
import com.ab.weatherforecast.ui.composables.RoundedCornerButton
import com.ab.weatherforecast.ui.theme.BungeeInlineFamily
import com.ab.weatherforecast.ui.theme.BungeeOutlineFamily

@Composable
fun SplashScreen(navController: NavController){
    AndroidSmall1(navController)
}

@Composable
fun AndroidSmall1(navController: NavController,modifier: Modifier = Modifier) {

    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Image(painter = painterResource(id = R.drawable.bg1),
            contentDescription = "Splash image",
            modifier = modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillBounds)

        Image(painter = painterResource(id = R.drawable.cloudy),
            contentDescription = "Splash image",
            modifier = modifier
                .height(35.dp)
                .width(30.dp)
                .offset(-(100).dp, -15.dp),
            contentScale = ContentScale.Inside)

        SplashScreenContent(navController)



    }
}





@Composable
fun SplashScreenContent(navController: NavController){
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
        verticalArrangement = Arrangement.Center) {

        Text(
            text = stringResource(id = R.string.trusted),
            style = TextStyle(
                fontFamily = BungeeOutlineFamily,
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),

            )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(id = R.string.weather),
            style = TextStyle(
                fontFamily =  BungeeInlineFamily,
                fontSize = 35.sp,
                color = Color.Black

            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(id = R.string.forecast),
            style = TextStyle(
                fontFamily = BungeeOutlineFamily,
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(id = R.string.app_desc),
            modifier = Modifier.padding(top = 10.dp, end = 20.dp),
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Normal
            )
        )

        Spacer(modifier = Modifier.weight(0.5f))

        RoundedCornerButton(buttonText = stringResource(id = R.string.get_started),
            modifier = Modifier
                .fillMaxWidth()
        ){
            navController.navigate("mapScreen")

        }

    }

}


