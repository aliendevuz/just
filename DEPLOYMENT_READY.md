# 🚀 AWS Lambda Kotlin/Native - Setup Complete!

## ✅ Nima Yaratildi

### 1. **Bootstrap Script** (`bootstrap` + `build/lambda/bootstrap`)
- AWS Lambda Runtime API bilan integrate bo'lgan bash script
- Events-ni qabul qiladi va process qiladi
- Response-ni Lambda-ga qaytaradi
- **Status**: ✅ Tayyor deploy uchun

### 2. **Kotlin Handler**
- `LambdaHandler.kt` - Main handler implementation
- `ApiGatewayExample.kt` - API Gateway example
- `Main.kt` - Entry point
- **Features**:
  - JSON serialization/deserialization (kotlinx-serialization)
  - Error handling
  - Environment variable support
  - Custom response formatting

### 3. **Build Configuration**
- `build.gradle.kts` - Gradle configuration
- `gradle.properties` - Project properties
- **Gradle Task**: `prepareBootstrapForLambda`

### 4. **Documentation**
- `GETTING_STARTED.md` - Quick start guide
- `LAMBDA_DEPLOY.md` - Deployment instructions
- `SETUP_COMPLETE.md` - Complete setup guide
- `README.md` - Project overview

## 📋 Deploy Qilish Uchun Kerak Bo'lgan Narsalar

### Immediate Actions
```bash
# 1. ZIP package yaratish
cd build/lambda
zip lambda-handler.zip bootstrap

# 2. AWS CLI bilan deploy
aws lambda create-function \
  --function-name my-kotlin-handler \
  --runtime provided.al2 \
  --role arn:aws:iam::YOUR_ACCOUNT:role/lambda-role \
  --handler bootstrap \
  --zip-file fileb://lambda-handler.zip
```

## 🎯 Structure Overview

```
Project Root
├── bootstrap                          # Source bootstrap script
├── build/
│   └── lambda/
│       └── bootstrap                 # ✅ Ready for deployment
├── src/
│   ├── nativeMain/kotlin/
│   │   ├── Main.kt                  # Entry point
│   │   ├── LambdaHandler.kt         # ✅ Customize this
│   │   └── ApiGatewayExample.kt     # Reference example
│   └── nativeTest/kotlin/
│       └── Test.kt                  # Unit tests
├── build.gradle.kts                 # Build config
└── Documentation
    ├── GETTING_STARTED.md           # Start here
    ├── LAMBDA_DEPLOY.md             # Deployment steps
    └── README.md                    # Overview
```

## 🔑 Key Configuration

### Gradle Task
```bash
./gradlew prepareBootstrapForLambda
# Copies bootstrap to build/lambda/ and makes it executable
```

### Handler Function
Location: `src/nativeMain/kotlin/LambdaHandler.kt`
```kotlin
fun handleRequest(event: String): LambdaResponse {
    // Customize this function with your business logic
}
```

### Dependencies
- Kotlin 2.0
- kotlinx-serialization-json 1.7.3
- Kotlin/Native linuxX64

## 🧪 Testing Checklist

- [ ] `./gradlew prepareBootstrapForLambda` runs successfully
- [ ] `build/lambda/bootstrap` exists and is readable
- [ ] Bootstrap script starts with `#!/bin/bash`
- [ ] Handler.kt compiles without errors
- [ ] ZIP package created successfully
- [ ] AWS Lambda function created
- [ ] Test invocation works
- [ ] CloudWatch logs visible

## 📝 Handler Customization Example

```kotlin
// In LambdaHandler.kt
fun handleRequest(event: String): LambdaResponse {
    val json = Json { ignoreUnknownKeys = true }
    
    // Parse input
    val request = json.decodeFromString<YourRequestType>(event)
    
    // Process
    val result = yourBusinessLogic(request)
    
    // Return response
    return LambdaResponse(
        statusCode = 200,
        headers = mapOf("Content-Type" to "application/json"),
        body = json.encodeToString(result)
    )
}
```

## ⚠️ Important Notes

1. **Runtime**: `provided.al2` (Amazon Linux 2) - Linux x64 uchun
2. **Bootstrap**: Must be executable and start with `#!/bin/bash`
3. **Memory**: Minimal 128MB, recommended 256MB+
4. **Timeout**: Set accordingly (default 30 seconds)
5. **Handler**: Entry point is `bootstrap` script

## 🚀 Deployment Workflow

```
1. Code write/modify
   ↓
2. ./gradlew prepareBootstrapForLambda
   ↓
3. cd build/lambda && zip lambda-handler.zip bootstrap
   ↓
4. aws lambda create-function / update-function-code
   ↓
5. aws lambda invoke --function-name my-handler ...
   ↓
6. aws logs tail /aws/lambda/my-handler --follow
```

## 📚 Next Reading Order

1. **Start here**: `GETTING_STARTED.md`
2. **Then read**: `README.md`
3. **For deployment**: `LAMBDA_DEPLOY.md`
4. **Reference**: `SETUP_COMPLETE.md`

## 💬 Handler Tips

- Use `println()` for CloudWatch logs
- Catch all exceptions in handler
- Use `Json { ignoreUnknownKeys = true }` for flexible parsing
- Always return proper HTTP status codes
- Test locally before deploying

## 🎉 Ready to Deploy!

Your AWS Lambda Kotlin/Native handler is ready. Follow the deployment steps in `LAMBDA_DEPLOY.md` to get started!

---

**Questions?** Check the documentation files or AWS Lambda documentation.
