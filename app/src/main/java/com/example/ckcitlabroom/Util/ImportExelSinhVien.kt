import android.content.Context
import android.net.Uri
import android.widget.Toast
import kotlinx.coroutines.delay
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream

suspend fun parseExcelFileAndImportSinhVien(
    context: Context,
    fileUri: Uri,
    sinhVienViewModel: SinhVienViewModel,
    dialogState: DialogState
) {
    val danhSachAllSV = sinhVienViewModel.danhSachAllSinhVien

    try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(fileUri)
        if (inputStream != null) {
            val workbook = WorkbookFactory.create(inputStream)
            val sheet = workbook.getSheetAt(0)

            val expectedHeaders = listOf("MSSV", "TenSinhVien", "NgaySinh", "GioiTinh", "MaLop")

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
                    val maSV = row.getCell(0)?.toString()?.trim() ?: continue
                    val tenSV = row.getCell(1)?.toString()?.trim() ?: continue
                    val ngaySinh = row.getCell(2)?.toString()?.trim() ?: continue
                    val gioiTinh = row.getCell(3)?.toString()?.trim() ?: continue
                    val maLop = row.getCell(4)?.toString()?.trim() ?: continue

                    val svByMa = danhSachAllSV.firstOrNull { it.MaSinhVien == maSV }
                    if (svByMa != null) {
                        // Hiện dialog rồi chờ đóng mới tiếp
                        dialogState.errorMessage.value =
                            "Sinh viên $tenSV trùng MSSV với (${svByMa.TenSinhVien}), bỏ qua."

                        // Chờ đến khi dialog đóng
                        while (dialogState.errorMessage.value != null) {
                            delay(100)
                        }

                        continue
                    }

                    val sinhVien = SinhVien(
                        MaSinhVien = maSV,
                        TenSinhVien = tenSV,
                        NgaySinh = formatNgaySinhDB(ngaySinh),
                        GioiTinh = gioiTinh,
                        MaLop = maLop,
                        Email = "$maSV@caothang.edu.vn",
                        MatKhau = hashPasswordMD5(maSV),
                        MaLoaiTaiKhoan = 3,
                        TrangThai = 1
                    )
                    sinhVienViewModel.createSinhVien(sinhVien)
                }
            }

            inputStream.close()
            Toast.makeText(context, "Đã thêm danh sách sinh viên!", Toast.LENGTH_LONG).show()
        }
    } catch (e: Exception) {
        dialogState.errorMessage.value = "Lỗi khi đọc file Excel!"
        e.printStackTrace()
    }
}



