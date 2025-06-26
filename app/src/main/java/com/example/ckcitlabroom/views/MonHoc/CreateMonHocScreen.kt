import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CreateMonHocScreen(
    navController: NavHostController,
    monHocViewModel: MonHocViewModel
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var danhSachMonHoc = monHocViewModel.danhSachAllMonHoc

    LaunchedEffect(Unit) {
        monHocViewModel.getAllMonHoc()
    }

    val maMonHocState = remember { mutableStateOf("") }
    val tenMonHocState = remember { mutableStateOf("") }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Thêm Môn Học",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Text("Mã Môn Học", color = Color.Black, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                value = maMonHocState.value,
                onValueChange = { maMonHocState.value = it },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                placeholder = { Text("Nhập thông tin") },
                shape = RoundedCornerShape(12.dp),
            )

            Text("Tên Môn Học", color = Color.Black, fontWeight = FontWeight.Bold)
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                value = tenMonHocState.value,
                onValueChange = { tenMonHocState.value = it },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                placeholder = { Text("Nhập thông tin") },
                shape = RoundedCornerShape(12.dp),
            )

            Button(
                onClick = {
                    val maMon = maMonHocState.value.trim()
                    val tenMon = tenMonHocState.value.trim()
                    val daTonTai = danhSachMonHoc.any { it.MaMonHoc == maMon }

                    when {
                        maMon.isEmpty() -> {
                            Toast.makeText(context, "Mã môn học không được để trống!", Toast.LENGTH_SHORT).show()
                        }
                        tenMon.isEmpty() -> {
                            Toast.makeText(context, "Tên môn học không được để trống!", Toast.LENGTH_SHORT).show()
                        }
                        daTonTai -> {
                            Toast.makeText(context, "Mã môn học đã tồn tại!", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            val monHocMoi = MonHoc(maMon, tenMon, 1)
                            monHocViewModel.createMonHoc(monHocMoi)
                            Toast.makeText(context, "Thêm môn học thành công", Toast.LENGTH_SHORT).show()

                            coroutineScope.launch {
                                delay(1000)
                                navController.popBackStack()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0XFF1B8DDE))
            ) {
                Text("Thêm Môn Học", color = Color.White)
            }
        }
    }
}
