#!/bin/bash

# Simple script to compile and run Java sample without Maven
# Usage: ./compile-and-run.sh

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SDK_DIR="$SCRIPT_DIR/../../bindings/java"
SAMPLE_DIR="$SCRIPT_DIR"
M2_REPO="$HOME/.m2/repository"

# Check if Maven dependencies exist
if [ ! -d "$M2_REPO" ]; then
    echo "❌ Maven repository not found at $M2_REPO"
    echo "Please install Maven first: brew install maven"
    exit 1
fi

echo "🔨 Compiling SDK..."
cd "$SDK_DIR/src/main/java"

# Create output directory
mkdir -p ../../../target/classes

# Compile SDK
javac -cp "$M2_REPO/com/squareup/okhttp3/okhttp/4.12.0/okhttp-4.12.0.jar:\
$M2_REPO/com/fasterxml/jackson/core/jackson-databind/2.16.1/jackson-databind-2.16.1.jar:\
$M2_REPO/com/fasterxml/jackson/core/jackson-core/2.16.1/jackson-core-2.16.1.jar:\
$M2_REPO/com/fasterxml/jackson/core/jackson-annotations/2.16.1/jackson-annotations-2.16.1.jar:\
$M2_REPO/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.16.1/jackson-datatype-jsr310-2.16.1.jar:\
$M2_REPO/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar" \
  -d ../../../target/classes \
  $(find com -name "*.java")

echo "🔨 Compiling sample..."
cd "$SAMPLE_DIR/src/main/java"

# Create output directory
mkdir -p ../../target/classes

# Compile sample
javac -cp "$SDK_DIR/target/classes:\
$M2_REPO/com/squareup/okhttp3/okhttp/4.12.0/okhttp-4.12.0.jar:\
$M2_REPO/com/fasterxml/jackson/core/jackson-databind/2.16.1/jackson-databind-2.16.1.jar:\
$M2_REPO/com/fasterxml/jackson/core/jackson-core/2.16.1/jackson-core-2.16.1.jar:\
$M2_REPO/com/fasterxml/jackson/core/jackson-annotations/2.16.1/jackson-annotations-2.16.1.jar:\
$M2_REPO/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.16.1/jackson-datatype-jsr310-2.16.1.jar:\
$M2_REPO/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar:\
$M2_REPO/org/slf4j/slf4j-simple/2.0.9/slf4j-simple-2.0.9.jar" \
  -d ../../target/classes \
  com/tingee/sdk/example/Example.java

echo "🚀 Running sample..."
echo ""

# Run
java -cp "$SDK_DIR/target/classes:\
$SAMPLE_DIR/target/classes:\
$M2_REPO/com/squareup/okhttp3/okhttp/4.12.0/okhttp-4.12.0.jar:\
$M2_REPO/com/fasterxml/jackson/core/jackson-databind/2.16.1/jackson-databind-2.16.1.jar:\
$M2_REPO/com/fasterxml/jackson/core/jackson-core/2.16.1/jackson-core-2.16.1.jar:\
$M2_REPO/com/fasterxml/jackson/core/jackson-annotations/2.16.1/jackson-annotations-2.16.1.jar:\
$M2_REPO/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.16.1/jackson-datatype-jsr310-2.16.1.jar:\
$M2_REPO/org/slf4j/slf4j-api/2.0.9/slf4j-api-2.0.9.jar:\
$M2_REPO/org/slf4j/slf4j-simple/2.0.9/slf4j-simple-2.0.9.jar" \
  com.tingee.sdk.example.Example
