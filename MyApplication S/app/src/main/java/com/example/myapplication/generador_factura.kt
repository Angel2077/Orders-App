import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.myapplication.Compra
import java.io.File
import java.io.FileOutputStream

fun generaFacturaPDF(compra: Compra, context: Context) {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
    val page = pdfDocument.startPage(pageInfo)

    val canvas = page.canvas
    val paint = Paint()
    paint.textSize = 16f

    // Contenido de la factura
    canvas.drawText("Factura de Compra", 80f, 50f, paint)
    canvas.drawText("Producto: ${compra.nombre}", 20f, 80f, paint)
    canvas.drawText("Cantidad: ${compra.cantidad}", 20f, 100f, paint)
    canvas.drawText("Precio Unitario: ${compra.precioUnitario} CLP", 20f, 120f, paint)
    canvas.drawText("Total: ${compra.total} CLP", 20f, 140f, paint)
    canvas.drawText("Fecha: ${compra.fecha}", 20f, 160f, paint)

    pdfDocument.finishPage(page)

    try {
        // Guardamos el archivo en la carpeta Descargas
        val filePath = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Factura_${compra.nombre}.pdf")
        if (!filePath.parentFile?.exists()!!) {
            filePath.parentFile?.mkdirs()
        }
        pdfDocument.writeTo(FileOutputStream(filePath))
        pdfDocument.close()

        // Mostrar mensaje de éxito
        Toast.makeText(context, "Factura generada en ${filePath.path}", Toast.LENGTH_LONG).show()

        // Obtener URI segura a través de FileProvider
        val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", filePath)

        // Abrir el PDF generado
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "application/pdf")
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)  // Permiso de lectura de URI
        context.startActivity(intent)

    } catch (e: Exception) {
        Toast.makeText(context, "Error al generar PDF: ${e.message}", Toast.LENGTH_LONG).show()
    }
}

