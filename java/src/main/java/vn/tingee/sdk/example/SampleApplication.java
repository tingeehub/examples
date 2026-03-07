package vn.tingee.sdk.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import vn.tingee.sdk.TingeeClient;
import vn.tingee.sdk.client.TingeeEnvironment;
import vn.tingee.sdk.model.*;
import vn.tingee.sdk.types.TingeeApiResponse;

import java.util.List;

/**
 * Tingee SDK – Java Sample (đầy đủ các API)
 *
 * Chạy: mvn spring-boot:run
 *
 * Mỗi nhóm API được tổ chức thành method riêng.
 * Bỏ comment dòng muốn chạy trong CommandLineRunner bên dưới.
 *
 * Pattern Java SDK:
 *   - Constructor chỉ nhận required fields (@NonNull)
 *   - Optional fields: dùng setter setXxx(value)
 */
@SpringBootApplication
public class SampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
    }

    // ─── Khởi tạo client ──────────────────────────────────────────────────────
    @Bean
    TingeeClient tingeeClient() {
        return TingeeClient
            .builder(
                System.getenv().getOrDefault("TINGEE_SECRET_KEY", "YOUR_SECRET_KEY"),
                System.getenv().getOrDefault("TINGEE_CLIENT_ID",   "YOUR_CLIENT_ID")
            )
            .environment(TingeeEnvironment.UAT)
            .build();
    }

    // ─── Helper ───────────────────────────────────────────────────────────────
    private static final ObjectMapper mapper = new ObjectMapper();

    static void log(String label, TingeeApiResponse<?> r) {
        try {
            if ("00".equals(r.getCode()))
                System.out.printf("✅ [%s] %s%n", label, mapper.writeValueAsString(r.getData()));
            else
                System.out.printf("⚠️  [%s] code=%s  msg=%s%n", label, r.getCode(), r.getMessage());
        } catch (Exception e) {
            System.err.println("log error: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // BANK
    // ═══════════════════════════════════════════════════════════════════════════

    static void getBanks(TingeeClient c) {
        log("bank.getBanks", c.bank.getBanks());
    }

    static void generateVietQr(TingeeClient c) {
        // Constructor: (String accountNumber)
        var dto = new OpenApiGenerateVietQRInputDto("0123456789");
        dto.setBankBin("970422");
        dto.setAmount(50_000L);
        dto.setContent("Thanh toan don hang 001");
        log("bank.generateVietQr", c.bank.generateVietQr(dto));
    }

    static void generateDynamicQr(TingeeClient c) {
        // Constructor: (String vaAccountNumber, QRCodeTypeEnum qrCodeType, Long amount, Integer expireInMinute)
        var dto = new GenerateDynamicQRInputDto("9704221234567890", QRCodeTypeEnum.DYNAMIC_ONE_TIME_PAYMENT, 100_000L, 5);
        dto.setBankBin("970422");
        log("bank.generateDynamicQr", c.bank.generateDynamicQr(dto));
    }

    static void getStatusDynamicQr(TingeeClient c) {
        // Constructor: (String qrAccount, String billId)
        var dto = new OpenApiGetStatusDynamicQRInputDto("QR_ACCOUNT", "BILL_ID");
        log("bank.getStatusDynamicQr", c.bank.getStatusDynamicQr(dto));
    }

    static void getVaPaging(TingeeClient c) {
        // Constructor: (Integer skipCount, Integer maxResultCount, Integer merchantId,
        //               BankAccountTypeEnum accountType, DataAccessFilterEnum dataAccess)
        var dto = new OpenApiGetVAPagedInputDto(0, 10, 1234, BankAccountTypeEnum.PERSONAL_ACCOUNT, DataAccessFilterEnum.REFERRAL_ONLY);
        log("bank.getVaPaging", c.bank.getVaPaging(dto));
    }

    static void registerNotify(TingeeClient c) {
        // Constructor: (String vaAccountNumber)
        var dto = new OpenApiRegisterNotifyDto("VA_ACCOUNT_NUMBER");
        dto.setBankBin("970422");
        log("bank.registerNotify", c.bank.registerNotify(dto));
    }

    static void bankRefund(TingeeClient c) {
        // Constructor: (String transactionCode)
        var dto = new OpenApiRefundDto("TRANSACTION_CODE");
        dto.setBankBin("970422");
        log("bank.refund", c.bank.refund(dto));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // DEVICE
    // ═══════════════════════════════════════════════════════════════════════════

    static void readSecurityCode(TingeeClient c) {
        // Constructor: (String uuid)
        var dto = new OpenApiReadSecurityCodeDto("DEVICE_UUID");
        log("device.readSecurityCode", c.device.readSecurityCode(dto));
    }

    static void addDeviceToShop(TingeeClient c) {
        // Constructor: (String uuid, String securityCode, AppTypeEnum appType)
        var dto = new OpenApiAddDeviceToShop("DEVICE_UUID", "SECURITY_CODE", AppTypeEnum.TINGEE_APP);
        dto.setShopIds(List.of(5678.0));
        log("device.addDeviceToShop", c.device.addDeviceToShop(dto));
    }

    static void getDevicePaging(TingeeClient c) {
        // Constructor: (Integer skipCount, Integer maxResultCount, Integer merchantId)
        var dto = new OpenApiGetPagingDeviceInputDto(0, 10, 1234);
        log("device.getPaging", c.device.getPaging(dto));
    }

    static void showQrOnDevice(TingeeClient c) {
        // Constructor: (String uuid, Long amount, String qrCode)
        var dto = new OpenApiShowQRCodeDto("DEVICE_UUID", 50_000L, "QR_CODE_STRING");
        dto.setVaAccountNumber("VA_ACCOUNT_NUMBER");
        log("device.showQrCode", c.device.showQrCode(dto));
    }

    static void generateAndShowDynamicQr(TingeeClient c) {
        // Constructor: (String vaAccountNumber, QRCodeTypeEnum qrCodeType, Long amount, Integer expireInMinute, String uuid)
        var dto = new OpenApiGenerateAndShowDynamicQrCodeDto(
            "VA_ACCOUNT_NUMBER", QRCodeTypeEnum.DYNAMIC_ONE_TIME_PAYMENT, 75_000L, 5, "DEVICE_UUID");
        dto.setBankBin("970422");
        log("device.generateAndShowDynamicQrCode", c.device.generateAndShowDynamicQrCode(dto));
    }

    static void readAmount(TingeeClient c) {
        // Constructor: (String uuid, String transactionId, Long amount, String bankBin)
        var dto = new OpenApiReadAmountDto("DEVICE_UUID", "TXN_ID", 100_000L, "970422");
        log("device.readAmountLinkToMerchant", c.device.readAmountLinkToMerchant(dto));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // SHOP
    // ═══════════════════════════════════════════════════════════════════════════

    static void createOrUpdateShop(TingeeClient c) {
        // Constructor: (String name, Boolean isActive)
        var dto = new OpenApiCreateOrUpdateShopDto("Cửa hàng demo", "0123456789", Boolean.TRUE);
        dto.setPhoneNumber("0909090909");
        dto.setAddress("123 Lê Lợi, Q1, TP.HCM");
        log("shop.createOrUpdate", c.shop.createOrUpdate(dto));
    }

    static void getShopPaging(TingeeClient c) {
        // Constructor: (Integer skipCount, Integer maxResultCount)
        var dto = new OpenApiGetShopPagedInputDto(0, 10);
        log("shop.getPaging", c.shop.getPaging(dto));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // TRANSACTION
    // ═══════════════════════════════════════════════════════════════════════════

    static void getTransactionPaging(TingeeClient c) {
        // Constructor: (Integer skipCount, Integer maxResultCount)
        var dto = new OpenApiTransactionPagedInputDto(0, 20);
        dto.setStartTime("2025-01-01");
        dto.setEndTime("2025-12-31");
        log("transaction.getPaging", c.transaction.getPaging(dto));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // MERCHANT
    // ═══════════════════════════════════════════════════════════════════════════

    static void createMerchant(TingeeClient c) {
        // Constructor: (String name, String phoneNumber, String password, AppTypeEnum appType)
        var dto = new OpenApiCreateMerchantDto("Merchant Demo", "0988888888", "P@ssw0rd!", AppTypeEnum.TINGEE_APP);
        dto.setEmail("demo@example.com");
        log("merchant.create", c.merchant.create(dto));
    }

    static void getMerchantPaging(TingeeClient c) {
        // Constructor: (Integer skipCount, Integer maxResultCount)
        var dto = new OpenApiGetPagingMerchantsDto(0, 10);
        log("merchant.getPaging", c.merchant.getPaging(dto));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // ACCOUNT NUMBER
    // ═══════════════════════════════════════════════════════════════════════════

    static void getAccountNumberDdl(TingeeClient c) {
        // Constructor: (Integer skipCount, Integer maxResultCount)
        var dto = new OpenApiAccountNumberDDLPagedInputDto(0, 50);
        log("accountNumber.getAllDdl", c.accountNumber.getAllDdl(dto));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // DEEP LINK
    // ═══════════════════════════════════════════════════════════════════════════

    static void generateDeepLink(TingeeClient c) {
        // Constructor: (String type, String qrCode, String redirectUrl, String callbackUrl,
        //               String bankBin, String destinationBankBin, String accountName,
        //               String accountNumber, String billNumber)
        var dto = new OpenApiDeepLinkDto(
            "payment", "QR_CODE_STRING",
            "https://your-domain.vn/payment-result", "https://your-domain.vn/webhook",
            "970422", "970422", "NGUYEN VAN A", "0123456789", "BILL_001");
        dto.setAmount(200_000L);
        dto.setContent("Thanh toan giao hang");
        log("deepLink.generate", c.deepLink.generate(dto));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // USER
    // ═══════════════════════════════════════════════════════════════════════════

    static void verifyReferralCode(TingeeClient c) {
        log("user.verifyReferralCode", c.user.verifyReferralCode("ABCD1234"));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // DIRECT DEBIT
    // ═══════════════════════════════════════════════════════════════════════════

    static void registerDirectDebit(TingeeClient c) {
        // Constructor: (String requestId, String phone, String returnUrl)
        var dto = new OpenApiRegisterDto("REQ_001", "0909090909", "https://your-domain.vn/direct-debit/callback");
        log("directDebit.register", c.directDebit.register(dto));
    }

    static void directDebitPayment(TingeeClient c) {
        // Constructor: (String requestId, String subscriptionId, String amount,
        //               String description, DirectDebitPartnerEnum partnerCode, String serviceProviderName)
        var dto = new OpenApiPaymentBillDto(
            "REQ_PAY_001", "SUB_001", "150000",
            "Thanh toan hang thang", DirectDebitPartnerEnum.ONE_PAY, "Your Service");
        log("directDebit.paymentBill", c.directDebit.paymentBill(dto));
    }

    static void getSubscriptionStatus(TingeeClient c) {
        log("directDebit.getSubscriptionStatus", c.directDebit.getSubscriptionStatus("REQ_001", "SUB_001", "TOKEN_REF"));
    }

    static void directDebitRefund(TingeeClient c) {
        // Constructor: (String subscriptionId, String tokenRef, String transactionId, Long amount)
        var dto = new OpenApiRefundInputDto("SUB_001", "TOKEN_REF", "TXN_ID", 150_000L);
        log("directDebit.refund", c.directDebit.refund(dto));
    }

    static void deleteSubscription(TingeeClient c) {
        // Constructor: (String requestId, String returnUrl, String subscriptionId, String tokenRef)
        var dto = new OpenApiDeleteSubscriptionDto(
            "REQ_DEL_001", "https://your-domain.vn/direct-debit/cancel", "SUB_001", "TOKEN_REF");
        log("directDebit.deleteSubscription", c.directDebit.deleteSubscription(dto));
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // WEBHOOK — nhận và xác minh chữ ký
    //
    // Thêm @RestController vào project Spring Boot:
    //
    // @RestController
    // public class WebhookController {
    //     @Autowired TingeeClient client;
    //
    //     @PostMapping("/webhook")
    //     public ResponseEntity<?> webhook(
    //         @RequestHeader("x-signature") String signature,
    //         @RequestHeader("x-request-timestamp") String timestamp,
    //         @RequestBody TingeeWebhookBody body
    //     ) {
    //         var result = client.verifyWebhookSignature(signature, timestamp, body);
    //         if (!result.isValid()) return ResponseEntity.status(401).body(result);
    //
    //         // Webhook hợp lệ — xử lý nghiệp vụ
    //         System.out.println("clientId:        " + body.getClientId());
    //         System.out.println("transactionCode: " + body.getTransactionCode());
    //         System.out.println("amount:          " + body.getAmount());
    //         System.out.println("bank:            " + body.getBank());
    //         System.out.println("accountNumber:   " + body.getAccountNumber());
    //         // TODO: lưu DB, cập nhật đơn hàng, ...
    //
    //         return ResponseEntity.ok(Map.of("code", "00", "message", "OK"));
    //     }
    // }
    // ═══════════════════════════════════════════════════════════════════════════

    @Bean
    CommandLineRunner run(TingeeClient client) {
        return args -> {
            System.out.println("📦 Tingee SDK – Java Sample\n");

            getBanks(client);
            // generateVietQr(client);
            // generateDynamicQr(client);
            // getStatusDynamicQr(client);
            // getVaPaging(client);
            // registerNotify(client);
            // bankRefund(client);

            // readSecurityCode(client);
            // addDeviceToShop(client);
            // getDevicePaging(client);
            // showQrOnDevice(client);
            // generateAndShowDynamicQr(client);
            // readAmount(client);

            // createOrUpdateShop(client);
            // getShopPaging(client);

            // getTransactionPaging(client);

            // createMerchant(client);
            // getMerchantPaging(client);

            // getAccountNumberDdl(client);
            // generateDeepLink(client);
            // verifyReferralCode(client);

            // registerDirectDebit(client);
            // directDebitPayment(client);
            // getSubscriptionStatus(client);
            // directDebitRefund(client);
            // deleteSubscription(client);
        };
    }
}
