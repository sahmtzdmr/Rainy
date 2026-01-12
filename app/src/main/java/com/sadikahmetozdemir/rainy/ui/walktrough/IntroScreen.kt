package com.sadikahmetozdemir.rainy.ui.walktrough

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
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
    var isVisible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(600), label = ""
    )
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = tween(600), label = ""
    )
    
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    val painter = painterResource(imageResId)
    Image(
        painter, 
        contentDescription = null, 
        modifier = modifier
            .size(250.dp, 250.dp)
            .alpha(alpha)
            .scale(scale)
    )
}

@Preview
@Composable
private fun WalkThroughImagePreview() {
    WalkThroughImage(imageResId = R.drawable.walkthrough_second)
}

@Composable
fun WalkThroughTitle(title: String, modifier: Modifier = Modifier) {
    var isVisible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(800), label = ""
    )
    
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    Text(
        text = title,
        modifier = modifier
            .wrapContentSize()
            .padding(top = 16.dp)
            .alpha(alpha),
        fontSize = 28.sp,
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
    var isVisible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(800), label = ""
    )
    
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .alpha(alpha)
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF7699D4),
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 4.dp
        )
    ) {
        Text(
            "Devam Et",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun WalkThroughDescription(title: String, modifier: Modifier = Modifier) {
    var isVisible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(1000), label = ""
    )
    
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    Text(
        text = title,
        modifier = modifier
            .wrapContentSize()
            .padding(16.dp)
            .alpha(alpha),
        fontSize = 18.sp,
        fontStyle = FontStyle.Italic,
        color = Color(0xFF333333),
        lineHeight = 24.sp
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
