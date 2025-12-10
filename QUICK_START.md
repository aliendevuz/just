# Telegram Bot - Quick Start Guide 🚀

## 1️⃣ Bot Token Olish (2 minutasi)

### Step 1: @BotFather-ga kirib, yangi bot yaratish
```
Telegram Search: @BotFather
/newbot
Name: My Awesome Bot
Username: my_awesome_bot_1234
```

### Step 2: Token-ni copy qiling
```
Token: 6234567890:ABCDEFGHIjklmnopqrstuvwxyz123456789
```

## 2️⃣ Kodni Sozlash (1 minutasi)

**File**: `src/jvmMain/kotlin/TelegramBot.kt`

```kotlin
// Line 14 - Token-ni o'rnating
const val BOT_TOKEN = "6234567890:ABCDEFGHIjklmnopqrstuvwxyz123456789"
```

## 3️⃣ Build Qilish (10 sekundasi)

```bash
./gradlew createFatJar
```

**Output**: `build/libs/telegram-bot-fat.jar` ✅

## 4️⃣ Run Qilish

```bash
java -jar build/libs/telegram-bot-fat.jar
```

**Output**:
```
[BOT] Starting Telegram bot webhook server...
[BOT] Listening on http://0.0.0.0:8080/webhook
```

## 5️⃣ Telegram-ga Webhook Registr Qilish

### Local test (ngrok bilan):
```bash
# ngrok-ni install qiling va run qiling
ngrok http 8080

# Natija: https://abc123.ngrok.io

# Token o'rnating va webhook qilish:
TOKEN="6234567890:ABCDEFGHIjklmnopqrstuvwxyz123456789"
curl -X POST https://api.telegram.org/bot${TOKEN}/setWebhook \
  -F "url=https://abc123.ngrok.io/webhook"
```

### Production (own domain):
```bash
TOKEN="YOUR_TOKEN"
DOMAIN="your-domain.com"

curl -X POST https://api.telegram.org/bot${TOKEN}/setWebhook \
  -F "url=https://${DOMAIN}/webhook"
```

## 6️⃣ Test Qilish

### Option 1: Telegram-da bot-ga xabar yuboring
1. Telegram-da bot-ni find qiling
2. `/start` buyrug'ini yuboring
3. "Assalomu aleykum!" javob olish kerak ✅

### Option 2: curl bilan test
```bash
# Health check
curl http://localhost:8080/health

# Response:
# {"status":"alive","bot":"Telegram"}
```

## 📋 Bot Buyruqlari

| Komanda | Javob |
|---------|-------|
| `/start` | Assalomu aleykum! 👋 |
| `/help` | Yordam ma'lumoti |
| Boshqa xabar | Echo qilish |

## 🔧 Kustomizatsiya

### Yangi komanda qo'shish

**File**: `src/jvmMain/kotlin/TelegramBot.kt`

**Function**: `handleUpdate()` tarkibida qo'shish:

```kotlin
// /hello buyrug'i uchun
text.startsWith("/hello") -> {
    val response = "Assalomu aleykum! 👋 Salom! 😊"
    sendMessage(chatId, response)
}
```

Keyin rebuild qiling:
```bash
./gradlew createFatJar
java -jar build/libs/telegram-bot-fat.jar
```

## 🌐 Deploy Qilish

### VPS (Linux):
```bash
# 1. Server-ga kiring
ssh user@server.com

# 2. Code-ni clone qiling
git clone https://github.com/your-repo.git
cd telegram-bot

# 3. Build qiling
./gradlew createFatJar

# 4. nohup bilan background-da run qiling
nohup java -jar build/libs/telegram-bot-fat.jar > bot.log 2>&1 &

# 5. Nginx reverse proxy (HTTPS) qo'shish
sudo nano /etc/nginx/sites-available/telegram-bot

# NGINX config:
server {
    listen 443 ssl;
    server_name your-domain.com;
    
    ssl_certificate /path/to/cert.pem;
    ssl_certificate_key /path/to/key.pem;
    
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

### Docker:
```dockerfile
FROM openjdk:17-jdk-alpine
COPY build/libs/telegram-bot-fat.jar /app/bot.jar
ENTRYPOINT ["java", "-jar", "/app/bot.jar"]
```

```bash
docker build -t telegram-bot .
docker run -p 8080:8080 telegram-bot
```

## 📊 Architecture

```
User sends /start
        ↓
    Telegram
        ↓ (HTTPS webhook)
Your Server (POST /webhook)
        ↓
    handleUpdate()
        ↓ (Process command)
    sendMessage()
        ↓ (HTTPS to Telegram API)
    Telegram API
        ↓
    Message sent to user
```

## 🐛 Troubleshooting

### Bot javob bermaydi?
1. Bot token-ni check qiling
2. Webhook-ni verify qiling: `curl -X POST https://api.telegram.org/botTOKEN/getWebhookInfo`
3. Server logs-ni check qiling

### "Address already in use" error?
```bash
# Windows-da port free qilish
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux-da
lsof -i :8080
kill -9 <PID>
```

### HTTPS muammosi (local test)?
ngrok ishlatish:
```bash
ngrok http 8080
```

## 📚 Resources

- [Telegram Bot API Docs](https://core.telegram.org/bots/api)
- [BotFather Guide](https://core.telegram.org/bots#botfather)
- [Webhook Best Practices](https://core.telegram.org/bots/webhooks)

## ✅ Checklist

- [ ] Bot token olingan
- [ ] Token code-ga qo'yilgan
- [ ] JAR build qilishga muvaffaq
- [ ] Server ishga tushgan
- [ ] Webhook registr qilishga muvaffaq
- [ ] Telegram-da test qilishga muvaffaq
- [ ] Deploy qilishga tayyor

---

**Bottaging muvaffaqiyat! Bot ishga tushdi! 🎉**

Qiziqish bo'lsa, quyidagi kustomizatsiyalarni qo'shishingiz mumkin:
- Database-ga user info saqalash
- NLP bilan intent recognition
- Webhook rate limiting
- Admin panel
- Database integration (PostgreSQL, MongoDB)

**Happy botting! 🤖**
