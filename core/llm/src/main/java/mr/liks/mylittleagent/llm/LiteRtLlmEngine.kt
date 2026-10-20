package mr.liks.mylittleagent.llm


import android.content.Context
import com.google.ai.edge.litertlm.Backend
import com.google.ai.edge.litertlm.Engine
import com.google.ai.edge.litertlm.EngineConfig
import com.google.ai.edge.litertlm.LogSeverity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import mr.liks.mylittleagent.llm.model.LlmConfig

/** Обёртка над LiteRT-LM Engine */
class LiteRtLlmEngine(
    private val context: Context,
    private val config: LlmConfig,
    private val modelFileManager: ModelFileManager,
) {
    private val initMutex = Mutex()
    private var engine: Engine? = null

    /**
     * Возвращает инициализированный Engine
     * При первом вызове загружает модель
     */
    suspend fun getOrCreateEngine(): Engine = initMutex.withLock {
        engine?.let { return it }

        // Загрузка модели в фоновом потоке
        withContext(Dispatchers.IO) {
            Engine.setNativeMinLogSeverity(LogSeverity.ERROR)

            val modelPath = modelFileManager.ensureModelReady(config.modelFileName)

            val backend = if (config.useGpu) Backend.GPU() else Backend.CPU()

            val engineConfig = EngineConfig(
                modelPath = modelPath,
                backend = backend,
                cacheDir = config.cacheDir ?: context.cacheDir.path,
            )

            val newEngine = Engine(engineConfig)
            newEngine.initialize()

            engine = newEngine
            newEngine
        }
    }

    /**
     * Создаёт новую сессию (Conversation) с опциональной историей.
     */
    suspend fun createSession(
        history: List<Pair<String, Boolean>> = emptyList(),
    ): LiteRtLlmSession {
        val engine = getOrCreateEngine()
        return LiteRtLlmSession(engine, config, history)
    }

    /**
     * Освобождает ресурсы движка
     */
    suspend fun close() {
        initMutex.withLock {
            engine?.close()
            engine = null
        }
    }
}