import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.viewmodels.ChiTietDonNhapyViewModel
import com.example.ckcitlabroom.viewmodels.DonNhapViewModel
import com.example.ckcitlabroom.viewmodels.MayTinhViewModel
import kotlinx.coroutines.launch

@Suppress("OptInUsageError")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class    // HorizontalPager
)
@Composable
fun QuanLyPhieuSuaChuaScreen(
    navController: NavHostController,
    phieuSuaChuaViewModel: PhieuSuaChuaViewModel,
    mayTinhViewModel: MayTinhViewModel,
    lichhsuSuaMayViewModel: LichSuSuaMayViewModel,
    giangVienViewModel: GiangVienViewModel,
    phongMayViewModel: PhongMayViewModel,
    donNhapViewModel: DonNhapViewModel,
    chiTietDonNhapyViewModel: ChiTietDonNhapyViewModel,
    startIndex: Int = 0                       // ← thêm tham số
) {

    BackHandler {
        navController.navigate(NavRoute.QUANLY.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
        }
    }
    /* ---------- Pager & Tab state ---------- */
    val pageCount = 3
    val pagerState = rememberPagerState(
        initialPage = startIndex.coerceIn(0, pageCount - 1),
        pageCount = { pageCount }
    )
    val scope = rememberCoroutineScope()


    /* ---------- Tiêu đề Tab ---------- */
    val tabTitles = listOf(
        "Phiếu Chưa Sửa",
        "Phiếu Đã Sửa",
        "Lịch Sử Sửa Máy"
    )

    /* ---------- UI ---------- */
    Column(Modifier.fillMaxSize()) {

        /* ----- Tabs ----- */
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

        /* ----- Pager ----- */
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> ListPhieuChuaSua(
                    navController,
                    phieuSuaChuaViewModel,
                    mayTinhViewModel,
                    lichhsuSuaMayViewModel,
                    giangVienViewModel,
                )

                1 -> ListPhieuDaSua(
                    navController,
                    phieuSuaChuaViewModel,
                    mayTinhViewModel,
                    lichhsuSuaMayViewModel,
                    giangVienViewModel
                )

                2 -> LichSuSuaMayScreen(
                    navController,
                    phongMayViewModel,
                    mayTinhViewModel,
                    donNhapViewModel,
                    chiTietDonNhapyViewModel
                )
            }
        }
    }
}

