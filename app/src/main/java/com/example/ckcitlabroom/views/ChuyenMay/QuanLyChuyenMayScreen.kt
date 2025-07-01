import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.viewmodels.ChiTietDonNhapyViewModel
import com.example.ckcitlabroom.viewmodels.DonNhapViewModel
import com.example.ckcitlabroom.viewmodels.MayTinhViewModel
import kotlinx.coroutines.launch

@Suppress("OptInUsageError")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class      // HorizontalPager
)
@Composable
fun QuanLyChuyenMayScreen(
    navController: NavHostController,
    mayTinhViewModel: MayTinhViewModel,
    phongMayViewModel: PhongMayViewModel,
    donNhapViewModel: DonNhapViewModel,
    chitetdonnhapViewModel: ChiTietDonNhapyViewModel
) {
    /* ─── reset chọn máy khi vào màn ─── */
    LaunchedEffect(Unit) { mayTinhViewModel.clearDanhSachMayTinhDuocChon() }

    /* ─── Pager & Tab state ─── */
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })
    val scope = rememberCoroutineScope()
    val primary = Color(0xFF1B8DDE)

    /* ─── Tab tiêu đề ─── */
    val tabTitles = listOf("Danh Sách Phòng Máy", "Lịch Sử Chuyển Theo Đơn")

    /* ─── UI ─── */
    Column(Modifier.fillMaxSize()) {

        /* ---------- Tabs ---------- */
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
                    text = {
                        Text(
                            title,
                            fontSize = 12.sp,
                            maxLines = 1,                        // chỉ 1 dòng
                            overflow = TextOverflow.Ellipsis,   // cắt + “…”
                            softWrap = false,                   // không xuống dòng
                            modifier = Modifier.padding(horizontal = 0.dp)
                        )
                    },
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
                0 -> ListPhongMayChuyenScreen(
                    navController,
                    mayTinhViewModel,
                    phongMayViewModel
                )

                1 -> LichSuChuyenMayScreen(
                    navController,
                    donNhapViewModel,
                    chitetdonnhapViewModel,
                    mayTinhViewModel
                )
            }
        }
    }
}


