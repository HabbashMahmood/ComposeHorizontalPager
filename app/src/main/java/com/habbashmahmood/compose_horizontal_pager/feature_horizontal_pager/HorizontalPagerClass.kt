package com.habbashmahmood.compose_horizontal_pager.feature_horizontal_pager

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.habbashmahmood.compose_horizontal_pager.R
import com.habbashmahmood.compose_horizontal_pager.ui.theme.ComposeHorizontalPagerAppTheme
import com.habbashmahmood.compose_horizontal_pager.ui.theme.black54
import com.habbashmahmood.compose_horizontal_pager.ui.theme.mainColor
import com.habbashmahmood.compose_horizontal_pager.ui.theme.textColor2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
class HorizontalPagerClass : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val mContext = LocalContext.current
            val scope = rememberCoroutineScope()

            ComposeHorizontalPagerAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val state = rememberPagerState()
                    Column {
                        HorizontalPager(
                            state = state,
                            count = detailsList.size
                        ) { page ->
                            PagerLayout(page, mContext, state, scope)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun PagerLayout(
        page: Int,
        mContext: Context,
        state: PagerState,
        scope: CoroutineScope,
    ) {

        val configuration = LocalConfiguration.current

        val screenHeight = configuration.screenHeightDp.dp
        val screenWidth = configuration.screenWidthDp.dp

        // Parent Layout to draw Pager Slides
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            // Background image for each slide
            Image(
                painter = painterResource(detailsList[page].image),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            // Row to display 'SKIP' on the top right corner of screen
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "SKIP",
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen._20sdp))
                        .clickable {
                            Toast
                                .makeText(mContext, "Skip", Toast.LENGTH_SHORT)
                                .show()
                        },
                )
            }

            // Box Layout for Slide Content
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomStart
            ) {

                // Function to display horizontal dots
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = dimensionResource(R.dimen._20sdp))
                        .padding(horizontal = dimensionResource(R.dimen._20sdp)),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    VerticalDotsIndicator(
                        totalDots = detailsList.size,
                        selectedIndex = page,
                        selectedColor = mainColor,
                        unSelectedColor = mainColor.copy(alpha = 0.3f),
                    )
                }

                // Row to display textual content
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = dimensionResource(R.dimen._100sdp))
                        .padding(horizontal = dimensionResource(R.dimen._20sdp)),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier
                    ) {

                        Row(
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            Text(
                                text = detailsList[page].title1,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,

                                )
                            Spacer(modifier = Modifier.width(dimensionResource(R.dimen._5sdp)))
                            Text(
                                text = detailsList[page].title2,
                                fontSize = 30.sp,
                                color = black54,
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = dimensionResource(R.dimen._70sdp))
                                .padding(top = dimensionResource(R.dimen._20sdp))
                        ) {
                            Text(
                                text = detailsList[page].description,
                                fontSize = 14.sp,
                                color = textColor2,
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = dimensionResource(R.dimen._40sdp)),
                            horizontalArrangement = Arrangement.End,
                        ) {
                            TextButton(
                                modifier = Modifier
                                    .width(dimensionResource(R.dimen._100sdp))
                                    .height(dimensionResource(R.dimen._50sdp))
                                    .background(
                                        color = mainColor,
                                        shape = MaterialTheme.shapes.medium
                                    ),
                                onClick = {
                                    if (page < detailsList.size - 1) {
                                        scope.launch {
                                            state.scrollToPage(state.currentPage + 1)
                                        }
                                    } else {
                                        Toast
                                            .makeText(mContext, "Done", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                },
                            ) {
                                Text(
                                    text = if (page < detailsList.size - 1) "NEXT" else "DONE",
                                    style = MaterialTheme.typography.subtitle2,
                                    color = Color.White,
                                    maxLines = 1,
                                    fontWeight = FontWeight.Light,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.width(dimensionResource(R.dimen._12sdp)))

                                Icon(
                                    imageVector = Icons.Default.DoubleArrow,
                                    contentDescription = "Sort",
                                    tint = Color.White,
                                )
                            }

                        }
                    }
                }
            }
        }
    }

    @Composable
    fun VerticalDotsIndicator(
        totalDots: Int,
        selectedIndex: Int,
        selectedColor: Color,
        unSelectedColor: Color,
    ) {
        LazyRow {
            items(totalDots) { index ->
                Box(
                    modifier = Modifier
                        .width(
                            if (index == selectedIndex) dimensionResource(R.dimen._20sdp) else dimensionResource(
                                R.dimen._6sdp
                            )
                        )
                        .height(dimensionResource(R.dimen._6sdp))
                        .clip(CircleShape)
                        .background(if (index == selectedIndex) selectedColor else unSelectedColor)
                )
                if (index != totalDots - 1) {
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen._3sdp)))
                }
            }
        }
    }
}

data class Details(
    val image: Int,
    val title1: String,
    val title2: String,
    val description: String
)

private val detailsList = listOf(
    Details(
        image = R.drawable.ic_onboarding2,
        title1 = "Get",
        title2 = "Burn",
        description = "Let’s keep burning, to achieve yours goals, it hurts only temporarily, if you give up now you will be in pain forever"
    ),
    Details(
        image = R.drawable.ic_onboarding3,
        title1 = "Eat",
        title2 = "Well",
        description = "Let's start a healthy lifestyle with us, we can determine your diet every day. healthy eating is fun"
    ),
    Details(
        image = R.drawable.ic_onboarding1,
        title1 = "Track",
        title2 = "Goal",
        description = "Don't worry if you have trouble determining your goals, We can help you determine your goals and track your goals"
    ),
    Details(
        image = R.drawable.ic_onboarding4,
        title1 = "Improve",
        title2 = "Sleep",
        description = "Improve the quality of your sleep with us, good quality sleep can bring a good mood in the morning"
    ),
)