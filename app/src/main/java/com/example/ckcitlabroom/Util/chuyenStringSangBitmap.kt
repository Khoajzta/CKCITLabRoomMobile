import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Base64
import android.widget.Toast
import java.io.File
import java.io.FileOutputStream



fun base64ToBitmap(base64Str: String): Bitmap {
    val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
}

fun createPdfWithQRCodeBase64(
    context: Context,
    tenphong: String,
    danhSachMay: List<MayTinh>,
    fileName: String = "QR_Phong_May_${tenphong}.pdf"
) {
    val pageWidth = 595 // A4 ngang (points)
    val pageHeight = 842 // A4 dọc
    val padding = 32
    val qrSize = 85
    val columns = 4
    val spacingY = 60
    var currentY = padding + 40
    var pageNumber = 1

    val titlePaint = Paint().apply {
        textSize = 20f
        isFakeBoldText = true
        textAlign = Paint.Align.CENTER
        color = Color.BLACK
    }

    val paintText = Paint().apply {
        textAlign = Paint.Align.CENTER
        textSize = 14f
        isFakeBoldText = true
        color = Color.BLACK
    }

    val borderPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        color = Color.BLACK
    }

    val cellWidth = (pageWidth - 2 * padding) / columns

    val pdfDocument = PdfDocument()
    var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
    var page = pdfDocument.startPage(pageInfo)
    var canvas = page.canvas

    // Vẽ tiêu đề
    canvas.drawText("QR máy: $tenphong", (pageWidth / 2).toFloat(), padding.toFloat(), titlePaint)

    danhSachMay.forEachIndexed { index, may ->
        try {
            val qrBitmap = base64ToBitmap(may.QRCode)
            val resizedQR = Bitmap.createScaledBitmap(qrBitmap, qrSize, qrSize, false)

            val col = index % columns
            val rowXStart = padding + col * cellWidth
            val centerX = (rowXStart + cellWidth / 2).toFloat()
            val qrLeft = centerX - qrSize / 2f

            // Vẽ mã máy (ở trên QR)
            canvas.drawText(
                may.MaMay ?: "",
                centerX,
                (currentY - 10).toFloat(),
                paintText
            )

            // Vẽ mã QR
            canvas.drawBitmap(resizedQR, qrLeft, currentY.toFloat(), null)

            // Vẽ viền QR
            canvas.drawRect(
                qrLeft,
                currentY.toFloat(),
                (qrLeft + qrSize).toFloat(),
                (currentY + qrSize).toFloat(),
                borderPaint
            )

            // Vẽ vị trí máy (dưới QR)
            canvas.drawText(
                may.ViTri,
                centerX,
                (currentY + qrSize + 20).toFloat(),
                paintText
            )

            // Nếu đã hết hàng, chuyển xuống dòng mới
            if ((index + 1) % columns == 0) {
                currentY += qrSize + spacingY
            }

            // Nếu vượt quá chiều cao trang
            if (currentY + qrSize + spacingY > pageHeight - padding) {
                pdfDocument.finishPage(page)
                pageNumber++
                pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                canvas.drawText("QR máy phòng: $tenphong", (pageWidth / 2).toFloat(), padding.toFloat(), titlePaint)
                currentY = padding + 40
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    pdfDocument.finishPage(page)

    val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val file = File(downloadsDir, fileName)
    pdfDocument.writeTo(FileOutputStream(file))
    pdfDocument.close()

    Toast.makeText(context, "Đã lưu tại: ${file.absolutePath}", Toast.LENGTH_LONG).show()
}








