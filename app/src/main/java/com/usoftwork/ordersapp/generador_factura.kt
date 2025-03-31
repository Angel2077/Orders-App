import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.usoftwork.ordersapp.Compra
import java.io.File
import java.io.FileOutputStream

fun generaFacturaPDF(compra: Compra, context: Context) {
    // Crear documento PDF con tamaño A4 (595 x 842 puntos aproximadamente)
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    // Fondo blanco (opcional, en caso de que el canvas tenga otro color por defecto)
    canvas.drawColor(Color.WHITE)

    // Configuración del paint para el título
    val paint = Paint().apply {
        color = Color.BLACK
        textSize = 24f
        isFakeBoldText = true
        textAlign = Paint.Align.CENTER
    }

    // Título centrado en la parte superior
    canvas.drawText("Factura de Compra", pageInfo.pageWidth / 2f, 50f, paint)

    // Línea separadora
    paint.strokeWidth = 2f
    paint.textSize = 16f
    paint.isFakeBoldText = false
    paint.textAlign = Paint.Align.LEFT
    canvas.drawLine(20f, 70f, pageInfo.pageWidth - 20f, 70f, paint)

    // Contenido de la factura con espaciado uniforme
    val startY = 100f
    val lineSpacing = 25f

    canvas.drawText("Producto: ${compra.nombre}", 20f, startY, paint)
    canvas.drawText("Cantidad: ${compra.cantidad}", 20f, startY + lineSpacing, paint)
    canvas.drawText("Precio Unitario: ${compra.precioUnitario} CLP", 20f, startY + 2 * lineSpacing, paint)
    canvas.drawText("Total: ${compra.total} CLP", 20f, startY + 3 * lineSpacing, paint)
    canvas.drawText("Fecha: ${compra.fecha}", 20f, startY + 4 * lineSpacing, paint)

    // Finalizar la página
    pdfDocument.finishPage(page)

    try {
        // Guardar el PDF en la carpeta Descargas
        val filePath = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "Factura_${compra.nombre}.pdf"
        )
        if (!filePath.parentFile?.exists()!!) {
            filePath.parentFile?.mkdirs()
        }
        pdfDocument.writeTo(FileOutputStream(filePath))
        pdfDocument.close()

        Toast.makeText(context, "Factura generada en ${filePath.path}", Toast.LENGTH_LONG).show()

        // Obtener URI segura a través de FileProvider
        val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", filePath)

        // Abrir el PDF generado
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        context.startActivity(intent)

    } catch (e: Exception) {
        Toast.makeText(context, "Error al generar PDF: ${e.message}", Toast.LENGTH_LONG).show()
    }
}
