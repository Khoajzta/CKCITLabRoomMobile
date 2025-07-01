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
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
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
    ExperimentalFoundationApi::class
)
@Composable
fun QuanLyGiangVien(
    navController: NavHostController,
    giangVienViewModel: GiangVienViewModel,
    startIndex: Int = 0                     // ← THÊM THAM SỐ
) {
    BackHandler {
        navController.navigate(NavRoute.QUANLY.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
        }
    }

    /* ---------- Pager & Tab state ---------- */
    val pageCount = 4
    val pagerState = rememberPagerState(
        initialPage = startIndex.coerceIn(0, pageCount - 1),   // an toàn 0–3
        pageCount = { pageCount }
    )
    val scope = rememberCoroutineScope()


    /* ---------- Tiêu đề tab ---------- */
    val tabTitles = listOf(
        "Giảng Viên Công Tác",
        "Giảng Viên Ngừng Công Tác",
        "Phân Quyền Admin",
        "Xóa Quyền Admin"
    )

    /* ---------- UI ---------- */
    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButtonCustom {
                navController.navigate(NavRoute.ADDGIANGVIEN.route)
            }
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

            /* Tabs */
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                        color = Color(0xFF1B8DDE)
                    )
                }
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(title) },
                        selectedContentColor = Color(0xFF1B8DDE),
                        unselectedContentColor = Color.Black
                    )
                }
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = Color(0xFF1B8DDE),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            /* Pager */
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> ListGiangVienCongTac(navController, giangVienViewModel)
                    1 -> ListGiangVienNgungCongTac(navController, giangVienViewModel)
                    2 -> PhanQuyenAdminGVScreen(navController, giangVienViewModel)
                    3 -> PhanQuyenGiangVienScreen(navController, giangVienViewModel)
                }
            }
        }
    }
}



