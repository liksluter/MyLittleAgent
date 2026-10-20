package mr.liks.mylittleagent.llm

import android.content.Context
import java.io.File

/**
 * Отвечает за подготовку файла модели в filesDir
 */
class ModelFileManager(
    private val context: Context,
) {
    fun ensureModelReady(fileName: String): String {
        val targetFile = File(context.filesDir, fileName)

        if (targetFile.exists()) {
            return targetFile.absolutePath
        }

        // Копируем из assets при первом запуске
        context.assets.open(fileName).use { input ->
            targetFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        return targetFile.absolutePath
    }
}