<?php

require __DIR__ . '/vendor/autoload.php';

use Tingee\Sdk\TingeeClient;
use Tingee\Sdk\Types\Dtos\OpenApiGetShopPagedInputDto;
use Tingee\Sdk\Types\Dtos\OpenApiGetPagingMerchantsDto;

$secretKey = 'OwKVEX9j+0pTGUaXL5FqYb7j0VTeiofsSY+35eEUTu0=';
$clientId  = 'bdc76d6e0b2987316c676ee21bb90238';

$client = new TingeeClient($secretKey, $clientId, 'uat');

try {
    // ─── Ngân hàng ───────────────────────────────────────────────
    echo "=== Lấy danh sách Ngân hàng ===\n";
    $response = $client->bank->getBanks();
    echo "Code: " . $response->getCode() . " | Message: " . $response->getMessage() . "\n";
    $banks = $response->getData();
    if (!empty($banks)) {
        echo "Số ngân hàng: " . count($banks) . " - Đầu tiên: " . $banks[0]->name . "\n";
    }

    // ─── Shop (required fields qua constructor) ───────────────────
    echo "\n=== Lấy danh sách Shop ===\n";
    $req = new OpenApiGetShopPagedInputDto(
        skipCount:      0,
        maxResultCount: 10
    );
    // optional fields set thêm nếu cần:
    // $req->filter = 'keyword';

    $response = $client->shop->getPaging($req);
    echo "Code: " . $response->getCode() . " | Message: " . $response->getMessage() . "\n";
    if ($response->isSuccess()) {
        $paged = $response->getData();
        echo "Tổng số: "          . $paged->totalCount          . "\n";
        echo "Số shop trả về: "   . count($paged->items)        . "\n";
        if (!empty($paged->items)) {
            echo "Shop đầu tiên: " . $paged->items[0]->id . "\n";
        }
    }

    // ─── Merchant (required fields qua constructor) ───────────────
    echo "\n=== Lấy danh sách Merchant ===\n";
    $req = new OpenApiGetPagingMerchantsDto(
        skipCount:      0,
        maxResultCount: 10
    );

    $response = $client->merchant->getPaging($req);
    echo "Code: " . $response->getCode() . " | Message: " . $response->getMessage() . "\n";
    if ($response->isSuccess()) {
        $paged = $response->getData();
        echo "Tổng số: "              . $paged->totalCount           . "\n";
        echo "Số merchant trả về: "   . count($paged->items)         . "\n";
        if (!empty($paged->items)) {
            echo "Merchant đầu tiên: " . $paged->items[0]->name . "\n";
        }
    }

} catch (\Exception $e) {
    echo "Lỗi: " . $e->getMessage() . "\n";
}
