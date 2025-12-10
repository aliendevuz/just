# AWS Lambda Kotlin/Native Handler - Complete Setup ✅

## Sizning uchun Tayyorlangan Narsalar

### 1. **Bootstrap Fayli** (`bootstrap`)
- AWS Lambda Runtime API bilan integrate bo'lgan bash script
- Event loop-da Lambda-dan events qabul qiladi
- Response-ni qaytaradi
- Location: `build/lambda/bootstrap`

### 2. **Kotlin Handler Implementation**

#### Main Handler (`LambdaHandler.kt`)
```kotlin
fun handleRequest(event: String): LambdaResponse {
    // Event'ni process qilish
    // Kotlin serialization bilan JSON parse qilish
    return LambdaResponse(statusCode = 200, body = "...")
}
```

#### Example Handler (`ApiGatewayExample.kt`)
- API Gateway events uchun example
- GET, POST, PUT, DELETE request handling

### 3. **Build Configuration** (`build.gradle.kts`)
- Kotlin/Native linuxX64 target
- kotlinx-serialization dependency
- Bootstrap executable configuration
- Gradle task: `prepareBootstrapForLambda`

### 4. **Deployment Instructions**
- `LAMBDA_DEPLOY.md` - Step-by-step deployment guide
- `SETUP_COMPLETE.md` - Complete setup guide
- `README.md` - Project overview

## 🚀 Quick Start

### Step 1: Bootstrap faylni tayyorlash
```bash
./gradlew prepareBootstrapForLambda
# Output: build/lambda/bootstrap
```

### Step 2: ZIP package yaratish
```bash
# Windows PowerShell
cd build/lambda
Compress-Archive -Path bootstrap -DestinationPath lambda-handler.zip

# Linux/Mac
cd build/lambda
zip lambda-handler.zip bootstrap
chmod +x bootstrap  # Make executable
```

### Step 3: AWS-da function yaratish
```bash
aws lambda create-function \
  --function-name my-kotlin-handler \
  --runtime provided.al2 \
  --role arn:aws:iam::YOUR_ACCOUNT:role/lambda-role \
  --handler bootstrap \
  --zip-file fileb://build/lambda/lambda-handler.zip
```

### Step 4: Test qilish
```bash
aws lambda invoke \
  --function-name my-kotlin-handler \
  --payload '{"test":"data"}' \
  response.json

cat response.json
```

## 📁 Loyiha Tuzilishi

```
.
├── bootstrap                           # Lambda bootstrap script
├── build.gradle.kts                    # Build configuration
├── README.md                           # Project overview
├── LAMBDA_DEPLOY.md                    # Deployment guide
├── SETUP_COMPLETE.md                   # Complete setup
├── src/
│   ├── nativeMain/kotlin/
│   │   ├── Main.kt                    # Entry point
│   │   ├── LambdaHandler.kt           # Lambda handler
│   │   └── ApiGatewayExample.kt       # API Gateway example
│   └── nativeTest/kotlin/
│       └── Test.kt                    # Tests
└── build/
    └── lambda/
        └── bootstrap                  # Compiled bootstrap
```

## 🔧 Handler Customization

`LambdaHandler.kt` faylida `handleRequest()` function-ni modify qiling:

```kotlin
fun handleRequest(event: String): LambdaResponse {
    val json = Json { ignoreUnknownKeys = true }
    val request = json.decodeFromString<YourEventType>(event)
    
    // Your business logic here
    val result = processRequest(request)
    
    return LambdaResponse(
        statusCode = 200,
        headers = mapOf("Content-Type" to "application/json"),
        body = json.encodeToString(result)
    )
}
```

## 📊 Environment Variables

Lambda runtime quyidagi variables-ni automatic set qiladi:

- `AWS_LAMBDA_FUNCTION_NAME` - Function name
- `AWS_LAMBDA_FUNCTION_VERSION` - Function version
- `AWS_LAMBDA_RUNTIME_API` - Runtime API endpoint
- `AWS_EXECUTION_ENV` - Execution environment

## 🐛 Common Issues & Solutions

### Issue: "cannot find -lgcc_s"
**Solution**: Shell script bootstrap-ni ishlatish (tayyor)

### Issue: "Cold start time too long"
**Solution**: Memory size-ni increase qiling (256MB+ recommended)

### Issue: "Handler not executing"
**Solution**: 
1. Bootstrap fayli executable bo'lishi kerak
2. Shebang line bo'lishi kerak (`#!/bin/bash`)
3. Runtime API endpoint correct bo'lishi kerak

## 📚 Resources

- [AWS Lambda Custom Runtimes](https://docs.aws.amazon.com/lambda/latest/dg/runtimes-custom.html)
- [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization)
- [Lambda Runtime API](https://docs.aws.amazon.com/lambda/latest/dg/runtimes-api.html)

## ✨ Key Features

✅ AWS Lambda Runtime API integration
✅ Kotlin serialization support
✅ HTTP request/response handling
✅ Error handling
✅ Environment variable access
✅ CloudWatch logging
✅ Custom bootstrap script

## 🎯 Next Steps

1. **Handler customize qilish** - `LambdaHandler.kt` edit qiling
2. **Local test** - `./gradlew build` run qiling
3. **AWS CLI setup** - AWS credentials configure qiling
4. **Deploy** - LAMBDA_DEPLOY.md bo'yicha deploy qiling
5. **Monitor** - CloudWatch logs-ni check qiling

## 💡 Tips

- JSON event-ni parse qilsh uchun `Json { ignoreUnknownKeys = true }` ishlating
- Error handling uchun try-catch block ishlating
- Response statusCode important - 200, 201, 404, 500 etc
- CloudWatch logs uchun `println()` ishlating
- Memory size 256MB+ recommended production uchun

---

**Setup Complete!** 🎉 Endi Lambda-ga deploy qilishga tayyor!
