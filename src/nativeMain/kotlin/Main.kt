// ============ AWS Lambda Runtime API Handler (Kotlin/Native) ============

data class Message(
    val messageId: Int,
    val chatId: Long,
    val text: String,
    val firstName: String
)

fun extractBodyFromApiGateway(eventJson: String): String {
    // API Gateway V2 format-dan "body" fieldni extract qilish
    val bodyRegex = """"body"\s*:\s*"([^"]+(?:\\.[^"]*)*)"(?:\s*,|\s*})""".toRegex()
    val match = bodyRegex.find(eventJson)
    
    if (match != null) {
        var body = match.groupValues[1]
        // Escaped characters-ni decode qilish
        body = body.replace("\\\"", "\"")
        body = body.replace("\\\\", "\\")
        body = body.replace("\\n", "\n")
        return body
    }
    
    // Agar API Gateway V2 format bo'lmasa, to'g'ridan-to'g'ri Telegram JSON bo'ladi
    return eventJson
}

fun parseJsonUpdate(jsonString: String): Message? {
    return try {
        val messageIdRegex = """"message_id"\s*:\s*(\d+)""".toRegex()
        val chatIdRegex = """"id"\s*:\s*(\d+)""".toRegex()
        val textRegex = """"text"\s*:\s*"([^"]+)""".toRegex()
        val firstNameRegex = """"first_name"\s*:\s*"([^"]+)""".toRegex()
        
        val chatId = chatIdRegex.find(jsonString)?.groupValues?.get(1)?.toLongOrNull() ?: return null
        val messageId = messageIdRegex.find(jsonString)?.groupValues?.get(1)?.toIntOrNull() ?: return null
        val text = textRegex.find(jsonString)?.groupValues?.get(1) ?: return null
        val firstName = firstNameRegex.find(jsonString)?.groupValues?.get(1) ?: "User"
        
        Message(messageId, chatId, text, firstName)
    } catch (e: Exception) {
        null
    }
}

fun getResponseText(text: String): String {
    return when {
        text.startsWith("/start") -> "Assalomu aleykum! Telegram Bot (Kotlin/Native)"
        text.startsWith("/help") -> "/start - Boshlash\n/help - Yordam\n/echo TEXT - Ekho"
        text.startsWith("/echo") -> "Echo: ${text.removePrefix("/echo").trim()}"
        else -> "Received: $text\n\n/help yuboring"
    }
}

// ============ Telegram Message Processing ============

// ============ Main Entry Point ============

const val BOT_TOKEN = "7605219483:AAGDHcc-MKlH3fJLQkkJh1TSXkc5h-vArOo"

fun main(args: Array<String>) {
    // Argument-dan event JSON olish (bootstrap script-dan)
    if (args.isEmpty()) {
        println("{\"statusCode\": 400, \"body\": \"No event provided\"}")
        return
    }
    
    val eventJson = args[0]
    
    // API Gateway V2 format-dan Telegram JSON extract qilish
    val telegramJson = extractBodyFromApiGateway(eventJson)
    
    val message = parseJsonUpdate(telegramJson)
    if (message != null) {
        val response = getResponseText(message.text)
        println("{\"statusCode\": 200, \"body\": \"$response\"}")
    } else {
        println("{\"statusCode\": 400, \"body\": \"Invalid message\"}")
    }
}
