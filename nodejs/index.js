// @ts-check
import { isErrorResponse, isSuccessResponse, TingeeClient } from "@tingee/sdk-node";

// const client = new TingeeClient({
//   secretKey:
//     process.env.TINGEE_SECRET_KEY ||
//     "OwKVEX9j+0pTGUaXL5FqYb7j0VTeiofsSY+35eEUTu0=",
//   clientId: process.env.TINGEE_CLIENT_ID || "bdc76d6e0b2987316c676ee21bb90238",
//   environment: "uat",
//   timeout: 90000,
  
// });

const client = new TingeeClient({
      secretKey: "sHBvj4fZuDvCsKKwYNAootVaRSi0YZgTUHsw/RhxmHY=",
      clientId: "63b7eefd1bd3f003a2c3950857d2fd90",
      environment:'uat',
      timeout: 90000,
    })

async function exampleGetShopPaging() {
  try {
    // console.log("\n=== Ví dụ 2: Get Shop Paging ===");

    // const result = await client.v1.getAccountNumberInfoByQrCode({qrCode: '6etgerg'});

    // console.log("Response:", JSON.stringify(result, null, 2));

    // // result is TingeeApiResponse with { code, message, data }

    //   console.log("Lấy danh sách shop thành công!");
    //   console.log("Total it,ems:", result.data);
const _accounts = await client.v1.merchantGetPaging({maxResultCount: 10, skipCount: 0})
  } catch (error) {
    console.error("Error:", error);
  }
}

await exampleGetShopPaging();
