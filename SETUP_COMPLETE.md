## AWS Lambda Kotlin/Native Handler - Tayyor Setup

### ✅ Tayyor Fayllar

1. **bootstrap** - Lambda Runtime API bilan integrate bo'lgan bash script
2. **src/nativeMain/kotlin/LambdaHandler.kt** - Kotlin handler implementation
3. **LAMBDA_DEPLOY.md** - Deployment instructions

### 📦 Lambda-ga Deploy qilish

#### 1. ZIP Package yaratish

```bash
# Windows PowerShell-da
cd build/lambda
Compress-Archive -Path bootstrap -DestinationPath lambda-handler.zip
# Yoki 7-Zip/WinRAR bilan

# Linux/Mac-da
cd build/lambda
zip -r lambda-handler.zip bootstrap
```

#### 2. AWS CLI bilan deploy qilish

```bash
# Function yaratish (birinchi marta)
aws lambda create-function \
  --function-name my-kotlin-handler \
  --runtime provided.al2 \
  --role arn:aws:iam::YOUR_ACCOUNT_ID:role/lambda-execution-role \
  --handler bootstrap \
  --zip-file fileb://build/lambda/lambda-handler.zip \
  --timeout 30 \
  --memory-size 256

# Yoki code update qilish
aws lambda update-function-code \
  --function-name my-kotlin-handler \
  --zip-file fileb://build/lambda/lambda-handler.zip
```

#### 3. AWS Console bilan deploy qilish

1. AWS Lambda Console-ga kirib "Create function" bosing
2. Runtime: **provided.al2**
3. Custom bootstrap faylni upload qiling (build/lambda/bootstrap)

### 🧪 Testing

```bash
# Lambda function invoke qilish
aws lambda invoke \
  --function-name my-kotlin-handler \
  --payload '{"test":"data"}' \
  response.json

# Response ko'rish
cat response.json
```

### 🛠️ Handler Customization

`src/nativeMain/kotlin/LambdaHandler.kt` faylida `handleRequest()` function-ni modify qiling:

```kotlin
fun handleRequest(event: String): LambdaResponse {
    // Your custom logic here
    // Event - JSON string sifatida keladi
    
    return LambdaResponse(
        statusCode = 200,
        body = "Your response"
    )
}
```

### 📋 Bootstrap Script Explained

`bootstrap` script:
- Lambda Runtime API-dan events qabul qiladi
- Event'ni process qiladi (Kotlin handler)
- Response'ni qaytaradi
- Loop-da davom etadi

### 🐛 Debugging

```bash
# CloudWatch logs ko'rish
aws logs tail /aws/lambda/my-kotlin-handler --follow

# Function details
aws lambda get-function --function-name my-kotlin-handler

# Test invoke
aws lambda invoke \
  --function-name my-kotlin-handler \
  --payload '{}' \
  --log-type Tail \
  response.json
```

### ⚙️ Environment Variables

Lambda setting-larida quyidagi variables-ni set qilishingiz mumkin:

```bash
aws lambda update-function-configuration \
  --function-name my-kotlin-handler \
  --environment Variables='{
    KEY1=value1,
    KEY2=value2
  }'
```

### 📚 Resources

- [AWS Lambda Runtime API](https://docs.aws.amazon.com/lambda/latest/dg/runtimes-custom.html)
- [Lambda Function Configuration](https://docs.aws.amazon.com/lambda/latest/dg/API_FunctionConfiguration.html)
- [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization)

### ⚠️ Important Notes

1. **Runtime**: `provided.al2` (Amazon Linux 2) - Linux x64 uchun
2. **Bootstrap file**: Executable bo'lishi va shebang line bilan boshlashi kerak (`#!/bin/bash`)
3. **Memory**: Minimal 128MB, optimal 256MB yoki undan ko'p
4. **Timeout**: Handler logic-ga qarab set qiling (default 30 sec)
5. **Cold start**: Kotlin runtime-ni load qilish 1-2 sekundni olishi mumkin

### 🚀 Next Steps

- [ ] IAM role-ni configure qilish
- [ ] Lambda function-ni AWS-da create qilish
- [ ] Test event bilan invoke qilish
- [ ] CloudWatch logs-ni monitor qilish
- [ ] Handler logic-ni customize qilish
- [ ] Production deploy qilish

Good luck! 🎉
