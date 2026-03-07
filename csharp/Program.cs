using System.Text.Json;
using Microsoft.AspNetCore.Mvc;
using Tingee.Sdk.Client;
using Tingee.Sdk.Signature;
using Tingee.Sdk.Types;
using Tingee.Sdk.Types.Dtos;

// ─── Builder ─────────────────────────────────────────────────────────────────
var builder = WebApplication.CreateBuilder(args);

builder.Services.AddSingleton(new TingeeClient(new TingeeClientOptions
{
    SecretKey   = Environment.GetEnvironmentVariable("TINGEE_SECRET_KEY") ?? "YOUR_SECRET_KEY",
    ClientId    = Environment.GetEnvironmentVariable("TINGEE_CLIENT_ID")  ?? "YOUR_CLIENT_ID",
    Environment = TingeeEnvironment.Uat,
}));

var app = builder.Build();

// ─── Helper ───────────────────────────────────────────────────────────────────
static IResult ApiResult<T>(TingeeApiResponse<T> r)
    => r.Code == "00"
        ? Results.Ok(r.Data)
        : Results.BadRequest(new { r.Code, r.Message });

// =============================================================================
// BANK
// =============================================================================

app.MapGet("/bank/banks", async (TingeeClient client) =>
    ApiResult(await client.Bank.GetBanksAsync()));

app.MapPost("/bank/viet-qr", async (TingeeClient client,
    [FromBody] OpenApiGenerateVietQRInputDto body) =>
    ApiResult(await client.Bank.GenerateVietQrAsync(body)));

app.MapPost("/bank/dynamic-qr", async (TingeeClient client,
    [FromBody] GenerateDynamicQRInputDto body) =>
    ApiResult(await client.Bank.GenerateDynamicQrAsync(body)));

app.MapPost("/bank/dynamic-qr/status", async (TingeeClient client,
    [FromBody] OpenApiGetStatusDynamicQRInputDto body) =>
    ApiResult(await client.Bank.GetStatusDynamicQrAsync(body)));

app.MapPost("/bank/va", async (TingeeClient client,
    [FromBody] OpenApiGetVAPagedInputDto body) =>
    ApiResult(await client.Bank.GetVaPagingAsync(body)));

app.MapPost("/bank/register-notify", async (TingeeClient client,
    [FromBody] OpenApiRegisterNotifyDto body) =>
    ApiResult(await client.Bank.RegisterNotifyAsync(body)));

app.MapPost("/bank/refund", async (TingeeClient client,
    [FromBody] OpenApiRefundDto body) =>
    ApiResult(await client.Bank.RefundAsync(body)));

// =============================================================================
// DEVICE
// =============================================================================

app.MapPost("/device/read-security-code", async (TingeeClient client,
    [FromBody] OpenApiReadSecurityCodeDto body) =>
    ApiResult(await client.Device.ReadSecurityCodeAsync(body)));

app.MapPost("/device/add-to-shop", async (TingeeClient client,
    [FromBody] OpenApiAddDeviceToShop body) =>
    ApiResult(await client.Device.AddDeviceToShopAsync(body)));

app.MapPost("/device/paging", async (TingeeClient client,
    [FromBody] OpenApiGetPagingDeviceInputDto body) =>
    ApiResult(await client.Device.GetPagingAsync(body)));

app.MapPost("/device/show-qr", async (TingeeClient client,
    [FromBody] OpenApiShowQRCodeDto body) =>
    ApiResult(await client.Device.ShowQrCodeAsync(body)));

app.MapPost("/device/generate-and-show-qr", async (TingeeClient client,
    [FromBody] OpenApiGenerateAndShowDynamicQrCodeDto body) =>
    ApiResult(await client.Device.GenerateAndShowDynamicQrCodeAsync(body)));

app.MapPost("/device/read-amount", async (TingeeClient client,
    [FromBody] OpenApiReadAmountDto body) =>
    ApiResult(await client.Device.ReadAmountLinkToMerchantAsync(body)));

// =============================================================================
// SHOP
// =============================================================================

app.MapPost("/shop", async (TingeeClient client,
    [FromBody] OpenApiCreateOrUpdateShopDto body) =>
    ApiResult(await client.Shop.CreateOrUpdateAsync(body)));

app.MapPost("/shop/paging", async (TingeeClient client,
    [FromBody] OpenApiGetShopPagedInputDto body) =>
    ApiResult(await client.Shop.GetPagingAsync(body)));

// =============================================================================
// TRANSACTION
// =============================================================================

app.MapPost("/transaction/paging", async (TingeeClient client,
    [FromBody] OpenApiTransactionPagedInputDto body) =>
    ApiResult(await client.Transaction.GetPagingAsync(body)));

// =============================================================================
// MERCHANT
// =============================================================================

app.MapPost("/merchant", async (TingeeClient client,
    [FromBody] OpenApiCreateMerchantDto body) =>
    ApiResult(await client.Merchant.CreateAsync(body)));

app.MapPost("/merchant/paging", async (TingeeClient client,
    [FromBody] OpenApiGetPagingMerchantsDto body) =>
    ApiResult(await client.Merchant.GetPagingAsync(body)));

// =============================================================================
// ACCOUNT NUMBER
// =============================================================================

app.MapPost("/account-number/ddl", async (TingeeClient client,
    [FromBody] OpenApiAccountNumberDDLPagedInputDto body) =>
    ApiResult(await client.AccountNumber.GetAllDdlAsync(body)));

// =============================================================================
// DEEP LINK
// =============================================================================

app.MapPost("/deep-link", async (TingeeClient client,
    [FromBody] OpenApiDeepLinkDto body) =>
    ApiResult(await client.DeepLink.GenerateAsync(body)));

// =============================================================================
// USER
// =============================================================================

app.MapGet("/user/verify-referral", async (TingeeClient client,
    [FromQuery] string referralCode) =>
    ApiResult(await client.User.VerifyReferralCodeAsync(referralCode)));

// =============================================================================
// DIRECT DEBIT
// =============================================================================

app.MapPost("/direct-debit/register", async (TingeeClient client,
    [FromBody] OpenApiRegisterDto body) =>
    ApiResult(await client.DirectDebit.RegisterAsync(body)));

app.MapPost("/direct-debit/payment", async (TingeeClient client,
    [FromBody] OpenApiPaymentBillDto body) =>
    ApiResult(await client.DirectDebit.PaymentBillAsync(body)));

app.MapGet("/direct-debit/subscription", async (TingeeClient client,
    [FromQuery] string requestId, [FromQuery] string subscriptionId, [FromQuery] string tokenRef) =>
    ApiResult(await client.DirectDebit.GetSubscriptionStatusAsync(subscriptionId, tokenRef)));

app.MapPost("/direct-debit/refund", async (TingeeClient client,
    [FromBody] OpenApiRefundInputDto body) =>
    ApiResult(await client.DirectDebit.RefundAsync(body)));

app.MapDelete("/direct-debit/subscription", async (TingeeClient client,
    [FromBody] OpenApiDeleteSubscriptionDto body) =>
    ApiResult(await client.DirectDebit.DeleteSubscriptionAsync(body)));

// =============================================================================
// WEBHOOK — POST /webhook/tingee
// Cấu hình URL này trong merchant portal để nhận callback từ Tingee
// =============================================================================

app.MapPost("/webhook/tingee", async (TingeeClient client, HttpContext ctx) =>
{
    var signature = ctx.Request.Headers["x-signature"].ToString();
    var timestamp = ctx.Request.Headers["x-request-timestamp"].ToString();

    // Đọc raw body để verify
    ctx.Request.EnableBuffering();
    using var reader = new System.IO.StreamReader(ctx.Request.Body, leaveOpen: true);
    var rawBody = await reader.ReadToEndAsync();
    ctx.Request.Body.Position = 0;

    // ── 1. Xác minh chữ ký ────────────────────────────────────────────────────
    var result = client.VerifyWebhookSignature(new()
    {
        Signature = signature,
        Timestamp = timestamp,
        BodyJson  = rawBody,
    });

    if (!result.IsValid)
    {
        Console.WriteLine($"❌ Webhook không hợp lệ: {result.Code} — {result.Message}");
        return Results.Json(
            new { code = result.Code, message = result.Message },
            statusCode: 401);
    }

    // ── 2. Parse và xử lý nghiệp vụ ─────────────────────────────────────────
    var body = JsonSerializer.Deserialize<TingeeWebhookBody>(rawBody,
        new JsonSerializerOptions { PropertyNameCaseInsensitive = true });

    Console.WriteLine("✅ Webhook hợp lệ!");
    Console.WriteLine($"   clientId:        {body?.ClientId}");
    Console.WriteLine($"   transactionCode: {body?.TransactionCode}");
    Console.WriteLine($"   amount:          {body?.Amount}");
    Console.WriteLine($"   bank:            {body?.Bank}");
    Console.WriteLine($"   bankBin:         {body?.BankBin}");
    Console.WriteLine($"   accountNumber:   {body?.AccountNumber}");
    Console.WriteLine($"   vaAccountNumber: {body?.VaAccountNumber}");
    Console.WriteLine($"   transactionDate: {body?.TransactionDate}");

    // TODO: lưu DB, cập nhật trạng thái đơn hàng, gửi notification, ...

    // ── 3. Trả về {"code":"00","message":"OK"} để Tingee biết đã nhận ─────────
    return Results.Ok(new { code = "00", message = "OK" });
});

// =============================================================================
app.Run();
