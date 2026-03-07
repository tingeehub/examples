// @ts-check
/**
 * Tingee SDK – Node.js Sample (đầy đủ các API)
 * Chạy: node index.js
 */

import http from 'node:http'
import { TingeeClient, isSuccessResponse, isErrorResponse } from '@tingee/sdk-node'

// ─── Khởi tạo client ─────────────────────────────────────────────────────────
const client = new TingeeClient({
  secretKey:   process.env.TINGEE_SECRET_KEY   || 'YOUR_SECRET_KEY',
  clientId:    process.env.TINGEE_CLIENT_ID    || 'YOUR_CLIENT_ID',
  environment: 'uat',
  timeout:     90_000,
})

// ─── Helper ───────────────────────────────────────────────────────────────────
function log(label, result) {
  if (isSuccessResponse(result))  console.log(`✅ [${label}]`, JSON.stringify(result.data, null, 2))
  else console.warn(`⚠️  [${label}] code=${result.code}  msg=${result.message}`)
}

// ═══════════════════════════════════════════════════════════════════════════════
// BANK
// ═══════════════════════════════════════════════════════════════════════════════

async function getBanks() {
  const r = await client.bank.getBanks()
  log('bank.getBanks', r)
}

async function generateVietQr() {
  const r = await client.bank.generateVietQr({
    bankBin:       '970422',      // bin MBBank
    accountNumber: '0123456789',
    amount:        50_000,
    content:       'Thanh toan don hang 001',
  })
  log('bank.generateVietQr', r)
}

async function generateDynamicQr() {
  const r = await client.bank.generateDynamicQr({
    vaAccountNumber: '9704221234567890',
    qrCodeType:      'dynamic-one-time-payment',
    amount:          100_000,
    expireInMinute:  5,
    bankBin:         '970422',
  })
  log('bank.generateDynamicQr', r)
}

async function getStatusDynamicQr() {
  const r = await client.bank.getStatusDynamicQr({
    qrAccount: 'QR_ACCOUNT',
    billId:    'BILL_ID',
  })
  log('bank.getStatusDynamicQr', r)
}

async function deleteDynamicQr() {
  const r = await client.bank.deleteDynamicQr({
    qrAccount: 'QR_ACCOUNT',
    billId:    'BILL_ID',
  })
  log('bank.deleteDynamicQr', r)
}

async function getVaPaging() {
  const r = await client.bank.getVaPaging({
    skipCount:      0,
    maxResultCount: 10,
    merchantId:     1234,
    accountType:    'personal-account',
    dataAccess:     'referral-only',
  })
  log('bank.getVaPaging', r)
}

async function createVa() {
  const r = await client.bank.createVa({
    accountType:   'personal-account',
    accountNumber: '0123456789',
    accountName:   'NGUYEN VAN A',
    identity:      '012345678901',
    mobile:        '0909090909',
    appType:       'tingee-app',
    redirectUrl:   'https://your-domain.vn/va/callback',
    webhookUrl:    'https://your-domain.vn/webhook',
    bankBin:       '970422',
  })
  log('bank.createVa', r)
}

async function confirmVa() {
  const r = await client.bank.confirmVa({
    confirmId:  'CONFIRM_ID_FROM_createVa',
    otpNumber:  '123456',
    bankName:   'MBB',
    bankBin:    '970422',
  })
  log('bank.confirmVa', r)
}

async function deleteVa() {
  const r = await client.bank.deleteVa({
    vaAccountNumber: 'VA_ACCOUNT_NUMBER',
    bankBin:         '970422',
  })
  log('bank.deleteVa', r)
}

async function registerNotify() {
  const r = await client.bank.registerNotify({
    vaAccountNumber: 'VA_ACCOUNT_NUMBER',
    bankBin:         '970422',
  })
  log('bank.registerNotify', r)
}

async function bankRefund() {
  const r = await client.bank.refund({
    transactionCode: 'TRANSACTION_CODE',
    bankBin:         '970422',
  })
  log('bank.refund', r)
}

async function getAccountByQrCode() {
  const r = await client.bank.getAccountNumberInfoByQrCode({ qrCode: 'QR_CODE_STRING' })
  log('bank.getAccountNumberInfoByQrCode', r)
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEVICE
// ═══════════════════════════════════════════════════════════════════════════════

async function readSecurityCode() {
  const r = await client.device.readSecurityCode({ uuid: 'DEVICE_UUID' })
  log('device.readSecurityCode', r)
}

async function addDeviceToShop() {
  const r = await client.device.addDeviceToShop({
    uuid:         'DEVICE_UUID',
    securityCode: 'SECURITY_CODE',
    appType:      'tingee-app',
    shopIds:      [5678],
  })
  log('device.addDeviceToShop', r)
}

async function getDevicesLinkToShopOrVa() {
  const r = await client.device.getDevicesLinkToShopOrVa({
    merchantId:      1234,
    vaAccountNumber: 'VA_ACCOUNT_NUMBER',
    shopId:          5678,
  })
  log('device.getDevicesLinkToShopOrVa', r)
}

async function getDevicePaging() {
  const r = await client.device.getPaging({
    skipCount:      0,
    maxResultCount: 10,
    merchantId:     1234,
  })
  log('device.getPaging', r)
}

async function showQrOnDevice() {
  const r = await client.device.showQrCode({
    uuid:           'DEVICE_UUID',
    amount:         50_000,
    qrCode:         'QR_CODE_STRING',
    vaAccountNumber: 'VA_ACCOUNT_NUMBER',
  })
  log('device.showQrCode', r)
}

async function generateAndShowDynamicQr() {
  const r = await client.device.generateAndShowDynamicQrCode({
    uuid:            'DEVICE_UUID',
    vaAccountNumber: 'VA_ACCOUNT_NUMBER',
    qrCodeType:      'dynamic-one-time-payment',
    amount:          75_000,
    expireInMinute:  5,
    bankBin:         '970422',
  })
  log('device.generateAndShowDynamicQrCode', r)
}

async function readAmount() {
  const r = await client.device.readAmountLinkToMerchant({
    uuid:            'DEVICE_UUID',
    transactionId:   'TXN_ID',
    amount:          100_000,
    bankBin:         '970422',
  })
  log('device.readAmountLinkToMerchant', r)
}

// ═══════════════════════════════════════════════════════════════════════════════
// SHOP
// ═══════════════════════════════════════════════════════════════════════════════

async function createOrUpdateShop() {
  const r = await client.shop.createOrUpdate({
    name:        'Cửa hàng demo',
    phoneNumber: '0909090909',
    isActive:    true,
    address:     '123 Lê Lợi, Q1, TP.HCM',
  })
  log('shop.createOrUpdate', r)
}

async function getShopPaging() {
  const r = await client.shop.getPaging({ skipCount: 0, maxResultCount: 10 })
  log('shop.getPaging', r)
}

// ═══════════════════════════════════════════════════════════════════════════════
// TRANSACTION
// ═══════════════════════════════════════════════════════════════════════════════

async function getTransactionPaging() {
  const r = await client.transaction.getPaging({
    skipCount:      0,
    maxResultCount: 20,
    startTime:      '2025-01-01',
    endTime:        '2025-12-31',
  })
  log('transaction.getPaging', r)
}

// ═══════════════════════════════════════════════════════════════════════════════
// MERCHANT
// ═══════════════════════════════════════════════════════════════════════════════

async function createMerchant() {
  const r = await client.merchant.create({
    name:        'Merchant Demo',
    phoneNumber: '0988888888',
    email:       'demo@example.com',
    appType:     'tingee-app',
    password:    'P@ssw0rd!',
  })
  log('merchant.create', r)
}

async function getMerchantPaging() {
  const r = await client.merchant.getPaging({ skipCount: 0, maxResultCount: 10 })
  log('merchant.getPaging', r)
}

// ═══════════════════════════════════════════════════════════════════════════════
// ACCOUNT NUMBER
// ═══════════════════════════════════════════════════════════════════════════════

async function getAccountNumberDdl() {
  const r = await client.accountNumber.getAllDdl({ skipCount: 0, maxResultCount: 50 })
  log('accountNumber.getAllDdl', r)
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEEP LINK
// ═══════════════════════════════════════════════════════════════════════════════

async function generateDeepLink() {
  const r = await client.deepLink.generate({
    type:               'payment',
    qrCode:             'QR_CODE_STRING',
    redirectUrl:        'https://your-domain.vn/payment-result',
    callbackUrl:        'https://your-domain.vn/webhook',
    bankBin:            '970422',
    destinationBankBin: '970422',
    accountName:        'NGUYEN VAN A',
    accountNumber:      '0123456789',
    billNumber:         'BILL_001',
    amount:             200_000,
    content:            'Thanh toan giao hang',
  })
  log('deepLink.generate', r)
}

// ═══════════════════════════════════════════════════════════════════════════════
// USER
// ═══════════════════════════════════════════════════════════════════════════════

async function verifyReferralCode() {
  const r = await client.user.verifyReferralCode({ referralCode: 'ABCD1234' })
  log('user.verifyReferralCode', r)
}

// ═══════════════════════════════════════════════════════════════════════════════
// DIRECT DEBIT
// ═══════════════════════════════════════════════════════════════════════════════

// Bước 1: Đăng ký liên kết tài khoản, nhận URL redirect người dùng
async function registerDirectDebit() {
  const r = await client.directDebit.register({
    requestId: 'REQ_001',
    phone:     '0909090909',
    returnUrl: 'https://your-domain.vn/direct-debit/callback',
  })
  log('directDebit.register', r)
  if (isSuccessResponse(r)) console.log('  → Redirect người dùng đến:', r.data)
}

// Bước 2: Thanh toán hóa đơn
async function directDebitPayment() {
  const r = await client.directDebit.paymentBill({
    requestId:           'REQ_PAY_001',
    subscriptionId:      'SUB_001',
    amount:              '150000',   // string theo API spec
    description:         'Thanh toan hang thang',
    partnerCode:         'one-pay',
    serviceProviderName: 'Your Service',
  })
  log('directDebit.paymentBill', r)
}

// Kiểm tra trạng thái subscription
async function getSubscriptionStatus() {
  const r = await client.directDebit.getSubscriptionStatus({
    subscriptionId: 'SUB_001',
    tokenRef:       'TOKEN_REF',
  })
  log('directDebit.getSubscriptionStatus', r)
}

// Kiểm tra trạng thái giao dịch
async function getDirectDebitTransactionStatus() {
  const r = await client.directDebit.getTransactionStatus({
    transactionId:  'TXN_ID',
    tokenRef:       'TOKEN_REF',
    subscriptionId: 'SUB_001',
  })
  log('directDebit.getTransactionStatus', r)
}

// Lịch sử giao dịch
async function getDirectDebitTransactions() {
  const r = await client.directDebit.getPagingTransactions({
    subscriptionId: 'SUB_001',
    tokenRef:       'TOKEN_REF',
    skipCount:      0,
    maxResultCount: 10,
  })
  log('directDebit.getPagingTransactions', r)
}

// Hoàn tiền
async function directDebitRefund() {
  const r = await client.directDebit.refund({
    subscriptionId: 'SUB_001',
    tokenRef:       'TOKEN_REF',
    transactionId:  'TXN_ID',
    amount:         150_000,
  })
  log('directDebit.refund', r)
}

// Xóa subscription
async function deleteSubscription() {
  const r = await client.directDebit.deleteSubscription({
    requestId:      'REQ_DEL_001',
    returnUrl:      'https://your-domain.vn/direct-debit/cancel',
    subscriptionId: 'SUB_001',
    tokenRef:       'TOKEN_REF',
  })
  log('directDebit.deleteSubscription', r)
}

// ═══════════════════════════════════════════════════════════════════════════════
// WEBHOOK SERVER — verifyWebhookSignature
// Dùng Node.js http thuần (không cần cài thêm thư viện)
// ═══════════════════════════════════════════════════════════════════════════════

const WEBHOOK_PORT = Number(process.env.PORT) || 3000

function startWebhookServer() {
  const server = http.createServer((req, res) => {
    if (req.method !== 'POST' || req.url !== '/webhook') {
      res.writeHead(404).end()
      return
    }

    let rawBody = ''
    req.on('data', chunk => { rawBody += chunk })
    req.on('end', () => {
      const signature = String(req.headers['x-signature']         || '')
      const timestamp = String(req.headers['x-request-timestamp'] || '')

      // ✅ Xác minh chữ ký — truyền raw JSON string, SDK tự parse
      const result = client.verifyWebhookSignature({
        signature,
        timestamp,
        body: rawBody,
      })

      if (isSuccessResponse(result)) {
        console.warn('❌ Webhook không hợp lệ:', result.code, result.message)
        res.writeHead(401, { 'Content-Type': 'application/json' })
        res.end(JSON.stringify({ code: result.code, message: result.message }))
        return
      }

      const body = JSON.parse(rawBody)
      console.log('✅ Webhook hợp lệ!')
      console.log('   clientId:        ', body.clientId)
      console.log('   transactionCode: ', body.transactionCode)
      console.log('   amount:          ', body.amount)
      console.log('   bank:            ', body.bank)
      console.log('   accountNumber:   ', body.accountNumber)
      console.log('   transactionDate: ', body.transactionDate)

      // TODO: lưu DB, cập nhật đơn hàng, ...

      res.writeHead(200, { 'Content-Type': 'application/json' })
      res.end(JSON.stringify({ code: '00', message: 'OK' }))
    })
  })

  server.listen(WEBHOOK_PORT, () => {
    console.log(`🚀 Webhook server: http://localhost:${WEBHOOK_PORT}/webhook`)
    console.log('   Dùng ngrok để expose khi test UAT\n')
  })
}

// ═══════════════════════════════════════════════════════════════════════════════
// CHẠY MẪU
// Bỏ comment các dòng muốn chạy. Thay placeholder bằng dữ liệu thực.
// ═══════════════════════════════════════════════════════════════════════════════

async function main() {
  console.log('📦 Tingee SDK – Node.js Sample\n')

  await getBanks()
  // await generateVietQr()
  // await generateDynamicQr()
  // await getStatusDynamicQr()
  // await deleteDynamicQr()
  // await getVaPaging()
  // await createVa()
  // await confirmVa()
  // await deleteVa()
  // await registerNotify()
  // await bankRefund()
  // await getAccountByQrCode()

  // await readSecurityCode()
  // await addDeviceToShop()
  // await getDevicesLinkToShopOrVa()
  // await getDevicePaging()
  // await showQrOnDevice()
  // await generateAndShowDynamicQr()
  // await readAmount()

  // await createOrUpdateShop()
  // await getShopPaging()

  // await getTransactionPaging()

  // await createMerchant()
  // await getMerchantPaging()

  // await getAccountNumberDdl()

  // await generateDeepLink()
  // await verifyReferralCode()

  // await registerDirectDebit()
  // await directDebitPayment()
  // await getSubscriptionStatus()
  // await getDirectDebitTransactionStatus()
  // await getDirectDebitTransactions()
  // await directDebitRefund()
  // await deleteSubscription()
}

// Uncomment để chạy webhook server thay vì examples:
// startWebhookServer()

await main()
