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
    // Tamaño de página adaptado para impresoras de boletas (puedes ajustar según la impresora)
    val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
    val pdfDocument = PdfDocument()
    val page = pdfDocument.startPage(pageInfo)
    val canvas = page.canvas

    // Fondo blanco para asegurar la buena legibilidad
    canvas.drawColor(Color.WHITE)

    // Configuración del paint para título y contenido
    val paint = Paint().apply {
        color = Color.BLACK
        textSize = 18f  // Tamaño reducido para boletas
        isFakeBoldText = true
        textAlign = Paint.Align.CENTER
    }

    // Título centrado
    canvas.drawText("Factura", pageInfo.pageWidth / 2f, 30f, paint)

    // Línea separadora para separar el título del contenido
    paint.strokeWidth = 1f
    paint.isFakeBoldText = false
    paint.textAlign = Paint.Align.LEFT
    canvas.drawLine(10f, 40f, pageInfo.pageWidth - 10f, 40f, paint)

    // Ajustar tamaño de fuente para el contenido
    paint.textSize = 14f

    // Contenido de la factura con espaciado reducido
    val startY = 60f
    val lineSpacing = 20f

    canvas.drawText("Producto: ${compra.nombre}", 10f, startY, paint)
    canvas.drawText("Cant: ${compra.cantidad}", 10f, startY + lineSpacing, paint)
    canvas.drawText("Precio: ${compra.precioUnitario} CLP", 10f, startY + 2 * lineSpacing, paint)
    canvas.drawText("Total: ${compra.total} CLP", 10f, startY + 3 * lineSpacing, paint)
    canvas.drawText("Fecha: ${compra.fecha}", 10f, startY + 4 * lineSpacing, paint)

    pdfDocument.finishPage(page)

    try {
        // Guardar el PDF en la carpeta Descargas (verificar permisos en Android 10+)
        val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val filePath = File(downloadsDir, "Factura_${compra.nombre}.pdf")

        // Asegúrate de que el directorio existe antes de escribir
        filePath.parentFile?.takeIf { !it.exists() }?.mkdirs()

        pdfDocument.writeTo(FileOutputStream(filePath))
        pdfDocument.close()

        Toast.makeText(context, "Factura generada en ${filePath.path}", Toast.LENGTH_LONG).show()

        // Obtener URI segura con FileProvider
        val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", filePath)

        // Intent para abrir el PDF
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        context.startActivity(intent)

    } catch (e: Exception) {
        Toast.makeText(context, "Error al generar PDF: ${e.message}", Toast.LENGTH_LONG).show()
    }
}
