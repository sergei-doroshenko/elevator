#!/bin/bash

# Clean up
rm -rf dist/*
rm elevator-app.jar

# Compile
javac -cp "lib/*" $(find src -name "*.java") -d dist
cp src/*.properties dist
cp src/*.xml dist
#ls -la dist/com/epam/javatraining/elevator

# Create fat, runnable jar
jar cvfm elevator-app.jar src/META-INF/MANIFEST.MF -C dist .

# Run
java -jar elevator-app.jar
