package com.sadikahmetozdemir.rainy.ui.walktrough

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sadikahmetozdemir.rainy.R
import com.sadikahmetozdemir.rainy.core.shared.remote.IntroModel

@Composable
fun WalkThroughImage(@DrawableRes imageResId: Int, modifier: Modifier = Modifier) {
    val painter = painterResource(imageResId)
    Image(painter, contentDescription = null, modifier = modifier.size(250.dp, 250.dp))

}

@Preview
@Composable
private fun WalkThroughImagePreview() {
    WalkThroughImage(imageResId = R.drawable.walkthrough_second)
}

@Composable
fun WalkThroughTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        modifier = modifier
            .wrapContentSize()
            .padding(top = 16.dp),
        fontSize = 24.sp,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Bold,
        color = Color.Black
    )
}

@Preview
@Composable
private fun WalkthroughTitlePreview() {
    WalkThroughTitle("Title")
}
@Composable
fun ButtonStart(onClick: () -> Unit) {
    Button(onClick = { onClick() }) {
        Text("Devam Et")
    }
}

@Composable
fun WalkThroughDescription(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        modifier = modifier
            .wrapContentSize()
            .padding(16.dp),
        fontSize = 20.sp,
        fontStyle = FontStyle.Italic
    )
}

@Preview
@Composable
private fun WalkthroughDescriptionPreview() {
    WalkThroughDescription("description")
}

@Composable
fun IntroScreenWithPager(
    introItems: List<IntroModel>,
    onFinish: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { introItems.size })

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            IntroScreen(
                item = introItems[page],
                isLastPage = page == introItems.lastIndex,
                onFinish = onFinish
            )
        }

        // indicator
        Row(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(introItems.size) { iteration ->
                val color = if (pagerState.currentPage == iteration)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(12.dp)
                        .background(color, CircleShape)
                )
            }
        }
    }
}

@Composable
fun IntroScreen(
    item: IntroModel,
    isLastPage: Boolean,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier.fillMaxSize()) {
        item.backgroundId?.let { painterResource(it) }?.let {
            Image(
                painter = it,
                contentDescription = null,
                modifier = modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        IntroPrepare(
            item = item,
            isLastPage = isLastPage,
            onFinish = onFinish
        )
    }
}

@Composable
fun IntroPrepare(
    item: IntroModel,
    isLastPage: Boolean,
    onFinish: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item.drawableId?.let { WalkThroughImage(imageResId = it) }
        item.tittle?.let { WalkThroughTitle(title = it) }
        item.description?.let { WalkThroughDescription(title = it) }

        if (isLastPage) {
            ButtonStart {
                Log.d("Intro", "Devam butonuna basıldı")
                onFinish()
            }
        }
    }
}
