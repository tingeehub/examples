#!/bin/bash

# Script to compile and run Java sample
# Usage: ./run.sh

set -e

echo "🔨 Building SDK..."
cd ../../bindings/java
mvn clean compile -q

echo "🔨 Compiling sample..."
cd ../../samples/java
mvn clean compile -q

echo "🚀 Running sample..."
echo ""

# Get classpath with SDK and dependencies
SDK_CLASSES="../../bindings/java/target/classes"
SAMPLE_CLASSES="target/classes"
DEPS=$(mvn dependency:build-classpath -q -DincludeScope=compile)

java -cp "$SDK_CLASSES:$SAMPLE_CLASSES:$DEPS" \
  com.tingee.sdk.example.Example
