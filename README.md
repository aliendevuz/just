[![official JetBrains project](https://jb.gg/badges/official-plastic.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)

# Telegram Bot - Kotlin/Native Webhook Handler

Sof Kotlin/Native - hech qanday framework yo'q. Faqat webhook handler.

## 🚀 Build

```bash
./gradlew buildBootstrap
```

**Output**: `build/bin/telegram/releaseExecutable/bootstrap`

## 🔧 Setup

1. **Token**: `src/nativeMain/kotlin/Main.kt` faylida BOT_TOKEN o'rnating
2. **Compile**: `./gradlew buildBootstrap`
3. **Deploy**: Bootstrap faylni serverga yuklang
4. **Webhook**: Telegram-ga webhook URL registr qiling

## 📝 Komandalari

- `/start` - Salomlashish
- `/help` - Yordam
- `/echo TEXT` - Ekho
- Boshqa - Info

## 🧪 Test

```bash
./gradlew buildBootstrap
./build/bin/telegram/releaseExecutable/bootstrap --test
```

---

**Minimal Kotlin/Native webhook bot**

## 🚀 Tez Boshlash

### 1. Bot Tokenini Olish
1. Telegram-da `@BotFather`-ga xabar yuboring
2. `/newbot` buyrug'ini kiritib yangi bot yarating
3. Bot tokenini copy qiling

### 2. Kodni Sozlash
`src/jvmMain/kotlin/TelegramBot.kt` faylida tokenni o'rnating:
```kotlin
const val BOT_TOKEN = "YOUR_BOT_TOKEN_HERE"
```

### 3. Build Qilish
```bash
./gradlew createFatJar
```

Output: `build/libs/telegram-bot-fat.jar`

### 4. Serverda Run Qilish

#### Local test uchun:
```bash
java -jar build/libs/telegram-bot-fat.jar
```

Server quyidagi portlarda ishga tushadi:
- **HTTP**: `http://localhost:8080`
- **Webhook**: `http://localhost:8080/webhook`
- **Health**: `http://localhost:8080/health`

### 5. Test Qilish

```bash
# Webhook-ni check qilish
curl http://localhost:8080/health

# Response:
# {"status":"alive","bot":"Telegram"}
```

## 📋 Bot Komandalari

| Buyruq | Tarif |
|--------|-------|
| `/start` | Salomlashish |
| `/help` | Yordam ko'rsatish |
| Boshqa xabarlar | Echo orqali qaytarish |

## 📁 Fayl Tuzilishi

```
src/jvmMain/kotlin/
├── TelegramModels.kt    # Data classes
└── TelegramBot.kt       # Main bot handler
```

## ✨ Features

✅ Webhook-based (polling yo'q)
✅ Minimal va tez
✅ Error handling
✅ Logging
✅ Async support

---

Telegram bot-ingizni boshlang! 🚀

