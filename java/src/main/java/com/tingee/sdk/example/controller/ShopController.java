package com.tingee.sdk.example.controller;

import com.tingee.sdk.TingeeClient;
import com.tingee.sdk.model.MerchantDto;
import com.tingee.sdk.model.OpenApiGetPagingMerchantsDto;
import com.tingee.sdk.model.PagedResultDto;
import com.tingee.sdk.types.TingeeApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shops")
public class ShopController {

    private final TingeeClient tingeeClient;

    @Autowired
    public ShopController(TingeeClient tingeeClient) {
        this.tingeeClient = tingeeClient;
    }

    @GetMapping
    public ResponseEntity<?> getShops(
            @RequestParam(defaultValue = "0") Integer skipCount,
            @RequestParam(defaultValue = "10") Integer maxResultCount) {
        
        try {
           OpenApiGetPagingMerchantsDto request = new OpenApiGetPagingMerchantsDto();
            request.setSkipcount(skipCount.doubleValue());
            request.setMaxresultcount(maxResultCount.doubleValue());

            TingeeApiResponse<PagedResultDto<MerchantDto>> result = tingeeClient.v1.merchantGetPaging(request);
            
            if (result.isSuccess()) {
                return ResponseEntity.ok(result.getData().getItems());
            } else {
                return ResponseEntity.badRequest().body(result.getMessage());
            }
        } catch (Exception e) {
            TingeeApiResponse<Object> errorResponse = new TingeeApiResponse<>();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
