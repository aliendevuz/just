
data class Message(
    val messageId: Int,
    val chatId: Long,
    val text: String,
    val firstName: String
)

fun base64Decode(input: String): String {
    val table = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
    val padding = '='

    val cleanInput = input.trim().replace("\n", "")
    val output = StringBuilder()

    var buffer = 0
    var bitsCollected = 0

    for (c in cleanInput) {
        if (c == padding) break
        val value = table.indexOf(c)
        if (value == -1) continue

        buffer = (buffer shl 6) or value
        bitsCollected += 6

        if (bitsCollected >= 8) {
            bitsCollected -= 8
            output.append(((buffer shr bitsCollected) and 0xFF).toChar())
        }
    }
    return output.toString()
}

fun unescapeJsonString(escaped: String): String {
    var result = escaped
    
    // Order matters! Process in correct sequence
    // 1. First, handle escaped backslashes
    result = result.replace("\\\\", "\u0000")  // temporary placeholder for \\
    
    // 2. Then handle other escaped sequences
    result = result.replace("\\\"", "\"")
    result = result.replace("\\n", "\n")
    result = result.replace("\\r", "\r")
    result = result.replace("\\t", "\t")
    result = result.replace("\\/", "/")
    result = result.replace("\\b", "\b")
    
    // 3. Finally, restore backslashes
    result = result.replace("\u0000", "\\")
    
    return result
}

fun extractBodyFromApiGateway(eventJson: String): String {
    // API Gateway HTTP API (v2) formatidan body ni olish
    // Pattern: "body":"value" where value can have escaped characters
    val bodyRegex = """"body"\s*:\s*"((?:[^"\\]|\\.)*?)"""".toRegex()
    val base64Regex = """"isBase64Encoded"\s*:\s*(true|false)""".toRegex()

    val bodyEscaped = bodyRegex.find(eventJson)?.groupValues?.get(1)
        ?: return eventJson

    val isBase64 = base64Regex.find(eventJson)?.groupValues?.get(1)?.toBooleanStrictOrNull() ?: false

    if (isBase64) {
        return base64Decode(bodyEscaped.replace("\n", "").replace("\r", ""))
    }

    // Escaped JSON stringni to'g'ri ochish
    return unescapeJsonString(bodyEscaped)
}

fun parseJsonUpdate(jsonString: String): Message? {
    // Regex patterns to extract fields from JSON
    val messageIdRegex = """"message_id"\s*:\s*(\d+)""".toRegex()
    val chatIdRegex    = """"id"\s*:\s*(\d+)""".toRegex()
    val textRegex      = """"text"\s*:\s*"((?:[^"\\]|\\.)*?)"""".toRegex()
    val firstNameRegex = """"first_name"\s*:\s*"((?:[^"\\]|\\.)*?)"""".toRegex()

    // message_id ni olish
    val messageId = messageIdRegex.find(jsonString)?.groupValues?.get(1)?.toIntOrNull() 
        ?: return null

    // "chat" blockini topish va uning ichidan "id" ni olish
    val chatBlock = """"chat"\s*:\s*\{[^}]+}""".toRegex().find(jsonString)?.value 
    
    val chatId = if (chatBlock != null) {
        chatIdRegex.find(chatBlock)?.groupValues?.get(1)?.toLongOrNull()
    } else {
        null
    } ?: return null
    
    // text ni olish
    val textMatchRaw = textRegex.find(jsonString)?.groupValues?.get(1) 
        ?: return null
    
    val text = unescapeJsonString(textMatchRaw)
    
    // first_name ni olish (ixtiyoriy)
    val firstNameMatchRaw = firstNameRegex.find(jsonString)?.groupValues?.get(1)
    val firstName = if (firstNameMatchRaw != null) {
        unescapeJsonString(firstNameMatchRaw)
    } else {
        "User"
    }

    return Message(messageId, chatId, text, firstName)
}

fun getResponseText(text: String): String {
    return when {
        text.startsWith("/start") -> "Assalomu aleykum! Telegram Bot (Kotlin/Native)"
        text.startsWith("/help") -> "/start - Boshlash\n/help - Yordam\n/echo TEXT - Ekho"
        text.startsWith("/echo") -> "Echo: ${text.removePrefix("/echo").trim()}"
        else -> "Received: $text\n\n/help yuboring"
    }
}

const val BOT_TOKEN = "7605219483:AAGDHcc-MKlH3fJLQkkJh1TSXkc5h-vArOo"

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("{\"statusCode\": 400, \"body\": \"No event provided\"}")
        return
    }
    
    var eventJson = args[0]
    
    // Agar event string escaped bo'lsa (environment variable orqali o'tsa), ochish
    eventJson = unescapeJsonString(eventJson)
    
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
