# Tingee Java SDK Sample

## Cách chạy

### Cách 1: Dùng script tự động (Khuyến nghị)

```bash
cd samples/java
./run.sh
```

### Cách 2: Dùng Maven

```bash
# 1. Build SDK trước
cd bindings/java
mvn clean compile

# 2. Chạy sample
cd ../../samples/java
mvn clean compile exec:java
```

### Cách 3: Dùng javac và java trực tiếp

```bash
# 1. Tìm Maven repository (thường ở ~/.m2/repository)
M2_REPO="$HOME/.m2/repository"

# 2. Build SDK
cd bindings/java/src/main/java
javac -cp "$M2_REPO/com/squareup/okhttp3/okhttp/4.12.0/okhttp-4.12.0.jar:\
$M2_REPO/com/fasterxml/jackson/core/jackson-databind/2.16.1/jackson-databind-2.16.1.jar:\
$M2_REPO/com/fasterxml/jackson/core/jackson-core/2.16.1/jackson-core-2.16.1.jar:\
$M2_REPO/com/fasterxml/jackson/core/jackson-annotations/2.16.1/jackson-annotations-2.16.1.jar:\
$M2_REPO/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.16.1/jackson-datatype-jsr310-2.16.1.jar:\
$M2_REPO/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar" \
  -d ../../../target/classes \
  com/tingee/sdk/**/*.java \
  com/tingee/sdk/client/**/*.java \
  com/tingee/sdk/signature/**/*.java \
  com/tingee/sdk/types/**/*.java \
  com/tingee/sdk/model/**/*.java

# 3. Compile và chạy sample
cd ../../../../samples/java/src/main/java
javac -cp "../../../../bindings/java/target/classes:\
$M2_REPO/com/squareup/okhttp3/okhttp/4.12.0/okhttp-4.12.0.jar:\
$M2_REPO/com/fasterxml/jackson/core/jackson-databind/2.16.1/jackson-databind-2.16.1.jar:\
$M2_REPO/com/fasterxml/jackson/core/jackson-core/2.16.1/jackson-core-2.16.1.jar:\
$M2_REPO/com/fasterxml/jackson/core/jackson-annotations/2.16.1/jackson-annotations-2.16.1.jar:\
$M2_REPO/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.16.1/jackson-datatype-jsr310-2.16.1.jar:\
$M2_REPO/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar:\
$M2_REPO/org/slf4j/slf4j-simple/2.0.9/slf4j-simple-2.0.9.jar" \
  com/tingee/sdk/example/Example.java

java -cp "../../../../bindings/java/target/classes:\
.:\
$M2_REPO/com/squareup/okhttp3/okhttp/4.12.0/okhttp-4.12.0.jar:\
$M2_REPO/com/fasterxml/jackson/core/jackson-databind/2.16.1/jackson-databind-2.16.1.jar:\
$M2_REPO/com/fasterxml/jackson/core/jackson-core/2.16.1/jackson-core-2.16.1.jar:\
$M2_REPO/com/fasterxml/jackson/core/jackson-annotations/2.16.1/jackson-annotations-2.16.1.jar:\
$M2_REPO/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.16.1/jackson-datatype-jsr310-2.16.1.jar:\
$M2_REPO/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar:\
$M2_REPO/org/slf4j/slf4j-simple/2.0.9/slf4j-simple-2.0.9.jar" \
  com.tingee.sdk.example.Example
```

## Set environment variables (optional)

```bash
export TINGEE_SECRET_KEY="your-secret-key"
export TINGEE_CLIENT_ID="your-client-id"
```

## Lưu ý

- Đảm bảo đã chạy `npm run generate:java` để generate code từ OpenAPI spec
- SDK cần được compile trước khi chạy sample
- Nếu chưa có Maven, cài đặt: `brew install maven` (macOS) hoặc download từ https://maven.apache.org
