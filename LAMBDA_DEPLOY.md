# AWS Lambda Deployment Guide

## 1. Bootstrap Faylni Tayyorlash

Kotlin/Native code bilan static binary qurish muammoli bo'lsa, bash-da yozilgan bootstrap fayldan foydalaning.

### Fayl: `bootstrap`
```bash
#!/bin/bash
# AWS Lambda Runtime API bilan kommunikatsiya
while true; do
    EVENT_DATA=$(curl -sS "http://${AWS_LAMBDA_RUNTIME_API}/2015-03-31/runtime/invocation/next")
    # ... event processing ...
done
```

## 2. Lambda Function Create qilish

```bash
# 1. IAM Role yaratish
aws iam create-role \
  --role-name lambda-kotlin-role \
  --assume-role-policy-document '{
    "Version": "2012-10-17",
    "Statement": [{
      "Effect": "Allow",
      "Principal": {"Service": "lambda.amazonaws.com"},
      "Action": "sts:AssumeRole"
    }]
  }'

# 2. Bootstrap faylini prepare qilish
./gradlew prepareBootstrapForLambda

# 3. ZIP file yaratish
cd build/lambda
zip -r ../lambda-handler.zip bootstrap

# 4. Lambda function yaratish
aws lambda create-function \
  --function-name my-kotlin-handler \
  --runtime provided.al2 \
  --role arn:aws:iam::YOUR_ACCOUNT_ID:role/lambda-kotlin-role \
  --handler bootstrap \
  --zip-file fileb://lambda-handler.zip \
  --timeout 30 \
  --memory-size 256 \
  --environment Variables='{AWS_LAMBDA_FUNCTION_NAME=my-kotlin-handler}'
```

## 3. Test qilish

```bash
# Local test
aws lambda invoke \
  --function-name my-kotlin-handler \
  --payload '{"test":"data"}' \
  response.json

cat response.json
```

## 4. Update qilish (Code o'zgarsa)

```bash
./gradlew prepareBootstrapForLambda
cd build/lambda
zip -r ../lambda-handler.zip bootstrap

aws lambda update-function-code \
  --function-name my-kotlin-handler \
  --zip-file fileb://lambda-handler.zip
```

## 5. CloudWatch Logs ko'rish

```bash
# Logs ko'rish
aws logs describe-log-groups --query 'logGroups[?logGroupName==`/aws/lambda/my-kotlin-handler`]'

# Latest logs
aws logs tail /aws/lambda/my-kotlin-handler --follow
```

## Notes

- **Runtime**: `provided.al2` (Amazon Linux 2) yoki `provided` (older Amazon Linux)
- **Memory**: 128MB-dan 10GB-gacha
- **Timeout**: 1 sekunddan 15 minutagacha
- **Environment variables**: Lambda setting-larida o'zgartirilishi mumkin
