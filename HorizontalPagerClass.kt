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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
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
                            count = detailsList.size, modifier = Modifier
                                .fillMaxSize()
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

        // Parent Layout to draw Pager Slides
        ConstraintLayout {

            // Background image for each slide
            Image(
                painter = painterResource(detailsList[page].image),
                contentDescription = "",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )

            val (title, description, button, dots) = createRefs()
            val topGuidelineTitle = createGuidelineFromTop(0.60f)
            val topGuidelineDescription = createGuidelineFromTop(0.68f)
            val topGuidelineButton = createGuidelineFromTop(0.78f)
            val bottomGuidelineDots = createGuidelineFromBottom(20.dp)
            val startGuideline = createGuidelineFromStart(20.dp)
            val endGuideline = createGuidelineFromEnd(20.dp)


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
                        .padding(20.dp)
                        .clickable {
                            Toast
                                .makeText(mContext, "Skip", Toast.LENGTH_SHORT)
                                .show()
                        },
                )
            }

            Row(
                modifier = Modifier
                    .constrainAs(title) {
                        start.linkTo(startGuideline)
                        top.linkTo(topGuidelineTitle)
                    }
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = detailsList[page].title1,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,

                    )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = detailsList[page].title2,
                    fontSize = 30.sp,
                    color = black54,
                )
            }


            Row(
                modifier = Modifier
                    .constrainAs(description) {
                        start.linkTo(startGuideline)
                        top.linkTo(topGuidelineDescription)
                    }
                    .fillMaxWidth().padding(end = 20.dp),
                horizontalArrangement = Arrangement.Start,
            ) {

                Text(
                    text = detailsList[page].description,
                    fontSize = 14.sp,
                    color = textColor2,
                )
            }

            TextButton(
                modifier = Modifier
                    .constrainAs(button) {
                        top.linkTo(topGuidelineButton)
                        end.linkTo(endGuideline)
                    }
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
                Spacer(modifier = Modifier.width(15.dp))

                Icon(
                    imageVector = Icons.Default.DoubleArrow,
                    contentDescription = "Sort",
                    tint = Color.White,
                )

            }

            // Function to display horizontal dots
            Row(
                modifier = Modifier
                    .constrainAs(dots) {
                        bottom.linkTo(bottomGuidelineDots)
                    }
                    .fillMaxWidth(),
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
                        .width(if (index == selectedIndex) 25.dp else 8.dp)
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(if (index == selectedIndex) selectedColor else unSelectedColor)
                )
                if (index != totalDots - 1) {
                    Spacer(modifier = Modifier.width(3.dp))
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