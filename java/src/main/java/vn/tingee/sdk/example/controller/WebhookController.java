package vn.tingee.sdk.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tingee.sdk.TingeeClient;
import vn.tingee.sdk.signature.TingeeSigner.WebhookVerifyResult;
import vn.tingee.sdk.types.TingeeWebhookBody;

import java.util.Map;

/**
 * Webhook endpoint — nhận callback từ Tingee và xác minh chữ ký.
 *
 * Cấu hình webhook URL này trong merchant portal:
 *   POST http://your-domain.com/webhook/tingee
 *
 * Tingee sẽ gửi kèm 2 header:
 *   x-signature          — HMAC-SHA512 của (timestamp + ":" + bodyJson)
 *   x-request-timestamp  — yyyyMMddHHmmssSSS (17 chữ số)
 */
@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final TingeeClient tingeeClient;

    @Autowired
    public WebhookController(TingeeClient tingeeClient) {
        this.tingeeClient = tingeeClient;
    }

    @PostMapping("/tingee")
    public ResponseEntity<?> handleWebhook(
            @RequestHeader(value = "x-signature",          required = false) String signature,
            @RequestHeader(value = "x-request-timestamp",  required = false) String timestamp,
            @RequestBody TingeeWebhookBody body
    ) {
        // ── 1. Xác minh chữ ký ────────────────────────────────────────────────
        //   TingeeClient dùng secretKey đã cấu hình để verify
        WebhookVerifyResult result = tingeeClient.verifyWebhookSignature(signature, timestamp, body);

        if (!result.isValid()) {
            return ResponseEntity
                    .status(401)
                    .body(Map.of(
                            "code",    result.getCode(),
                            "message", result.getMessage()
                    ));
        }

        // ── 2. Xử lý nghiệp vụ ───────────────────────────────────────────────
        System.out.println("✅ Webhook hợp lệ!");
        System.out.println("   clientId:        " + body.getClientId());
        System.out.println("   transactionCode: " + body.getTransactionCode());
        System.out.println("   amount:          " + body.getAmount());
        System.out.println("   bank:            " + body.getBank());
        System.out.println("   bankBin:         " + body.getBankBin());
        System.out.println("   accountNumber:   " + body.getAccountNumber());
        System.out.println("   vaAccountNumber: " + body.getVaAccountNumber());
        System.out.println("   transactionDate: " + body.getTransactionDate());
        if (body.getAdditionalData() != null) {
            body.getAdditionalData().forEach(d ->
                System.out.println("   additionalData:  " + d.getName() + " = " + d.getValue())
            );
        }

        // TODO: lưu DB, cập nhật trạng thái đơn hàng, gửi notification, ...

        // ── 3. Trả về {"code":"00","message":"OK"} để Tingee biết đã nhận ────
        return ResponseEntity.ok(Map.of("code", "00", "message", "OK"));
    }
}
