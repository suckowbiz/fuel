#!/usr/bin/env bash

echo "Building with Eclipse support.."
mvn clean install -Dmaven.test.skip eclipse:clean eclipse:eclipse
