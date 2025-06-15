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
    val qrSize = 85 // ~3cm
    val spacing = 24
    val columns = 5
    var currentX = padding
    var currentY = padding
    var pageNumber = 1

    // Vẽ tiêu đề
    val titlePaint = Paint().apply {
        textSize = 20f
        isFakeBoldText = true
        textAlign = Paint.Align.CENTER
        color = Color.BLACK
    }

    // Vẽ tên máy
    val paintTenMay = Paint().apply {
        textAlign = Paint.Align.CENTER
        textSize = 16f
        isFakeBoldText = true
        color = Color.BLACK
    }

    // Viền QR
    val borderPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        color = Color.BLACK
    }

    val pdfDocument = PdfDocument()
    var pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
    var page = pdfDocument.startPage(pageInfo)
    var canvas = page.canvas

    // Vẽ tiêu đề
    canvas.drawText("QR máy phòng: $tenphong", (pageWidth / 2).toFloat(), (padding).toFloat(), titlePaint)
    currentY = padding + 40 // dời sau tiêu đề

    danhSachMay.forEachIndexed { index, may ->
        try {
            val qrBitmap = base64ToBitmap(may.QRCode)
            val resizedQR = Bitmap.createScaledBitmap(qrBitmap, qrSize, qrSize, false)

            // Vẽ QR
            canvas.drawBitmap(resizedQR, currentX.toFloat(), currentY.toFloat(), null)

            // Vẽ viền quanh QR
            canvas.drawRect(
                currentX.toFloat(),
                currentY.toFloat(),
                (currentX + qrSize).toFloat(),
                (currentY + qrSize).toFloat(),
                borderPaint
            )

            // Vẽ tên máy dưới QR
            canvas.drawText(
                may.TenMay,
                (currentX + qrSize / 2).toFloat(),
                (currentY + qrSize + 20).toFloat(),
                paintTenMay
            )

            // Cập nhật vị trí
            currentX += qrSize + spacing
            if ((index + 1) % columns == 0) {
                currentX = padding
                currentY += qrSize + 50
            }

            // Nếu vượt trang
            if (currentY + qrSize + 50 > pageHeight - padding) {
                pdfDocument.finishPage(page)
                pageNumber++
                pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                canvas.drawText("QR máy phòng: $tenphong", (pageWidth / 2).toFloat(), (padding).toFloat(), titlePaint)
                currentY = padding + 40
                currentX = padding
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



