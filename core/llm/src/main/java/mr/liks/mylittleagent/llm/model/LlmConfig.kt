package mr.liks.mylittleagent.llm.model

/**
 * Конфигурация для LiteRT-LM движка
 */
data class LlmConfig(
    val modelFileName: String = "Qwen2.5-Coder-1.5B-Instruct_int4.litertlm",
    val useGpu: Boolean = true,
    val maxNumTokens: Int? = null, // null = дефолт из модели
    val cacheDir: String? = null,
    val systemInstruction: String = "You are a helpful coding assistant.",
)