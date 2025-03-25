package com.example.myapplication.ui.theme

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

fun generarFacturaPDF(context: Context, nombreArchivo: String, contenido: String): File? {
    // Verificar que el contenido no esté vacío
    if (contenido.isEmpty()) {
        println("El contenido está vacío, no se generará el PDF.")
        return null
    }

    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(300, 600, 1).create()
    val page = pdfDocument.startPage(pageInfo)

    val canvas = page.canvas
    val paint = android.graphics.Paint()
    paint.textSize = 14f

    // Escribir el contenido en la página
    canvas.drawText(contenido, 10f, 25f, paint)

    pdfDocument.finishPage(page)

    // Directorio para guardar el archivo
    val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Facturas")
    if (!directory.exists()) {
        directory.mkdirs()
    }

    val file = File(directory, "$nombreArchivo.pdf")
    return try {
        // Escribir el documento en el archivo
        val outputStream = FileOutputStream(file)
        pdfDocument.writeTo(outputStream)
        pdfDocument.close()
        outputStream.close()
        println("PDF generado con éxito en: ${file.absolutePath}")
        file
    } catch (e: IOException) {
        e.printStackTrace()
        pdfDocument.close()
        null
    }
}
