# Tingee SDK Node.js Samples

Ví dụ sử dụng Tingee SDK cho Node.js

## Cài đặt

```bash
cd samples/nodejs
npm install
```

## Cấu hình

Tạo file `.env` hoặc set environment variables:

```bash
export TINGEE_SECRET_KEY="your-secret-key"
export TINGEE_CLIENT_ID="your-client-id"
```

Hoặc sửa trực tiếp trong `index.js`

## Chạy ví dụ

```bash
npm start
```

## Các ví dụ

### 1. Verify Referral Code
Xác thực mã giới thiệu

### 2. Get Shop Paging
Lấy danh sách shop có phân trang

### 3. Create Merchant
Tạo merchant mới

### 4. Get Merchant Paging
Lấy danh sách merchant có phân trang

### 5. Error Handling
Xử lý lỗi từ API

## Cách sử dụng trong project của bạn

```javascript
import { TingeeClient } from '@tingee/sdk-node'

const client = new TingeeClient({
  secretKey: 'your-secret-key',
  clientId: 'your-client-id',
  environment: 'production' // hoặc 'uat'
})

// Sử dụng các methods
const result = await client.v1MerchantCreate({
  name: 'My Merchant',
  phoneNumber: '0123456789',
  password: 'SecurePassword123!',
  appType: 'WEB'
})

if (result.data.code === '00') {
  console.log('Success!', result.data.data)
}
```
