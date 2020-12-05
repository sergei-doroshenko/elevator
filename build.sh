#!/bin/bash

# Create output directory
mkdir -p dist

# Clean up
rm -rf dist/*
rm elevator-app.jar

# Compile
javac -cp "lib/*" $(find src -name "*.java") -d dist
cp src/*.properties dist
cp src/*.xml dist

# Create fat, runnable jar
jar cvfm elevator-app.jar src/META-INF/MANIFEST.MF -C dist .

# Run
java -jar elevator-app.jar
