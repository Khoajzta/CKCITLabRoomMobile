import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.viewmodels.CaHocViewModel
import com.example.ckcitlabroom.viewmodels.ChiTietDonNhapyViewModel
import com.example.ckcitlabroom.viewmodels.ChiTietSuDungMayViewModel
import com.example.ckcitlabroom.viewmodels.DonNhapViewModel
import com.example.ckcitlabroom.viewmodels.LichHocViewModel
import com.example.ckcitlabroom.viewmodels.MayTinhViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun MayTinhDetailScreen(
    maMay: String,
    phongMayViewModel: PhongMayViewModel,
    giangVienViewModel: GiangVienViewModel,
    sinhVienViewModel: SinhVienViewModel,
    navController: NavHostController,
    cahocViewModel: CaHocViewModel,
    chitietsudungmayViewModel: ChiTietSuDungMayViewModel,
    lichhocViewModel: LichHocViewModel
) {

    val context = LocalContext.current
    val result = chitietsudungmayViewModel.chitietsudungmayCreateResult

    var donNhapViewModel: DonNhapViewModel = viewModel()
    var chiTietDonNhapyViewModel: ChiTietDonNhapyViewModel = viewModel()
    var mayTinhViewModel: MayTinhViewModel = viewModel()

    val giangVien = giangVienViewModel.giangvienSet
    val maytinh = mayTinhViewModel.maytinh
    val danhSachChiTietDonNhap = chiTietDonNhapyViewModel.danhSachAllChiTietDonNhap
    val danhSachDonNhap = donNhapViewModel.danhSachDonNhap


    val chiTiet = danhSachChiTietDonNhap.find { it.MaMay == maytinh?.MaMay }

    val maDonNhap = chiTiet?.MaDonNhap

    val donNhap = danhSachDonNhap.find { it.MaDonNhap == maDonNhap }

    val ngayNhap = donNhap?.NgayNhap

    val maMayState = remember { mutableStateOf("") }
    val tenMayState = remember { mutableStateOf("") }
    val viTriState = remember { mutableStateOf("") }
    val mainState = remember { mutableStateOf("") }
    val cpuState = remember { mutableStateOf("") }
    val ramState = remember { mutableStateOf("") }
    val vgaState = remember { mutableStateOf("") }
    val manHinhState = remember { mutableStateOf("") }
    val banPhimState = remember { mutableStateOf("") }
    val chuotState = remember { mutableStateOf("") }
    val hddState = remember { mutableStateOf("") }
    val ssdState = remember { mutableStateOf("") }
    val trangThaiState = remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarData = remember { mutableStateOf<CustomSnackbarData?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(maMay) {
        mayTinhViewModel.getMayTinhByMaMay(maMay)
        phongMayViewModel.getAllPhongMay()
        chiTietDonNhapyViewModel.getAllChiTietDonNhap()
        donNhapViewModel.getAllDonNhap()
    }

    DisposableEffect(Unit) {
        onDispose {
            chiTietDonNhapyViewModel.stopPollingAllChiTietDonNhap()
            donNhapViewModel.stopPollingAllDonNhap()
        }
    }

    LaunchedEffect(maytinh) {
        maytinh?.let {
            maMayState.value = it.MaMay
            tenMayState.value = it.TenMay
            viTriState.value = it.ViTri
            mainState.value = it.Main
            cpuState.value = it.CPU
            ramState.value = it.RAM
            vgaState.value = it.VGA
            manHinhState.value = it.ManHinh
            banPhimState.value = it.BanPhim
            chuotState.value = it.Chuot
            hddState.value = it.HDD
            ssdState.value = it.SSD
            trangThaiState.value = it.TrangThai.toString()
        }
    }

    //=============================================================================

    var sinhvien = sinhVienViewModel.sinhvienSet

    LaunchedEffect(Unit) {
        sinhvien?.let {
            lichhocViewModel.getAllLichHoc()
            cahocViewModel.getAllCaHoc()
        }
        chitietsudungmayViewModel.getAllChiTietSuDungMay()
    }

    val danhSachCaHoc = cahocViewModel.danhSachAllCaHoc
    val danhSachLichHoc = lichhocViewModel.danhSachLichHoc

    // Lấy giờ hiện tại
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    val gioHienTai = LocalTime.now().format(formatter)

    // Tìm ca học hiện tại
    val caHienTai = danhSachCaHoc.firstOrNull {
        it.GioBatDau <= gioHienTai && it.GioKetThuc >= gioHienTai
    }


    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(640.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Cấu Hình Máy Tính", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (maytinh != null) {
                    item {
                        Text(
                            text = "Mã Máy",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(Color.White)
                                .border(
                                    width = 1.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 10.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    maytinh.MaMay,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Tên Máy",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(Color.White)
                                .border(
                                    width = 1.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 10.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    maytinh.TenMay,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Vị Trí",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(Color.White)
                                .border(
                                    width = 1.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 10.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    maytinh.ViTri,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            }
                        }
                    }
//
                    item {
                        Text(
                            text = "Main",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(Color.White)
                                .border(
                                    width = 1.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 10.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    maytinh.Main,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "CPU",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(Color.White)
                                .border(
                                    width = 1.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 10.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    maytinh.CPU,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "RAM",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(Color.White)
                                .border(
                                    width = 1.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 10.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    maytinh.RAM,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            }
                        }
                    }
//
                    item {
                        Text(
                            text = "VGA",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(Color.White)
                                .border(
                                    width = 1.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 10.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    maytinh.VGA,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            }
                        }
                    }
//
                    item {
                        Text(
                            text = "Màn Hình",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(Color.White)
                                .border(
                                    width = 1.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 10.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    maytinh.ManHinh,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            }
                        }
                    }
//
                    item {
                        Text(
                            text = "Bàn Phím",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(Color.White)
                                .border(
                                    width = 1.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 10.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    maytinh.BanPhim,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            }
                        }
                    }
//
                    item {
                        Text(
                            text = "Chuột",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(Color.White)
                                .border(
                                    width = 1.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 10.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    maytinh.Chuot,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            }
                        }
                    }
//
                    item {
                        Text(
                            text = "HDD",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(Color.White)
                                .border(
                                    width = 1.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 10.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    maytinh.HDD,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            }
                        }
                    }
//
                    item {
                        Text(
                            text = "SSD",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(Color.White)
                                .border(
                                    width = 1.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 10.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    maytinh.SSD,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Ngày Nhập",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(Color.White)
                                .border(
                                    width = 1.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 10.dp),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    formatNgay(ngayNhap.toString()),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            }
                        }
                    }

                } else {
                    item {
                        Text("Lỗi khi lấy API")
                    }

                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = 16.dp)
            ) { data ->
                snackbarData.value?.let { customData ->
                    Snackbar(
                        containerColor = Color(0xFF1B8DDE),
                        contentColor = Color.White,
                        shape = RoundedCornerShape(12.dp),
                        action = {
                            TextButton(onClick = {
                                snackbarData.value = null
                            }) {
                                Text("Đóng", color = Color.White)
                            }
                        }
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (customData.type == SnackbarType.SUCCESS) Icons.Default.Info else Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (customData.type == SnackbarType.SUCCESS) Color.Cyan else Color.Yellow,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = customData.message)
                        }
                    }
                }
            }


            if (giangVien != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            navController.navigate(NavRoute.CHITIETLICHSUCHUYENMAY.route + "?mamay=${maytinh.MaMay}")
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1B8DDE)),
                        border = BorderStroke(1.dp, Color(0xFF1B8DDE)),
                    ) {
                        Icon(
                            Icons.Outlined.History,
                            contentDescription = "Lịch sử chuyển",
                            tint = Color(0xFF1B8DDE),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("Lịch sử chuyển", color = Color(0xFF1B8DDE), fontSize = 14.sp)
                    }

                    OutlinedButton(
                        onClick = {
                            navController.navigate(NavRoute.DETAILLICHSUSUAMAY.route + "?mamay=${maytinh.MaMay}")
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF1B8DDE)),
                        border = BorderStroke(1.dp, Color(0xFF1B8DDE))
                    ) {
                        Icon(
                            Icons.Outlined.Build,
                            contentDescription = "Lịch sử sửa",
                            tint = Color(0xFF1B8DDE),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text("Lịch sử sửa", color = Color(0xFF1B8DDE), fontSize = 14.sp)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (giangVien != null) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            navController.navigate(NavRoute.EDITMAYTINH.route + "?mamay=${maytinh.MaMay}")
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFF1B8DDE))
                    ) {
                        Text("Chỉnh sửa", color = Color.White)
                    }
                } else {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            val ngayHomNay = LocalDate.now().toString()
                            val maPhongMay = maytinh?.MaPhong

                            // Kiểm tra thông tin cần thiết
                            if (sinhvien == null) {
                                Toast.makeText(
                                    context,
                                    "Không thể điểm danh: Không tìm thấy thông tin sinh viên",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            if (maytinh == null) {
                                Toast.makeText(
                                    context,
                                    "Không thể điểm danh: Không tìm thấy thông tin máy tính",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            if (caHienTai == null) {
                                Toast.makeText(
                                    context,
                                    "Không thể điểm danh: Hiện không nằm trong ca học nào",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            // Lấy lịch học có cùng ca và ngày
                            val lichCungCaVaNgay = danhSachLichHoc.firstOrNull { lichHoc ->
                                lichHoc.MaCaHoc == caHienTai.MaCaHoc &&
                                        lichHoc.NgayDay == ngayHomNay
                            }

                            if (lichCungCaVaNgay == null) {
                                Toast.makeText(
                                    context,
                                    "Không có lịch học trong ${caHienTai.TenCa} ngày hôm nay",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            if (lichCungCaVaNgay.MaPhong != maPhongMay) {
                                Toast.makeText(
                                    context,
                                    "Máy tính không đúng phòng học hiện tại",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            val daDiemDanh =
                                chitietsudungmayViewModel.danhSachAllChiTiet.any { chiTiet ->
                                    chiTiet.MaSV == sinhvien.MaSinhVien &&
                                            chiTiet.MaCa == caHienTai.MaCaHoc &&
                                            chiTiet.NgaySuDung == ngayHomNay
                                }

                            if (daDiemDanh) {
                                Toast.makeText(
                                    context,
                                    "Bạn đã điểm danh trước đó rồi",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@Button
                            }

                            // ✅ Chưa điểm danh → tiến hành
                            val chitiet = ChiTietSuDungMay(
                                MaChiTietSuDung = 0,
                                MaSV = sinhvien.MaSinhVien,
                                MaCa = lichCungCaVaNgay.MaCaHoc,
                                MaTuan = lichCungCaVaNgay.MaTuan,
                                NgaySuDung = lichCungCaVaNgay.NgayDay,
                                MaMay = maytinh.MaMay,
                                MaPhong = lichCungCaVaNgay.MaPhong
                            )

                            chitietsudungmayViewModel.createChiTietSuDungMay(chitiet)

                            Toast.makeText(context, "Điểm danh thành công", Toast.LENGTH_SHORT)
                                .show()
                            navController.popBackStack()
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFF4CAF50))
                    ) {
                        Text("Điểm Danh", color = Color.White)
                    }
                }


                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        navController.navigate(NavRoute.ADDPHIEUSUACHUA.route + "?mamay=${maytinh.MaMay}")
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFFF44336))
                ) {
                    Text("Báo Hỏng", color = Color.White)
                }
            }
        }
    }
}

