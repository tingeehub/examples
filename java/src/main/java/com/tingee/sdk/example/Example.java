package com.tingee.sdk.example;

import com.tingee.sdk.TingeeClient;
import com.tingee.sdk.client.TingeeEnvironment;
import com.tingee.sdk.model.OpenApiGetShopPagedInputDto;
import com.tingee.sdk.types.TingeeApiResponse;

/**
 * Example usage of Tingee Java SDK
 */
public class Example {
    public static void main(String[] args) {
        System.out.println("🚀 Tingee Java SDK Example");
        System.out.println("==========================\n");

        // Create client
        TingeeClient client = TingeeClient.builder()
            .secretKey(System.getenv("TINGEE_SECRET_KEY") != null 
                ? System.getenv("TINGEE_SECRET_KEY") 
                : "OwKVEX9j+0pTGUaXL5FqYb7j0VTeiofsSY+35eEUTu0=")
            .clientId(System.getenv("TINGEE_CLIENT_ID") != null 
                ? System.getenv("TINGEE_CLIENT_ID") 
                : "bdc76d6e0b2987316c676ee21bb90238")
            .environment(TingeeEnvironment.UAT)
            .timeout(90000)
            .build();

        System.out.println("Base URL: " + client.getBaseUrl());
        System.out.println();

        // Example: Get Shop Paging
        try {
            System.out.println("=== Example: Get Shop Paging ===");
            
            OpenApiGetShopPagedInputDto request = new OpenApiGetShopPagedInputDto();
            request.setSkipCount(0);
            request.setMaxResultCount(10);

            TingeeApiResponse<Object> result = client.shopGetPaging(request);

            if (result.isSuccess()) {
                System.out.println("✅ Lấy danh sách shop thành công!");
                System.out.println("Code: " + result.getCode());
                System.out.println("Message: " + result.getMessage());
                System.out.println("Data: " + result.getData());
            } else {
                System.out.println("❌ Lỗi: " + result.getMessage());
                System.out.println("Error Code: " + result.getCode());
            }
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
