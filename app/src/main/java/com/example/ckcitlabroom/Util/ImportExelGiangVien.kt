import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream

class DialogState {
    var errorMessage: MutableState<String?> = mutableStateOf(null)
}


@Composable
fun ErrorDialog(dialogState: DialogState) {
    dialogState.errorMessage.value?.let { message ->
        AlertDialog(
            onDismissRequest = {},
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            confirmButton = {
                TextButton(
                    onClick = { dialogState.errorMessage.value = null }
                ) {
                    Text(
                        text = "OK",
                        color = Color(0xFF1B8DDE),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            title = {
                Text(
                    text = "⚠️ Lỗi nhập dữ liệu",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD32F2F),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            },
            text = {
                Text(
                    text = message,
                    fontSize = 15.sp,
                    color = Color.Black,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        )
    }
}


suspend fun parseExcelFileAndImportGiangVien(
    context: Context,
    fileUri: Uri,
    giangVienViewModel: GiangVienViewModel,
    dialogState: DialogState
) {
    val danhSachAllGV = giangVienViewModel.danhSachAllGiangVien

    try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(fileUri)
        if (inputStream != null) {
            val workbook = WorkbookFactory.create(inputStream)
            val sheet = workbook.getSheetAt(0)

            val expectedHeaders = listOf("MaGV", "TenGiangVien", "NgaySinh", "GioiTinh", "Email")

            val headerRow = sheet.getRow(0)
            if (headerRow == null || headerRow.physicalNumberOfCells < expectedHeaders.size) {
                dialogState.errorMessage.value =
                    "File phải có đủ các cột và đặt tên đúng: ${expectedHeaders.joinToString(", ")}"
                inputStream.close()
                return
            }

            for (i in expectedHeaders.indices) {
                val cellValue = headerRow.getCell(i)?.toString()?.trim()
                if (cellValue != expectedHeaders[i]) {
                    dialogState.errorMessage.value =
                        "File phải có đủ các cột và đặt tên đúng: ${expectedHeaders.joinToString(", ")}"
                    inputStream.close()
                    return
                }
            }

            for (rowIndex in 1..sheet.lastRowNum) {
                val row = sheet.getRow(rowIndex)
                if (row != null) {
                    val maGV = row.getCell(0)?.toString()?.trim() ?: continue
                    val tenGV = row.getCell(1)?.toString()?.trim() ?: continue
                    val ngaySinh = row.getCell(2)?.toString()?.trim() ?: continue
                    val gioiTinh = row.getCell(3)?.toString()?.trim() ?: continue
                    val email = row.getCell(4)?.toString()?.trim() ?: continue

                    if (!email.endsWith("@caothang.edu.vn")) {
                        dialogState.errorMessage.value =
                            "Email $email không hợp lệ — phải là email @caothang.edu.vn!"
                        while (dialogState.errorMessage.value != null) {
                            delay(100)
                        }
                        continue
                    }

                    val gvByMa = danhSachAllGV.firstOrNull { it.MaGV == maGV }
                    val gvByEmail = danhSachAllGV.firstOrNull { it.Email == email }

                    if (gvByMa != null || gvByEmail != null) {
                        val message = when {
                            gvByMa != null && gvByEmail != null ->
                                "Giảng viên $tenGV trùng Mã GV với (${gvByMa.TenGiangVien}) và Email với (${gvByEmail.TenGiangVien}), bỏ qua."

                            gvByMa != null ->
                                "Giảng viên $tenGV trùng Mã GV với (${gvByMa.TenGiangVien}), bỏ qua."

                            gvByEmail != null ->
                                "Giảng viên $tenGV trùng Email với (${gvByEmail.TenGiangVien}), bỏ qua."

                            else -> ""
                        }

                        dialogState.errorMessage.value = message
                        while (dialogState.errorMessage.value != null) {
                            delay(100)
                        }
                        continue
                    }

                    val giangVien = GiangVien(
                        MaGV = maGV,
                        TenGiangVien = tenGV,
                        NgaySinh = formatNgaySinhDB(ngaySinh),
                        GioiTinh = gioiTinh,
                        Email = email,
                        MatKhau = hashPasswordMD5(maGV),
                        MaLoaiTaiKhoan = 2,
                        TrangThai = 1
                    )
                    giangVienViewModel.createGiangVien(giangVien)
                }
            }

            inputStream.close()
            Toast.makeText(context, "Đã thêm danh sách giảng viên!", Toast.LENGTH_LONG).show()
        }
    } catch (e: Exception) {
        dialogState.errorMessage.value = "Lỗi khi đọc file Excel!"
        e.printStackTrace()
    }
}




