// @ts-check
import { TingeeClient } from "@tingee/sdk-node";

const client = new TingeeClient({
  secretKey:
    process.env.TINGEE_SECRET_KEY ||
    "OwKVEX9j+0pTGUaXL5FqYb7j0VTeiofsSY+35eEUTu0=",
  clientId: process.env.TINGEE_CLIENT_ID || "bdc76d6e0b2987316c676ee21bb90238",
  environment: "uat",
  timeout: 90000,
});

async function exampleGetShopPaging() {
  try {
    console.log("\n=== Ví dụ 2: Get Shop Paging ===");

    const result = await client.v1ShopGetPaging({
      skipCount: 0,
      maxResultCount: 10,
    });

    console.log("Response:", JSON.stringify(result, null, 2));

    // result is TingeeApiResponse with { code, message, data }
    if (result.code === "00") {
      console.log("Lấy danh sách shop thành công!");
      console.log("Total items:", result.data?.totalCount || 0);
    } else {
      console.log("Lỗi:", result.message);
    }
  } catch (error) {
    console.error("Error:", error);
  }
}

await exampleGetShopPaging();
