#!/bin/bash

# Create output directory
mkdir -p dist

# Clean up
rm -rf dist/*
rm -f elevator-app.jar

# Compile
javac -cp "lib/*" $(find src -name "*.java") -d dist
cp src/*.properties src/*.xml dist
javac -cp dist $(find test -name "*.java") -d dist

# Run
java -cp "lib/*:dist" org.sdoroshenko.elevator.LauncherTest
