#!/usr/bin/env bash

echo "Running unit and integration tests to verify dependend components work properly with each other.."
mvn verify -q
