import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatNgay(ngay: String): String {
    return try {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val date = LocalDate.parse(ngay, inputFormatter)
        date.format(outputFormatter)
    } catch (e: Exception) {
        ngay
    }
}

fun formatNgaySinhDB(ngaySinhStr: String): String {
    val possibleFormats = listOf(
        "d/M/yyyy",
        "dd/MM/yyyy",
        "yyyy-MM-dd",
        "MM/dd/yyyy",
        "yyyy/MM/dd",
        "dd-MM-yyyy",
        "d-M-yyyy",
        "d/M/yy",
        "dd/MM/yy",
        "MM/dd/yy",
    )

    for (format in possibleFormats) {
        try {
            val dateFormat = SimpleDateFormat(format, Locale.getDefault())
            dateFormat.isLenient = false
            val date = dateFormat.parse(ngaySinhStr)
            if (date != null) {
                val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                return outputFormat.format(date)
            }
        } catch (_: Exception) {
            // bỏ qua nếu không parse được format này
        }
    }

    // Nếu không khớp format nào thì trả về nguyên chuỗi để hiển thị lỗi sau
    return ngaySinhStr
}

