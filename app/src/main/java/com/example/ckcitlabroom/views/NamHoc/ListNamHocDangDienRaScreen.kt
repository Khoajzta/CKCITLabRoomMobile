import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun ListNamHocDangDienRaScreen(
    navController: NavHostController,
    namHocViewModel: NamHocViewModel
){
    LaunchedEffect(Unit) { namHocViewModel.getAllNamHoc() }
    DisposableEffect(Unit) { onDispose { namHocViewModel.stopPollingAllNamHoc() } }

    /* 1️⃣  Lấy danh sách live từ ViewModel (mutableStateOf) */
    val danhSachAllNamHoc = namHocViewModel.danhSachAllNamHoc
    var danhsachnamhocdangdienra = danhSachAllNamHoc.filter { it.TrangThai == 1 }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            if (danhsachnamhocdangdienra == null || danhsachnamhocdangdienra.isEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Chưa có năm học nào",
                            color = Color.Black,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                }
            } else {
                items(danhsachnamhocdangdienra) { namhoc ->
                    CardNamHoc(namhoc, navController, namHocViewModel)
                }
            }
        }

    }
}