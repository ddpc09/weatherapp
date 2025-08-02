package com.floveit.weatherwidget.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import com.airbnb.lottie.compose.*
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.ImageLoader
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import coil.decode.ImageDecoderDecoder


//@Composable
//fun WeatherAnimation(
//    condition: WeatherCondition,
//    modifier: Modifier = Modifier.fillMaxSize()
//) {
////    val animationFile = when (condition) {
////        WeatherCondition.Clear -> "Sunny.json"
////        // Later: add "Rain.json", "Thunder.json", etc.
////        else -> "Sunny.json"
////    }
//
//    val animationFile = "testRainy.json"
//
//    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(animationFile))
//    val progress by animateLottieCompositionAsState(
//        composition,
//        iterations = LottieConstants.IterateForever
//    )
//    LaunchedEffect(composition) {
//        if (composition != null) {
//            Log.d("WeatherAnimation", "✅ Lottie composition loaded: $animationFile")
//        } else {
//            Log.e("WeatherAnimation", "❌ Failed to load Lottie composition: $animationFile")
//        }
//    }
//    LottieAnimation(
//        composition = composition,
//        progress = { progress },
//        modifier = modifier
//    )
//}

@SuppressLint("ObsoleteSdkInt")
@Composable
fun WeatherGifAnimation(resourceId: Int, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
        .components {
            if (android.os.Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(resourceId)
            .build(),
        contentDescription = null,
        imageLoader = imageLoader,
        modifier = modifier
    )
}