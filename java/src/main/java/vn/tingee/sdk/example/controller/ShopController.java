package vn.tingee.sdk.example.controller;

import vn.tingee.sdk.TingeeClient;
import vn.tingee.sdk.model.MerchantDto;
import vn.tingee.sdk.model.OpenApiGetPagingMerchantsDto;
import vn.tingee.sdk.model.PagedResultDto;
import vn.tingee.sdk.types.TingeeApiResponse;
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
            @RequestParam(name = "skipCount", defaultValue = "0") Integer skipCount,
            @RequestParam(name = "maxResultCount", defaultValue = "10") Integer maxResultCount) {
        

           // Required fields passed via constructor (compile-time enforced)
           OpenApiGetPagingMerchantsDto request = new OpenApiGetPagingMerchantsDto(skipCount, maxResultCount);


            TingeeApiResponse<PagedResultDto<MerchantDto>> result = tingeeClient.merchant.getPaging(request);
            
            if (result.isSuccess()) {
                return ResponseEntity.ok(result.getData().getItems());
            } else {
                return ResponseEntity.badRequest().body(result.getMessage());
            }
    }
}

