#!/bin/bash

# Simple script to build and run the Spring Boot sample

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SDK_DIR="$SCRIPT_DIR/../../bindings/java"
SAMPLE_DIR="$SCRIPT_DIR"

echo "🔨 Installing SDK locally..."
cd "$SDK_DIR"
mvn clean install -DskipTests

echo "🔨 Compiling and running Spring Boot sample..."
cd "$SAMPLE_DIR"
mvn spring-boot:run
