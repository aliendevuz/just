# AWS Lambda Deployment Guide

## GitHub Secrets sozlamash

GitHub repository-da quyidagi secrets-larni qo'shish kerak:

### 1. AWS Credentials
1. **AWS_ACCESS_KEY_ID**
   - AWS IAM → Access keys
   - Access Key ID-ni copy qilish

2. **AWS_SECRET_ACCESS_KEY**
   - Shu JWT token-dan Secret access key-ni copy qilish

3. **AWS_REGION**
   - Masalan: `us-east-1`, `eu-west-1`, va boshqalar

### 2. Lambda Configuration
4. **LAMBDA_FUNCTION_NAME**
   - Masalan: `telegram-bot` yoki `telegram-handler`

5. **LAMBDA_ROLE_ARN**
   - AWS IAM → Roles
   - Lambda uchun role ARN-ni copy qilish
   - Format: `arn:aws:iam::123456789:role/lambda-role`

### 3. Telegram Bot
6. **BOT_TOKEN**
   - Telegram Bot Father-dan olgan token
   - Format: `123456789:ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefgh`

## GitHub-da Secrets qo'shish

1. Repository → Settings → Secrets and variables → Actions
2. "New repository secret" tugmasini bosish
3. Name va Value qo'shish
4. Repeat har bir secret uchun

## IAM Role yaratish (AWS)

### 1. Role Creation
```
IAM → Roles → Create role
```

### 2. Trust Policy
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "lambda.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
```

### 3. Permissions Policy
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents"
      ],
      "Resource": "arn:aws:logs:*:*:*"
    }
  ]
}
```

## Deployment Process

### Automatic (GitHub Actions)
1. Code-ni push qilish main branch-ga
2. GitHub Actions workflow trigger bo'ladi
3. Build → ZIP yaratish → AWS-ga upload qilish
4. Lambda function update yoki create bo'ladi

### Manual Deployment
```bash
# Build
./gradlew buildBootstrap

# Package
mkdir -p lambda_pkg
cp bootstrap lambda_pkg/
chmod 755 lambda_pkg/bootstrap
mkdir -p lambda_pkg/helper
cp build/bin/telegram/bootstrapReleaseExecutable/bootstrap lambda_pkg/helper/
chmod 755 lambda_pkg/helper/bootstrap

# ZIP
cd lambda_pkg
zip -r ../telegram-bot.zip .
cd ..

# Upload (AWS CLI kerak)
aws lambda update-function-code \
  --function-name telegram-bot \
  --zip-file fileb://telegram-bot.zip \
  --region us-east-1
```

## Telegram Webhook Setup

Lambda function deployed bo'lgandan keyin, API Gateway (yoki ALB) orqali webhook-ni configure qilish kerak:

```bash
curl -X POST https://api.telegram.org/bot<BOT_TOKEN>/setWebhook \
  -H "Content-Type: application/json" \
  -d "{\"url\": \"https://<API_GATEWAY_URL>/telegram\"}"
```

## Troubleshooting

### "Handler not found" error
- `bootstrap` file executable ekanligini check qilish
- `helper/bootstrap` path-ni verify qilish

### "No event provided" error
- Lambda Runtime API endpoint health check qilish
- CloudWatch Logs-da error-larni ko'rish

### Build failed
- Java/Gradle version-ni check qilish
- `./gradlew clean buildBootstrap` run qilish

## File Structure

```
telegram-bot-lambda.zip
├── bootstrap (shell script, chmod 755)
└── helper/
    └── bootstrap (Kotlin/Native executable, chmod 755)
```

## Lambda Configuration

- Runtime: `Provided.al2`
- Handler: (custom bootstrap ishlatiladi)
- Timeout: 60 seconds (yoki more)
- Memory: 512 MB (yoki more)
- Environment Variables:
  - `BOT_TOKEN`: Telegram bot token
