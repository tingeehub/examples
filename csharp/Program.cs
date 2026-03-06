using System;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Tingee.Sdk.Client;
using Tingee.Sdk.Types.Dtos;

var builder = Host.CreateApplicationBuilder(args);

// Register TingeeClientOptions from environment variables or hardcoded values
builder.Services.AddSingleton(new TingeeClientOptions
{
    SecretKey = Environment.GetEnvironmentVariable("TINGEE_SECRET_KEY") ?? "OwKVEX9j+0pTGUaXL5FqYb7j0VTeiofsSY+35eEUTu0=",
    ClientId = Environment.GetEnvironmentVariable("TINGEE_CLIENT_ID") ?? "bdc76d6e0b2987316c676ee21bb90238",
    Environment = TingeeEnvironment.Uat
});

// Register TingeeClient into the DI container
builder.Services.AddSingleton<TingeeClient>();

using var host = builder.Build();

Console.WriteLine("Tingee C# SDK sample - .NET 8 DI Pattern");

var client = host.Services.GetRequiredService<TingeeClient>();
Console.WriteLine($"Base URL: {client.BaseUrl}");

var request = new OpenApiGetPagingMerchantsDto
{
    SkipCount = 0,
    MaxResultCount = 10
};

Console.WriteLine("Executing client.Merchant.GetPagingAsync...");
try
{
    // API methods are namespaced under group properties: client.Bank, client.Merchant, client.Shop, etc.
    var response = await client.Merchant.GetPagingAsync(request);
    Console.WriteLine($"Code={response.Code}, Message={response.Message}");
    if (response.Data != null)
    {
        Console.WriteLine($"Total Count: {response.Data.TotalCount}");
        Console.WriteLine($"Items Count: {response.Data.Items?.Count}");
    }
}
catch (Exception ex)
{
    Console.WriteLine($"Exception: {ex.Message}");
}
