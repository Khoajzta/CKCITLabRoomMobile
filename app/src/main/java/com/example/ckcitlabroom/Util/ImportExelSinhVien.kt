import android.content.Context
import android.net.Uri
import android.widget.Toast
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream

suspend fun parseExcelFileAndImportSinhVien(
    context: Context,
    fileUri: Uri,
    sinhVienViewModel: SinhVienViewModel
) {
    // Lấy danh sách sinh viên hiện tại một lần duy nhất
    val danhSachAllSV = sinhVienViewModel.danhSachAllSinhVien

    try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(fileUri)
        if (inputStream != null) {
            val workbook = WorkbookFactory.create(inputStream)
            val sheet = workbook.getSheetAt(0)

            val headerRow = sheet.getRow(0)
            val expectedColumns = 6
            if (headerRow == null || headerRow.physicalNumberOfCells < expectedColumns) {
                Toast.makeText(
                    context,
                    "File Excel không đúng định dạng hoặc thiếu cột!",
                    Toast.LENGTH_LONG
                ).show()
                inputStream.close()
                return
            }

            for (rowIndex in 1..sheet.lastRowNum) { // row 0 = header
                val row = sheet.getRow(rowIndex)
                if (row != null) {
                    // Nếu dòng này thiếu cột, bỏ qua luôn
                    if (row.physicalNumberOfCells < expectedColumns) {
                        Toast.makeText(
                            context,
                            "Dòng ${rowIndex + 1} thiếu dữ liệu! Bỏ qua.",
                            Toast.LENGTH_SHORT
                        ).show()
                        continue
                    }
                    val maSV = row.getCell(0)?.toString()?.trim() ?: continue
                    val tenSV = row.getCell(1)?.toString()?.trim() ?: continue
                    val ngaySinh = row.getCell(2)?.toString()?.trim() ?: continue
                    val gioiTinh = row.getCell(3)?.toString()?.trim() ?: continue
                    val maLop = row.getCell(4)?.toString()?.trim() ?: continue
                    val email = row.getCell(5)?.toString()?.trim() ?: continue

                    // Kiểm tra trùng mã SV hoặc email bằng list đã lấy!
                    val svByMa = danhSachAllSV.firstOrNull { it.MaSinhVien == maSV }
                    val svByEmail = danhSachAllSV.firstOrNull { it.Email == email }
                    if (svByMa != null || svByEmail != null) {
                        val message = when {
                            svByMa != null && svByEmail != null ->
                                "Sinh viên $tenSV bị trùng Mã SV với (${svByMa.TenSinhVien}) và trùng Email với (${svByEmail.TenSinhVien})! Bỏ qua."

                            svByMa != null ->
                                "Sinh viên $tenSV bị trùng Mã SV với (${svByMa.TenSinhVien})! Bỏ qua."

                            svByEmail != null ->
                                "Sinh viên $tenSV bị trùng Email với (${svByEmail.TenSinhVien})! Bỏ qua."

                            else -> ""
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        continue
                    }
                    val sinhVien = SinhVien(
                        MaSinhVien = maSV,
                        TenSinhVien = tenSV,
                        NgaySinh = ngaySinh,
                        GioiTinh = gioiTinh,
                        Email = email,
                        MaLop = maLop,
                        MatKhau = hashPasswordMD5(maSV),
                        MaLoaiTaiKhoan = 3,
                        TrangThai = 1
                    )
                    sinhVienViewModel.createSinhVien(sinhVien)
                }
            }
            inputStream.close()
            Toast.makeText(
                context,
                "Đã thêm danh sách sinh viên!",
                Toast.LENGTH_LONG
            ).show()
        }
    } catch (e: Exception) {
        Toast.makeText(
            context,
            "Lỗi khi đọc file Excel!",
            Toast.LENGTH_LONG
        ).show()
        e.printStackTrace()
    }
}
