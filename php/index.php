<?php
/**
 * Tingee SDK – PHP Sample (đầy đủ các API)
 * Cài đặt: composer install
 * Chạy:    php index.php
 */

require __DIR__ . '/vendor/autoload.php';

use Tingee\Sdk\TingeeClient;
use Tingee\Sdk\Types\TingeeWebhookBody;
use Tingee\Sdk\Types\Dtos\GenerateDynamicQRInputDto;
use Tingee\Sdk\Types\Dtos\OpenApiGetStatusDynamicQRInputDto;
use Tingee\Sdk\Types\Dtos\OpenApiGenerateVietQRInputDto;
use Tingee\Sdk\Types\Dtos\OpenApiGetVAPagedInputDto;
use Tingee\Sdk\Types\Dtos\OpenApiBankConfirmVAInputDto;
use Tingee\Sdk\Types\Dtos\OpenApiRegisterNotifyDto;
use Tingee\Sdk\Types\Dtos\OpenApiRefundDto;
use Tingee\Sdk\Types\Dtos\OpenApiReadSecurityCodeDto;
use Tingee\Sdk\Types\Dtos\OpenApiAddDeviceToShop;
use Tingee\Sdk\Types\Dtos\OpenApiGetPagingDeviceInputDto;
use Tingee\Sdk\Types\Dtos\OpenApiShowQRCodeDto;
use Tingee\Sdk\Types\Dtos\OpenApiGenerateAndShowDynamicQrCodeDto;
use Tingee\Sdk\Types\Dtos\OpenApiCreateOrUpdateShopDto;
use Tingee\Sdk\Types\Dtos\OpenApiGetShopPagedInputDto;
use Tingee\Sdk\Types\Dtos\OpenApiTransactionPagedInputDto;
use Tingee\Sdk\Types\Dtos\OpenApiCreateMerchantDto;
use Tingee\Sdk\Types\Dtos\OpenApiGetPagingMerchantsDto;
use Tingee\Sdk\Types\Dtos\OpenApiAccountNumberDDLPagedInputDto;
use Tingee\Sdk\Types\Dtos\OpenApiDeepLinkDto;
use Tingee\Sdk\Types\Dtos\OpenApiRegisterDto;
use Tingee\Sdk\Types\Dtos\OpenApiPaymentBillDto;
use Tingee\Sdk\Types\Dtos\OpenApiDeleteSubscriptionDto;
use Tingee\Sdk\Types\Dtos\OpenApiRefundInputDto;

// ─── Khởi tạo client ─────────────────────────────────────────────────────────
$client = new TingeeClient(
    secretKey:   getenv('TINGEE_SECRET_KEY')  ?: 'YOUR_SECRET_KEY',
    clientId:    getenv('TINGEE_CLIENT_ID')   ?: 'YOUR_CLIENT_ID',
    environment: 'uat',
);

echo "📦 Tingee SDK – PHP Sample\n\n";

// ─── Helper ───────────────────────────────────────────────────────────────────
function log_result(string $label, $response): void
{
    if ($response->isSuccess()) {
        echo "✅ [{$label}] " . json_encode($response->getData(), JSON_UNESCAPED_UNICODE) . "\n";
    } else {
        echo "⚠️  [{$label}] code={$response->getCode()}  msg={$response->getMessage()}\n";
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// BANK
// ═══════════════════════════════════════════════════════════════════════════════

function getBanks(TingeeClient $client): void
{
    log_result('bank.getBanks', $client->bank->getBanks());
}

function generateVietQr(TingeeClient $client): void
{
    // Constructor: __construct(string $accountNumber)
    $body = new OpenApiGenerateVietQRInputDto('0123456789');
    $body->bankBin = '970422';
    $body->amount  = 50_000;
    $body->content = 'Thanh toan don hang 001';

    log_result('bank.generateVietQr', $client->bank->generateVietQr($body));
}

function generateDynamicQr(TingeeClient $client): void
{
    // Constructor: __construct(string $vaAccountNumber, string $qrCodeType, int $amount, int $expireInMinute)
    $body = new GenerateDynamicQRInputDto(
        '9704221234567890',
        'dynamic-one-time-payment',
        100_000,
        5,
    );
    $body->bankBin = '970422';

    log_result('bank.generateDynamicQr', $client->bank->generateDynamicQr($body));
}

function getStatusDynamicQr(TingeeClient $client): void
{
    // Constructor: __construct(string $qrAccount, string $billId)
    $body = new OpenApiGetStatusDynamicQRInputDto('QR_ACCOUNT', 'BILL_ID');

    log_result('bank.getStatusDynamicQr', $client->bank->getStatusDynamicQr($body));
}

function getVaPaging(TingeeClient $client): void
{
    // Constructor: __construct(int $skipCount, int $maxResultCount, int $merchantId, string $accountType, string $dataAccess)
    $body = new OpenApiGetVAPagedInputDto(0, 10, 1234, 'personal-account', 'referral-only');

    log_result('bank.getVaPaging', $client->bank->getVaPaging($body));
}

function registerNotify(TingeeClient $client): void
{
    // Constructor: __construct(string $vaAccountNumber)
    $body = new OpenApiRegisterNotifyDto('VA_ACCOUNT_NUMBER');
    $body->bankBin = '970422';

    log_result('bank.registerNotify', $client->bank->registerNotify($body));
}

function bankRefund(TingeeClient $client): void
{
    // Constructor: __construct(string $transactionCode)
    $body = new OpenApiRefundDto('TRANSACTION_CODE');
    $body->bankBin = '970422';

    log_result('bank.refund', $client->bank->refund($body));
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEVICE
// ═══════════════════════════════════════════════════════════════════════════════

function readSecurityCode(TingeeClient $client): void
{
    // Constructor: __construct(string $uuid)
    $body = new OpenApiReadSecurityCodeDto('DEVICE_UUID');

    log_result('device.readSecurityCode', $client->device->readSecurityCode($body));
}

function addDeviceToShop(TingeeClient $client): void
{
    // Constructor: __construct(string $uuid, string $securityCode, string $appType)
    $body = new OpenApiAddDeviceToShop('DEVICE_UUID', 'SECURITY_CODE', 'tingee-app');
    $body->shopIds = [5678];

    log_result('device.addDeviceToShop', $client->device->addDeviceToShop($body));
}

function getDevicePaging(TingeeClient $client): void
{
    // Constructor: __construct(int $skipCount, int $maxResultCount, int $merchantId)
    $body = new OpenApiGetPagingDeviceInputDto(0, 10, 1234);

    log_result('device.getPaging', $client->device->getPaging($body));
}

function showQrOnDevice(TingeeClient $client): void
{
    // Constructor: __construct(string $uuid, int $amount, string $qrCode)
    $body = new OpenApiShowQRCodeDto('DEVICE_UUID', 50_000, 'QR_CODE_STRING');
    $body->vaAccountNumber = 'VA_ACCOUNT_NUMBER';

    log_result('device.showQrCode', $client->device->showQrCode($body));
}

function generateAndShowDynamicQr(TingeeClient $client): void
{
    // Constructor: __construct(string $vaAccountNumber, string $qrCodeType, int $amount, int $expireInMinute, string $uuid)
    $body = new OpenApiGenerateAndShowDynamicQrCodeDto(
        'VA_ACCOUNT_NUMBER',
        'dynamic-one-time-payment',
        75_000,
        5,
        'DEVICE_UUID',
    );
    $body->bankBin = '970422';

    log_result('device.generateAndShowDynamicQrCode', $client->device->generateAndShowDynamicQrCode($body));
}

// ═══════════════════════════════════════════════════════════════════════════════
// SHOP
// ═══════════════════════════════════════════════════════════════════════════════

function createOrUpdateShop(TingeeClient $client): void
{
    // Constructor: __construct(string $name, string $phoneNumber, bool $isActive)
    $body = new OpenApiCreateOrUpdateShopDto('Cửa hàng demo', '0909090909', true);
    $body->address = '123 Lê Lợi, Q1, TP.HCM';

    log_result('shop.createOrUpdate', $client->shop->createOrUpdate($body));
}

function getShopPaging(TingeeClient $client): void
{
    // Constructor: __construct(int $skipCount, int $maxResultCount)
    $body = new OpenApiGetShopPagedInputDto(0, 10);

    log_result('shop.getPaging', $client->shop->getPaging($body));
}

// ═══════════════════════════════════════════════════════════════════════════════
// TRANSACTION
// ═══════════════════════════════════════════════════════════════════════════════

function getTransactionPaging(TingeeClient $client): void
{
    // Constructor: __construct(int $skipCount, int $maxResultCount)
    $body = new OpenApiTransactionPagedInputDto(0, 20);
    $body->startTime = '2025-01-01';
    $body->endTime   = '2025-12-31';

    log_result('transaction.getPaging', $client->transaction->getPaging($body));
}

// ═══════════════════════════════════════════════════════════════════════════════
// MERCHANT
// ═══════════════════════════════════════════════════════════════════════════════

function createMerchant(TingeeClient $client): void
{
    // Constructor: __construct(string $name, string $phoneNumber, string $password, string $appType)
    $body = new OpenApiCreateMerchantDto('Merchant Demo', '0988888888', 'P@ssw0rd!', 'tingee-app');
    $body->email = 'demo@example.com';

    log_result('merchant.create', $client->merchant->create($body));
}

function getMerchantPaging(TingeeClient $client): void
{
    // Constructor: __construct(int $skipCount, int $maxResultCount)
    $body = new OpenApiGetPagingMerchantsDto(0, 10);

    log_result('merchant.getPaging', $client->merchant->getPaging($body));
}

// ═══════════════════════════════════════════════════════════════════════════════
// ACCOUNT NUMBER
// ═══════════════════════════════════════════════════════════════════════════════

function getAccountNumberDdl(TingeeClient $client): void
{
    // Constructor: __construct(int $skipCount, int $maxResultCount)
    $body = new OpenApiAccountNumberDDLPagedInputDto(0, 50);

    log_result('accountNumber.getAllDdl', $client->accountNumber->getAllDdl($body));
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEEP LINK
// ═══════════════════════════════════════════════════════════════════════════════

function generateDeepLink(TingeeClient $client): void
{
    // Constructor: __construct(string $type, string $qrCode, string $redirectUrl, string $callbackUrl,
    //                          string $bankBin, string $destinationBankBin, string $accountName,
    //                          string $accountNumber, string $billNumber)
    $body = new OpenApiDeepLinkDto(
        'payment',
        'QR_CODE_STRING',
        'https://your-domain.vn/payment-result',
        'https://your-domain.vn/webhook',
        '970422',
        '970422',
        'NGUYEN VAN A',
        '0123456789',
        'BILL_001',
    );
    $body->amount  = 200_000;
    $body->content = 'Thanh toan giao hang';

    log_result('deepLink.generate', $client->deepLink->generate($body));
}

// ═══════════════════════════════════════════════════════════════════════════════
// DIRECT DEBIT
// ═══════════════════════════════════════════════════════════════════════════════

function registerDirectDebit(TingeeClient $client): void
{
    // Constructor: __construct(string $requestId, string $phone, string $returnUrl)
    $body = new OpenApiRegisterDto('REQ_001', '0909090909', 'https://your-domain.vn/direct-debit/callback');

    $r = $client->directDebit->register($body);
    log_result('directDebit.register', $r);
    if ($r->isSuccess()) echo "  → Redirect người dùng đến: {$r->getData()}\n";
}

function directDebitPayment(TingeeClient $client): void
{
    // Constructor: __construct(string $requestId, string $subscriptionId, string $amount,
    //                          string $description, string $partnerCode, string $serviceProviderName)
    $body = new OpenApiPaymentBillDto(
        'REQ_PAY_001',
        'SUB_001',
        '150000',
        'Thanh toan hang thang',
        'one-pay',
        'Your Service',
    );

    log_result('directDebit.paymentBill', $client->directDebit->paymentBill($body));
}

function getSubscriptionStatus(TingeeClient $client): void
{
    log_result(
        'directDebit.getSubscriptionStatus',
        $client->directDebit->getSubscriptionStatus('SUB_001', 'TOKEN_REF'),
    );
}

function directDebitRefund(TingeeClient $client): void
{
    // Constructor: __construct(string $subscriptionId, string $tokenRef, string $transactionId, int $amount)
    $body = new OpenApiRefundInputDto('SUB_001', 'TOKEN_REF', 'TXN_ID', 150_000);

    log_result('directDebit.refund', $client->directDebit->refund($body));
}

function deleteSubscription(TingeeClient $client): void
{
    // Constructor: __construct(string $requestId, string $returnUrl, string $subscriptionId, string $tokenRef)
    $body = new OpenApiDeleteSubscriptionDto(
        'REQ_DEL_001',
        'https://your-domain.vn/direct-debit/cancel',
        'SUB_001',
        'TOKEN_REF',
    );

    log_result('directDebit.deleteSubscription', $client->directDebit->deleteSubscription($body));
}

// ═══════════════════════════════════════════════════════════════════════════════
// WEBHOOK — verifyWebhookSignature
// Tạo file webhook.php riêng rồi chạy: php -S localhost:3000 webhook.php
// ═══════════════════════════════════════════════════════════════════════════════

function handleWebhook(TingeeClient $client): void
{
    $signature = $_SERVER['HTTP_X_SIGNATURE']         ?? '';
    $timestamp = $_SERVER['HTTP_X_REQUEST_TIMESTAMP'] ?? '';
    $rawBody   = file_get_contents('php://input');

    $result = $client->verifyWebhookSignature($signature, $timestamp, $rawBody);

    header('Content-Type: application/json');
    if (!$result->isValid()) {
        http_response_code(401);
        echo json_encode(['code' => $result->code, 'message' => $result->message]);
        return;
    }

    $body = json_decode($rawBody, true);
    echo "✅ Webhook hợp lệ!\n";
    echo "   clientId:        {$body['clientId']}\n";
    echo "   transactionCode: {$body['transactionCode']}\n";
    echo "   amount:          {$body['amount']}\n";
    echo "   bank:            {$body['bank']}\n";
    echo "   accountNumber:   {$body['accountNumber']}\n";
    echo "   transactionDate: {$body['transactionDate']}\n";

    // TODO: lưu DB, cập nhật đơn hàng, ...
    http_response_code(200);
    echo json_encode(['code' => '00', 'message' => 'OK']);
}

// ═══════════════════════════════════════════════════════════════════════════════
// CHẠY — Bỏ comment dòng muốn chạy. Thay placeholder bằng dữ liệu thực.
// ═══════════════════════════════════════════════════════════════════════════════

getBanks($client);
// generateVietQr($client);
// generateDynamicQr($client);
// getStatusDynamicQr($client);
// getVaPaging($client);
// registerNotify($client);
// bankRefund($client);

// readSecurityCode($client);
// addDeviceToShop($client);
// getDevicePaging($client);
// showQrOnDevice($client);
// generateAndShowDynamicQr($client);

// createOrUpdateShop($client);
// getShopPaging($client);

// getTransactionPaging($client);

// createMerchant($client);
// getMerchantPaging($client);

// getAccountNumberDdl($client);
// generateDeepLink($client);

// registerDirectDebit($client);
// directDebitPayment($client);
// getSubscriptionStatus($client);
// directDebitRefund($client);
// deleteSubscription($client);

// Để nhận webhook, tạo riêng webhook.php với nội dung:
// <?php
// require __DIR__ . '/vendor/autoload.php';
// // ... (khởi tạo $client như trên)
// handleWebhook($client);
