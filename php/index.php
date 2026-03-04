<?php

require __DIR__ . '/vendor/autoload.php';

use Tingee\Sdk\TingeeClient;
use Tingee\Sdk\Types\Dtos\OpenApiGetShopPagedInputDto;


$secretKey = 'OwKVEX9j+0pTGUaXL5FqYb7j0VTeiofsSY+35eEUTu0=';
$clientId = 'bdc76d6e0b2987316c676ee21bb90238';

$client = new TingeeClient($secretKey, $clientId);

try {
    echo "=== Ví dụ: Lấy danh sách Ngân hàng ===\n";
    $response = $client->v1->getBanks();
    echo "Code: " . $response->getCode() . " | Message: " . $response->getMessage() . "\n";
    $banks = $response->getData();
    echo "Số ngân hàng: " . count($banks) . " - Đầu tiên: " . $banks[0]->name . "\n";

    echo "\n=== Ví dụ: Shop GetPaging ===\n";
    $body = new OpenApiGetShopPagedInputDto();
    $body->skipCount = 0;
    $body->maxResultCount = 10;

    $response = $client->v1->shopGetPaging($body);
    echo "Code: " . $response->getCode() . " | Message: " . $response->getMessage() . "\n";
    if ($response->isSuccess()) {
        $paged = $response->getData();
            echo "Tổng số: " . $paged->totalCount . "\n";
            echo "Số shop trả về: " . count($paged->items) . "\n";
            echo "Shop đầu tiên: " . $paged->items[0]->id . "\n";
    }

} catch (\Exception $e) {
    echo "Lỗi khi gọi API: " . $e->getMessage() . "\n";
}
