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
import com.example.ckcitlabroom.models.LopHoc
import com.example.ckcitlabroom.viewmodels.LopHocViewModel

@Composable
fun CreateLopHocScreen(
    navController: NavHostController,
    lopHocViewModel: LopHocViewModel
) {

    var context = LocalContext.current
    val danhSachLopHoc = lopHocViewModel.danhSachAllLopHoc

    LaunchedEffect(Unit) {
        lopHocViewModel.getAllLopHoc()
    }

    val maLopState = remember { mutableStateOf("") }
    val tenLopState = remember { mutableStateOf("") }

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
                text = "Thêm Lớp Học",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "Mã Lớp Học", color = Color.Black, fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                value = maLopState.value.toUpperCase(),
                onValueChange = { maLopState.value = it },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                placeholder = { Text("Nhập mã lớp học") },
                shape = RoundedCornerShape(12.dp),
            )

            Text(
                text = "Lớp ", color = Color.Black, fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                value = tenLopState.value,
                onValueChange = { tenLopState.value = it },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                placeholder = { Text("Nhập tên lớp học") },
                shape = RoundedCornerShape(12.dp),
            )


            Button(
                onClick = {
                    val maLopMoi = maLopState.value.trim()
                    val tenLopMoi = tenLopState.value.trim()
                    val daTonTai = danhSachLopHoc.any { it.MaLopHoc == maLopMoi }

                    when {
                        maLopMoi.isEmpty() -> {
                            Toast.makeText(
                                context,
                                "Mã lớp không được để trống",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        tenLopMoi.isEmpty() -> {
                            Toast.makeText(
                                context,
                                "Tên lớp không được để trống",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        daTonTai -> {
                            Toast.makeText(context, "Mã lớp đã tồn tại", Toast.LENGTH_SHORT).show()
                        }

                        else -> {
                            val lopMoi = LopHoc(maLopMoi.toUpperCase(), tenLopMoi, 1)
                            lopHocViewModel.createLopHoc(lopMoi)
                            Toast.makeText(context, "Thêm lớp học thành công", Toast.LENGTH_SHORT)
                                .show()
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0XFF1B8DDE))
            ) {
                Text("Thêm Lớp học", color = Color.White)
            }
        }
    }
}