import android.content.Context
import android.net.Uri
import android.widget.Toast
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream

suspend fun parseExcelFileAndImport(
    context: Context,
    fileUri: Uri,
    giangVienViewModel: GiangVienViewModel
) {
    // Lấy danh sách giảng viên hiện tại một lần duy nhất
    val danhSachAllGV = giangVienViewModel.danhSachAllGiangVien

    try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(fileUri)
        if (inputStream != null) {
            val workbook = WorkbookFactory.create(inputStream)
            val sheet = workbook.getSheetAt(0)
            for (rowIndex in 1..sheet.lastRowNum) { // row 0 = header
                val row = sheet.getRow(rowIndex)
                if (row != null) {
                    val maGV = row.getCell(0)?.toString()?.trim() ?: continue
                    val tenGV = row.getCell(1)?.toString()?.trim() ?: continue
                    val ngaySinh = row.getCell(2)?.toString()?.trim() ?: continue
                    val gioiTinh = row.getCell(3)?.toString()?.trim() ?: continue
                    val email = row.getCell(4)?.toString()?.trim() ?: continue

                    // Kiểm tra trùng mã GV hoặc email bằng list đã lấy!
                    val gvByMa = danhSachAllGV.firstOrNull { it.MaGV == maGV }
                    val gvByEmail = danhSachAllGV.firstOrNull { it.Email == email }
                    if (gvByMa != null || gvByEmail != null) {
                        val message = when {
                            gvByMa != null && gvByEmail != null ->
                                "Giảng viên $tenGV bị trùng Mã GV với (${gvByMa.TenGiangVien}) và trùng Email với (${gvByEmail.TenGiangVien})! Bỏ qua."

                            gvByMa != null ->
                                "Giảng viên $tenGV bị trùng Mã GV với (${gvByMa.TenGiangVien})! Bỏ qua."

                            gvByEmail != null ->
                                "Giảng viên $tenGV bị trùng Email với (${gvByEmail.TenGiangVien})! Bỏ qua."

                            else -> ""
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        continue
                    }
                    val giangVien = GiangVien(
                        MaGV = maGV,
                        TenGiangVien = tenGV,
                        NgaySinh = ngaySinh,
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
            Toast.makeText(
                context,
                "Đã thêm danh sách giảng viên!",
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

