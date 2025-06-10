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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ckcitlabroom.models.LopHoc
import com.example.ckcitlabroom.viewmodels.LopHocViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CreateLopHocScreen(
    navController: NavHostController,
    lopHocViewModel: LopHocViewModel
){

    val danhSachLopHoc = lopHocViewModel.danhSachAllLopHoc

    LaunchedEffect(Unit) {
        lopHocViewModel.getAllLopHoc()
    }

    val maLopState = remember { mutableStateOf("") }
    val tenLopState = remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarData = remember { mutableStateOf<CustomSnackbarData?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 15.dp),
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
                value = maLopState.value,
                onValueChange = { maLopState.value = it },
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
                placeholder = { Text("Nhập thông tin") },
                shape = RoundedCornerShape(12.dp),
            )

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(16.dp)
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


            Button(
                onClick = {
                    val maLopMoi = maLopState.value
                    val daTonTai = danhSachLopHoc.any { it.MaLopHoc == maLopMoi }

                    if(maLopState.value == "" ){
                        coroutineScope.launch {
                            snackbarData.value = CustomSnackbarData(
                                message = "Mã lớp không được để trống!", type = SnackbarType.ERROR
                            )
                            snackbarHostState.showSnackbar("Thông báo")
                        }
                    }
                    else if (daTonTai) {
                        // Hiện snackbar lỗi
                        coroutineScope.launch {
                            snackbarData.value = CustomSnackbarData(
                                message = "Mã lớp đã tồn tại!", type = SnackbarType.ERROR
                            )
                            snackbarHostState.showSnackbar("Thông báo")
                        }

                    } else {

                        var lopnew = LopHoc(maLopState.value,tenLopState.value,1)

                        lopHocViewModel.createLopHoc(lopnew)

                        coroutineScope.launch {
                            snackbarData.value = CustomSnackbarData(
                                message = "Thêm lớp học thành công",
                                type = SnackbarType.SUCCESS
                            )
                            snackbarHostState.showSnackbar("Thông báo")
                            delay(1000)
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