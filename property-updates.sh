#!/usr/bin/env bash

echo "Getting information about newer versions of dependencies specified by properties in this build.."
mvn versions:display-property-updates
