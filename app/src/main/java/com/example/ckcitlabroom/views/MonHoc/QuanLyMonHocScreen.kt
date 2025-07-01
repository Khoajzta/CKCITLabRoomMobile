import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@Suppress("OptInUsageError")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class      // HorizontalPager
)
@Composable
fun QuanLyMonHocScreen(
    navController: NavHostController,
    monHocViewModel: MonHocViewModel
) {
    BackHandler {
        navController.navigate(NavRoute.QUANLY.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
        }
    }

    /* ---------- Pager & Tab state ---------- */
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })
    val scope = rememberCoroutineScope()
    val primary = Color(0xFF1B8DDE)

    /* ---------- Tiêu đề tab ---------- */
    val tabTitles = listOf("Môn Học Đang Dạy", "Môn Học Ngừng Dạy")

    /* ---------- UI ---------- */
    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButtonCustom(
                onClick = { navController.navigate(NavRoute.ADDMONHOC.route) }
            )
        }
    ) { padding ->

        Column(
            Modifier
                .fillMaxSize()
                .padding(
                    start = padding.calculateStartPadding(LayoutDirection.Ltr),
                    end = padding.calculateEndPadding(LayoutDirection.Ltr)
                )
        ) {

            /* ---------- Tabs (không cuộn) ---------- */
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = Color.Transparent,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        color = primary
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(title) },
                        selectedContentColor = primary,
                        unselectedContentColor = Color.Black
                    )
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFF1B8DDE),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            /* ---------- Pager ---------- */
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> ListMonHocDangDayScreen(navController, monHocViewModel)
                    1 -> ListMonHocNgungDayScreen(navController, monHocViewModel)
                }
            }
        }
    }
}
