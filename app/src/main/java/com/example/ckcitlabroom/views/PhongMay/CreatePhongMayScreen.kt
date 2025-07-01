import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun CreatePhongMayScreen(
    navController: NavHostController,
    phongMayViewModel: PhongMayViewModel
) {
    var context = LocalContext.current
    val danhSachLoaiPhong = listOf(
        LoaiPhong(1, "Phòng máy"),
        LoaiPhong(2, "Phòng kho"),
        LoaiPhong(3, "Phòng khoa")
    )


    val danhSachPhongMay = phongMayViewModel.danhSachAllPhongMay

    LaunchedEffect(Unit) {
        phongMayViewModel.getAllPhongMay()
    }

    val maPhongState = remember { mutableStateOf("") }
    val tenPhongState = remember { mutableStateOf("") }

    val selectedLoaiPhong = remember { mutableStateOf<LoaiPhong?>(danhSachLoaiPhong.first()) }


    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Thêm phòng máy",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Mã Phòng", color = Color.Black, fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                value = maPhongState.value,
                onValueChange = { maPhongState.value = it },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                placeholder = { Text("Nhập mã phòng") },
                shape = RoundedCornerShape(12.dp),
            )

            Text(
                text = "Tên Phòng", color = Color.Black, fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                value = tenPhongState.value,
                onValueChange = { tenPhongState.value = it },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                placeholder = { Text("Nhập tên phòng") },
                shape = RoundedCornerShape(12.dp),
            )

            Text(
                text = "Loại Phòng", color = Color.Black, fontWeight = FontWeight.Bold
            )

            CustomDropdownSelector(
                label = "Chọn loại phòng",
                items = danhSachLoaiPhong,
                selectedItem = selectedLoaiPhong.value,
                itemLabel = { it.ten },
                onItemSelected = { selectedLoaiPhong.value = it }
            )

            Button(
                onClick = {
                    val maPhongMoi = maPhongState.value
                    val daTonTai = danhSachPhongMay.any { it.MaPhong == maPhongMoi }

                    if (maPhongState.value == "") {
                        Toast.makeText(context, "Mã phòng không được để trống", Toast.LENGTH_SHORT)
                            .show()
                    } else if (daTonTai) {
                        Toast.makeText(context, "Mã phòng đã tồn tại", Toast.LENGTH_SHORT).show()
                    } else {

                        val loai = selectedLoaiPhong.value?.ma ?: 1
                        var phongmaynew = PhongMay(maPhongState.value, tenPhongState.value, loai, 1)

                        phongMayViewModel.createPhongMay(phongmaynew)
                        Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()

                    }
                },

                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0XFF1B8DDE))
            ) {
                Text("Thêm Phòng máy", color = Color.White)
            }
        }
    }
}