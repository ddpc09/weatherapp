package com.floveit.weatherwidget.ui

import android.annotation.SuppressLint
import android.os.Build
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
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder


@SuppressLint("ObsoleteSdkInt")
@Composable
fun WeatherGifAnimation(
    @androidx.annotation.DrawableRes resourceId: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // One ImageLoader per process is fine; remember it per composition.
    val imageLoader = remember(context) {
        ImageLoader.Builder(context).components {
            if (Build.VERSION.SDK_INT >= 28) add(ImageDecoderDecoder.Factory())
            else add(GifDecoder.Factory())
        }.build()
    }

    // One Request + one Painter per resourceId.
    val request = remember(resourceId, context) {
        ImageRequest.Builder(context)
            .data(resourceId)
            .memoryCacheKey("gif-$resourceId")   // extra stability
            .build()
    }

    val painter = rememberAsyncImagePainter(
        model = request,
        imageLoader = imageLoader
    )

    Image(
        painter = painter,
        contentDescription = null,
        modifier = modifier
    )
}
